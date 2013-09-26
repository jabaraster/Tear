/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jabara.general.ExceptionUtil;
import jabara.general.IoUtil;
import jabara.general.NotFound;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import jp.co.city.tear.model.LargeDataOperation;
import jp.co.city.tear.model.LargeDataOperation.Mode;
import jp.co.city.tear.model.NamedInputStream;
import jp.co.city.tear.service.ITempFileService;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.template.PackageTextTemplate;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class FileUploadPanel extends Panel {
    private static final long       serialVersionUID = -2121159238236955334L;

    private String                  fileName;

    @Inject
    ITempFileService                tempFileService;

    private final Handler           handler          = new Handler();

    private LargeDataOperation.Mode dataOperation    = LargeDataOperation.Mode.NOOP;
    private File                    temporary;                                      // TODO 後始末

    private FileUploadField         fileUpload;
    private Button                  uploader;
    private Button                  deleter;
    private Button                  restorer;
    private HiddenField<String>     uploadId;

    private AjaxFormSubmitBehavior  autoUploadBehavior;

    private boolean                 autoUpload       = false;

    /**
     * @param pId -
     */
    public FileUploadPanel(final String pId) {
        super(pId);

        this.add(getFileUpload());
        this.add(getUploader());
        this.add(getDeleter());
        this.add(getRestorer());
        this.add(getUploadId());
    }

    /**
 * 
 */
    public void clear() {
        this.handler.restore();
    }

    /**
     * @return -
     * @throws NotFound -
     */
    public NamedInputStream getInputStream() throws NotFound {
        if (this.temporary == null) {
            throw NotFound.GLOBAL;
        }
        try {
            return new NamedInputStream(this.fileName, new BufferedInputStream(new FileInputStream(this.temporary)));

        } catch (final FileNotFoundException e) {
            throw NotFound.GLOBAL;
        }
    }

    /**
     * @return -
     */
    public LargeDataOperation getOperation() {
        try {
            final LargeDataOperation ret = new LargeDataOperation();
            switch (this.dataOperation) {
            case DELETE:
                ret.delete();
                break;
            case NOOP:
                ret.cancel();
                break;
            case UPDATE:
                final BufferedInputStream in = new BufferedInputStream(new FileInputStream(this.temporary));
                ret.update(new NamedInputStream(this.fileName, in));
                break;
            default:
                throw new IllegalStateException();
            }
            return ret;

        } catch (final FileNotFoundException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    /**
     * @return -
     */
    @SuppressWarnings("serial")
    public Button getRestorer() {
        if (this.restorer == null) {
            this.restorer = new Button("restorer") { //$NON-NLS-1$
                @Override
                public void onSubmit() {
                    FileUploadPanel.this.handler.restore();
                }
            };
        }
        return this.restorer;
    }

    /**
     * @param pAutoUpload -
     */
    public void setAutoUpload(final boolean pAutoUpload) {
        if (pAutoUpload == this.autoUpload) {
            return;
        }
        if (pAutoUpload) {
            getFileUpload().add(getAutoUploadBehavior());
        } else {
            getFileUpload().remove(getAutoUploadBehavior());
        }
        this.autoUpload = pAutoUpload;
    }

    private void deleteTemporaryFile() {
        if (this.temporary != null) {
            this.temporary.delete();
        }
    }

    @SuppressWarnings("serial")
    private AjaxFormSubmitBehavior getAutoUploadBehavior() {
        if (this.autoUploadBehavior == null) {
            this.autoUploadBehavior = new AjaxFormSubmitBehavior("change") { //$NON-NLS-1$
                @Override
                protected void onSubmit(final AjaxRequestTarget pTarget) {
                    FileUploadPanel.this.handler.upload();

                    // TODO 再描画対象を決め打ちするのは良くない. コールバックに作り変えた方がよい.
                    final Form<?> form = FileUploadPanel.this.findParent(Form.class);
                    if (form != null) {
                        pTarget.add(form);
                    }
                }
            };
        }
        return this.autoUploadBehavior;
    }

    @SuppressWarnings("serial")
    private Button getDeleter() {
        if (this.deleter == null) {
            this.deleter = new Button("deleter") { //$NON-NLS-1$
                @Override
                public void onSubmit() {
                    FileUploadPanel.this.handler.delete();
                }
            };
        }
        return this.deleter;
    }

    @SuppressWarnings("serial")
    private FileUploadField getFileUpload() {
        if (this.fileUpload == null) {
            this.fileUpload = new FileUploadField("fileUpload"); //$NON-NLS-1$
            this.fileUpload.add(new AbstractDefaultAjaxBehavior() {

                @SuppressWarnings("nls")
                @Override
                public void renderHead(final Component pComponent, final IHeaderResponse pResponse) {
                    super.renderHead(pComponent, pResponse);

                    try (final PackageTextTemplate text = new PackageTextTemplate(FileUploadPanel.class, FileUploadPanel.class.getSimpleName()
                            + ".js")) {
                        final Map<String, Object> params = new HashMap<>();
                        params.put("formId", getFileUpload().findParent(Form.class).getMarkupId());
                        params.put("uploadId", getFileUpload().getMarkupId());
                        params.put("callbackUrl", getCallbackUrl());
                        pResponse.render(OnDomReadyHeaderItem.forScript(text.asString(params)));

                    } catch (final IOException e) {
                        throw ExceptionUtil.rethrow(e);
                    }
                }

                @Override
                protected void respond(final AjaxRequestTarget pTarget) {
                    FileUploadPanel.this.handler.upload();
                    pTarget.add(FileUploadPanel.this.findParent(Form.class));
                }
            });
        }
        return this.fileUpload;
    }

    @SuppressWarnings("serial")
    private Button getUploader() {
        if (this.uploader == null) {
            this.uploader = new Button("uploader") { //$NON-NLS-1$
                // @Override
                // public boolean isVisible() {
                // if (FileUploadPanel.this.autoUpload) {
                // return false;
                // }
                // return super.isVisibilityAllowed();
                // }

                @Override
                public void onSubmit() {
                    FileUploadPanel.this.handler.upload();
                }
            };
        }
        return this.uploader;
    }

    @SuppressWarnings({ "serial", "nls" })
    private HiddenField<String> getUploadId() {
        if (this.uploadId == null) {
            this.uploadId = new HiddenField<>("uploadId", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return getUploadIdValue();
                }
            });
        }
        return this.uploadId;
    }

    private String getUploadIdValue() {
        return getSession().getId() + "_" + this.getMarkupId(); //$NON-NLS-1$
    }

    private static NamedInputStream getDataFromFileUpload(final FileUploadField pField) {
        try {
            final FileUpload upload = pField.getFileUpload();
            if (upload == null) {
                return null;
            }
            return new NamedInputStream(upload.getClientFileName(), upload.getInputStream());

        } catch (final IOException e) {
            throw ExceptionUtil.rethrow(e);
        }
    }

    private class Handler implements Serializable {
        private static final long serialVersionUID = -2699703816024152769L;

        private void delete() {
            deleteTemporaryFile();
            FileUploadPanel.this.dataOperation = Mode.DELETE;
        }

        private void restore() {
            deleteTemporaryFile();
            FileUploadPanel.this.dataOperation = Mode.NOOP;
        }

        private File save(final NamedInputStream pData) {
            try {
                final File file = FileUploadPanel.this.tempFileService.create(FileUploadPanel.class, "dat"); //$NON-NLS-1$
                try (final OutputStream out = new FileOutputStream(file); //
                        final BufferedOutputStream bufOut = new BufferedOutputStream(out)) {
                    IOUtils.copy(IoUtil.toBuffered(pData.getInputStream()), bufOut);
                    bufOut.flush();

                    FileUploadPanel.this.fileName = pData.getName();
                }
                return file;

            } catch (final IOException e) {
                throw ExceptionUtil.rethrow(e);
            }
        }

        private void upload() {
            try (NamedInputStream data = getDataFromFileUpload(getFileUpload())) {
                if (data == null) {
                    return;
                }
                final File temp = save(data);
                deleteTemporaryFile();
                FileUploadPanel.this.temporary = temp;
                FileUploadPanel.this.dataOperation = Mode.UPDATE;

            } catch (final IOException e) {
                throw ExceptionUtil.rethrow(e);
            }
        }
    }
}

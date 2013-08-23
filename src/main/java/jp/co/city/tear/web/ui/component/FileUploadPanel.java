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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.inject.Inject;

import jp.co.city.tear.model.LargeDataOperation;
import jp.co.city.tear.model.LargeDataOperation.Mode;
import jp.co.city.tear.service.ITempFileService;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class FileUploadPanel extends Panel {
    private static final long       serialVersionUID = -2121159238236955334L;

    @Inject
    ITempFileService                tempFileService;

    private final Handler           handler          = new Handler();

    private LargeDataOperation.Mode dataOperation    = LargeDataOperation.Mode.NOOP;
    private File                    temporary;                                      // TODO 後始末

    private FileUploadField         fileUpload;
    private Button                  uploader;
    private Button                  deleter;
    private Button                  restorer;

    /**
     * @param pId -
     */
    public FileUploadPanel(final String pId) {
        super(pId);

        this.add(getFileUpload());
        this.add(getUploader());
        this.add(getDeleter());
        this.add(getRestorer());
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
    public InputStream getInputStream() throws NotFound {
        if (this.temporary == null) {
            throw NotFound.GLOBAL;
        }
        try {
            return new BufferedInputStream(new FileInputStream(this.temporary));

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
                ret.update(new BufferedInputStream(new FileInputStream(this.temporary)));
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

    private void deleteTemporaryFile() {
        if (this.temporary != null) {
            this.temporary.delete();
        }
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

    private FileUploadField getFileUpload() {
        if (this.fileUpload == null) {
            this.fileUpload = new FileUploadField("fileUpload"); //$NON-NLS-1$
        }
        return this.fileUpload;
    }

    @SuppressWarnings("serial")
    private Button getUploader() {
        if (this.uploader == null) {
            this.uploader = new Button("uploader") { //$NON-NLS-1$
                @Override
                public void onSubmit() {
                    FileUploadPanel.this.handler.upload();
                }
            };
        }
        return this.uploader;
    }

    private static InputStream getDataFromFileUpload(final FileUploadField pField) {
        try {
            final FileUpload upload = pField.getFileUpload();
            if (upload == null) {
                return null;
            }
            return upload.getInputStream();
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

        private File save(final InputStream pData) {
            try {
                final File file = FileUploadPanel.this.tempFileService.create(FileUploadPanel.class, "dat"); //$NON-NLS-1$
                try (final OutputStream out = new FileOutputStream(file); //
                        final BufferedOutputStream bufOut = new BufferedOutputStream(out)) {
                    IOUtils.copy(IoUtil.toBuffered(pData), bufOut);
                    bufOut.flush();
                }
                return file;

            } catch (final IOException e) {
                throw ExceptionUtil.rethrow(e);
            }
        }

        private void upload() {
            try (InputStream data = getDataFromFileUpload(getFileUpload())) {
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

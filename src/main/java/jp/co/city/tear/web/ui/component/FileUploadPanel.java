/**
 * 
 */
package jp.co.city.tear.web.ui.component;

import jabara.general.ArgUtil;
import jabara.general.ExceptionUtil;
import jabara.general.IoUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author jabaraster
 */
@SuppressWarnings("synthetic-access")
public class FileUploadPanel extends Panel {
    private static final long          serialVersionUID = -2121159238236955334L;

    private final IModel<Serializable> labelTextModel;

    private final Handler              handler          = new Handler();

    private File                       temporary;

    private Label                      label;
    private FileUploadField            fileUpload;
    private Button                     uploader;

    /**
     * @param pId -
     * @param pLabelTextModel -
     */
    public FileUploadPanel(final String pId, final IModel<Serializable> pLabelTextModel) {
        super(pId);
        this.labelTextModel = ArgUtil.checkNull(pLabelTextModel, "pLabelTextModel"); //$NON-NLS-1$

        this.add(getLabel());
        this.add(getFileUpload());
        this.add(getUploader());
    }

    private void deleteTemporaryFile() {
        if (this.temporary != null) {
            this.temporary.delete();
        }
    }

    private FileUploadField getFileUpload() {
        if (this.fileUpload == null) {
            this.fileUpload = new FileUploadField("flieUpload"); //$NON-NLS-1$
        }
        return this.fileUpload;
    }

    private Label getLabel() {
        if (this.label == null) {
            this.label = new Label("label", this.labelTextModel); //$NON-NLS-1$
        }
        return this.label;
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

    private static File save(final InputStream pData) {
        try {
            final File file = File.createTempFile(FileUploadPanel.class.getName(), ".dat"); //$NON-NLS-1$
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

    private class Handler implements Serializable {
        private static final long serialVersionUID = -2699703816024152769L;

        void upload() {
            try (InputStream data = getDataFromFileUpload(getFileUpload())) {
                if (data == null) {
                    return;
                }
                final File temp = save(data);
                deleteTemporaryFile();
                FileUploadPanel.this.temporary = temp;

            } catch (final IOException e) {
                throw ExceptionUtil.rethrow(e);
            }
        }
    }
}

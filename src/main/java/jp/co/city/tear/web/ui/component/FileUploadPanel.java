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
    private static final long serialVersionUID = -2121159238236955334L;

    private final IOperation  onRemove;

    private final Handler     handler          = new Handler();

    private File              temporary;

    private FileUploadField   fileUpload;
    private Button            uploader;
    private Button            remover;
    private Button            restorer;

    /**
     * @param pId -
     * @param pOnRemove -
     */
    public FileUploadPanel(final String pId, final IOperation pOnRemove) {
        super(pId);

        this.onRemove = pOnRemove;

        this.add(getFileUpload());
        this.add(getUploader());
        this.add(getRemover());
        this.add(getRestorer());
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
            throw ExceptionUtil.rethrow(e);
        }
    }

    private void deleteTemporaryFile() {
        if (this.temporary != null) {
            this.temporary.delete();
        }
    }

    private FileUploadField getFileUpload() {
        if (this.fileUpload == null) {
            this.fileUpload = new FileUploadField("fileUpload"); //$NON-NLS-1$
        }
        return this.fileUpload;
    }

    @SuppressWarnings("serial")
    private Button getRemover() {
        if (this.remover == null) {
            this.remover = new Button("remover") { //$NON-NLS-1$
                @Override
                public void onSubmit() {
                    deleteTemporaryFile();
                    if (FileUploadPanel.this.onRemove != null) {
                        FileUploadPanel.this.onRemove.run();
                    }
                }
            };
        }
        return this.remover;
    }

    @SuppressWarnings("serial")
    private Button getRestorer() {
        if (this.restorer == null) {
            this.restorer = new Button("restorer") { //$NON-NLS-1$
                @Override
                public void onSubmit() {
                    deleteTemporaryFile();
                }
            };
        }
        return this.restorer;
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

    /**
     * @author jabaraster
     */
    public interface IOperation extends Runnable, Serializable {
        //
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

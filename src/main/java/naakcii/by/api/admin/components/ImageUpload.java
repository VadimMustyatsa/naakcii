package naakcii.by.api.admin.components;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import naakcii.by.api.admin.views.CrudForm;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class ImageUpload extends HorizontalLayout {

    private final String uploadLocation;

    private final String pathPattern;

    private final MultiFileMemoryBuffer buffer;
    private final Upload upload;

    private final CrudForm form;

    public ImageUpload(CrudForm form, String uploadLocation, String pathPattern) {
        this.form = form;
        this.uploadLocation = uploadLocation;
        this.pathPattern = pathPattern;
        buffer = new MultiFileMemoryBuffer();
        upload = new Upload(buffer);
        uploadImage();
        add(upload);
    }

    public void setAcceptedFileTypes(String... fileTypes) {
        upload.setAcceptedFileTypes(fileTypes);
    }

    private void uploadImage() {
        upload.setMaxFileSize(10000000);
        upload.setMaxFiles(1);
        upload.addSucceededListener(event-> {
            try {
                byte[] buf = new byte[(int)event.getContentLength()];
                InputStream is = buffer.getInputStream(event.getFileName());
                is.read(buf);
                File targetFile = new File(uploadLocation+event.getFileName());
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buf);
                form.getImageField().setValue(pathPattern + event.getFileName());
                outStream.flush();
                outStream.close();
                String fileName = event.getFileName();
                if(fileName.indexOf('%')>=0) {
                    fileName = fileName.replace("%", "proc");
                    File renamedFile = new File(uploadLocation + fileName);
                    if (renamedFile.exists()) {
                        form.getImageField().setValue(pathPattern + fileName);
                    } else {
                        FileUtils.moveFile(FileUtils.getFile(targetFile), FileUtils.getFile(renamedFile));
                        form.getImageField().setValue(pathPattern + fileName);
                    }
                }
            } catch (IOException ex) {
                Notification.show("Error");
                ex.printStackTrace();
            }
        });
    }
}

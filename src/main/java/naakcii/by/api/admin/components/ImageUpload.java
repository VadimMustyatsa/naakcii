package naakcii.by.api.admin.components;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import naakcii.by.api.admin.views.CrudForm;

import java.io.*;

public class ImageUpload extends HorizontalLayout {

    private String uploadLocation;

    private String pathPattern;

    private MultiFileMemoryBuffer buffer;
    private Upload upload;

    private CrudForm form;

    public ImageUpload(CrudForm form, String uploadLocation, String pathPattern) {
        this.form = form;
        this.uploadLocation = uploadLocation;
        this.pathPattern = pathPattern;
        buffer = new MultiFileMemoryBuffer();
        upload = new Upload(buffer);
        uploadImage();
        add(upload);
    }

    public void uploadImage() {
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
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
            } catch (IOException ex) {
                Notification.show("Error");
                ex.printStackTrace();
            }
        });
    }
}

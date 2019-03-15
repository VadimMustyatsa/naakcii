package naakcii.by.api.admin.views;

import com.vaadin.flow.component.textfield.TextField;
import naakcii.by.api.admin.components.FormButtonsBar;

public interface CrudForm {

    FormButtonsBar getButtons();

    TextField getImageField();
}

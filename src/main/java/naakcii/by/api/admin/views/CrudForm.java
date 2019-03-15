package naakcii.by.api.admin.views;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.entity.AbstractDTOEntity;

public interface CrudForm<E extends AbstractDTOEntity> {

    FormButtonsBar getButtons();

    TextField getImageField();

    void setBinder(Binder<E> binder, E entity);

    E getDTO();

    String getChangedDTOName();
}

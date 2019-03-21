package naakcii.by.api.admin.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import naakcii.by.api.admin.views.CrudView;
import naakcii.by.api.entity.AbstractDTOEntity;

public class SearchBar<E  extends AbstractDTOEntity> extends HorizontalLayout {

    private final CrudView<E> crudView;

    public SearchBar(CrudView<E> crudView, Component filterComponent) {
        this.crudView = crudView;
        TextField search = new TextField("Поиск");
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.setPlaceholder("Введите название");
        search.setClearButtonVisible(true);
        search.addValueChangeListener(e-> getCrudView().updateList(e.getValue()));
        Button addEntity = new Button("Добавить");
        addEntity.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        addEntity.setHeight("70%");
        addEntity.addClickListener(e -> onComponentEvent());

        add(search, filterComponent, addEntity);
        setWidth("100%");
        addEntity.getStyle().set("margin-left", "auto").set("margin-top", "auto");
    }

    private void onComponentEvent() {
        getCrudView().getGrid().asSingleSelect().clear();
        getCrudView().getForm().setBinder(getCrudView().getBinder(), getCrudView().getCrudService().createNewDTO());
        getCrudView().getDialog().open();
    }

    private CrudView<E> getCrudView() {
        return crudView;
    }
}

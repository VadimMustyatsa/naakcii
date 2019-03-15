package naakcii.by.api.admin.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import naakcii.by.api.entity.AbstractDTOEntity;
import naakcii.by.api.service.CrudService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

public abstract class CrudView<E extends AbstractDTOEntity> extends VerticalLayout implements HasUrlParameter<String> {

    @Value("${adminka.token}")
    private String adminkaPath;

    private final CrudService<E> crudService;
    private final Dialog dialog = new Dialog();
    private final CrudForm<E> form;
    private final Grid<E> grid;

    protected abstract Binder<E> getBinder();

    protected abstract void setupGrid();

    public CrudView(CrudForm<E> form, CrudService<E> crudService) {
        this.form = form;
        this.crudService = crudService;
        setSizeFull();

        dialog.add((Component) getForm());

        TextField search = new TextField("Поиск");
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.setPlaceholder("Введите название");
        search.addValueChangeListener(e-> updateList(e.getValue()));
        Button addEntity = new Button("Добавить");
        addEntity.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        addEntity.setHeight("70%");
        addEntity.addClickListener(e -> onComponentEvent());

        HorizontalLayout toolbar = new HorizontalLayout(search, addEntity);
        toolbar.setWidth("100%");
        addEntity.getStyle().set("margin-left", "auto").set("margin-top", "auto");
        
        grid = new Grid<>();
        setupGrid();
        updateList(null);
        add(toolbar, grid);

        grid.asSingleSelect().addValueChangeListener(e-> {
            getForm().setBinder(getBinder(), e.getValue());
            getBinder().readBean(e.getValue());
            dialog.open();
        });

        setupEventListeners();
        grid.getDataProvider().refreshAll();

        getForm().getButtons().getSaveButton().addClickShortcut(Key.ENTER);
    }

    private void setupEventListeners() {
        getForm().getButtons().addSaveListener(e -> save());
        getForm().getButtons().addCancelListener(e -> cancel());
        getForm().getButtons().addDeleteListener(e -> delete());

        getDialog().getElement().addEventListener("opened-changed", e -> {
            if(!getDialog().isOpened()) {
                cancel();
            }
        });
    }

    private void save() {
        E entityDTO = getForm().getDTO();
        boolean isValid = getBinder().writeBeanIfValid(entityDTO);
        if(isValid) {
            crudService.saveDTO(entityDTO);
            Notification.show(getForm().getChangedDTOName() + " сохранён");
            closeUpdate();
        }
    }

    private void cancel() {
        getDialog().close();
        grid.getDataProvider().refreshAll();
    }

    private void delete() {
        E entityDTO = getForm().getDTO();
        crudService.deleteDTO(entityDTO);
        Notification.show(getForm().getChangedDTOName() + " удалён");
        closeUpdate();
    }

    private void closeUpdate() {
        grid.asSingleSelect().clear();
        getDialog().close();
        updateList(null);
        grid.getDataProvider().refreshAll();
    }

    private void updateList(String search) {
        if(StringUtils.isEmpty(search)) {
            grid.setItems(crudService.findAllDTOs());
        } else {
            grid.setItems(crudService.searchName(search));
        }
    }

    private CrudForm<E> getForm() {
        return form;
    }

    private Dialog getDialog() {
        return dialog;
    }

    public Grid<E> getGrid() {return grid;}

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (!parameter.equals(adminkaPath)) {
            throw new IllegalArgumentException();
        }
    }

    private void onComponentEvent() {
        grid.asSingleSelect().clear();
        getForm().setBinder(getBinder(), crudService.createNewDTO());
        dialog.open();
    }
}

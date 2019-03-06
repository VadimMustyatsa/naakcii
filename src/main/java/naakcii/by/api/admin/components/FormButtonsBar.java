package naakcii.by.api.admin.components;


import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

public class FormButtonsBar extends HorizontalLayout {

    private Button save;
    private Button delete;
    private Button cancel;

    public FormButtonsBar() {
        save = new Button("Сохранить");
        delete = new Button("Удалить");
        cancel = new Button("Отменить");
        add(save, cancel, delete);
        setAlignItems(Alignment.STRETCH);
        getStyle().set("margin-top", "30px");
        save.setEnabled(false);
    }

    public static class SaveEvent extends ComponentEvent<FormButtonsBar> {
        public SaveEvent(FormButtonsBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return save.addClickListener(e -> listener.onComponentEvent(new SaveEvent(this, true)));
    }

    public static class CancelEvent extends ComponentEvent<FormButtonsBar> {
        public CancelEvent(FormButtonsBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return cancel.addClickListener(e -> listener.onComponentEvent(new CancelEvent(this, true)));
    }

    public static class DeleteEvent extends ComponentEvent<FormButtonsBar> {
        public DeleteEvent(FormButtonsBar source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return delete.addClickListener(e -> listener.onComponentEvent(new DeleteEvent(this, true)));
    }

    public Button getSaveButton() {
        return save;
    }
}

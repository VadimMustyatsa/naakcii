package naakcii.by.api.admin.views.unitOfMeasure;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.unitofmeasure.UnitOfMeasureDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UnitOfMeasureForm extends VerticalLayout implements CrudForm<UnitOfMeasureDTO> {

    private final TextField name;
    private final TextField step;
    private final FormButtonsBar buttons;

    private UnitOfMeasureDTO unitOfMeasureDTO;

    @Autowired
    public UnitOfMeasureForm() {
        setSizeFull();
        name = new TextField("Единица измерения");
        name.focus();
        name.setWidth("100%");

        step = new TextField("Шаг изменения");

        buttons = new FormButtonsBar();
        buttons.getDeleteButton().setEnabled(false);

        add(name, step, buttons);
    }

    @Override
    public FormButtonsBar getButtons() {
        return buttons;
    }

    @Override
    public TextField getImageField() {
        return null;
    }

    @Override
    public void setBinder(Binder<UnitOfMeasureDTO> binder, UnitOfMeasureDTO unitOfMeasureDTO) {
        this.unitOfMeasureDTO = unitOfMeasureDTO;
        binder.forField(name)
                .asRequired("Поле не может быть пустым")
                .withValidator(field -> field.trim().length()>=2, "Не менее 2-х символов")
                .withValidator(field -> field.trim().length()<=10, "Не более 10 символов")
                .bind(UnitOfMeasureDTO::getName, UnitOfMeasureDTO::setName);
        binder.forField(step)
                .withValidator(new RegexpValidator("Не более 3-х цифр и не более 1 десятичного знака","[0-9]{1,3}(\\.[0-9])?"))
                .withConverter(new StringToBigDecimalConverter("Дожны быть только числа"))
                .bind(UnitOfMeasureDTO::getStep, UnitOfMeasureDTO::setStep);
    }

    @Override
    public UnitOfMeasureDTO getDTO() {
        return unitOfMeasureDTO;
    }

    @Override
    public String getChangedDTOName() {
        return unitOfMeasureDTO.getName();
    }
}

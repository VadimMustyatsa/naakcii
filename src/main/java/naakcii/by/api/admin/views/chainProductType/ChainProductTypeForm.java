package naakcii.by.api.admin.views.chainProductType;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.admin.components.ImageUpload;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.chainproducttype.ChainProductTypeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChainProductTypeForm extends VerticalLayout implements CrudForm<ChainProductTypeDTO> {

    private final TextField name;
    private final TextField tooltip;
    private final TextField synonym;

    private final FormButtonsBar buttons;
    private ChainProductTypeDTO chainProductTypeDTO;

    @Autowired
    public ChainProductTypeForm(@Value("${upload.location}") String uploadLocation, @Value("${images.path.pattern}") String pathPattern) {
        setSizeFull();
        name = new TextField("Акция");
        name.focus();
        name.setWidth("100%");

        tooltip = new TextField("Тултип");
        tooltip.setWidth("100%");
        uploadLocation = uploadLocation + "CPTTooltips/";
        pathPattern = pathPattern + "CPTTooltips/";
        ImageUpload imageUpload = new ImageUpload(this, uploadLocation, pathPattern);

        synonym = new TextField("Синоним");
        synonym.setWidth("100%");
        buttons = new FormButtonsBar();

        add(name, tooltip, imageUpload, synonym, buttons);
    }

    @Override
    public FormButtonsBar getButtons() {
        return buttons;
    }

    @Override
    public TextField getImageField() {
        return tooltip;
    }

    @Override
    public void setBinder(Binder<ChainProductTypeDTO> binder, ChainProductTypeDTO chainProductTypeDTO) {
        this.chainProductTypeDTO = chainProductTypeDTO;
        binder.forField(name)
                .asRequired("Поле не может быть пустым")
                .withValidator(field -> field.trim().length()>=3, "Не менее 3-х символов")
                .withValidator(field -> field.trim().length()<=25, "Не более 25 символов")
                .bind(ChainProductTypeDTO::getName, ChainProductTypeDTO::setName);
        binder.forField(synonym)
                .asRequired("Поле не может быть пустым")
                .withValidator(field -> field.trim().length()>=3, "Не менее 3-х символов")
                .withValidator(field -> field.trim().length()<=25, "Не более 25 символов")
                .bind(ChainProductTypeDTO::getSynonym, ChainProductTypeDTO::setSynonym);
        binder.forField(tooltip)
                .withValidator(field -> field.length()<=255, "Не более 255 символов")
                .bind(ChainProductTypeDTO::getTooltip, ChainProductTypeDTO::setTooltip);
    }

    @Override
    public ChainProductTypeDTO getDTO() {
        return chainProductTypeDTO;
    }

    @Override
    public String getChangedDTOName() {
        return chainProductTypeDTO.getName();
    }
}

package naakcii.by.api.admin.views.chain;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.admin.components.ImageUpload;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.chain.ChainDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChainForm extends VerticalLayout implements CrudForm<ChainDTO> {

    private final TextField name;
    private final TextField logo;
    private final TextField link;
    private final TextField synonym;
    private final Checkbox isActive;

    private final FormButtonsBar buttons;
    private ChainDTO chainDTO;

    public ChainForm(@Value("${upload.location}") String uploadLocation, @Value("${images.path.pattern}") String pathPattern) {
        setSizeFull();
        name = new TextField("Торговая сеть");
        name.focus();
        name.setWidth("100%");

        logo = new TextField("Адрес логотипа");
        logo.setWidth("100%");
        uploadLocation = uploadLocation + "ChainsLogos/";
        pathPattern = pathPattern + "ChainsLogos/";
        ImageUpload uploadImage = new ImageUpload(this, uploadLocation, pathPattern);

        isActive = new Checkbox("Активна");
        link = new TextField("Сайт");
        link.setWidth("100%");
        synonym = new TextField("Синоним");
        synonym.setWidth("100%");
        buttons = new FormButtonsBar();
        buttons.getDeleteButton().setEnabled(false);
        add(name, logo, uploadImage, link, synonym, isActive, buttons);
    }
    @Override
    public FormButtonsBar getButtons() {
        return buttons;
    }

    @Override
    public TextField getImageField() {
        return logo;
    }

    @Override
    public void setBinder(Binder<ChainDTO> binder, ChainDTO chainDTO) {
        this.chainDTO = chainDTO;
        binder.forField(name)
                .asRequired("Поле не может быть пустым")
                .withValidator(field -> field.trim().length()>=3, "Не менее 3-х символов")
                .withValidator(field -> field.trim().length()<=25, "Не более 25 символов")
                .bind(ChainDTO::getName, ChainDTO::setName);
        binder.forField(logo)
                .withValidator(field -> field.length()<=255, "Не более 255 символов")
                .bind(ChainDTO::getLogo, ChainDTO::setLogo);
        binder.forField(synonym)
                .asRequired("Поле не может быть пустым")
                .withValidator(field -> field.trim().length()>=3, "Не менее 3-х символов")
                .withValidator(field -> field.trim().length()<=25, "Не более 25 символов")
                .bind(ChainDTO::getSynonym, ChainDTO::setSynonym);
        binder.forField(link)
                .asRequired("Поле не может быть пустым")
                .withValidator(field -> field.trim().length()>=10, "Не менее 10 символов")
                .withValidator(field -> field.trim().length()<=255, "Не более 255 символов")
                .bind(ChainDTO::getLink, ChainDTO::setLink);
        binder.bind(isActive, "isActive");
    }

    @Override
    public ChainDTO getDTO() {
        return chainDTO;
    }

    @Override
    public String getChangedDTOName() {
        return chainDTO.getName();
    }
}

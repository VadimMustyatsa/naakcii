package naakcii.by.api.admin.views.country;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.country.CountryDTO;
import naakcii.by.api.country.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CountryForm extends VerticalLayout implements CrudForm<CountryDTO> {

    private final CountryService countryService;
    private final TextField name;
    private final TextField alphaCode2;
    private final TextField alphaCode3;
    private final FormButtonsBar buttons;

    private CountryDTO countryDTO;

    @Autowired
    public CountryForm(CountryService countryService) {
        this.countryService = countryService;
        name = new TextField("Страна");
        name.focus();
        alphaCode2 = new TextField("Код2");
        alphaCode3 = new TextField("Код3");
        buttons = new FormButtonsBar();

        add(name, alphaCode2, alphaCode3, buttons);
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
    public void setBinder(Binder<CountryDTO> binder, CountryDTO countryDTO) {
        this.countryDTO = countryDTO;
        binder.forField(name)
                .asRequired("Введите название страны")
                .withValidator(field -> field.trim().length()>=3, "Длина не менее 3-х букв")
                .withValidator(field -> field.trim().length()<=50, "Длина не более 50-ти букв")
                .withValidator(field -> countryService.findByName(field)==null, "Эта страна уже существует")
                .bind(CountryDTO::getName, CountryDTO::setName);
        binder.forField(alphaCode2)
                .asRequired("Поле не может быть пустым")
                .withValidator(field-> field.length()==2, "Длина должна быть равна 2-м символам")
                .withValidator(field -> !countryService.findByAlphaCode2(field).isPresent(), "Этот код уже существует")
                .bind(CountryDTO::getAlphaCode2, CountryDTO::setAlphaCode2UpperCase);
        binder.forField(alphaCode3)
                .asRequired("Поле не может быть пустым")
                .withValidator(field-> field.length()==3, "Длина должна быть равна 3-м символам")
                .withValidator(field -> !countryService.findByAlphaCode3(field).isPresent(), "Этот код уже существует")
                .bind(CountryDTO::getAlphaCode3, CountryDTO::setAlphaCode3UpperCase);
    }

    @Override
    public CountryDTO getDTO() {
        return countryDTO;
    }

    @Override
    public String getChangedDTOName() {
        return countryDTO.getName();
    }
}

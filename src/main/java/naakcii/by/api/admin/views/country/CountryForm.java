package naakcii.by.api.admin.views.country;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.country.CountryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CountryForm extends VerticalLayout implements CrudForm {

    private TextField name;
    private TextField alphaCode2;
    private TextField alphaCode3;
    private FormButtonsBar buttons;

    private CountryDTO countryDTO;

    @Autowired
    public CountryForm() {
        name = new TextField("Страна");
        name.focus();
        alphaCode2 = new TextField("Код2");
        alphaCode3 = new TextField("Код3");
        buttons = new FormButtonsBar();

        add(name, alphaCode2, alphaCode3, buttons);
    }

    public void setBinder(Binder<CountryDTO> binder, CountryDTO countryDTO) {
        this.countryDTO = countryDTO;
        binder.forField(name)
                .asRequired("Введите название страны")
                .withValidator(field -> field.length()>=3, "Длина не менее 3-х букв")
                .withValidator(field -> field.length()<=50, "Длина не более 50-ти букв")
                .bind(CountryDTO::getName, CountryDTO::setName);
        binder.forField(alphaCode2)
                .asRequired("Поле не может быть пустым")
                .withValidator(field-> field.length()==2, "Длина должна быть равна 2-м символам")
                .bind(CountryDTO::getAlphaCode2, CountryDTO::setAlphaCode2UpperCase);
        binder.forField(alphaCode3)
                .asRequired("Поле не может быть пустым")
                .withValidator(field-> field.length()==3, "Длина должна быть равна 3-м символам")
                .bind(CountryDTO::getAlphaCode3, CountryDTO::setAlphaCode3UpperCase);
    }
    @Override
    public FormButtonsBar getButtons() {
        return buttons;
    }

    public CountryDTO getCountryDTO() {
        return countryDTO;
    }
}

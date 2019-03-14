package naakcii.by.api.admin.views.country;

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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.country.CountryDTO;
import naakcii.by.api.country.CountryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_COUNTRY, layout = MainView.class)
@PageTitle(AppConsts.TITLE_COUNTRY)
public class CountryView extends VerticalLayout implements HasUrlParameter<String> {

    private CountryService countryService;
    private Grid<CountryDTO> grid;
    private TextField search;
    private Button addCountry;

    private CountryForm form;
    private Dialog dialog;
    Binder<CountryDTO> binder;

    @Value("${adminka.token}")
    private String adminkaPath;

    @Autowired
    public CountryView(CountryService countryService, CountryForm form) {
        this.countryService = countryService;
        this.form = form;

        dialog = new Dialog();
        dialog.add(form);
        binder = new Binder<>(CountryDTO.class);
        search = new TextField("Поиск страны");
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.setPlaceholder("Введите название страны");
        search.setWidth("50%");
        search.addValueChangeListener(e-> {
           grid.setItems(countryService.searchName(e.getValue()));
        });
        addCountry = new Button("Добавить страну");
        addCountry.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        addCountry.setHeight("70%");
        addCountry.addClickListener(e-> {
            grid.asSingleSelect().clear();
            getForm().setBinder(binder, new CountryDTO());
            dialog.open();
        });

        HorizontalLayout toolbar = new HorizontalLayout(search, addCountry);
        toolbar.setWidth("100%");
        addCountry.getStyle().set("margin-left", "auto").set("margin-top", "auto");

        grid = new Grid<>();
        grid.addColumn(CountryDTO::getName).setHeader("Страна");
        grid.addColumn(CountryDTO::getAlphaCode2).setHeader("Код2");
        grid.addColumn(CountryDTO::getAlphaCode3).setHeader("Код3");
        updateList(null);

        add(toolbar, grid);
        setSizeFull();

        grid.asSingleSelect().addValueChangeListener(e-> {
           getForm().setBinder(binder, e.getValue());
           binder.readBean(e.getValue());
            dialog.open();
        });

        setupEventListeners();
        grid.getDataProvider().refreshAll();

        getForm().getButtons().getSaveButton().addClickShortcut(Key.ENTER);
    }

    private void updateList(String search) {
        if (StringUtils.isEmpty(search)) {
            grid.setItems(countryService.findAllDTOs());
        }
    }

    public void setupEventListeners() {
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
        CountryDTO countryDTO = getForm().getCountryDTO();
        boolean isValid = binder.writeBeanIfValid(countryDTO);
        if(isValid) {
            countryService.saveCountryDTO(countryDTO);
            Notification.show(countryDTO.getName() + " сохранён");
            closeUpdate();
        }
    }

    private void cancel() {
        getDialog().close();
        grid.getDataProvider().refreshAll();
    }

    private void delete() {
        CountryDTO countryDTO = getForm().getCountryDTO();
        countryService.delete(countryDTO);
        Notification.show(countryDTO.getName() + " удалён");
        closeUpdate();
    }

    private void closeUpdate() {
        grid.asSingleSelect().clear();
        getDialog().close();
        updateList(null);
        grid.getDataProvider().refreshAll();
    }

    private CountryForm getForm() {
        return form;
    }

    private Dialog getDialog() {
        return dialog;
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (!parameter.equals(adminkaPath)) {
            throw new IllegalArgumentException();
        }
    }
}

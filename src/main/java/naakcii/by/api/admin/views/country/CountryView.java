package naakcii.by.api.admin.views.country;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.admin.views.CrudView;
import naakcii.by.api.country.CountryDTO;
import naakcii.by.api.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_COUNTRY, layout = MainView.class)
@PageTitle(AppConsts.TITLE_COUNTRY)
public class CountryView extends CrudView<CountryDTO> {

    private Binder<CountryDTO> binder;

    @Autowired
    public CountryView(CrudForm<CountryDTO> form, CrudService<CountryDTO> crudService) {
        super(form, crudService);
        binder = new Binder<>(CountryDTO.class);
    }

    @Override
    protected Binder<CountryDTO> getBinder() {
        return binder;
    }

    @Override
    protected void setupGrid() {
        getGrid().addColumn(CountryDTO::getName).setHeader("Страна").setSortable(true);
        getGrid().addColumn(CountryDTO::getAlphaCode2).setHeader("Код2").setSortable(true);
        getGrid().addColumn(CountryDTO::getAlphaCode3).setHeader("Код3").setSortable(true);
    }
}

package naakcii.by.api.admin.views.subcategory;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.admin.views.CrudView;
import naakcii.by.api.service.CrudService;
import naakcii.by.api.subcategory.SubcategoryDTO;
import naakcii.by.api.subcategory.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_SUBCATEGORY, layout = MainView.class)
@PageTitle(AppConsts.TITLE_SUBCATEGORY)
public class SubcategoryView extends CrudView<SubcategoryDTO> {

    private final Binder<SubcategoryDTO> binder;

    @Autowired
    public SubcategoryView(CrudForm<SubcategoryDTO> form, CrudService<SubcategoryDTO> crudService, SubcategoryService subcategoryService) {
        super(form, crudService, new ComboBox<String>("Фильтр"));
        ComboBox<String> filter = (ComboBox<String>) getFilterComponent();
        filter.setItems("Активные", "Неактивные");
        filter.setValue("Активные");
        getGrid().setItems(subcategoryService.checkIsActive(filter.getValue()));
        filter.addValueChangeListener(e-> getGrid().setItems(subcategoryService.checkIsActive(e.getValue())));
        binder = new Binder<>(SubcategoryDTO.class);
    }

    @Override
    public Binder<SubcategoryDTO> getBinder() {
        return binder;
    }

    @Override
    protected void setupGrid() {
        getGrid().addColumn(SubcategoryDTO::getName).setSortable(true).setHeader("Подкатегория");
        getGrid().addColumn(SubcategoryDTO::getCategoryName).setSortable(true).setHeader("Категория");
        getGrid().addColumn(SubcategoryDTO::getPriority).setSortable(true).setHeader("Порядок отображения");
    }
}

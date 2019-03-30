package naakcii.by.api.admin.views.subcategory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.admin.views.CrudView;
import naakcii.by.api.category.CategoryService;
import naakcii.by.api.service.CrudService;
import naakcii.by.api.subcategory.SubcategoryDTO;
import naakcii.by.api.subcategory.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_SUBCATEGORY, layout = MainView.class)
@PageTitle(AppConsts.TITLE_SUBCATEGORY)
public class SubcategoryView extends CrudView<SubcategoryDTO> {

    private final Binder<SubcategoryDTO> binder;
    private final SubcategoryService subcategoryService;

    private String subcategoryName;
    private String activeStatus = "Активные";
    private String categoryName;

    @Autowired
    public SubcategoryView(CrudForm<SubcategoryDTO> form, CrudService<SubcategoryDTO> crudService,
                           SubcategoryService subcategoryService, CategoryService categoryService) {
        super(form, crudService, null);
        this.subcategoryService = subcategoryService;
        getSearchBar().setVisible(false);

        TextField searchSubcategory = new TextField("Поиск по товару");
        searchSubcategory.setValueChangeMode(ValueChangeMode.EAGER);
        searchSubcategory.setPlaceholder("Введите товар");
        searchSubcategory.setClearButtonVisible(true);
        searchSubcategory.addValueChangeListener(e-> {
            subcategoryName = e.getValue();
            filterApply();
        });

        ComboBox<String> filterActive = new ComboBox<>("Фильтр");
        filterActive.setItems("Активные", "Неактивные");
        filterActive.setValue("Активные");
        getGrid().setItems(subcategoryService.checkIsActive(filterActive.getValue()));
        filterActive.addValueChangeListener(e-> {
            activeStatus = e.getValue();
            filterApply();
        });

        ComboBox<String> searchCategory = new ComboBox<>("Категория");
        searchCategory.setPlaceholder("Название категории");
        searchCategory.setItems(categoryService.getAllCategoriesNames());
        searchCategory.addValueChangeListener(e -> {
            categoryName = e.getValue();
            filterApply();
        });

        Button addEntity = new Button("Добавить");
        addEntity.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        addEntity.setHeight("70%");
        addEntity.addClickListener(e -> onComponentEvent());
        addEntity.getStyle().set("margin-left", "auto").set("margin-top", "auto");

        HorizontalLayout searchBarCustom = new HorizontalLayout(searchSubcategory, filterActive, searchCategory, addEntity);
        searchBarCustom.setWidth("100%");
        add(searchBarCustom, getGrid());
        binder = new Binder<>(SubcategoryDTO.class);
    }

    private void filterApply() {
        boolean isActive = false;
        if (activeStatus!=null && activeStatus.equals("Активные")) {
                isActive = true;
        }
        getGrid().setItems(subcategoryService.findAllByFilter(subcategoryName, isActive, categoryName));
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

    private void onComponentEvent() {
        getGrid().asSingleSelect().clear();
        getForm().setBinder(getBinder(), getCrudService().createNewDTO());
        getDialog().open();
    }
}

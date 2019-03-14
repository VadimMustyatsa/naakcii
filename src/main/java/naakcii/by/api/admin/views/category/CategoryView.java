package naakcii.by.api.admin.views.category;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.category.CategoryDTO;
import naakcii.by.api.category.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_CATEGORY, layout = MainView.class)
@PageTitle(AppConsts.TITLE_CATEGORY)
public class CategoryView extends VerticalLayout implements HasUrlParameter<String> {

    @Value("${adminka.token}")
    private String adminkaPath;
    private TextField search;
    private Button addCategory;
    private CategoryService categoryService;

    private Grid<CategoryDTO> grid;

    @Autowired
    public CategoryView(CategoryService categoryService) {
        this.categoryService = categoryService;
        setSizeFull();

        search = new TextField("Поиск категории");
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.setPlaceholder("Введите название категории");
        search.setWidth("50%");
        search.addValueChangeListener(e-> updateList(e.getValue()));
        addCategory = new Button("Добавить категорию");
        addCategory.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        addCategory.setHeight("70%");

        HorizontalLayout toolbar = new HorizontalLayout(search, addCategory);
        toolbar.setWidth("100%");
        addCategory.getStyle().set("margin-left", "auto").set("margin-top", "auto");

        grid = new Grid<>();
        grid.addColumn(CategoryDTO::getName).setHeader("Категория");
        grid.addColumn(CategoryDTO::getPriority).setHeader("Порядок отображения");
        grid.addColumn(new ComponentRenderer<>(categoryDTO -> {
            Checkbox isActive = new Checkbox();
            isActive.setValue(categoryDTO.getIsActive());
            return isActive;
        })).setHeader("Активна");
        updateList(null);
        add(toolbar, grid);
    }

    private void updateList(String search) {
        if(StringUtils.isEmpty(search)) {
            grid.setItems(categoryService.findAllDTOs());
        } else {
            grid.setItems(categoryService.searchName(search));
        }
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (!parameter.equals(adminkaPath)) {
            throw new IllegalArgumentException();
        }
    }
}

package naakcii.by.api.admin.views.category;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
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

    @Value("${no.image}")
    private String noImage;

    private CategoryService categoryService;
    private CategoryForm form;
    private Binder<CategoryDTO> binder;
    private Dialog dialog;
    private TextField search;
    private Button addCategory;

    private Grid<CategoryDTO> grid;

    @Autowired
    public CategoryView(CategoryService categoryService, CategoryForm form) {
        this.categoryService = categoryService;
        this.form = form;
        setSizeFull();

        binder = new Binder<>(CategoryDTO.class);
        dialog = new Dialog();
        dialog.add(form);
        search = new TextField("Поиск категории");
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.setPlaceholder("Введите название категории");
        search.setWidth("50%");
        search.addValueChangeListener(e-> updateList(e.getValue()));
        addCategory = new Button("Добавить категорию");
        addCategory.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        addCategory.setHeight("70%");
        addCategory.addClickListener(e-> {
            grid.asSingleSelect().clear();
            getForm().setBinder(binder, new CategoryDTO());
            dialog.open();
        });

        HorizontalLayout toolbar = new HorizontalLayout(search, addCategory);
        toolbar.setWidth("100%");
        addCategory.getStyle().set("margin-left", "auto").set("margin-top", "auto");

        grid = new Grid<>();
        grid.addColumn(new ComponentRenderer<>(categoryDTO -> {
            if ((categoryDTO.getIcon() != null) && !StringUtils.isEmpty(categoryDTO.getIcon())) {
                Image image = new Image(categoryDTO.getIcon(), categoryDTO.getName());
                image.setWidth("50px");
                image.setHeight("50px");
                return image;
            } else {
                Image imageEmpty = new Image(noImage, "No image");
                imageEmpty.setHeight("50px");
                imageEmpty.setWidth("50px");
                return imageEmpty;
            }}
        ))
                .setHeader("Изображение").setFlexGrow(0);
        grid.addColumn(CategoryDTO::getName).setHeader("Категория").setSortable(true);
        grid.addColumn(CategoryDTO::getPriority).setHeader("Порядок отображения").setSortable(true);
        grid.addColumn(new ComponentRenderer<>(categoryDTO -> {
            Checkbox isActive = new Checkbox();
            isActive.setValue(categoryDTO.getIsActive());
            return isActive;
        })).setHeader("Активна").setSortable(true);
        updateList(null);
        add(toolbar, grid);

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
        if(StringUtils.isEmpty(search)) {
            grid.setItems(categoryService.findAllDTOs());
        } else {
            grid.setItems(categoryService.searchName(search));
        }
    }

    private void setupEventListeners() {
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
        CategoryDTO categoryDTO = getForm().getCategoryDTO();
        boolean isValid = binder.writeBeanIfValid(categoryDTO);
        if(isValid) {
            categoryService.saveCategoryDTO(categoryDTO);
            Notification.show(categoryDTO.getName() + " сохранён");
            closeUpdate();
        }
    }

    private void cancel() {
        getDialog().close();
        grid.getDataProvider().refreshAll();
    }

    private void delete() {
        CategoryDTO categoryDTO = getForm().getCategoryDTO();
       categoryService.delete(categoryDTO);
        Notification.show(categoryDTO.getName() + " удалён");
        closeUpdate();
    }

    private void closeUpdate() {
        grid.asSingleSelect().clear();
        getDialog().close();
        updateList(null);
        grid.getDataProvider().refreshAll();
    }

    private CategoryForm getForm() {
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

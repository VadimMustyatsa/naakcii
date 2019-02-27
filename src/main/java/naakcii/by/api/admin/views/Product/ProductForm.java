package naakcii.by.api.admin.views.Product;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.category.CategoryDTO;
import naakcii.by.api.category.CategoryService;
import naakcii.by.api.product.Product;
import naakcii.by.api.product.ProductService;
import naakcii.by.api.subcategory.Subcategory;
import naakcii.by.api.subcategory.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductForm extends VerticalLayout implements CrudForm {

    private CategoryService categoryService;
    private SubcategoryService subcategoryService;
    private Product product;
    private Subcategory subcategory;

    private TextField name;
    private ComboBox<String> categoryName;
    private ComboBox<String> subcategoryName;
    private TextField barcode;
    private TextField unitOfMeasure;
    private TextField manufacturer;
    private TextField brand;
    private TextField countryOfOrigin;
    private Checkbox isActive;
    private FormButtonsBar buttons;

    @Autowired
    public ProductForm(CategoryService categoryService, SubcategoryService subcategoryService) {
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;

        name = new TextField("Наименование товара");
        categoryName = new ComboBox<>("Категория");
        categoryName.setItems(getAllCategoriesNames());
        categoryName.addValueChangeListener(event -> {
           subcategoryName.setItems(getAllSubcategoriesNames(event.getValue()));
        });
        subcategoryName = new ComboBox<>("Подкатегория");
        subcategoryName.addValueChangeListener(event -> {
            subcategory = subcategoryService.findByName(event.getValue());
        });
        HorizontalLayout categories = new HorizontalLayout(categoryName, subcategoryName);

        barcode = new TextField("Штрих-код");
        barcode.setReadOnly(true);
        unitOfMeasure = new TextField("Единица измерения");
        HorizontalLayout layout1 = new HorizontalLayout(barcode, unitOfMeasure);

        manufacturer = new TextField("Производитель");
        manufacturer.setReadOnly(true);
        brand = new TextField("Торговая марка");
        brand.setReadOnly(true);
        HorizontalLayout layout2 = new HorizontalLayout(manufacturer, brand);

        countryOfOrigin = new TextField("Страна происхождения");
        isActive = new Checkbox("Акционный товар");
        isActive.setReadOnly(true);
        buttons = new FormButtonsBar();
        add(name, categories, layout1, layout2,
                countryOfOrigin, isActive, buttons);
        setWidth("70%");
        name.setWidth("500px");
    }

    public void setBinder(Binder<Product> binder, Product product) {
        this.product = product;
        binder.forField(name).asRequired("Наименование товара не может быть пустым").bind(Product::getName, Product::setName);
        binder.bind(categoryName, Product::getCategoryName, Product::setCategoryName);
        binder.bind(subcategoryName, Product::getSubcategoryName, Product::setSubcategoryName);
        binder.bind(barcode, "barcode");
        binder.bind(unitOfMeasure, Product::getUnitOfMeasureName, null);
        binder.bind(manufacturer, "manufacturer");
        binder.bind(brand, "brand");
        if (product.getCountryOfOrigin()!=null) {
            binder.bind(countryOfOrigin, Product::getCountryName, null); }
        binder.bind(isActive, "isActive");
    }

    private List<String> getAllCategoriesNames() {
        List<CategoryDTO> allCategories = categoryService.getAllCategories();
        List<String> categoriesNames = new ArrayList<>();
        for(CategoryDTO temp : allCategories) {
            categoriesNames.add(temp.getName());
        }
        return categoriesNames;
    }

    private List<String> getAllSubcategoriesNames(String categoryName) {
        List<Subcategory> subcategories = subcategoryService.getAllSubcategoriesByCategoryName(categoryName);
        List<String> subcategoriesNames = new ArrayList<>();
        for (Subcategory temp : subcategories) {
            subcategoriesNames.add(temp.getName());
        }
        return subcategoriesNames;
    }

    @Override
    public FormButtonsBar getButtons() {
        return buttons;
    }

    public Product getProduct() {
        return product;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }
}

package naakcii.by.api.admin.views.Product;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.admin.components.ImageUpload;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.category.CategoryDTO;
import naakcii.by.api.category.CategoryService;
import naakcii.by.api.country.Country;
import naakcii.by.api.country.CountryService;
import naakcii.by.api.product.ProductDTO;
import naakcii.by.api.subcategory.Subcategory;
import naakcii.by.api.subcategory.SubcategoryService;
import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import naakcii.by.api.unitofmeasure.UnitOfMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag("product-dialog")
//@HtmlImport("product-dialog.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductForm extends VerticalLayout implements CrudForm<ProductDTO> {

    private CategoryService categoryService;
    private SubcategoryService subcategoryService;
    private UnitOfMeasureService unitOfMeasureService;
    private CountryService countryService;
    private ProductDTO productDTO;
    private Subcategory subcategory;
    private UnitOfMeasure unitOfMeasure;
    private Country countryOfOrigin;

    private TextField name;
    private TextField picture;
    private ImageUpload imageUpload;
    private ComboBox<String> categoryName;
    private ComboBox<String> subcategoryName;
    private TextField barcode;
    private ComboBox<String> unitOfMeasureName;
    private TextField manufacturer;
    private TextField brand;
    private ComboBox<String> countryOfOriginName;
    private Checkbox isActive;
    private FormButtonsBar buttons;

    @Autowired
    public ProductForm(CategoryService categoryService, SubcategoryService subcategoryService,
                       UnitOfMeasureService unitOfMeasureService, CountryService countryService,
                       @Value("${upload.location}") String uploadLocation, @Value("${images.path.pattern}") String pathPattern) {
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
        this.unitOfMeasureService = unitOfMeasureService;
        this.countryService = countryService;

        setSizeFull();
        name = new TextField("Наименование товара");
        name.setWidth("100%");

        picture = new TextField("Адрес картинки");
        picture.setWidth("50%");
        imageUpload = new ImageUpload(this, uploadLocation, pathPattern);
        HorizontalLayout chosePic = new HorizontalLayout(picture, imageUpload);

        categoryName = new ComboBox<>("Категория");
        categoryName.setItems(getAllCategoriesNames());
        categoryName.addValueChangeListener(event ->
           subcategoryName.setItems(getAllSubcategoriesNames(event.getValue()))
        );
        categoryName.setWidth("50%");
        subcategoryName = new ComboBox<>("Подкатегория");
        subcategoryName.addValueChangeListener(event ->
            subcategory = subcategoryService.findByName(event.getValue())
        );
        subcategoryName.setWidth("50%");
        HorizontalLayout categories = new HorizontalLayout(categoryName, subcategoryName);

        barcode = new TextField("Штрих-код");
        barcode.setWidth("50%");
        unitOfMeasureName = new ComboBox<>("Единица измерения");
        unitOfMeasureName.setItems(getAllUnitsOfMeasure());
        unitOfMeasureName.addValueChangeListener(e->
           unitOfMeasure = unitOfMeasureService.findUnitOfMeasureByName(e.getValue())
        );
        unitOfMeasureName.setWidth("50%");
        HorizontalLayout layout1 = new HorizontalLayout(barcode, unitOfMeasureName);

        manufacturer = new TextField("Производитель");
        manufacturer.setWidth("50%");
        brand = new TextField("Торговая марка");
        brand.setWidth("50%");
        HorizontalLayout layout2 = new HorizontalLayout(manufacturer, brand);

        countryOfOriginName = new ComboBox<>("Страна происхождения");
        countryOfOriginName.setItems(getAllCountryNames());
        countryOfOriginName.addValueChangeListener(e ->
            countryOfOrigin = countryService.findByName(e.getValue())
        );
        countryOfOriginName.setWidth("50%");
        isActive = new Checkbox("Активен");
        buttons = new FormButtonsBar();
        add(name, chosePic, categories, layout1, layout2,
                countryOfOriginName, isActive, buttons);
    }

    private List<String> getAllCountryNames() {
        return countryService.findAll()
                .stream()
                .map(Country::getName)
                .collect(Collectors.toList());
    }

    private List<String> getAllUnitsOfMeasure() {
        UnitCode[] unitCodes = UnitCode.values();
        List<String> unitCodesRepresentation = new ArrayList<>();
        for (UnitCode temp : unitCodes) {
            unitCodesRepresentation.add(temp.getRepresentation());
        }
        return unitCodesRepresentation;
    }

    @Override
    public void setBinder(Binder<ProductDTO> binder, ProductDTO productDTO) {
        this.productDTO = productDTO;
        binder.forField(name).asRequired("Наименование товара не может быть пустым")
                .withValidator(field -> field.trim().length()>=3, "Не менее 3-х символов")
                .withValidator(field -> field.trim().length()<=100, "Не более 100 символов")
                .bind(ProductDTO::getName, ProductDTO::setName);
        binder.forField(picture)
                .withValidator(field -> field.length()<=255, "Не более 255 символов")
                .bind(ProductDTO::getPicture, ProductDTO::setPicture);
        binder.forField(categoryName)
                .asRequired("Поле не может быть пустым")
                .bind(ProductDTO::getCategoryName, ProductDTO::setCategoryName);
        binder.forField(subcategoryName)
                .withValidator(field -> (!field.isEmpty()),"Поле не может быть пустым")
                .bind(ProductDTO::getSubcategoryName, ProductDTO::setSubcategoryName);
        binder.forField(barcode)
                .asRequired("Поле не может быть пустым")
                .withValidator(field -> (field.length() == 4 || field.length() == 8 || field.length() == 12
                    || field.length() == 13 || field.length() == 14), "4, 8, 12, 13 или 14 символов")
                .withValidator(new RegexpValidator("Должны быть только цифры","^[0-9]*$"))
                .bind(ProductDTO::getBarcode, ProductDTO::setBarcode);
        binder.forField(unitOfMeasureName).asRequired("Поле не может быть пустым").bind(ProductDTO::getUnitOfMeasureName, ProductDTO::setUnitOfMeasureName);
        binder.forField(manufacturer)
                .withValidator(field -> field.length()<=50, "Не более 50 символов")
                .bind(ProductDTO::getManufacturer, ProductDTO::setManufacturer);
        binder.forField(brand)
                .withValidator(field -> field.length()<=50, "Не более 50 символов")
                .bind(ProductDTO::getBrand, ProductDTO::setBrand);
        binder.bind(countryOfOriginName, "countryOfOriginName");
        binder.bind(isActive, "isActive");
    }

    @Override
    public ProductDTO getDTO() {
        return productDTO;
    }

    @Override
    public String getChangedDTOName() {
        return productDTO.getName();
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
    public TextField getImageField() {
        return picture;
    }

    @Override
    public FormButtonsBar getButtons() {
        return buttons;
    }

    protected ProductDTO getProductDTO() {
        return productDTO;
    }

    protected Subcategory getSubcategory() {
        return subcategory;
    }

    protected UnitOfMeasure getUnitOfMeasure() {return unitOfMeasure;}

    protected Country getCountryOfOrigin() {
        return countryOfOrigin;
    }
}

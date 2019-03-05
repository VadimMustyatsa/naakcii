package naakcii.by.api.admin.views.Product;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.category.CategoryDTO;
import naakcii.by.api.category.CategoryService;
import naakcii.by.api.product.Product;
import naakcii.by.api.subcategory.Subcategory;
import naakcii.by.api.subcategory.SubcategoryService;
import naakcii.by.api.unitofmeasure.UnitCode;
import naakcii.by.api.unitofmeasure.UnitOfMeasure;
import naakcii.by.api.unitofmeasure.UnitOfMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag("product-dialog")
@HtmlImport("product-dialog.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductForm extends VerticalLayout implements CrudForm {

    private CategoryService categoryService;
    private SubcategoryService subcategoryService;
    private UnitOfMeasureService unitOfMeasureService;
    private Product product;
    private Subcategory subcategory;
    private Optional<UnitOfMeasure> unitOfMeasure;

    private TextField name;
    private TextField picture;
    private Upload upload;
    private ComboBox<String> categoryName;
    private ComboBox<String> subcategoryName;
    private TextField barcode;
    private ComboBox<String> unitOfMeasureName;
    private TextField manufacturer;
    private TextField brand;
    private TextField countryOfOrigin;
    private Checkbox isActive;
    private FormButtonsBar buttons;

    @Value("${upload.location}")
    private String uploadLocation;

    @Autowired
    public ProductForm(CategoryService categoryService, SubcategoryService subcategoryService,
                       UnitOfMeasureService unitOfMeasureService) {
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
        this.unitOfMeasureService = unitOfMeasureService;

        name = new TextField("Наименование товара");

        picture = new TextField("Адрес картинки");
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        upload = new Upload(buffer);
        uploadImage(buffer);
        HorizontalLayout chosePic = new HorizontalLayout(picture, upload);

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
        unitOfMeasureName = new ComboBox<>("Единица измерения");
        unitOfMeasureName.setItems(getAllUnitsOfMeasure());
        unitOfMeasureName.addValueChangeListener(e-> {
           unitOfMeasure = unitOfMeasureService.findUnitOfMeasureByName(e.getValue());
        });
        HorizontalLayout layout1 = new HorizontalLayout(barcode, unitOfMeasureName);

        manufacturer = new TextField("Производитель");
        manufacturer.setReadOnly(true);
        brand = new TextField("Торговая марка");
        brand.setReadOnly(true);
        HorizontalLayout layout2 = new HorizontalLayout(manufacturer, brand);

        countryOfOrigin = new TextField("Страна происхождения");
        isActive = new Checkbox("Акционный товар");
        isActive.setReadOnly(true);
        buttons = new FormButtonsBar();
        add(name, chosePic, categories, layout1, layout2,
                countryOfOrigin, isActive, buttons);
    }

    private void uploadImage(MultiFileMemoryBuffer buffer) {
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFiles(1);
        upload.addSucceededListener(event-> {
            try {
                byte[] buf = new byte[(int)event.getContentLength()];
                InputStream is = buffer.getInputStream(event.getFileName());
                is.read(buf);
                File targetFile = new File(uploadLocation+event.getFileName());
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buf);
                picture.setValue("/images/" + event.getFileName());
                outStream.flush();
                outStream.close();
            } catch (IOException ex) {
                Notification.show("Error");
                ex.printStackTrace();
            }
        });
    }

    private List<String> getAllUnitsOfMeasure() {
        UnitCode[] unitCodes = UnitCode.values();
        List<String> unitCodesRepresentation = new ArrayList<>();
        for (UnitCode temp : unitCodes) {
            unitCodesRepresentation.add(temp.getRepresentation());
        }
        return unitCodesRepresentation;
    }


    public void setBinder(Binder<Product> binder, Product product) {
        this.product = product;
        binder.forField(name).asRequired("Наименование товара не может быть пустым").bind(Product::getName, Product::setName);
        binder.bind(picture, "picture");
        binder.bind(categoryName, Product::getCategoryName, Product::setCategoryName);
        binder.bind(subcategoryName, Product::getSubcategoryName, Product::setSubcategoryName);
        binder.forField(barcode).asRequired("Баркод не может быть пустым и должен соответствовать требованиям").bind(Product::getBarcode, Product::setBarcode);
        binder.bind(unitOfMeasureName, Product::getUnitOfMeasureName, Product::setUnitOfMeasureName);
        binder.bind(manufacturer, "manufacturer");
        binder.bind(brand, "brand");
        if (product !=null && product.getCountryOfOrigin()!=null) {
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

    public Optional<UnitOfMeasure> getUnitOfMeasure() {return unitOfMeasure;};
}

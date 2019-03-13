package naakcii.by.api.admin.views.Product;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.product.Product;
import naakcii.by.api.product.ProductDTO;
import naakcii.by.api.product.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@HtmlImport("styles.html")
@Route(value = "admin" + "/" + AppConsts.PAGE_PRODUCT, layout = MainView.class)
@PageTitle(AppConsts.TITLE_PRODUCT)
public class ProductView extends VerticalLayout implements HasUrlParameter<String> {

    private static final String NO_IMAGE = "/images/customProduct.png";

    private ProductService productService;
    private ProductForm form;
    private ProductDTO productDTO;

    private Grid<ProductDTO> grid;

    private Dialog dialog = new Dialog();
    private Binder<ProductDTO> binder = new Binder<>(ProductDTO.class);

    private TextField search;
    private Button addProduct;

    @Value("${adminka.token}")
    private String adminkaPath;

    @Autowired
    public ProductView(ProductService productService, ProductForm form) {
        setSizeFull();
        this.productService = productService;
        this.form = form;

        this.grid = new Grid<>();

        grid.addColumn(new ComponentRenderer<>(productDTO -> {
            if ((productDTO.getPicture() != null) && !StringUtils.isEmpty(productDTO.getPicture())) {
                Image image = new Image(productDTO.getPicture(), productDTO.getName());
                image.setWidth("50px");
                image.setHeight("50px");
                return image;
            } else {
                Image imageEmpty = new Image(NO_IMAGE, "No image");
                imageEmpty.setHeight("50px");
                imageEmpty.setWidth("50px");
                return imageEmpty;
            }}
        ))
            .setHeader("Изображение");
        grid.addColumn(ProductDTO::getName).setFlexGrow(5).setHeader("Товар");
        grid.addColumn(ProductDTO::getCategoryName).setHeader("Категория");
        grid.addColumn(ProductDTO::getSubcategoryName).setHeader("Подкатегория");

        grid.setDataProvider(updateList(productService));

        //drag and drop columns order
        grid.setColumnReorderingAllowed(true);

        search = new TextField("Поиск товара");
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.setPlaceholder("Введите наименование товара");
        search.setWidth("50%");
        search.addValueChangeListener(e ->{
            grid.setItems(productService.searchName(e.getValue()));
        });

        addProduct = new Button("Добавить товар");
        addProduct.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        addProduct.setHeight("70%");
        addProduct.addClickListener(e-> {
           grid.asSingleSelect().clear();
           getProductForm().setBinder(binder, new ProductDTO());
            binder.addStatusChangeListener(status -> {
                getProductForm().getButtons().getSaveButton().setEnabled(!status.hasValidationErrors());
            });
            dialog.open();
        });
        HorizontalLayout toolbar = new HorizontalLayout(search, addProduct);
        toolbar.setWidth("100%");
        addProduct.getStyle().set("margin-left", "auto").set("margin-top", "auto");

        add(toolbar, grid);
        dialog.add(form);

        grid.asSingleSelect().addValueChangeListener(event -> {
            getProductForm().setBinder(binder, event.getValue());
            binder.readBean(event.getValue());
            binder.addStatusChangeListener(status -> {
                getProductForm().getButtons().getSaveButton().setEnabled(!status.hasValidationErrors());
            });
            dialog.open();
        });

        setupEventListeners();
        grid.getDataProvider().refreshAll();
    }

    //Lazy loading
    protected CallbackDataProvider<ProductDTO, Void> updateList(ProductService productService) {
        return DataProvider
                    .fromCallbacks(query -> productService
                                    .fetchProducts(query.getOffset(), query.getLimit()).stream(),
                            query -> productService.getProductCount());
    }

    public void setupEventListeners() {
        getProductForm().getButtons().addSaveListener(e -> save());
        getProductForm().getButtons().addCancelListener(e -> cancel());
        getProductForm().getButtons().addDeleteListener(e -> delete());

        getDialog().getElement().addEventListener("opened-changed", e -> {
           if(!getDialog().isOpened()) {
               cancel();
           }
        });
    }

    //soft delete
    private void delete() {
        ProductDTO productDTO = getProductForm().getProductDTO();
        Product product = productService.findProduct(productDTO.getId());
        productService.softDelete(product);
        Notification.show(productDTO.getName() + " присвоен статус 'Не активен'");
        closeUpdate();
    }

    private void cancel() {
        getDialog().close();
        grid.getDataProvider().refreshAll();
    }

    private void save() {
        ProductDTO productDTO = getProductForm().getProductDTO();
        try {
            binder.writeBean(productDTO);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        Product product = new Product(productDTO);
        product.setSubcategory(getProductForm().getSubcategory());
        product.setUnitOfMeasure(getProductForm().getUnitOfMeasure());
        product.setCountryOfOrigin(getProductForm().getCountryOfOrigin());
        productService.save(product);
        Notification.show(productDTO.getName() + " сохранён");
        closeUpdate();
    }

    private void closeUpdate() {
        getDialog().close();
        updateList(productService);
        grid.getDataProvider().refreshAll();
    }

    public Dialog getDialog() {
        return dialog;
    }

    public ProductForm getProductForm() {
        return form;
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (!parameter.equals(adminkaPath)) {
            throw new IllegalArgumentException();
        }
    }
}

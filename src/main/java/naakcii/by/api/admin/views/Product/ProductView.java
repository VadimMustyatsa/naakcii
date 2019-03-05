package naakcii.by.api.admin.views.Product;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.AbstractStreamResource;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.product.Product;
import naakcii.by.api.product.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@HtmlImport("src/styles.html")
@Route(value = AppConsts.PAGE_PRODUCT, layout = MainView.class)
@PageTitle(AppConsts.TITLE_PRODUCT)
public class ProductView extends VerticalLayout {

    private static final String NO_IMAGE = "/images/customProduct.png";

    private ProductService productService;
    private ProductForm form;
    private Product product;

    private Grid<Product> grid;

    private Dialog dialog = new Dialog();
    private Binder<Product> binder = new Binder<>(Product.class);

    private TextField search;
    private Button addProduct;

    @Autowired
    public ProductView(ProductService productService, ProductForm form) {
        setSizeFull();
        this.productService = productService;
        this.form = form;

        this.grid = new Grid<>();

        grid.addColumn(new ComponentRenderer<>(product -> {
            if ((product.getPicture() != null) && !StringUtils.isEmpty(product.getPicture())) {
                Image image = new Image(product.getPicture(), product.getName());
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
        grid.addColumn(Product::getName).setFlexGrow(5).setHeader("Товар");
        grid.addColumn(Product::getCategoryName).setHeader("Категория");
        grid.addColumn(Product::getSubcategoryName).setHeader("Подкатегория");

        grid.setDataProvider(updateList(productService));

        //drag and drop columns order
        grid.setColumnReorderingAllowed(true);

        search = new TextField("Поиск товара");
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.setPlaceholder("Введите наименование товара");
        search.setWidth("400px");
        search.addValueChangeListener(e ->{
            grid.setItems(productService.searchName(e.getValue()));
        });

        addProduct = new Button("Добавить товар");
        addProduct.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        addProduct.addClickListener(e-> {
           grid.asSingleSelect().clear();
            form.setBinder(binder, new Product());
            binder.addStatusChangeListener(status -> {
                getProductForm().getButtons().getSaveButton().setEnabled(!status.hasValidationErrors());
            });
            dialog.open();
        });
        HorizontalLayout toolbar = new HorizontalLayout(search, addProduct);

        add(toolbar, grid);
        dialog.add(form);

        grid.asSingleSelect().addValueChangeListener(event -> {
            form.setBinder(binder, event.getValue());
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
    protected CallbackDataProvider<Product, Void> updateList(ProductService productService) {
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
        Product product = getProductForm().getProduct();
        productService.softDelete(product);
        Notification.show(product.getName() + " присвоен статус 'Не активен'");
        closeUpdate();
    }

    private void cancel() {
        getDialog().close();
        grid.getDataProvider().refreshAll();
    }

    private void save() {
        Product product = getProductForm().getProduct();
        product.setSubcategory(getProductForm().getSubcategory());
        product.setUnitOfMeasure(getProductForm().getUnitOfMeasure().orElse(null));
        try {
            binder.writeBean(product);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        productService.save(product);
        Notification.show(product.getName() + " сохранён");
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
}

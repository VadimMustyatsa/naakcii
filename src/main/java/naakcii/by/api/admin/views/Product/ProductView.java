package naakcii.by.api.admin.views.Product;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.product.Product;
import naakcii.by.api.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = AppConsts.PAGE_PRODUCT, layout = MainView.class)
@PageTitle(AppConsts.TITLE_PRODUCT)
public class ProductView extends VerticalLayout {

    private ProductService productService;
    private ProductForm form;
    private Product product;

    private Grid<Product> grid;

    private Dialog dialog = new Dialog();
    private Binder<Product> binder = new Binder<>(Product.class);

    private TextField search;

    @Autowired
    public ProductView(ProductService productService, ProductForm form) {
        setSizeFull();
        this.productService = productService;
        this.form = form;

        this.grid = new Grid<>(Product.class);

        grid.setDataProvider(updateList(productService));

        grid.setColumns("picture", "name", "categoryName", "subcategoryName");
        grid.getColumnByKey("name").setFlexGrow(5).setHeader("Товар");
        grid.getColumnByKey("picture").setHeader("Изображение");
        grid.getColumnByKey("categoryName").setHeader("Категория");
        grid.getColumnByKey("subcategoryName").setHeader("Подкатегория");

        //drag and drop columns order
        grid.setColumnReorderingAllowed(true);

        search = new TextField("Поиск товара");
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.setPlaceholder("Введите наименование товара");
        search.setWidth("50%");
        search.addValueChangeListener(e ->{
            grid.setItems(productService.searchName(e.getValue()));
        });

        add(search, grid);
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
    }

    private void save() {
        Product product = getProductForm().getProduct();
        product.setSubcategory(getProductForm().getSubcategory());
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

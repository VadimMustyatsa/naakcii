package naakcii.by.api.admin.views.Product;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.admin.views.CrudView;
import naakcii.by.api.product.ProductDTO;
import naakcii.by.api.product.ProductService;
import naakcii.by.api.service.CrudService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//@HtmlImport("src/styles.html")
@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_PRODUCT, layout = MainView.class)
@PageTitle(AppConsts.TITLE_PRODUCT)
public class ProductView extends CrudView<ProductDTO> {

    @Value("${no.image}")
    private String noImage;

    private final Binder<ProductDTO> binder;

    @Autowired
    public ProductView(CrudForm<ProductDTO> form, CrudService<ProductDTO> crudService, ProductService productService) {
        super(form, crudService, true);
        binder = new Binder<>(ProductDTO.class);
        ComboBox<String> filter = getSearchBar().getFilter();
        filter.setItems("Активные", "Неактивные");
        filter.setValue("Активные");
        getGrid().setItems(productService.checkIsActive(filter.getValue()));
        filter.addValueChangeListener(e-> getGrid().setItems(productService.checkIsActive(e.getValue())));
    }

    @Override
    public Binder<ProductDTO> getBinder() {
        return binder;
    }

    @Override
    protected void setupGrid() {
        getGrid().addColumn(new ComponentRenderer<>(productDTO -> {
            if ((productDTO.getPicture() != null) && !StringUtils.isEmpty(productDTO.getPicture())) {
                Image image = new Image(productDTO.getPicture(), productDTO.getName());
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
            .setHeader("Изображение");
        getGrid().addColumn(ProductDTO::getName).setFlexGrow(4).setHeader("Товар").setSortable(true);
        getGrid().addColumn(ProductDTO::getCategoryName).setHeader("Категория").setSortable(true);
        getGrid().addColumn(ProductDTO::getSubcategoryName).setHeader("Подкатегория").setSortable(true);
        getGrid().addColumn(new ComponentRenderer<>(productDTO -> {
            Checkbox isActive = new Checkbox();
            isActive.setReadOnly(true);
            isActive.setValue(productDTO.getIsActive());
            return isActive;
        })).setHeader("Активный").setSortable(true).setFlexGrow(0);
    }
}

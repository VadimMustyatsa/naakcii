package naakcii.by.api.admin.views.chainProduct;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import naakcii.by.api.chain.ChainService;
import naakcii.by.api.chainproduct.ChainProductDTO;
import naakcii.by.api.chainproduct.ChainProductService;
import naakcii.by.api.chainproducttype.ChainProductTypeService;
import naakcii.by.api.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_CHAINPRODUCT, layout = MainView.class)
@PageTitle(AppConsts.TITLE_CHAINPRODUCT)
public class ChainProductView extends CrudView<ChainProductDTO> {

    private final Binder<ChainProductDTO> binder;
    private final ChainProductService chainProductService;

    private LocalDate startDate;
    private LocalDate endDate;
    private String productName;
    private String chainName;
    private String typeName;

    @Autowired
    public ChainProductView(CrudForm<ChainProductDTO> form, CrudService<ChainProductDTO> crudService,
                            ChainProductService chainProductService, ChainService chainService,
                            ChainProductTypeService chainProductTypeService) {
        super(form, crudService, null);
        this.chainProductService = chainProductService;
        getSearchBar().setVisible(false);

        DatePicker filterStart = new DatePicker("Начало акции");
        filterStart.addValueChangeListener(e -> {
            startDate = e.getValue();
            filterApply();
                });

        DatePicker filterEnd = new DatePicker("Завершение акции");
        filterEnd.addValueChangeListener(e -> {
            endDate = e.getValue();
            filterApply();
        });

        TextField searchProduct = new TextField("Поиск по товару");
        searchProduct.setValueChangeMode(ValueChangeMode.EAGER);
        searchProduct.setPlaceholder("Введите товар");
        searchProduct.setClearButtonVisible(true);
        searchProduct.addValueChangeListener(e-> {
            productName = e.getValue();
            filterApply();
        });

        ComboBox<String> searchChain = new ComboBox<>("Поиск по сети");
        searchChain.setItems(chainService.getAllChainNames());
        searchChain.addValueChangeListener(e -> {
            chainName = e.getValue();
            filterApply();
        });

        ComboBox<String> searchChainProductType = new ComboBox<>("Поиск по типу акции");
        searchChainProductType.setItems(chainProductTypeService.getAllChainProductTypeNames());
        searchChainProductType.addValueChangeListener(e-> {
           typeName = e.getValue();
           filterApply();
        });

        Button addEntity = new Button("Добавить");
        addEntity.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        addEntity.setHeight("70%");
        addEntity.addClickListener(e -> onComponentEvent());
        addEntity.setEnabled(false);
        addEntity.getStyle().set("margin-left", "auto").set("margin-top", "auto");
        HorizontalLayout searchBarCustom = new HorizontalLayout(filterStart, filterEnd, searchProduct, searchChain, searchChainProductType, addEntity);
        searchBarCustom.setWidth("100%");
        add(searchBarCustom, getGrid());
        binder = new Binder<>(ChainProductDTO.class);
    }

    private void filterApply() {
        getGrid().setItems(chainProductService.findAllByFilter(startDate, endDate, chainName,
                productName, typeName));
    }

    @Override
    public Binder<ChainProductDTO> getBinder() {
        return binder;
    }

    @Override
    protected void setupGrid() {
        getGrid().addColumn(ChainProductDTO::getChainName).setHeader("Торговая сеть").setSortable(true);
        getGrid().addColumn(ChainProductDTO::getProductName).setHeader("Товар").setSortable(true).setFlexGrow(4);
        getGrid().addColumn(ChainProductDTO::getDiscountPrice).setHeader("Цена со скидкой").setSortable(true);
        getGrid().addColumn(ChainProductDTO::getStartDate).setHeader("Начало акции").setSortable(true);
        getGrid().addColumn(ChainProductDTO::getEndDate).setHeader("Завершение акции").setSortable(true);
        getGrid().addColumn(ChainProductDTO::getChainProductTypeName).setHeader("Тип акции").setSortable(true);
    }

    private void onComponentEvent() {
        getGrid().asSingleSelect().clear();
        getForm().setBinder(getBinder(), getCrudService().createNewDTO());
        getDialog().open();
    }
}

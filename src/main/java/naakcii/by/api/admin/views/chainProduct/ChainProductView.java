package naakcii.by.api.admin.views.chainProduct;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.admin.views.CrudView;
import naakcii.by.api.chainproduct.ChainProductDTO;
import naakcii.by.api.chainproduct.ChainProductService;
import naakcii.by.api.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_CHAINPRODUCT, layout = MainView.class)
@PageTitle(AppConsts.TITLE_CHAINPRODUCT)
public class ChainProductView extends CrudView<ChainProductDTO> {

    private final Binder<ChainProductDTO> binder;

    @Autowired
    public ChainProductView(CrudForm<ChainProductDTO> form, CrudService<ChainProductDTO> crudService,
                            ChainProductService chainProductService) {
        super(form, crudService, new DatePicker("Начало акции"));
        DatePicker filter = (DatePicker) getFilterComponent();
        filter.addValueChangeListener(e -> getGrid().setItems(chainProductService.findAllByFilter(e.getValue())));

        binder = new Binder<>(ChainProductDTO.class);
        getSearchBar().getAddEntity().setEnabled(false);
    }

    @Override
    public Binder<ChainProductDTO> getBinder() {
        return binder;
    }

    @Override
    protected void setupGrid() {
        getGrid().addColumn(ChainProductDTO::getChainName).setHeader("Торговая сеть").setSortable(true);
        getGrid().addColumn(ChainProductDTO::getProductName).setHeader("Товар").setSortable(true).setFlexGrow(4);
//        getGrid().addColumn(ChainProductDTO::getBasePrice).setHeader("Цена без скидки").setSortable(true);
//        getGrid().addColumn(ChainProductDTO::getDiscountPercent).setHeader("% скидки").setSortable(true);
        getGrid().addColumn(ChainProductDTO::getDiscountPrice).setHeader("Цена со скидкой").setSortable(true);
        getGrid().addColumn(ChainProductDTO::getStartDate).setHeader("Начало акции").setSortable(true);
        getGrid().addColumn(ChainProductDTO::getEndDate).setHeader("Завершение акции").setSortable(true);
        getGrid().addColumn(ChainProductDTO::getChainProductTypeName).setHeader("Тип акции").setSortable(true);
    }
}

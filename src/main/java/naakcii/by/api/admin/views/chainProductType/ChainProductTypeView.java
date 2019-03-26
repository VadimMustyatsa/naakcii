package naakcii.by.api.admin.views.chainProductType;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.admin.views.CrudView;
import naakcii.by.api.chainproducttype.ChainProductTypeDTO;
import naakcii.by.api.service.CrudService;
import org.springframework.beans.factory.annotation.Value;

@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_CHAINPRODUCTTYPE, layout = MainView.class)
@PageTitle(AppConsts.TITLE_CHAINPRODUCTTYPE)
public class ChainProductTypeView extends CrudView<ChainProductTypeDTO> {

    @Value("${no.image}")
    private String noImage;

    private final Binder<ChainProductTypeDTO> binder;

    public ChainProductTypeView(CrudForm<ChainProductTypeDTO> form, CrudService<ChainProductTypeDTO> crudService) {
        super(form, crudService, null);
        binder = new Binder<>(ChainProductTypeDTO.class);
    }

    @Override
    public Binder<ChainProductTypeDTO> getBinder() {
        return binder;
    }

    @Override
    protected void setupGrid() {
        getGrid().addColumn(ChainProductTypeDTO::getName).setSortable(true).setHeader("Акция");
        getGrid().addColumn(ChainProductTypeDTO::getSynonym).setSortable(true).setHeader("Синоним");
        getGrid().addColumn(ChainProductTypeDTO::getTooltip).setHeader("Тултип");
    }
}

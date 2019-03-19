package naakcii.by.api.admin.views.unitOfMeasure;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import naakcii.by.api.admin.MainView;
import naakcii.by.api.admin.utils.AppConsts;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.admin.views.CrudView;
import naakcii.by.api.service.CrudService;
import naakcii.by.api.unitofmeasure.UnitOfMeasureDTO;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_MEASURE, layout = MainView.class)
@PageTitle(AppConsts.TITLE_MEASURE)
public class UnitOfMeasureView extends CrudView<UnitOfMeasureDTO> {

    private final Binder<UnitOfMeasureDTO> binder;

    @Autowired
    public UnitOfMeasureView(CrudForm<UnitOfMeasureDTO> form, CrudService<UnitOfMeasureDTO> crudService) {
        super(form, crudService, false);
        binder = new Binder<>(UnitOfMeasureDTO.class);
    }


    @Override
    public Binder<UnitOfMeasureDTO> getBinder() {
        return binder;
    }

    @Override
    protected void setupGrid() {
        getGrid().addColumn(UnitOfMeasureDTO::getName).setHeader("Единица измерения");
        getGrid().addColumn(UnitOfMeasureDTO::getStep).setHeader("Шаг изменения");
    }
}

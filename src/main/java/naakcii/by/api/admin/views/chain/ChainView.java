package naakcii.by.api.admin.views.chain;

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
import naakcii.by.api.chain.ChainDTO;
import naakcii.by.api.chain.ChainService;
import naakcii.by.api.service.CrudService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Route(value = AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_CHAIN, layout = MainView.class)
@PageTitle(AppConsts.TITLE_CHAIN)
public class ChainView extends CrudView<ChainDTO> {

    @Value("${no.image}")
    private String noImage;

    private Binder<ChainDTO> binder;

    @Autowired
    public ChainView(CrudForm<ChainDTO> form, CrudService<ChainDTO> crudService, ChainService chainService) {
        super(form, crudService, new ComboBox<String>("Фильтр"));
        ComboBox<String> filter = (ComboBox<String>) getFilterComponent();
        filter.setItems("Активные", "Неактивные");
        filter.setValue("Активные");
        getGrid().setItems(chainService.checkIsActive(filter.getValue()));
        filter.addValueChangeListener(e-> getGrid().setItems(chainService.checkIsActive(e.getValue())));
        binder = new Binder<>(ChainDTO.class);
    }

    @Override
    public Binder<ChainDTO> getBinder() {
        return binder;
    }

    @Override
    protected void setupGrid() {
        getGrid().addColumn(new ComponentRenderer<>(chainDTO -> {
            if ((chainDTO.getLogo() != null) && !StringUtils.isEmpty(chainDTO.getLogo())) {
                Image image = new Image(chainDTO.getLogo(), chainDTO.getName());
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
                .setHeader("Логотип").setFlexGrow(0);
        getGrid().addColumn(ChainDTO::getName).setHeader("Торговая сеть").setSortable(true);
        getGrid().addColumn(ChainDTO::getSynonym).setHeader("Синоним").setSortable(true);
        getGrid().addColumn(ChainDTO::getLink).setHeader("Сайт").setSortable(true);
    }
}

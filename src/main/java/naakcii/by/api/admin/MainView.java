package naakcii.by.api.admin;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import naakcii.by.api.admin.components.AppNavigation;
import naakcii.by.api.admin.components.PageInfo;
import naakcii.by.api.admin.utils.AppConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;


@Route(AppConsts.PAGE_MAIN)
@Theme(Material.class)
@PageTitle("Admin Panel")
@Viewport(AppConsts.VIEWPORT)
public class MainView extends VerticalLayout implements RouterLayout, HasUrlParameter<String> {

    private AppNavigation appNavigation;

    @Value("${adminka.token}")
    private String adminkaPath;

    @Autowired
    public MainView(@Value("${adminka.token}") String path) {
        setSizeFull();
        List<PageInfo> pages = new ArrayList<>();
        pages.add(new PageInfo(AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_PRODUCT + "/" + path, AppConsts.ICON_PRODUCT,
                        AppConsts.TITLE_PRODUCT));
        pages.add(new PageInfo(AppConsts.PAGE_MAIN + "/" + AppConsts.PAGE_COUNTRY + "/" + path, AppConsts.ICON_COUNTRY,
                AppConsts.TITLE_COUNTRY));
        appNavigation = new AppNavigation();
        appNavigation.init(pages, AppConsts.PAGE_DEFAULT, path);
        add(appNavigation);
        setHorizontalComponentAlignment(Alignment.CENTER, appNavigation);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (!parameter.equals(adminkaPath)) {
            throw new IllegalArgumentException();
        }
    }
}

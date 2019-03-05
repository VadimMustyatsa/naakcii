package naakcii.by.api.admin;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import naakcii.by.api.admin.components.AppNavigation;
import naakcii.by.api.admin.utils.AppConsts;
import org.springframework.beans.factory.annotation.Autowired;


@Route("")
@Theme(Material.class)
@PageTitle("Admin Panel")
@Viewport(AppConsts.VIEWPORT)
public class MainView extends VerticalLayout implements RouterLayout {

    private AppNavigation appNavigation;

    @Autowired
    public MainView() {
        setSizeFull();
        appNavigation = new AppNavigation();
        add(appNavigation);
        setHorizontalComponentAlignment(Alignment.CENTER, appNavigation);
    }

}

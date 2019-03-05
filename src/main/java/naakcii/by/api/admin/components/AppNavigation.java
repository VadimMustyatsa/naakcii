package naakcii.by.api.admin.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import naakcii.by.api.admin.utils.AppConsts;

public class AppNavigation extends HorizontalLayout {

    private Button goToProduct;

   public AppNavigation() {
       goToProduct = new Button("Товары");
       goToProduct.addClickListener(e-> {
            goToProduct.getUI().ifPresent(ui -> ui.navigate(AppConsts.PAGE_PRODUCT));
       });

       add(goToProduct);

   }
}

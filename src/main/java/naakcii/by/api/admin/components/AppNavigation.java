package naakcii.by.api.admin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class AppNavigation extends HorizontalLayout {

    private Button goToProduct;
    private Button goToCountry;

   public AppNavigation(String pathProduct, String pathCountry) {
       goToProduct = new Button("Товары");
       goToProduct.addClickListener(e-> {
            goToProduct.getUI().ifPresent(ui -> ui.navigate(pathProduct));
       });
       goToCountry = new Button("Страны");
       goToCountry.addClickListener(e-> {
           goToCountry.getUI().ifPresent(ui -> ui.navigate(pathCountry));
       });

       add(goToProduct, goToCountry);

   }
}

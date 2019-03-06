package naakcii.by.api.admin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class AppNavigation extends HorizontalLayout {

    private Button goToProduct;

   public AppNavigation(String path) {
       goToProduct = new Button("Товары");
       goToProduct.addClickListener(e-> {
            goToProduct.getUI().ifPresent(ui -> ui.navigate(path));
       });

       add(goToProduct);

   }
}

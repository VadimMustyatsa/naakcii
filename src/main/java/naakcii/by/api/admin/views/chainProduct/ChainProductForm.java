package naakcii.by.api.admin.views.chainProduct;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.chainproduct.ChainProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChainProductForm extends VerticalLayout implements CrudForm<ChainProductDTO> {

    private final TextField chainName;
    private final TextField productName;
    private final TextField basePrice;
    private final TextField discountPercent;
    private final TextField discountPrice;
    private final DatePicker startDate;
    private final DatePicker endDate;
    private final TextField chainProductTypeName;
    private final FormButtonsBar buttons;

    private ChainProductDTO chainProductDTO;

    @Autowired
    public ChainProductForm(){
        setSizeFull();
        chainName = new TextField("Торговая сеть");
        chainName.setWidth("100%");
        chainName.setReadOnly(true);
        productName = new TextField("Товар");
        productName.setWidth("100%");
        productName.setReadOnly(true);

        basePrice = new TextField("Цена без скидки");
        basePrice.setReadOnly(true);
        basePrice.setWidth("30%");
        discountPercent = new TextField("Процент скидки");
        discountPercent.setReadOnly(true);
        discountPercent.setWidth("30%");
        discountPrice = new TextField("Цена со скидкой");
        discountPrice.setReadOnly(true);
        discountPrice.setWidth("30%");
        HorizontalLayout prices = new HorizontalLayout(basePrice, discountPercent, discountPrice);
        prices.setSizeFull();

        startDate = new DatePicker("Начало акции");
        startDate.setWidth("50%");
        endDate = new DatePicker("Окончание акции");
        endDate.setWidth("50%");
        HorizontalLayout dates = new HorizontalLayout(startDate, endDate);
        dates.setSizeFull();

        chainProductTypeName = new TextField("Тип акции");
        chainProductTypeName.setReadOnly(true);
        buttons = new FormButtonsBar();
        getButtons().getDeleteButton().setEnabled(false);
        getButtons().getSaveButton().setEnabled(false);
        add(chainName, productName, prices, dates, chainProductTypeName, buttons);

    }
    @Override
    public FormButtonsBar getButtons() {
        return buttons;
    }

    @Override
    public TextField getImageField() {
        return null;
    }

    @Override
    public void setBinder(Binder<ChainProductDTO> binder, ChainProductDTO chainProductDTO) {
        this.chainProductDTO = chainProductDTO;
        binder.forField(chainName)
                .bind(ChainProductDTO::getChainName, ChainProductDTO::setChainName);
        binder.forField(productName)
                .bind(ChainProductDTO::getProductName, ChainProductDTO::setProductName);
        binder.forField(basePrice)
                .withConverter(new StringToBigDecimalConverter("Только числа"))
                .bind(ChainProductDTO::getBasePrice, ChainProductDTO::setBasePrice);
        binder.forField(discountPercent)
                .withConverter(new StringToBigDecimalConverter("Только числа"))
                .bind(ChainProductDTO::getDiscountPercent, ChainProductDTO::setDiscountPercent);
        binder.forField(discountPrice)
                .withConverter(new StringToBigDecimalConverter("Только числа"))
                .bind(ChainProductDTO::getDiscountPrice, ChainProductDTO::setDiscountPrice);
        binder.forField(startDate)
                .bind(ChainProductDTO::getStartDate, ChainProductDTO::setStartDate);
        binder.forField(endDate)
                .bind(ChainProductDTO::getEndDate, ChainProductDTO::setEndDate);
        binder.forField(chainProductTypeName)
                .bind(ChainProductDTO::getChainProductTypeName, ChainProductDTO::setChainProductTypeName);
    }

    @Override
    public ChainProductDTO getDTO() {
        return chainProductDTO;
    }

    @Override
    public String getChangedDTOName() {
        return "Товар " + chainProductDTO.getProductName() + " торговой сети " + chainProductDTO.getChainName();
    }
}

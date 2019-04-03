package naakcii.by.api.admin.views.chainProduct;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.data.validator.DateRangeValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import naakcii.by.api.admin.components.FormButtonsBar;
import naakcii.by.api.admin.views.CrudForm;
import naakcii.by.api.chain.ChainService;
import naakcii.by.api.chainproduct.ChainProductDTO;
import naakcii.by.api.chainproducttype.ChainProductTypeService;
import naakcii.by.api.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChainProductForm extends VerticalLayout implements CrudForm<ChainProductDTO> {

    private final ComboBox<String> chainName;
    private final ComboBox<String> productName;
    private final TextField basePrice;
    private final TextField discountPercent;
    private TextField discountPrice;
    private final DatePicker startDate;
    private final DatePicker endDate;
    private final ComboBox<String> chainProductTypeName;
    private final FormButtonsBar buttons;

    private ChainProductDTO chainProductDTO;

    @Autowired
    public ChainProductForm(ChainService chainService, ProductService productService, ChainProductTypeService chainProductTypeService){
        setSizeFull();
        chainName = new ComboBox("Торговая сеть");
        chainName.setWidth("100%");
        chainName.setItems(chainService.getAllChainNames());

        productName = new ComboBox<>("Товар");
        productName.setWidth("100%");
        productName.setItems(productService.getAllProductNames());

        basePrice = new TextField("Цена без скидки");
        basePrice.setWidth("30%");
        basePrice.addValueChangeListener(e -> {
            if (basePrice.getValue().isEmpty()) {
                basePrice.setValue("");
            }
                calculateDiscountPercent(e.getValue(), discountPrice.getValue());
        });

        discountPrice = new TextField("Цена со скидкой");
        discountPrice.setWidth("30%");
        discountPrice.addValueChangeListener(e -> calculateDiscountPercent(basePrice.getValue(), e.getValue()));

        discountPercent = new TextField("Процент скидки");
        discountPercent.setReadOnly(true);
        discountPercent.setWidth("30%");

        HorizontalLayout prices = new HorizontalLayout(basePrice, discountPrice, discountPercent);
        prices.setSizeFull();

        startDate = new DatePicker("Начало акции");
        startDate.setWidth("50%");
        endDate = new DatePicker("Окончание акции");
        endDate.setWidth("50%");
        HorizontalLayout dates = new HorizontalLayout(startDate, endDate);
        dates.setSizeFull();

        chainProductTypeName = new ComboBox<>("Тип акции");
        chainProductTypeName.setItems(chainProductTypeService.getAllChainProductTypeNames());

        buttons = new FormButtonsBar();
        add(chainName, productName, prices, dates, chainProductTypeName, buttons);
    }

    private void calculateDiscountPercent(String basePrice, String discountPrice) {
        if (basePrice.isEmpty()) {
            discountPercent.setValue("0");
        } else if (Double.valueOf(basePrice).equals(0)) {
            discountPercent.setValue("0");
        } else if (discountPrice != null && !discountPrice.isEmpty()) {
            BigDecimal basePr = new BigDecimal(basePrice);
            BigDecimal discountPr = new BigDecimal(discountPrice);

            BigDecimal difference = basePr.subtract(discountPr);
            BigDecimal discount = difference.divide(basePr, new MathContext(10, RoundingMode.HALF_UP));
            BigDecimal discountPerc = discount.scaleByPowerOfTen(2).setScale(0, RoundingMode.HALF_UP);
            discountPercent.setValue(String.valueOf(discountPerc));
        }
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
                .asRequired("Поле не может быть пустым")
                .bind(ChainProductDTO::getChainName, ChainProductDTO::setChainName);
        binder.forField(productName)
                .asRequired("Поле не может быть пустым")
                .bind(ChainProductDTO::getProductName, ChainProductDTO::setProductName);
        binder.forField(basePrice)
                .withValidator(new RegexpValidator("Не более 2-х цифр и не более 2-x десятичных знаков", "([0-9]{1,2})?(\\.[0-9]{1,2})?"))
                .withValidator(field -> {
                    if (!field.isEmpty()) {
                       return Double.valueOf(field) >= 0.0 && Double.valueOf(field) <= 75;
                    } else {
                        return true;
                    }
                }, "Цена должна быть от 0.0 до 75")
                .withConverter(new StringToBigDecimalConverter("Только числа"))
                .bind(ChainProductDTO::getBasePrice, ChainProductDTO::setBasePrice);
        binder.forField(discountPercent)
                .withValidator(field -> {
                    if (!field.isEmpty()) {
                        return Double.valueOf(field) >= 0.0 && Double.valueOf(field) <= 50;
                    } else {
                        return true;
                    }
                }, "Значение должно быть от 0 до 50")
                .withConverter(new StringToBigDecimalConverter("Только числа"))
                .bind(ChainProductDTO::getDiscountPercent, ChainProductDTO::setDiscountPercent);
        binder.forField(discountPrice)
                .asRequired("Поле не может быть пустым")
                .withValidator(new RegexpValidator("Не более 2-х цифр и не более 2-x десятичных знаков", "[0-9]{1,2}(\\.[0-9]{1,2})?"))
                .withValidator(field -> Double.valueOf(field) >= 0.20 && Double.valueOf(field) <= 50, "Цена должна быть от 0.20 до 50")
                .withConverter(new StringToBigDecimalConverter("Только числа"))
                .bind(ChainProductDTO::getDiscountPrice, ChainProductDTO::setDiscountPrice);
        binder.forField(startDate)
                .asRequired("Поле не может быть пустым")
                .bind(ChainProductDTO::getStartDate, ChainProductDTO::setStartDate);
        binder.forField(endDate)
                .asRequired("Поле не может быть пустым")
                .withValidator(new DateRangeValidator("Дата должна быть в будущем", LocalDate.now(), LocalDate.now().plusMonths(12)))
                .bind(ChainProductDTO::getEndDate, ChainProductDTO::setEndDate);
        binder.forField(chainProductTypeName)
                .asRequired("Поле не может быть пустым")
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

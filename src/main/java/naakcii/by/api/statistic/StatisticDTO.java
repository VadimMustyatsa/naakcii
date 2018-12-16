package naakcii.by.api.statistic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@NoArgsConstructor
@Setter
@Getter
public class StatisticDTO {

    private Long id;
    private Integer chainQuantity;
    private Integer discountedProducts;
    private Integer averageDiscountPercentage;
    private Calendar creationDate;

    public StatisticDTO(Long id, Integer chainQuantity, Integer discountedProducts, Integer averageDiscountPercentage, Calendar creationDate) {
        this.id = id;
        this.chainQuantity = chainQuantity;
        this.discountedProducts = discountedProducts;
        this.averageDiscountPercentage = averageDiscountPercentage;
        this.creationDate = creationDate;
    }
}

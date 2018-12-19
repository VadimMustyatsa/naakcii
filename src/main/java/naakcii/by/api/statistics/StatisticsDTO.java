package naakcii.by.api.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

@NoArgsConstructor
@Setter
@Getter
public class StatisticsDTO {

    private Long id;
    private Integer chainQuantity;
    private Integer discountedProducts;
    private Integer averageDiscountPercentage;
    private Calendar creationDate;

    public StatisticsDTO(Statistics statistics) {
        this.id = statistics.getId();
        this.chainQuantity = statistics.getChainQuantity();
        this.discountedProducts = statistics.getDiscountedProducts();
        this.averageDiscountPercentage = statistics.getDiscountedProducts();
        this.creationDate = statistics.getCreationDate();
    }

}

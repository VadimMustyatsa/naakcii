package naakcii.by.api.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class StatisticsDTO {

    private Long id;
    private Integer chainQuantity;
    private Integer discountedProducts;
    private Integer averageDiscountPercentage;
    private Long creationDateMillis;

    public StatisticsDTO(Statistics statistics) {
        this.id = statistics.getId();
        this.chainQuantity = statistics.getChainQuantity();
        this.discountedProducts = statistics.getDiscountedProducts();
        this.averageDiscountPercentage = statistics.getAverageDiscountPercentage();
        this.creationDateMillis = statistics.getCreationDate().getTimeInMillis();
    }
}

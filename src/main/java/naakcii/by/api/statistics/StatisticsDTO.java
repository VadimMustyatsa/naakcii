package naakcii.by.api.statistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class StatisticsDTO {

    @ApiModelProperty(notes = "Id статистики", example="1L")
    private Long id;
    @ApiModelProperty(notes = "Количество торговых сетей", example="10")
    private Integer chainQuantity;
    @ApiModelProperty(notes = "Количество товаров со скидкой", example="15")
    private Integer discountedProducts;
    @ApiModelProperty(notes = "Средний процент скидки", example="10")
    private Integer averageDiscountPercentage;
    @ApiModelProperty(notes = "Дата создания записи статистики", example="1508484583267")
    private Long creationDateMillis;

    public StatisticsDTO(Statistics statistics) {
        this.id = statistics.getId();
        this.chainQuantity = statistics.getChainQuantity();
        this.discountedProducts = statistics.getDiscountedProducts();
        this.averageDiscountPercentage = statistics.getAverageDiscountPercentage();
        this.creationDateMillis = statistics.getCreationDate().getTimeInMillis();
    }
}

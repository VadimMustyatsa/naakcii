package naakcii.by.api.chainstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import naakcii.by.api.chain.Chain;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class ChainStatisticsDTO {

    @ApiModelProperty(notes = "Id торговой сети", example = "1L")
    private Long id;

    @ApiModelProperty(notes = "Название торговой сети", example = "Алми")
    private String name;

    @ApiModelProperty(notes = "Количество акционных продуктов в торговой сети", example = "58")
    private Integer discountedProducts;

    @ApiModelProperty(notes = "Средний процент скидки по торговой сети", example = "25")
    private Integer averageDiscountPercentage;

    public ChainStatisticsDTO(ChainStatistics chainStatistics) {
        this.discountedProducts = chainStatistics.getDiscountedProducts();
        this.averageDiscountPercentage = chainStatistics.getAverageDiscountPercentage();
        Chain chain = chainStatistics.getChain();
        if (chain != null) {
            this.id = chainStatistics.getChain().getId();
            this.name = chainStatistics.getChain().getName();
        } else {
            this.id = null;
            this.name = null;
        }

    }
}

package naakcii.by.api.chainstatistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ChainStatisticsDTO {

    @ApiModelProperty(notes = "Id торговой сети", example = "1L")
    private Long chainId;

    @ApiModelProperty(notes = "Название торговой сети", example = "Алми")
    private String chainName;

    @ApiModelProperty(notes = "Количество акционных продуктов в торговой сети", example = "58")
    private Integer discountedProducts;

    @ApiModelProperty(notes = "Средний процент скидки по торговой сети", example = "25")
    private Integer averageDiscountPercentage;

    public ChainStatisticsDTO(ChainStatistics chainStatistics) {
        this.discountedProducts = chainStatistics.getDiscountedProducts();
        this.averageDiscountPercentage = chainStatistics.getAverageDiscountPercentage();
        this.chainId = chainStatistics.getChain().getId();
        this.chainName = chainStatistics.getChain().getName();
    }
}

package naakcii.by.api.chain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chainstatistics.ChainStatistics;

@NoArgsConstructor
@Setter
@Getter
public class ChainDTO {

    @ApiModelProperty(notes = "Id торговой сети", example = "1L")
    private Long id;

    @ApiModelProperty(notes = "Название торговой сети", example = "Алми")
    private String name;

    @ApiModelProperty(notes = "Путь к файлу с изображением логотипа торговой сети", example = "http://pathtologo/logo.jpg")
    private String logo;

    @ApiModelProperty(notes = "Ссылка на сайт торговой сети", example = "http://almi.by")
    private String link;

    @ApiModelProperty(notes = "Количество акционных продуктов в торговой сети", example = "58")
    private Integer discountedProducts;

    @ApiModelProperty(notes = "Средний процент скидки по торговой сети", example = "25")
    private Integer averageDiscountPercentage;

    public ChainDTO(Chain chain) {
        this.id = chain.getId();
        this.name = chain.getName();
        this.logo = chain.getLogo();
        this.link = chain.getLink();
        ChainStatistics chainStatistics = chain.getChainStatistics();
        if (chainStatistics != null) {
            this.discountedProducts = chainStatistics.getDiscountedProducts();
            this.averageDiscountPercentage = chainStatistics.getDiscountedProducts();
        } else {
            this.discountedProducts = 0;
            this.averageDiscountPercentage = 0;
        }
    }
}

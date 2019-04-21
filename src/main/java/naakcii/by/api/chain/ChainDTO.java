package naakcii.by.api.chain;

import java.util.Optional;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chainstatistics.ChainStatistics;

@NoArgsConstructor
@Setter
@Getter
public class ChainDTO {

    @ApiModelProperty(notes = "Искусственный идентификатор (суррогатный ключ) торговой сети.", example = "1L")
    private Long id;

    @ApiModelProperty(notes = "Название торговой сети.", example = "Алми")
    private String name;

    @ApiModelProperty(notes = "Путь к файлу, содержащему логотип торговой сети.", example = "С:/chains/logos/almi.jpg")
    private String logo;

    @ApiModelProperty(notes = "Ссылка на официальный сайт торговой сети.", example = "http://almi.by")
    private String link;

    @ApiModelProperty(notes = "Количество акционных продуктов в торговой сети.", example = "58")
    private Integer discountedProducts;

    @ApiModelProperty(notes = "Средний процент скидки на акционные товары для торговой сети.", example = "25")
    private Integer averageDiscountPercentage;

    public ChainDTO(Chain chain) {
        this.id = chain.getId();
        this.name = chain.getName();
        this.logo = chain.getLogo();
        this.link = chain.getLink();
        Optional<ChainStatistics> optionalChainStatistics = chain.getOptionalChainStatistics();
        this.discountedProducts = optionalChainStatistics.map(ChainStatistics::getDiscountedProducts).orElse(0);
        this.averageDiscountPercentage = optionalChainStatistics.map(ChainStatistics::getAverageDiscountPercentage).orElse(0);
    }
}

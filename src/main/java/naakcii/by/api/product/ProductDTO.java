package naakcii.by.api.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.entity.AbstractDTOEntity;

@NoArgsConstructor
@Setter
@Getter
public class ProductDTO extends AbstractDTOEntity {

    private Long id;
    private String barcode;
    private String name;
    private String picture;
    private String unitOfMeasureName;
    private String manufacturer;
    private String brand;
    private String countryOfOriginName;
    private String categoryName;
    private String subcategoryName;
    private Boolean isActive;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.barcode = product.getBarcode();
        this.name = product.getName();
        this.picture = product.getPicture();
        this.unitOfMeasureName = product.getUnitOfMeasure().getName();
        this.manufacturer = product.getManufacturer();
        this.brand = product.getBrand();
        if (product.getCountryOfOrigin() != null) {
            this.countryOfOriginName = product.getCountryOfOrigin().getName();
        } else {
            this.countryOfOriginName = null;
        }
        this.categoryName = product.getSubcategory().getCategory().getName();
        this.subcategoryName = product.getSubcategory().getName();
        this.isActive = product.getIsActive();
    }
}

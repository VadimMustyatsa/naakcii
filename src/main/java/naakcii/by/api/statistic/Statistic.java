package naakcii.by.api.statistic;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Calendar;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "STATISTIC")
public class Statistic implements Serializable {

    private static final long serialVersionUID = 7800996290031207909L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CHAIN_QUANTITY")
    @NotNull(message = "Chain quantity of the statistic mustn't be null.")
    @Positive(message = "Chain quantity of the statistic '${validatedValue}' must be positive.")
    private Integer chainQuantity;

    @Column(name = "DISCOUNTED_PRODUCTS")
    @NotNull(message = "Discounted products of the statistic mustn't be null.")
    @Positive(message = "Discounted products of the statistic '${validatedValue}' must be positive.")
    private Integer discountedProducts;

    @Column(name = "AVERAGE_DISCOUNT_PERCENTAGE")
    @NotNull(message = "Average discount percentage of the statistic mustn't be null.")
    @Positive(message = "Average discount percentage of the statistic '${validatedValue}' must be positive.")
    @Max(value = 100, message = "Average discount percentage of the statistic '${validatedValue}' must be less 100")
    private Integer averageDiscountPercentage;

    @Column(name = "CREATION_DATE")
    @NotNull(message = "Creation date of the statistic mustn't be null.")
    private Calendar creationDate;

    public Statistic(Integer chainQuantity, Integer discountedProducts, Integer averageDiscountPercentage, Calendar creationDate) {
        this.chainQuantity = chainQuantity;
        this.discountedProducts = discountedProducts;
        this.averageDiscountPercentage = averageDiscountPercentage;
        this.creationDate = creationDate;
    }
}

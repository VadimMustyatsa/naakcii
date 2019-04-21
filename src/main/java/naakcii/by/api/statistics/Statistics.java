package naakcii.by.api.statistics;


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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "STATISTICS")
public class Statistics implements Serializable {

    private static final long serialVersionUID = 7800996290031207909L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATISTICS_ID")
    private Long id;

    @Column(name = "STATISTICS_CHAIN_QUANTITY")
    @NotNull(message = "Chain quantity of the statistics mustn't be null.")
    @Positive(message = "Chain quantity of the statistics '${validatedValue}' must be positive.")
    private Integer chainQuantity;

    @Column(name = "STATISTICS_DISCOUNTED_PRODUCTS")
    @NotNull(message = "Discounted products of the statistics mustn't be null.")
    @Positive(message = "Discounted products of the statistics '${validatedValue}' must be positive.")
    private Integer discountedProducts;

    @Column(name = "STATISTICS_AVERAGE_DISCOUNT_PERCENTAGE")
    @NotNull(message = "Average discount percentage of the statistics mustn't be null.")
    @Positive(message = "Average discount percentage of the statistics '${validatedValue}' must be positive.")
    @Max(value = 100, message = "Average discount percentage of the statistics '${validatedValue}' must be less 100")
    private Integer averageDiscountPercentage;

    @Column(name = "STATISTICS_CREATION_DATE")
    @NotNull(message = "Creation date of the statistics mustn't be null.")
    @Temporal(TemporalType.DATE)
    private Calendar creationDate;

    public Statistics(Integer chainQuantity, Integer discountedProducts, Integer averageDiscountPercentage, Calendar creationDate) {
        this.chainQuantity = chainQuantity;
        this.discountedProducts = discountedProducts;
        this.averageDiscountPercentage = averageDiscountPercentage;
        this.creationDate = creationDate;
    }
}

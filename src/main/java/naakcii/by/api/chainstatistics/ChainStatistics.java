package naakcii.by.api.chainstatistics;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chain.Chain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Calendar;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "CHAIN_STATISTICS")
public class ChainStatistics {

    @Id
    private Integer id;

    @Column(name = "DISCOUNTED_PRODUCTS")
    @NotNull(message = "Discounted products of the chain statistics mustn't be null.")
    @Positive(message = "Discounted products of the chain statistics '${validatedValue}' must be positive.")
    private Integer discountedProducts;

    @Column(name = "AVERAGE_DISCOUNT_PERCENTAGE")
    @NotNull(message = "Average discount percentage of the chain statistics mustn't be null.")
    @Positive(message = "Average discount percentage of the chain statistics '${validatedValue}' must be positive.")
    @Max(value = 100, message = "Average discount percentage of the chain statistics '${validatedValue}' must be less 100")
    private Integer averageDiscountPercentage;

    @Column(name = "CREATION_DATE")
    @NotNull(message = "Creation date of the chain statistics mustn't be null.")
    @Temporal(TemporalType.DATE)
    private Calendar creationDate;

    @OneToOne
    @JoinColumn
    @MapsId
    private Chain chain;

    public ChainStatistics(Integer discountedProducts, Integer averageDiscountPercentage, Calendar creationDate, Chain chain) {
        this.discountedProducts = discountedProducts;
        this.averageDiscountPercentage = averageDiscountPercentage;
        this.creationDate = creationDate;
        this.chain = chain;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Instance of " + ChainStatistics.class + ":");
        result.append(System.lineSeparator())
                .append("\t").append("id - " + id)
                .append(System.lineSeparator())
                .append("\t").append("discountedProducts - " + discountedProducts)
                .append(System.lineSeparator())
                .append("\t").append("averageDiscountPercentage - " + averageDiscountPercentage)
                .append(System.lineSeparator())
                .append("\t").append("creationDate - " + creationDate)
                .append(System.lineSeparator())
                .append("\t").append("chain - " + chain)
                .append(System.lineSeparator());
        return result.toString();
    }
}

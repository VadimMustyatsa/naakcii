package naakcii.by.api.chainstatistics;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chain.Chain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
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
@Table(name = "CHAIN_STATISTICS")
public class ChainStatistics implements Serializable {

    private static final long serialVersionUID = 7015953223377330148L;

    @Id
    @Column(name = "CHAIN_STATISTICS_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "CHAIN_STATISTICS_DISCOUNTED_PRODUCTS")
    @NotNull(message = "Discounted products of the chain statistics mustn't be null.")
    private Integer discountedProducts;

    @Column(name = "CHAIN_STATISTICS_AVERAGE_DISCOUNT_PERCENTAGE")
    @NotNull(message = "Average discount percentage of the chain statistics mustn't be null.")
    @Max(value = 100, message = "Average discount percentage of the chain statistics '${validatedValue}' must be less 100")
    private Integer averageDiscountPercentage;

    @Column(name = "CHAIN_STATISTICS_CREATION_DATE")
    @NotNull(message = "Creation date of the chain statistics mustn't be null.")
    @Temporal(TemporalType.DATE)
    private Calendar creationDate;

    @OneToOne
    @JoinColumn(name = "CHAIN_STATISTICS_ID")
    @MapsId
    private Chain chain;

    public ChainStatistics(Integer discountedProducts, Integer averageDiscountPercentage, Calendar creationDate) {
        this.discountedProducts = discountedProducts;
        this.averageDiscountPercentage = averageDiscountPercentage;
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Instance of " + ChainStatistics.class + ":");
        result.append(System.lineSeparator())
                .append("\t").append("id - ")
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

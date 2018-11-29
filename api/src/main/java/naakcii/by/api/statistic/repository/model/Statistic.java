package naakcii.by.api.statistic.repository.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DISCOUNT_COUNTER")
public class Statistic implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "CHAIN")
    private Long chainQuantity;
    @Column(name = "GOODS")
    private Long discountedGoods;
    @Column(name = "DISCOUNT")
    private Long averageDiscount;

    public Long getChainQuantity() {
        return chainQuantity;
    }

    public void setChainQuantity(Long chainQuantity) {
        this.chainQuantity = chainQuantity;
    }

    public Long getDiscountedGoods() {
        return discountedGoods;
    }

    public void setDiscountedGoods(Long discountedGoods) {
        this.discountedGoods = discountedGoods;
    }

    public Long getAverageDiscount() {
        return averageDiscount;
    }

    public void setAverageDiscount(Long averageDiscount) {
        this.averageDiscount = averageDiscount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

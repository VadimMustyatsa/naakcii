package naakcii.by.api.statistic.repository.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "v_discount_counter")
public class Statistic implements Serializable {
    @Column(name = "id")
    private Long id;
    @Column(name = "chain")
    private Long chainQuantity;
    @Column(name = "goods")
    private Long discountedGoods;
    @Column(name = "discount")
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

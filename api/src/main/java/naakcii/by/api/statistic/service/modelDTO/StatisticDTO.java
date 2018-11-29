package naakcii.by.api.statistic.service.modelDTO;

public class StatisticDTO {
    private Long id;
    private Long chainQuantity;
    private Long discountedGoods;
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

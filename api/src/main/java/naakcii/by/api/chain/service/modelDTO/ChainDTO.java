package naakcii.by.api.chain.service.modelDTO;

import naakcii.by.api.action.repository.model.Action;

import java.util.Set;

public class ChainDTO {

    private Long id;
    private String name;
    private String link;
    private String imgLogo;
    private String imgLogoSmall;
    private Integer countGoods;
    private Integer percent;
    private Set<Action> actions;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgLogo() {
        return imgLogo;
    }

    public void setImgLogo(String imgLogo) {
        this.imgLogo = imgLogo;
    }

    public Integer getCountGoods() {
        return countGoods;
    }

    public void setCountGoods(Integer countGoods) {
        this.countGoods = countGoods;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public String getImgLogoSmall() {
        return imgLogoSmall;
    }

    public void setImgLogoSmall(String imgLogoSmall) {
        this.imgLogoSmall = imgLogoSmall;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public Set<Action> getAction() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }
}

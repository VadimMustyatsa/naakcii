package naakcii.by.api.repository.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CHAIN")
@NamedQueries({
        @NamedQuery(name = "Chain.findAll", query = "select ch from Chain ch"),
        @NamedQuery(name = "Chain.findAllWithDetails",
                query = "select distinct ch from Chain ch left join fetch ch.actions ac"),
        @NamedQuery(name = "Chain.findById", query = "select ch from Chain ch where ch.id = :id"),
        @NamedQuery(name = "Chain.findByIdWithDetails",
                query = "select ch from Chain ch left join fetch ch.actions ac where ch.id = :id"),
        @NamedQuery(name = "Chain.softDelete",
                query = "update Chain ch set ch.isActive=false where ch.id = :id")
})
public class Chain implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4338838997190141797L;

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @Column(name = "CHAIN_ID")
    private Long id;

    @Column(name = "CHAIN_NAME")
    @NotNull
    @Size(min = 2, max = 45)
    private String name;

    @Column(name = "LINK")
    @NotNull
    @Size(min = 2, max = 45)
    private String link;

    @Column(name = "IS_ACTIVE")
    @NotNull
    private boolean isActive;

    @Column(name = "ICON")
    @Size(max = 45)
    private String icon;

    @Column(name = "ICON_SMALL")
    @Size(max = 45)
    private String smallIcon;

    @OneToMany(mappedBy = "chain", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Action> actions = new HashSet<Action>();

    public Chain() {

    }

    public Chain(String name, String link, boolean isActive) {
        this.name = name;
        this.link = link;
        this.isActive = isActive;
    }

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void setActions(Set<Action> actions) {
        this.actions = actions;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

}

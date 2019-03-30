package naakcii.by.api.chain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import naakcii.by.api.chainproduct.ChainProduct;
import naakcii.by.api.chainstatistics.ChainStatistics;
import naakcii.by.api.util.annotations.PureSize;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(exclude = {"id", "logo", "chainProducts"})
@Entity
@Table(name = "CHAIN")
public class Chain implements Serializable {

    private static final long serialVersionUID = -4338838997190141797L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "CHAIN_ID")
    private Long id;

    @Column(name = "CHAIN_NAME")
    @NotNull(message = "Chain's name mustn't be null.")
    @PureSize(
            min = 3,
            max = 25,
            message = "Chain's name '${validatedValue}' must be between '{min}' and '{max}' characters long."
    )
    private String name;

    @Column(name = "CHAIN_SYNONYM", unique = true, length = 50)
    @NotNull(message = "Chain's synonym chain mustn't be null.")
    @PureSize(
            min = 3,
            max = 25,
            message = "Chain's synonym '${validatedValue}' must be between '{min}' and '{max}' characters long."
    )
    private String synonym;

    @Column(name = "CHAIN_LOGO")
    @Size(
            max = 255,
            message = "Path to the logo of the chain '${validatedValue}' mustn't be more than '{max}' characters long."
    )
    private String logo;

    @Column(name = "CHAIN_LINK")
    @NotNull(message = "Chain's link mustn't be null.")
    @PureSize(
            min = 10,
            max = 255,
            message = "Chain's link '${validatedValue}' must be between '{min}' and '{max}' characters long."
    )
    private String link;

    @OneToMany(mappedBy = "chain")
    private Set<
            @Valid
            @NotNull(message = "Chain must have list of chainProducts without null elements.")
                    ChainProduct> chainProducts = new HashSet<ChainProduct>();

    @Column(name = "CHAIN_IS_ACTIVE")
    @NotNull(message = "Chain must have field 'isActive' defined.")
    private Boolean isActive;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "chain")
    private ChainStatistics chainStatistics;

    public Chain(String name, String synonym, String link, Boolean isActive) {
        this.name = name;
        this.synonym = synonym;
        this.link = link;
        this.isActive = isActive;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("Instance of " + Chain.class + ":");
        result.append(System.lineSeparator());
        result.append("\t").append("id - " + id + ";");
        result.append(System.lineSeparator());
        result.append("\t").append("name - " + name + ";");
        result.append(System.lineSeparator());
        result.append("\t").append("synonym - " + synonym + ";");
        result.append(System.lineSeparator());
        result.append("\t").append("logo - " + logo + ";");
        result.append(System.lineSeparator());
        result.append("\t").append("link - " + link + ";");
        result.append(System.lineSeparator());
        result.append("\t").append("isActive - " + isActive + ".");
        return result.toString();
    }
}

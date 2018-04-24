package naakcii.by.api.service.modelDTO;

public class CategoryDTO {

    private Long id;
    private String name;
    private String picture;

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

    public String getIcon() {
        return picture;
    }

    public void setIcon(String picture) {
        this.picture = picture;
    }
}

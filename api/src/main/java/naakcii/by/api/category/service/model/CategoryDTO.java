package naakcii.by.api.category.service.model;

public class CategoryDTO {
    private Long Id;
    private String picture;
    private String name;
    private Integer priority;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getIcon() {
        return picture;
    }

    public void setIcon(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}

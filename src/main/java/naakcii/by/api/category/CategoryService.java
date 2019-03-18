package naakcii.by.api.category;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    Category findByName(String name);
    List<String> getAllActiveCategoriesNames();
}

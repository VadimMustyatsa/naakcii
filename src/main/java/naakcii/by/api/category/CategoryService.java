package naakcii.by.api.category;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();
    List<CategoryDTO> findAllDTOs();
    List<CategoryDTO> searchName(String name);
}

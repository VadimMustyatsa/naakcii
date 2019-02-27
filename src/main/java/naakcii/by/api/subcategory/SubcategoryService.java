package naakcii.by.api.subcategory;

import java.util.List;

public interface SubcategoryService {

    List<SubcategoryDTO> getAllSubcategoriesByCategoryId(Long categoryId);

    List<Subcategory> getAllSubcategoriesByCategoryName(String categoryName);

    Subcategory findByName(String subcategoryName);
}

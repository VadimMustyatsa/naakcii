package naakcii.by.api.subcategory;

import java.util.List;

public interface SubcategoryService {

    List<SubcategoryDTO> getAllSubcategoriesByCategoryId(Long categoryId);

    List<String> getAllSubcategoriesNames(String categoryName);

//    Subcategory findByName(String subcategoryName);

    Subcategory findByNameAndCategoryName(String subcategoryName, String categoryName);
}

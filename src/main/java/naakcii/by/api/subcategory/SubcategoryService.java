package naakcii.by.api.subcategory;

import java.util.List;

public interface SubcategoryService {

    List<String> getAllSubcategoriesNames(String categoryName);
    List<SubcategoryDTO> checkIsActive(String filter);
    Subcategory findByNameAndCategoryName(String subcategoryName, String categoryName);
}

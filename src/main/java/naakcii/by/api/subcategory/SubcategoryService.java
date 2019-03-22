package naakcii.by.api.subcategory;

import java.util.List;

public interface SubcategoryService {

    List<String> getAllSubcategoriesNames(String categoryName);

    Subcategory findByNameAndCategoryName(String subcategoryName, String categoryName);
}

package naakcii.by.api.service.util;

import naakcii.by.api.repository.model.Subcategory;
import naakcii.by.api.service.modelDTO.SubcategoryDTO;

public class SubCategoryConverter {

    public SubCategoryConverter() {
    }

    public SubcategoryDTO convert(Subcategory subcategory) {
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO();
        subcategoryDTO.setId(subcategory.getId());
        subcategoryDTO.setCategoryId(subcategory.getCategory().getId());
        subcategoryDTO.setName(subcategory.getName());
        return subcategoryDTO;
    }

    public Subcategory convert(SubcategoryDTO subcategoryDTO) {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(subcategoryDTO.getId());
        subcategory.setName(subcategoryDTO.getName());
        return subcategory;
    }
}

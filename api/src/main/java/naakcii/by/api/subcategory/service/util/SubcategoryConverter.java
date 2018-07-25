package naakcii.by.api.subcategory.service.util;

import naakcii.by.api.subcategory.repository.model.Subcategory;
import naakcii.by.api.subcategory.service.modelDTO.SubcategoryDTO;

public class SubcategoryConverter {

    public SubcategoryDTO convert(Subcategory subcategory) {
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO();
        subcategoryDTO.setId(subcategory.getId());
        subcategoryDTO.setName(subcategory.getName());
        subcategoryDTO.setPriority(subcategory.getPriority());
        subcategoryDTO.setCategoryId(subcategory.getCategory().getId());
        return subcategoryDTO;
    }
}

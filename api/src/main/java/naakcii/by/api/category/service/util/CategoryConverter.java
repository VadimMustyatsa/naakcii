package naakcii.by.api.category.service.util;

import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.category.service.model.CategoryDTO;

public class CategoryConverter {

    public CategoryDTO converter(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setIcon(category.getIcon());
        categoryDTO.setPriority(category.getPriority());
        return categoryDTO;
    }
}

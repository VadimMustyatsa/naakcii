package naakcii.by.api.service.util;

import naakcii.by.api.repository.model.Category;
import naakcii.by.api.service.modelDTO.CategoryDTO;

public class CategoryConverter {

    public CategoryConverter() {
    }


    public CategoryDTO convert(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setPicture(categoryDTO.getPicture());
        return categoryDTO;
    }

    public Category convert(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setPicture(categoryDTO.getPicture());
        return category;
    }
}

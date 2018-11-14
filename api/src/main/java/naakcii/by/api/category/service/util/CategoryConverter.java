package naakcii.by.api.category.service.util;

import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.category.service.model.CategoryDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryDTO converter(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setIcon(category.getIcon());
        categoryDTO.setPriority(category.getPriority());
        return categoryDTO;
    }
    public Category toCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        category.setIcon(categoryDTO.getIcon());
        category.setPriority(categoryDTO.getPriority());
        return category;
    }
}

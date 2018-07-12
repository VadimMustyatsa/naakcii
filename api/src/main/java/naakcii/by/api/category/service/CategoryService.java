package naakcii.by.api.category.service;

import naakcii.by.api.category.service.model.CategoryDTO;

import java.util.List;

public interface CategoryService {
    public List<CategoryDTO> findAll();
}

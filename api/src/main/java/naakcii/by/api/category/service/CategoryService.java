package naakcii.by.api.category.service;

import naakcii.by.api.category.service.model.CategoryDTO;

import java.util.List;

public interface CategoryService {
   List<CategoryDTO> findAll();
}

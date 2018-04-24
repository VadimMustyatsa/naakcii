package naakcii.by.api.service;

import naakcii.by.api.service.modelDTO.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO create(CategoryDTO categoryDTO);

    CategoryDTO delete(Long id);

    List<CategoryDTO> findAll();

    CategoryDTO findById(Long id);

    CategoryDTO update(CategoryDTO categoryDTO);
}

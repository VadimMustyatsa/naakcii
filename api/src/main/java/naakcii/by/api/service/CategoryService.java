package naakcii.by.api.service;

import naakcii.by.api.service.modelDTO.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO create(CategoryDTO categoryDTO);

    CategoryDTO delete(int id);

    List<CategoryDTO> findAll();

    CategoryDTO findById(int id);

    CategoryDTO update(CategoryDTO categoryDTO);
}

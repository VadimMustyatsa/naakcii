package naakcii.by.api.service;

import naakcii.by.api.service.modelDTO.SubcategoryDTO;

import java.util.List;

public interface SubcategoryService {

    List<SubcategoryDTO> getSubcategoriesbyCategoryId(Long id);
}

package naakcii.by.api.subcategory.service;

import naakcii.by.api.subcategory.service.modelDTO.SubcategoryDTO;

import java.util.List;

public interface SubcategoryService {

    public List<SubcategoryDTO> getSubcategoryByCategoryId(Long id);
}

package naakcii.by.api.service.ServiceImpl;

import naakcii.by.api.repository.dao.SubcategoryDao;
import naakcii.by.api.repository.model.Subcategory;
import naakcii.by.api.service.SubcategoryService;
import naakcii.by.api.service.modelDTO.SubcategoryDTO;
import naakcii.by.api.service.util.SubCategoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {

    private SubCategoryConverter converter = new SubCategoryConverter();

    @Autowired
    private SubcategoryDao repository;

    @Override
    public List<SubcategoryDTO> getSubcategoriesbyCategoryId(Long id) {
        List<Subcategory> subcategoryList = repository.findByCategoryId(id);
        List<SubcategoryDTO> subcategoryDTOList = new ArrayList<SubcategoryDTO>();
        for (Subcategory subcategory : subcategoryList) {
            subcategoryDTOList.add(converter.convert(subcategory));
        }
        return subcategoryDTOList;
    }
}

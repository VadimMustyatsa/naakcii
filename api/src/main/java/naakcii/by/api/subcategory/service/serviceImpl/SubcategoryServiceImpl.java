package naakcii.by.api.subcategory.service.serviceImpl;

import naakcii.by.api.subcategory.repository.SubcategoryRepository;
import naakcii.by.api.subcategory.repository.model.Subcategory;
import naakcii.by.api.subcategory.service.SubcategoryService;
import naakcii.by.api.subcategory.service.modelDTO.SubcategoryDTO;
import naakcii.by.api.subcategory.service.util.SubcategoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {
    public SubcategoryConverter subcategoryConverter = new SubcategoryConverter();

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Override
    public List<SubcategoryDTO> getSubcategoryByCategoryId(Long id) {
        List<Subcategory> subcategories = subcategoryRepository.findByCategoryId(id);
        List<SubcategoryDTO> subcategoryDTOList = new ArrayList<>();
        for (Subcategory subcategory : subcategories) {
            SubcategoryDTO subcategoryDTO = subcategoryConverter.convert(subcategory);
            subcategoryDTOList.add(subcategoryDTO);
        }
        Collections.sort(subcategoryDTOList, Comparator.comparing(SubcategoryDTO::getPriority));
        Collections.reverse(subcategoryDTOList);
        return subcategoryDTOList;
    }
}

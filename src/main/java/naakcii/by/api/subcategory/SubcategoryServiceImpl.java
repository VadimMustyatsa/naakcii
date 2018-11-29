package naakcii.by.api.subcategory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import naakcii.by.api.util.ObjectFactory;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {
	   
    private SubcategoryRepository subcategoryRepository;
    private ObjectFactory objectFactory;
    
    @Autowired
    public SubcategoryServiceImpl(SubcategoryRepository subcategoryRepository, ObjectFactory objectFactory) {
    	this.subcategoryRepository = subcategoryRepository;
    	this.objectFactory = objectFactory;
    }

    @Override
    public List<SubcategoryDTO> getAllSubcategoriesByCategoryId(Long categoryId) {
    	return subcategoryRepository.findByIsActiveTrueAndCategoryIdOrderByPriorityAsc(categoryId)
    			.stream()
    			.filter(Objects::nonNull)
    			.map((Subcategory subcategory) -> objectFactory.getInstance(SubcategoryDTO.class, subcategory))
    			.collect(Collectors.toList());
    }
}

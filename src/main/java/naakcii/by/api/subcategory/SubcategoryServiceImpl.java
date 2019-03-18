package naakcii.by.api.subcategory;

import naakcii.by.api.category.CategoryRepository;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {
	   
    private SubcategoryRepository subcategoryRepository;
    private CategoryRepository categoryRepository;
    private ObjectFactory objectFactory;
    
    @Autowired
    public SubcategoryServiceImpl(SubcategoryRepository subcategoryRepository, CategoryRepository categoryRepository,
                                  ObjectFactory objectFactory) {
    	this.subcategoryRepository = subcategoryRepository;
    	this.categoryRepository = categoryRepository;
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

    @Override
    public List<String> getAllSubcategoriesNames(String categoryName) {
        return subcategoryRepository.findByCategory(categoryRepository.findByNameIgnoreCase(categoryName))
                .stream()
                .map(subcategory -> subcategory.getName())
                .collect(Collectors.toList());
    }

    @Override
    public Subcategory findByNameAndCategoryName(String subcategoryName, String categoryName) {
        return subcategoryRepository.findByNameAndCategoryName(subcategoryName, categoryName).orElse(null);
    }
}

package naakcii.by.api.subcategory;

import com.vaadin.flow.component.notification.Notification;
import naakcii.by.api.category.CategoryRepository;
import naakcii.by.api.product.Product;
import naakcii.by.api.service.CrudService;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SubcategoryServiceImpl implements SubcategoryService, CrudService<SubcategoryDTO> {
	   
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectFactory objectFactory;
    
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
                .map(Subcategory::getName)
                .collect(Collectors.toList());
    }

    @Override
    public Subcategory findByNameAndCategoryName(String subcategoryName, String categoryName) {
        return subcategoryRepository.findByNameAndCategoryName(subcategoryName, categoryName).orElse(null);
    }

    @Override
    public List<SubcategoryDTO> findAllDTOs() {
        return subcategoryRepository.findAllByOrderByCategoryName()
                .stream()
                .filter(Objects::nonNull)
                .map((Subcategory subcategory) -> objectFactory.getInstance(SubcategoryDTO.class, subcategory))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubcategoryDTO> searchName(String name) {
        return subcategoryRepository.findAllByNameContainingIgnoreCase(name)
                .stream()
                .filter(Objects::nonNull)
                .map((Subcategory subcategory) -> objectFactory.getInstance(SubcategoryDTO.class, subcategory))
                .collect(Collectors.toList());
    }

    @Override
    public SubcategoryDTO createNewDTO() {
        return new SubcategoryDTO();
    }

    @Override
    public SubcategoryDTO saveDTO(SubcategoryDTO entityDTO) {
        Subcategory subcategory = new Subcategory(entityDTO);
        subcategory.setCategory(categoryRepository.findByNameIgnoreCase(entityDTO.getCategoryName()).orElse(null));
        Optional<Subcategory> subcategoryDB = subcategoryRepository.findByNameAndCategoryName(subcategory.getName(), entityDTO.getCategoryName());
        if (subcategoryDB.isPresent() && entityDTO.getId()==null) {
            Notification.show("Данная подкатегория уже внесена в базу");
            return null;
        } else {
            return new SubcategoryDTO(subcategoryRepository.save(subcategory));
        }
    }

    @Override
    @Transactional
    public void deleteDTO(SubcategoryDTO entityDTO) {
        Subcategory subcategory = subcategoryRepository.findById(entityDTO.getId()).orElse(null);
        if(subcategory == null) {
            throw new EntityNotFoundException();
        } else {
            Set<Product> products = subcategory.getProducts();
            for (Product tempProduct : products) {
                tempProduct.setSubcategory(subcategoryRepository.findById(1L).orElse(null));
            }
            products.clear();
            subcategoryRepository.delete(subcategory);
        }

    }
}

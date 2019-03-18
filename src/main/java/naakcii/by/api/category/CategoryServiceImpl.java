package naakcii.by.api.category;

import naakcii.by.api.service.CrudService;
import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService, CrudService<CategoryDTO> {

    private final CategoryRepository categoryRepository;
    private final ObjectFactory objectFactory;
    
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ObjectFactory objectFactory) {
    	this.categoryRepository = categoryRepository;
    	this.objectFactory = objectFactory;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
    	return categoryRepository.findAllByIsActiveTrueOrderByPriorityAsc()
    			.stream()
    			.filter(Objects::nonNull)
    			.map((Category category) -> objectFactory.getInstance(CategoryDTO.class, category))
    			.collect(Collectors.toList());
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<String> getAllActiveCategoriesNames() {
        return categoryRepository.findAllByIsActiveTrueOrderByPriorityAsc()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> findAllDTOs() {
        return categoryRepository.findAllByOrderByPriority()
                .stream()
                .filter(Objects::nonNull)
                .map((Category category) -> objectFactory.getInstance(CategoryDTO.class, category))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> searchName(String name) {
        return categoryRepository.findAllByNameContainingIgnoreCase(name)
                .stream()
                .filter(Objects::nonNull)
                .map((Category category) -> objectFactory.getInstance(CategoryDTO.class, category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO createNewDTO() {
        return new CategoryDTO();
    }

    @Override
    public CategoryDTO saveDTO(CategoryDTO entityDTO) {
        return new CategoryDTO(categoryRepository.save(new Category(entityDTO)));
    }

    @Override
    public void deleteDTO(CategoryDTO entityDTO) {
        Category category = categoryRepository.findById(entityDTO.getId()).orElse(null);
        if(category == null) {
            throw new EntityNotFoundException();
        } else {
            categoryRepository.delete(category);
        }
    }
}

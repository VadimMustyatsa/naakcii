package naakcii.by.api.service.serviceImpl;

import naakcii.by.api.repository.model.Category;
import naakcii.by.api.repository.model.repository.CategoryRepository;
import naakcii.by.api.service.CategoryService;
import naakcii.by.api.service.modelDTO.CategoryDTO;
import naakcii.by.api.service.util.CategoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Repository
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class CategoryServiceImpl implements CategoryService {

    private CategoryConverter categoryConverter = new CategoryConverter();

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = categoryConverter.convert(categoryDTO);
        categoryRepository.save(category);
        return categoryDTO;
    }

    @Override
    public CategoryDTO delete(Long categoryId) {
        CategoryDTO categoryDTO = findById(categoryId);
        if (categoryDTO != null) {
            categoryRepository.softDelete(categoryId);
        }
        return categoryDTO;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categoryList = categoryRepository.findAllByIsActiveTrue();
        List<CategoryDTO> categoryDTOList = new ArrayList<CategoryDTO>();
        for (Category category : categoryList) {
            categoryDTOList.add(categoryConverter.convert(category));
        }
        return categoryDTOList;
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDTO findById(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
        	 CategoryDTO categoryDTO = categoryConverter.convert(category.get());
        	 return categoryDTO;
        }
        return null;
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        Category category = new Category();
        category = categoryConverter.convert(categoryDTO);
        return null;
    }
}

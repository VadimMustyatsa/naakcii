package naakcii.by.api.service.ServiceImpl;

import naakcii.by.api.repository.dao.CategoryDao;
import naakcii.by.api.repository.model.Category;
import naakcii.by.api.service.CategoryService;
import naakcii.by.api.service.modelDTO.CategoryDTO;
import naakcii.by.api.service.util.CategoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryConverter categoryConverter = new CategoryConverter();

    @Autowired
    private CategoryDao repository;


    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = categoryConverter.convert(categoryDTO);
        repository.save(category);
        return categoryDTO;
    }

    @Override
    public CategoryDTO delete(Long id) {
        CategoryDTO categoryDTO = findById(id);
        if (categoryDTO != null) {
            Category category = categoryConverter.convert(categoryDTO);
            repository.softDelete(category);
        }
        return categoryDTO;
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categoryList = repository.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<CategoryDTO>();
        for (Category category : categoryList) {
            categoryDTOList.add(categoryConverter.convert(category));
        }
        return categoryDTOList;
    }

    @Override
    public CategoryDTO findById(Long id) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO = categoryConverter.convert(repository.findById(id));
        return categoryDTO;
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        Category category = new Category();
        category = categoryConverter.convert(categoryDTO);
        return null;
    }
}

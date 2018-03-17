package naakcii.by.api.service.ServiceImpl;

import naakcii.by.api.repository.model.Category;
import naakcii.by.api.repository.CategoryRepository;
import naakcii.by.api.service.CategoryService;
import naakcii.by.api.service.util.CategoryConverter;
import naakcii.by.api.service.modelDTO.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryConverter categoryConverter = new CategoryConverter();

    @Autowired
    private CategoryRepository repository;


    @Override
    public CategoryDTO create(CategoryDTO categoryDTO) {
        Category category = categoryConverter.convert(categoryDTO);
        repository.save(category);
        return categoryDTO;
    }

    @Override
    public CategoryDTO delete(int id) {
        CategoryDTO categoryDTO = findById(id);
        if (categoryDTO != null) {
            Category category = categoryConverter.convert(categoryDTO);
            repository.delete(category);
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
    public CategoryDTO findById(int id) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO = categoryConverter.convert(repository.findOne(id));
        return categoryDTO;
    }

    @Override
    public CategoryDTO update(CategoryDTO categoryDTO) {
        Category category = new Category();
        category = categoryConverter.convert(categoryDTO);
        return null;
    }
}

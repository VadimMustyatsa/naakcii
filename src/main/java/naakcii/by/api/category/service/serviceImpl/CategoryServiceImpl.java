package naakcii.by.api.category.service.serviceImpl;

import naakcii.by.api.category.repository.CategoryRepository;
import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.category.service.CategoryService;
import naakcii.by.api.category.service.model.CategoryDTO;
import naakcii.by.api.category.service.util.CategoryConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
/*
    private CategoryConverter categoryConverter = new CategoryConverter();

    @Autowired
    CategoryRepository repository;

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categoryList = repository.findAll();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : categoryList) {
            CategoryDTO categoryDTO = categoryConverter.converter(category);
            categoryDTOList.add(categoryDTO);
        }
        Collections.sort(categoryDTOList, Comparator.comparing(CategoryDTO::getPriority));
        Collections.reverse(categoryDTOList);
        return categoryDTOList;
    }*/
}

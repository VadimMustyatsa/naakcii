package naakcii.by.api.repository;

import naakcii.by.api.repository.model.Category;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CategoryRepository extends Repository<Category, Integer> {

    void delete(Category category);

    List<Category> findAll();

    Category findOne(int id);

    Category save(Category category);
}

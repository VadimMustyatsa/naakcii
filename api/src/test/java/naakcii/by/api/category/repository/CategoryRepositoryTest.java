package naakcii.by.api.category.repository;

import naakcii.by.api.category.repository.model.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TestEntityManager entityManager;

    private Category firstCategory;

    @Before
    public void setUp() {
        firstCategory = new Category("firstCategory", true);
        Category secondCategory = new Category("secondCategory", false);
        Category thirdCategory = new Category("thirdCategory", true);
        entityManager.persist(firstCategory);
        entityManager.persist(secondCategory);
        entityManager.persist(thirdCategory);
        entityManager.flush();
    }

    @Test
    public void findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        assertThat(categoryList.size()).isEqualTo(3);
        assertThat(categoryList.get(0).getName()).isEqualTo("firstCategory");
        assertThat(categoryList.get(1).getName()).isEqualTo("secondCategory");
        assertThat(categoryList.get(2).getName()).isEqualTo("thirdCategory");
    }

    @Test
    public void findAllByIsActiveTrue() {
        List<Category> categoryList = categoryRepository.findAllByIsActiveTrue();
        assertThat(categoryList.size()).isEqualTo(2);
        assertThat(categoryList.get(0).getName()).isEqualTo("firstCategory");
        assertThat(categoryList.get(1).getName()).isEqualTo("thirdCategory");
    }

    @Test
    public void findAllByIsActiveTrueOrderByPriorityAsc() {
        List<Category> categoryList = categoryRepository.findAllByIsActiveTrueOrderByPriorityAsc();
        assertThat(categoryList.size()).isEqualTo(2);
        assertThat(categoryList.get(0).getName()).isEqualTo("firstCategory");
        assertThat(categoryList.get(1).getName()).isEqualTo("thirdCategory");
    }

    @Test
    public void softDelete() {
        categoryRepository.softDelete(firstCategory.getId());
        entityManager.refresh(firstCategory);
        entityManager.flush();
        assertThat(firstCategory.isActive()).isEqualTo(false);
    }

}
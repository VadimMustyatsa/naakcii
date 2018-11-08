package naakcii.by.api.subcategory.repository;

import naakcii.by.api.category.repository.model.Category;
import naakcii.by.api.subcategory.repository.model.Subcategory;
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
public class SubcategoryRepositoryTest {

    @Autowired
    SubcategoryRepository subcategoryRepository;

    @Autowired
    TestEntityManager entityManager;

    private Category firstCategory;
    private Subcategory firstSubcategory;

    @Before
    public void setUp() {
        firstCategory = new Category("firstCategory", true);
        Category secondCategory = new Category("secondCategory", true);
        entityManager.persist(firstCategory);
        entityManager.persist(secondCategory);
        firstSubcategory =
                new Subcategory("firstSubcategory", true, firstCategory, 1);
        Subcategory secondSubcategory =
                new Subcategory("secondSubcategory", false, firstCategory, 2);
        Subcategory thirdSubcategory =
                new Subcategory("thirdSubcategory", true, secondCategory, 3);
        Subcategory fourthSubcategory =
                new Subcategory("fourthSubcategory", true, firstCategory, 4);
        entityManager.persist(firstSubcategory);
        entityManager.persist(secondSubcategory);
        entityManager.persist(thirdSubcategory);
        entityManager.persist(fourthSubcategory);
        entityManager.flush();
    }

    @Test
    public void findByCategoryId() {
        List<Subcategory> subcategoryList = subcategoryRepository.findByCategoryId(firstCategory.getId());
        assertThat(subcategoryList.size()).isEqualTo(3);
        assertThat(subcategoryList.get(0).getName()).isEqualTo("firstSubcategory");
        assertThat(subcategoryList.get(1).getName()).isEqualTo("secondSubcategory");
        assertThat(subcategoryList.get(2).getName()).isEqualTo("fourthSubcategory");
    }

    @Test
    public void findByIsActiveTrueAndCategoryId() {
        List<Subcategory> subcategoryList = subcategoryRepository
                .findByIsActiveTrueAndCategoryId(firstCategory.getId());
        assertThat(subcategoryList.size()).isEqualTo(2);
        assertThat(subcategoryList.get(0).getName()).isEqualTo("firstSubcategory");
        assertThat(subcategoryList.get(1).getName()).isEqualTo("fourthSubcategory");
    }

    @Test
    public void findByIsActiveTrueAndCategoryIdOrderByNameAsc() {
        List<Subcategory> subcategoryList = subcategoryRepository
                .findByIsActiveTrueAndCategoryIdOrderByNameAsc(firstCategory.getId());
        assertThat(subcategoryList.size()).isEqualTo(2);
        assertThat(subcategoryList.get(0).getName()).isEqualTo("firstSubcategory");
        assertThat(subcategoryList.get(1).getName()).isEqualTo("fourthSubcategory");
    }

    @Test
    public void findByIsActiveTrueAndCategoryIdOrderByNameDesc() {
        List<Subcategory> subcategoryList = subcategoryRepository
                .findByIsActiveTrueAndCategoryIdOrderByNameDesc(firstCategory.getId());
        assertThat(subcategoryList.size()).isEqualTo(2);
        assertThat(subcategoryList.get(0).getName()).isEqualTo("fourthSubcategory");
        assertThat(subcategoryList.get(1).getName()).isEqualTo("firstSubcategory");
    }

    @Test
    public void findByNameAndCategoryName() {
        Subcategory firstSubcategory = subcategoryRepository
                .findByNameAndCategoryName("firstSubcategory", "firstCategory");
        Subcategory secondSubcategory = subcategoryRepository
                .findByNameAndCategoryName("firstSubcategory", "secondCategory");
        assertThat(firstSubcategory).isNotEqualTo(null);
        assertThat(secondSubcategory).isEqualTo(null);
    }

    @Test
    public void findByIsActiveTrueAndCategoryIdOrderByPriorityAsc() {
        List<Subcategory> subcategoryList = subcategoryRepository
                .findByIsActiveTrueAndCategoryIdOrderByPriorityAsc(firstCategory.getId());
        assertThat(subcategoryList.size()).isEqualTo(2);
        assertThat(subcategoryList.get(0).getName()).isEqualTo("firstSubcategory");
        assertThat(subcategoryList.get(1).getName()).isEqualTo("fourthSubcategory");
    }

    @Test
    public void findByIsActiveTrueAndCategoryIdOrderByPriorityDesc() {
        List<Subcategory> subcategoryList = subcategoryRepository
                .findByIsActiveTrueAndCategoryIdOrderByPriorityDesc(firstCategory.getId());
        assertThat(subcategoryList.size()).isEqualTo(2);
        assertThat(subcategoryList.get(0).getName()).isEqualTo("fourthSubcategory");
        assertThat(subcategoryList.get(1).getName()).isEqualTo("firstSubcategory");
    }

    @Test
    public void softDelete() {
        Long id = firstSubcategory.getId();
        subcategoryRepository.softDelete(id);
        entityManager.refresh(firstSubcategory);
        entityManager.flush();
        assertThat(firstSubcategory.isActive()).isEqualTo(false);
    }
}
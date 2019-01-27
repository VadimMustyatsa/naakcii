package naakcii.by.api.statistics;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-unit-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StatisticsRepositoryTest {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() {
        Statistics firstStatistics = new Statistics(1, 111, 11, new GregorianCalendar(2018, 0, 1));
        Statistics secondStatistics = new Statistics(2, 222, 22, new GregorianCalendar(2018, 1, 2));
        entityManager.persist(firstStatistics);
        entityManager.persist(secondStatistics);
        entityManager.flush();
    }

    @Test
    public void test_find_first_by_order_by_id_asc() {
        Statistics statistics = statisticsRepository.findFirstByOrderByIdAsc();
        assertThat(statistics.getId()).isEqualTo(1L);
        assertThat(statistics.getChainQuantity()).isEqualTo(1);
        assertThat(statistics.getDiscountedProducts()).isEqualTo(111);
        assertThat(statistics.getAverageDiscountPercentage()).isEqualTo(11);
        assertThat(statistics.getCreationDate().getTime()).isEqualTo("2018-01-01");
    }

    @Test
    public void test_save() {
        Statistics statistics = new Statistics(3, 333, 33, new GregorianCalendar(2018, 2, 3));
        statisticsRepository.save(statistics);
        assertThat(statistics.getId()).isNotNull();
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_incorrect_null_save() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Chain quantity of the statistics mustn't be null.");
        thrown.expectMessage("Discounted products of the statistics mustn't be null.");
        thrown.expectMessage("Average discount percentage of the statistics mustn't be null.");
        thrown.expectMessage("Creation date of the statistics mustn't be null.");
        Statistics statistics = new Statistics();
        statisticsRepository.save(statistics);
    }

    @Test
    public void test_incorrect_negative_save() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Chain quantity of the statistics '-1' must be positive.");
        thrown.expectMessage("Discounted products of the statistics '-1' must be positive.");
        thrown.expectMessage("Average discount percentage of the statistics '-1' must be positive.");
        Statistics statistics = new Statistics(-1, -1, -1, new GregorianCalendar());
        statisticsRepository.save(statistics);
    }

}

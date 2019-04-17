package naakcii.by.api.statistics;

import org.junit.Before;
import org.junit.Test;
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
        assertThat(statistics.getChainQuantity()).isEqualTo(1);
        assertThat(statistics.getDiscountedProducts()).isEqualTo(111);
        assertThat(statistics.getAverageDiscountPercentage()).isEqualTo(11);
        assertThat(statistics.getCreationDate().getTime()).isEqualTo("2018-01-01");
    }
}

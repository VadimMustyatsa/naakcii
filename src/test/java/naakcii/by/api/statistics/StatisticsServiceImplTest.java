package naakcii.by.api.statistics;

import naakcii.by.api.util.ObjectFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class StatisticsServiceImplTest {

    @MockBean
    private StatisticsRepository statisticsRepository;

    @Autowired
    private StatisticsService statisticsService;

    @Test
    public void test_find_current_statistics() throws Exception {
        GregorianCalendar calendar =new GregorianCalendar(2018, 0, 1);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC") );
        Statistics statistics = new Statistics(1, 111, 11,calendar);
        StatisticsDTO statisticsDTO = new StatisticsDTO(statistics);
        Mockito.when(statisticsRepository.findFirstByOrderByIdAsc()).thenReturn(statistics);
        ObjectFactory mock = Mockito.mock(ObjectFactory.class);
        Mockito.when(mock.getInstance(StatisticsDTO.class, statistics)).thenReturn(statisticsDTO);
        StatisticsDTO actualStatisticsDTO = statisticsService.findCurrentStatistics();
        assertThat(actualStatisticsDTO.getChainQuantity()).isEqualTo(1);
        assertThat(actualStatisticsDTO.getDiscountedProducts()).isEqualTo(111);
        assertThat(actualStatisticsDTO.getAverageDiscountPercentage()).isEqualTo(11);
        assertThat(actualStatisticsDTO.getCreationDateMillis()).isEqualTo(1514764800000L);
    }

    @Test
    public void test_update_statistics() throws Exception {
    }

    @Configuration
    @ComponentScan(basePackages = {"naakcii.by.api.statistics", "naakcii.by.api.util"})
    static class StatisticsTestConfig {

        @Bean
        public StatisticsService statisticsService() {
            return new StatisticsServiceImpl();
        }
    }
}

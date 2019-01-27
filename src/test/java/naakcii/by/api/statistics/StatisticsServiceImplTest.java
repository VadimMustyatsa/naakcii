package naakcii.by.api.statistics;

import naakcii.by.api.util.ObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceImplTest {

    private StatisticsService statisticsService;
    private Statistics statistics;
    private GregorianCalendar calendar;

    @Mock
    private StatisticsRepository statisticsRepository;

    @Mock
    private ObjectFactory objectFactory;

    @Before
    public void setUp() {
        statisticsService = new StatisticsServiceImpl(statisticsRepository, objectFactory);
        calendar = new GregorianCalendar(2018, 0, 1);
        statistics = new Statistics(2, 234, 11, calendar);
    }

    @Test
    public void test_find_current_statistics() {
        StatisticsDTO statisticsDTO = new StatisticsDTO(statistics);
        when(statisticsRepository.findFirstByOrderByIdAsc()).thenReturn(statistics);
        when(objectFactory.getInstance(StatisticsDTO.class, statistics)).thenReturn(statisticsDTO);
        StatisticsDTO actualStatisticsDTO = statisticsService.getCurrentStatistics();
        assertThat(actualStatisticsDTO.getChainQuantity()).isEqualTo(2);
        assertThat(actualStatisticsDTO.getDiscountedProducts()).isEqualTo(234);
        assertThat(actualStatisticsDTO.getAverageDiscountPercentage()).isEqualTo(11);
        assertThat(actualStatisticsDTO.getCreationDateMillis()).isEqualTo(1514754000000L);
    }

    @Test
    public void test_update_statistics() {
        Calendar updatedCalendar = new GregorianCalendar(2018, 0, 2);
        Statistics updatedStatistics = new Statistics(4, 345, 20, updatedCalendar);
        StatisticsDTO updatedStatisticsDTO = new StatisticsDTO(updatedStatistics);
        when(statisticsRepository.findFirstByOrderByIdAsc()).thenReturn(statistics);
        when(statisticsRepository.save(updatedStatistics)).thenReturn(updatedStatistics);
        when(objectFactory.getInstance(StatisticsDTO.class, updatedStatistics)).thenReturn(updatedStatisticsDTO);
        StatisticsDTO actualStatiscticsDTO = statisticsService.updateStatistics(4, 345, 20, updatedCalendar);
        assertThat(actualStatiscticsDTO.getChainQuantity()).isEqualTo(4);
        assertThat(actualStatiscticsDTO.getDiscountedProducts()).isEqualTo(345);
        assertThat(actualStatiscticsDTO.getAverageDiscountPercentage()).isEqualTo(20);
        assertThat(actualStatiscticsDTO.getCreationDateMillis()).isEqualTo(1514840400000L);
    }

}

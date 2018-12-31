package naakcii.by.api.statistics.service.ServiceImpl;

import naakcii.by.api.statistics.repository.StatisticsRepository;
import naakcii.by.api.statistics.service.StatisticsService;
import naakcii.by.api.statistics.service.modelDTO.StatisticsDTO;
import naakcii.by.api.statistics.service.util.StatisticsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    StatisticsRepository statisticsRepository;

    @Autowired
    StatisticsConverter statisticsConverter;

    @Override
    public StatisticsDTO getStatistics() {
        Long index = Long.valueOf(1);
        StatisticsDTO statisticList = statisticsConverter.convert(statisticsRepository.findOneById(index));
        return statisticList;
    }
}

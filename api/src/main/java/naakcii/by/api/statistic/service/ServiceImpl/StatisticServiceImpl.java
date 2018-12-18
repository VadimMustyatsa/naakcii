package naakcii.by.api.statistic.service.ServiceImpl;

import naakcii.by.api.statistic.repository.StatisticRepository;
import naakcii.by.api.statistic.service.StatisticService;
import naakcii.by.api.statistic.service.modelDTO.StatisticDTO;
import naakcii.by.api.statistic.service.util.StatisticConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    StatisticRepository statisticRepository;

    @Autowired
    StatisticConverter statisticConverter;

    @Override
    public StatisticDTO getStatistic() {
        Long index = Long.valueOf(1);
        StatisticDTO statisticList = statisticConverter.convert(statisticRepository.findOneById(index));
        return statisticList;
    }
}

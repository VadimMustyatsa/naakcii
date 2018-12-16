package naakcii.by.api.statistic;

import naakcii.by.api.util.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private StatisticRepository statisticRepository;
    private ObjectFactory objectFactory;

    @Override
    public StatisticDTO findCurrentStatistic() {
        Statistic statistic = statisticRepository.findTopByOrOrderByIdDesc();
        return objectFactory.getInstance(StatisticDTO.class, statistic);
    }
}

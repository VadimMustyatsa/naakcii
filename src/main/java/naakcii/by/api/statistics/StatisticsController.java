package naakcii.by.api.statistics;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import naakcii.by.api.config.ApiConfigConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "REST API для сущности Statistics")
@RestController
@RequestMapping({"/statistics"})
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping(produces = ApiConfigConstants.API_V_2_0)
    @ApiOperation("Возвращает статистику по сервису НаАкции.Бел")
    public StatisticsDTO getStatistics() {
        return statisticsService.findCurrentStatistics();
    }
}

package naakcii.by.api.chainstatistics;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import naakcii.by.api.config.ApiConfigConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "REST API для сущности ChainStatistics")
@RestController
@RequestMapping({"/statistics/chains"})
public class ChainStatisticsController {

    @Autowired
    private ChainStatisticsService chainStatisticsService;

    @ApiOperation("Возвращает статистику всех торговых сетей")
    @GetMapping(produces = ApiConfigConstants.API_V_2_0)
    private List<ChainStatisticsDTO> getAllChainsStatistics() {
        return chainStatisticsService.getAllChainsStatistics();
    }

    @ApiOperation("Возвращает статистику торговой сети по id")
    @GetMapping(path = "/{chainId}", produces = ApiConfigConstants.API_V_2_0)
    private ChainStatisticsDTO getChainStatistics(
            @ApiParam(value = "Id торговой сети", required = true)
            @PathVariable Integer chainId
    ) {
        return chainStatisticsService.getChainStatistics(chainId);
    }

}

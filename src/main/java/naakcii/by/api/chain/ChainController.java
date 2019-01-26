package naakcii.by.api.chain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import naakcii.by.api.config.ApiConfigConstants;

import java.util.List;

@Api(description = "REST API для сущности Chain")
@RestController
@RequestMapping({"/chains"})
public class ChainController {

    private ChainService chainService;
    
    @Autowired
    public ChainController(ChainService chainService) {
    	this.chainService = chainService;
    }

    @GetMapping(produces = ApiConfigConstants.API_V_2_0)
    @ApiOperation("Возвращает список всех торговых сетей")
    public List<ChainDTO> getAllChains() {
        return chainService.getAllChains();
    }
}

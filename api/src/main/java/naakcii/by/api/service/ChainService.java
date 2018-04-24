package naakcii.by.api.service;

import naakcii.by.api.service.modelDTO.ChainDTO;

import java.util.List;
import java.util.Map;

public interface ChainService {

    List<ChainDTO> findAll();

    Map<String, Integer> getChainProperties(ChainDTO chainDTO);
}

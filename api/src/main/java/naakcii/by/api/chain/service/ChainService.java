package naakcii.by.api.chain.service;

import naakcii.by.api.chain.service.modelDTO.ChainDTO;

import java.util.List;

public interface ChainService {
    List<ChainDTO> findAll();
}

package naakcii.by.api.service;

import naakcii.by.api.entity.AbstractDTOEntity;

import java.util.List;

public interface CrudService<DTO extends AbstractDTOEntity> {

    List<DTO> findAllDTOs();

    List<DTO> searchName(String name);

    DTO createNewDTO();

    DTO saveDTO(DTO entityDTO);

    void deleteDTO(DTO entityDTO);
}

package naakcii.by.api.chain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import naakcii.by.api.util.ObjectFactory;

@Service
public class ChainServiceImpl implements ChainService {

    private ChainRepository chainRepository;
    private ObjectFactory objectFactory;
    
    @Autowired
    public ChainServiceImpl(ChainRepository chainRepository, ObjectFactory objectFactory) {
    	this.chainRepository = chainRepository;
    	this.objectFactory = objectFactory;
    }  

    @Override
    public List<ChainDTO> getAllChains() {
        return chainRepository.findAllByIsActiveTrueOrderByNameAsc()
        		.stream()
        		.filter(Objects::nonNull)
        		.map((Chain chain) -> objectFactory.getInstance(ChainDTO.class, chain))
        		.collect(Collectors.toList());
    }
}

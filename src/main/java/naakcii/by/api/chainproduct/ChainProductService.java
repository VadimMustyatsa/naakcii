package naakcii.by.api.chainproduct;

import java.time.LocalDate;
import java.util.List;

public interface ChainProductService {

	List<ChainProductDTO> findAllByFilterStart(LocalDate date);
	List<ChainProductDTO> findAllByFilterEnd(LocalDate date);
	List<ChainProductDTO> findAllByChain(String chainName);
	List<ChainProductDTO> findAllByChainProductType(String chainProductTypeName);
 }

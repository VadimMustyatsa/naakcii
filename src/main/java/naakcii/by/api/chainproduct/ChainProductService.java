package naakcii.by.api.chainproduct;

import java.time.LocalDate;
import java.util.List;

public interface ChainProductService {

	List<ChainProductDTO> findAllByFilter(LocalDate startDate, LocalDate endDate, String chainName,
                                          String productName, String typeName);
 }

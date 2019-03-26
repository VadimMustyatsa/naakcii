package naakcii.by.api.chain;

import java.util.List;

public interface ChainService {

    List<ChainDTO> checkIsActive(String filter);
    List<String> getAllChainNames();
}

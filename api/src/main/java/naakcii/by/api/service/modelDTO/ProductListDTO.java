package naakcii.by.api.service.modelDTO;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class ProductListDTO {

    private Map<String,List<Integer>> map = new HashedMap<String, List<Integer>>();
    private List<ProductDTO> productDTOList = new ArrayList<ProductDTO>();

    public void setMapValue(List<Integer> list) {

    }
}

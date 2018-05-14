package naakcii.by.api.service.util;

import naakcii.by.api.repository.model.Chain;
import naakcii.by.api.service.modelDTO.ChainDTO;

public class ChainConverter {

    public ChainConverter() {
    }

    public ChainDTO convert(Chain chain) {
        ChainDTO chainDTO = new ChainDTO();
        /*chainDTO.setName(chain.getName());
        chainDTO.setImgLogo(chain.getIcon());
        chainDTO.setImgLogoSmall(chain.getSmallIcon());
        chainDTO.setId(chain.getId());
        chainDTO.setLink(chain.getLink());*/
        return chainDTO;
    }

    public Chain convert(ChainDTO chainDTO) {
        Chain chain = new Chain();
        /*chain.setId(chainDTO.getId());
        chain.setLink(chainDTO.getLink());
        chain.setName(chainDTO.getName());
        chain.setLink(chainDTO.getLink());
        chain.setSmallIcon(chainDTO.getImgLogoSmall());*/
        return chain;
    }
}

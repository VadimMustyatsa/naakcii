package naakcii.by.api.chain.service.util;


import naakcii.by.api.chain.repository.model.Chain;
import naakcii.by.api.chain.service.modelDTO.ChainDTO;

public class ChainConverter {

    public ChainConverter() {
    }

    public ChainDTO convert(Chain chain) {
        ChainDTO chainDTO = new ChainDTO();
        chainDTO.setName(chain.getName());
        chainDTO.setImgLogo(chain.getLogo());
        chainDTO.setImgLogoSmall(chain.getLogoSmall());
        chainDTO.setId(chain.getId());
        chainDTO.setLink(chain.getLink());
        return chainDTO;
    }

    public Chain convert(ChainDTO chainDTO) {
        Chain chain = new Chain();
        chain.setId(chainDTO.getId());
        chain.setLink(chainDTO.getLink());
        chain.setName(chainDTO.getName());
        chain.setLink(chainDTO.getLink());
        chain.setLogoSmall(chainDTO.getImgLogoSmall());
        chain.setLogo(chainDTO.getImgLogoSmall());
        return chain;
    }
}

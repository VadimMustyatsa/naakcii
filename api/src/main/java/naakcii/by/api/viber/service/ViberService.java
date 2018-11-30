package naakcii.by.api.viber.service;

import naakcii.by.api.viber.service.modelDTO.ViberMessageDTO;

public interface ViberService {
    ViberMessageDTO getFileMessage(String userId, String fileName);

    ViberMessageDTO getWelcomeMessage();

    ViberMessageDTO getTextMessage(String userId, String text);
}

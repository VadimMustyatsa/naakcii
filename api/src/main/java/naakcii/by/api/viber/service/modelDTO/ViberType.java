package naakcii.by.api.viber.service.modelDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ViberType {
    @JsonProperty("file")
    FILE,
    @JsonProperty("text")
    TEXT;
}

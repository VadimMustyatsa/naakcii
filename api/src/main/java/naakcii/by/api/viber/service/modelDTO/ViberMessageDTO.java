package naakcii.by.api.viber.service.modelDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class ViberMessageDTO {

    @JsonProperty("tracking_data")
    private static String TRACKING_DATA = "tracking data";

    private String receiver;

    @JsonProperty("min_api_version")
    private Integer apiVersion;

    private ViberType type;

    private String text;

    @JsonProperty("media")
    private String fileUrl;

    private Integer size;

    @JsonProperty("file_name")
    private String fileName;

    private ViberSenderDTO sender;

    public ViberMessageDTO(Integer apiVersion) {
        this.apiVersion = apiVersion;
    }
}

/*
{
   "receiver":"01234567890A=",
   "min_api_version":1,
   "sender":{
      "name":"John McClane",
      "avatar":"http://avatar.example.com"
   },
   "tracking_data":"tracking data",
   "type":"file",
   "media":"http://www.images.com/file.doc",
   "size":10000,
   "file_name":"name_of_file.doc"
}

{
   "receiver":"01234567890A=",
   "min_api_version":1,
   "sender":{
      "name":"John McClane",
      "avatar":"http://avatar.example.com"
   },
   "tracking_data":"tracking data",
   "type":"text",
   "text":"Hello world!"
}

 */

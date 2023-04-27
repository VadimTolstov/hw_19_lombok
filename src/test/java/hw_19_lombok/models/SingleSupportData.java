package hw_19_lombok.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

//"support": {
//        "url": "https://reqres.in/#support-heading",
//        "text": "To keep ReqRes free, contributions towards server costs are appreciated!"
//        }

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleSupportData {
    private String url;
}

package hw_19_lombok.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

//"data": {
//        "id": 2,
//        "email": "janet.weaver@reqres.in",
//        "first_name": "Janet",
//        "last_name": "Weaver",
//        "avatar": "https://reqres.in/img/faces/2-image.jpg"
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleUserData {
  //  String supportUrl;
    Integer id;
    String email;
    @JsonProperty("first_name")
    String firstName;


}

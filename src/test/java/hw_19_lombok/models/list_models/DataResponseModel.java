package hw_19_lombok.models.list_models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

//"data": [
//        {
//        "id": 7,
//        "email": "michael.lawson@reqres.in",
//        "first_name": "Michael",
//        "last_name": "Lawson",
//        "avatar": "https://reqres.in/img/faces/7-image.jpg"
//        },
//        {
//        "id": 8,
//        "email": "lindsay.ferguson@reqres.in",
//        "first_name": "Lindsay",
//        "last_name": "Ferguson",
//        "avatar": "https://reqres.in/img/faces/8-image.jpg"
//        }]
@Data
public class DataResponseModel {
    private Integer id;
    private String email,
            avatar;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;


}

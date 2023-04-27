package hw_19_lombok.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hw_19_lombok.models.list_models.DataResponseModel;
import lombok.Data;

import java.util.ArrayList;

//{
//        "page": 2,
//        "per_page": 6,
//        "total": 12,
//        "total_pages": 2,
//        "data": [
//        {
//        "id": 7,
//        "email": "michael.lawson@reqres.in",
//        "first_name": "Michael",
//        "last_name": "Lawson",
//        "avatar": "https://reqres.in/img/faces/7-image.jpg"
//        }
//        ]
//}
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListUserResponseModel {
    Integer page;
    ArrayList<DataResponseModel> data;
}

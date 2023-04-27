package hw_19_lombok.models;

import lombok.Data;

@Data

public class SingleUserResponse {
    private SingleUserData data;
    private SingleSupportData support;
}

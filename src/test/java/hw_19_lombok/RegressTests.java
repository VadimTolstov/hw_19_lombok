package hw_19_lombok;

import com.google.gson.Gson;
import hw_19_lombok.models.*;
import hw_19_lombok.models.list_models.DataResponseModel;
import hw_19_lombok.testdata.RandomUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static hw_19_lombok.specs.CreateSpec.createnRequestSpec;
import static hw_19_lombok.specs.CreateSpec.сreateResponseSpec;
import static hw_19_lombok.specs.UserSpec.userRequestSpec;
import static hw_19_lombok.specs.UserSpec.userResponseSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegressTests {

    private final String BASIC_URL = "https://reqres.in/";

    @Test
    void сreateUserTest() {
        step("Creates");
        RandomUser randomUser = new RandomUser();
        CreateUserBodyModel createUserBody = new CreateUserBodyModel();
        createUserBody.setName(randomUser.getName());
        createUserBody.setJob(randomUser.getJob());

        CreateUserResponseModel response = step("Make  request", () ->
                given(createnRequestSpec)
                        .body(createUserBody)
                        .when()
                        .post()
                        .then()
                        .spec(сreateResponseSpec)
                        .body(matchesJsonSchemaInClasspath("hw_18/schema/createUserTest.json"))
                        .extract().as(CreateUserResponseModel.class));

        step("Verify response name", () ->
                assertThat(response.getName()).isEqualTo(createUserBody.getName()));
        step("Verify response job", () ->
                assertThat(response.getJob()).isEqualTo(createUserBody.getJob()));
    }

    @Test
    void updateUserTest() {
        RandomUser randomUser = new RandomUser();
        UpdateUserBodyModel updateUserBody = new UpdateUserBodyModel();
        updateUserBody.setName(randomUser.getName());
        updateUserBody.setJob(randomUser.getJob());
        String id = "2";

        UpdateUserResponseModel response = given(userRequestSpec)
                .body(updateUserBody)
                .when()
                .put(id)
                .then()
                .spec(userResponseSpec)
                .body(matchesJsonSchemaInClasspath("hw_18/schema/updateUserTest.json"))
                .extract().as(UpdateUserResponseModel.class);

        assertThat(response.getName()).isEqualTo(updateUserBody.getName());
        assertThat(response.getJob()).isEqualTo(updateUserBody.getJob());
    }


    @Test
    void singleUserTest() {

        String id = "2";
        String url = "https://reqres.in/#support-heading";
        String firstName = "Janet";
        SingleUserResponse response = given(userRequestSpec)
                .when()
                .get(id)
                .then()
                .spec(userResponseSpec)
                .body(matchesJsonSchemaInClasspath("hw_18/schema/singleUserTest.json"))
                .extract().as(SingleUserResponse.class);

        assertThat(response.getData().getId()).isEqualTo(Integer.parseInt(id));
        assertThat(response.getData().getFirstName()).isEqualTo(firstName);
        assertThat(response.getSupport().getUrl()).isEqualTo(url);

    }

    @Test
    void failedRegistrationTest() {
        RandomUser randomUser = new RandomUser();
        String email = randomUser.getEmail();
        String error = "Missing password";
        HashMap<String, String> body = new HashMap<>();
        body.put("email", email);

        Gson gson = new Gson();
        given().body(gson.toJson(body))
                .log().uri()
                .log().body()
                .contentType(JSON)
                .when()
                .post(BASIC_URL + "api/login/")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .assertThat().body("error", equalTo(error))
                .body(matchesJsonSchemaInClasspath("hw_18/schema/failedRegistrationTest.json"));
    }

    @Test
    void listUserTest() {
        Integer page = 2;
        ListUserResponseModel response = step("Make request", () ->
                given(userRequestSpec)
                        .when()
                        .get("?page=2")
                        .then()
                        .spec(userResponseSpec)
                        .body(matchesJsonSchemaInClasspath("hw_18/schema/listUserTest.json"))
                        .extract().as(ListUserResponseModel.class));
        step("Verify page number in response", () ->
                Assertions.assertTrue(response.getPage() == page, "Number page doesn`t match"));
//        assertThat(response.getClass());
      ArrayList<DataResponseModel> arrayList= response.getData();
        step("Verify response id[1]", () -> assertEquals(8, arrayList.get(1).getId()));
        step("Verify response", () -> assertEquals(6, arrayList.size()));
//        step("Verify page number in response", ()->
//                Assertions.assertThat().body("data.last_name", hasItems(list.toArray())));
    }
}

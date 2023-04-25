package hw_19_lombok;

import com.google.gson.Gson;

import hw_19_lombok.models.CreateUserBodyModel;
import hw_19_lombok.models.CreateUserResponseModel;
import hw_19_lombok.testdata.RandomUser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.as;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class RegressTests {

    private final String BASIC_URL = "https://reqres.in/";

    @Test
    void сreateUserTest() {
        RandomUser randomUser = new RandomUser();
        CreateUserBodyModel createUserBody = new CreateUserBodyModel();
        createUserBody.setName(randomUser.getName());
        createUserBody.setJob(randomUser.getJob());

        CreateUserResponseModel response = given()
                .body(createUserBody)
                .log().uri()
                .log().body()
                .contentType(JSON)
                .when()
                .post(BASIC_URL + "api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("hw_18/schema/createUserTest.json"))
                .extract().as(CreateUserResponseModel.class);
    }

    @Test
    void updateUserTest() {
        RandomUser randomUser = new RandomUser();
        String name = randomUser.getName();
        String job = randomUser.getJob();
        String id = "2";
        HashMap<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("job", job);

        Gson gson = new Gson();

        given().body(gson.toJson(body))
                .log().uri()
                .log().body()
                .contentType(JSON)
                .when()
                .put(BASIC_URL + "api/users/" + id)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .assertThat().body("name", equalTo(name))
                .assertThat().body("job", equalTo(job))
                .body(matchesJsonSchemaInClasspath("hw_18/schema/updateUserTest.json"));
    }

    @Test
    void singleUserTest() {
        String id = "2";
        String url = "https://reqres.in/#support-heading";
        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .get(BASIC_URL + "api/users/" + id)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .assertThat().body("support.url", equalTo(url))
                .assertThat().body("data.id", equalTo(Integer.parseInt(id)))
                .body(matchesJsonSchemaInClasspath("hw_18/schema/singleUserTest.json"));
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
        List<String> list = Arrays.asList("Lawson", "Ferguson", "Funke", "Fields", "Edwards", "Howell");
        Integer page = 2;
        String url = "https://reqres.in/#support-heading";
        given()
                .log().uri()
                .contentType(JSON)
                .when()
                .get(BASIC_URL + "api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .assertThat().body("page", equalTo(page))
                .assertThat().body("data.last_name", hasItems(list.toArray()))
                .body(matchesJsonSchemaInClasspath("hw_18/schema/listUserTest.json"));
    }
}

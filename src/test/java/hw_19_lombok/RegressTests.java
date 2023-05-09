package hw_19_lombok;

import hw_19_lombok.helpers.ApiTest;
import hw_19_lombok.models.*;
import hw_19_lombok.models.list_models.DataResponseModel;
import hw_19_lombok.testdata.RandomUser;
import io.qameta.allure.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static hw_19_lombok.specs.CreateSpec.createnRequestSpec;
import static hw_19_lombok.specs.CreateSpec.сreateResponseSpec;
import static hw_19_lombok.specs.UserSpec.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Api Regress Tests")
@Owner("Vadim Tolstov")
@Story("Это @Stories класса")
public class RegressTests {
    private static String map;

    @Test
    @ApiTest
    @Story("Это @Stories теста")
    @Link(name = "Qa", type = "qa")
    @Severity(SeverityLevel.CRITICAL)
    @Link(value = "DEMOQA Practice Form", url = "https://demoqa.com/automation-practice-form")
    @DisplayName("Create request")
    void сreateUserTest() {
        RandomUser randomUser = new RandomUser();
        CreateUserBodyModel createUserBody = new CreateUserBodyModel();
        createUserBody.setName(randomUser.getName());
        createUserBody.setJob(randomUser.getJob());

        CreateUserResponseModel response = step("Make  request post", () ->
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
        map = response.getName();
        System.out.println("это " + map);
    }

    @Test
    @ApiTest
    @DisplayName("Update request")
    void updateUserTest() {
        RandomUser randomUser = new RandomUser();
        UpdateUserBodyModel updateUserBody = new UpdateUserBodyModel();
        updateUserBody.setName(randomUser.getName());
        updateUserBody.setJob(randomUser.getJob());
        String id = "2";

        UpdateUserResponseModel response = step("Обновление данных user " + id, () ->
                given(userRequestSpec)
                        .body(updateUserBody)
                        .when()
                        .put(id)
                        .then()
                        .spec(userResponseSpec)
                        .body(matchesJsonSchemaInClasspath("hw_18/schema/updateUserTest.json"))
                        .extract().as(UpdateUserResponseModel.class));
        step("Verify response name", () ->
                assertThat(response.getName()).isEqualTo(updateUserBody.getName()));
        step("Verify response job", () ->
                Assertions.assertThat(response.getJob()).isEqualTo(updateUserBody.getJob()));
    }

    @Test
    @ApiTest
    @DisplayName("Single request")
    void singleUserTest() {
        String id = "2";
        String url = "https://reqres.in/#support-heading";
        String firstName = "Janet";

        SingleUserResponse response = step("Make  request get", () ->
                given(userRequestSpec)
                        .when()
                        .get(id)
                        .then()
                        .spec(userResponseSpec)
                        .body(matchesJsonSchemaInClasspath("hw_18/schema/singleUserTest.json"))
                        .extract().as(SingleUserResponse.class));

        step("Verify response id", () ->
                assertThat(response.getData().getId()).isEqualTo(Integer.parseInt(id)));
        step("Verify response firstName", () ->
                assertThat(response.getData().getFirstName()).isEqualTo(firstName));
        step("Verify response url", () ->
                assertThat(response.getSupport().getUrl()).isEqualTo(url));

    }

    @Test
    @ApiTest
    @DisplayName("400 request")
    void failedRegistrationTest() {
        RandomUser randomUser = new RandomUser();
        FailedRegistrationBodyModel failedBody = new FailedRegistrationBodyModel();
        failedBody.setEmail(randomUser.getEmail());
        String error = "Missing password";

        FailedRegistrationResponseModel response = step("Make  request post", () ->
                given(failedRequestSpec).
                        body(failedBody)
                        .when()
                        .post()
                        .then()
                        .spec(failedResponseSpec)
                        .body(matchesJsonSchemaInClasspath("hw_18/schema/failedRegistrationTest.json"))
                        .extract().as(FailedRegistrationResponseModel.class));

        step("Verify response error", () ->
                assertThat(response.getError()).isEqualTo(error));
    }

    @Test
    @ApiTest
    @DisplayName("Request a list of users")
    void listUserTest() {
        Integer page = 2;
        String avatar = "https://reqres.in/img/faces/7-image.jpg";
        ListUserResponseModel response = step("Make request", () ->
                given(userRequestSpec)
                        .when()
                        .get("?page=2")
                        .then()
                        .spec(userResponseSpec)
                        .body(matchesJsonSchemaInClasspath("hw_18/schema/listUserTest.json"))
                        .extract().as(ListUserResponseModel.class));

        ArrayList<DataResponseModel> arrayList = response.getData();
        step("Verify page number in response", () ->
                assertThat(response.getPage()).isEqualTo(page));
        step("Verify response id[1]", () ->
                assertEquals(8, arrayList.get(1).getId()));
        step("Verify response", () ->
                assertEquals(6, arrayList.size()));
        step("Verify response avatar[0]", () ->
                assertEquals(avatar, arrayList.get(0).getAvatar()));
//1
    }
}

package user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {
    private final static String USER_REGISTER = "/api/auth/register";
    private final static String USER_LOGIN = "/api/auth/login";
    private final static String USER_PATH = "/api/auth/user";

    @Step("Выполнить запрос регистрации")
    public ValidatableResponse register(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .post(USER_REGISTER)
                .then().log().all();
    }

    @Step("Выполнить запрос авторизации")
    public ValidatableResponse login(UserCredentials user) {
        return given()
                .spec(getSpec())
                .body(user)
                .post(USER_LOGIN)
                .then().log().status();
    }

    @Step("Выполнить запрос на удаление пользователя")
    public ValidatableResponse delete(String token) {
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .delete(USER_PATH)
                .then().log().status();
    }

    @Step("Выполнить запрос на изменение данных пользователя с авторизацией")
    public ValidatableResponse updateWithAuth(User user, String token) {
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .body(user)
                .patch(USER_PATH)
                .then().log().status();
    }

    @Step("Выполнить запрос на изменение данных пользователя без авторизации")
    public ValidatableResponse updateWithoutAuth(User user) {
        return given()
                .spec(getSpec())
                .patch(USER_PATH)
                .then().log().status();
    }

    @Step("Выполнить запрос на получение информации о пользователе")
    public ValidatableResponse getUserInfo(String token) {
        return given()
                .header("Authorization", token)
                .spec(getSpec())
                .get(USER_PATH)
                .then().log().status();
    }
}

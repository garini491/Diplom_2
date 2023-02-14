package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {
    private final static String ORDER = "/api/orders";

    @Step("Выполнить запрос создания заказа с авторизацией")
    public ValidatableResponse createOrderWithAuth(Order order, String token) {
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .body(order)
                .post(ORDER)
                .then();
    }

    @Step("Выполнить запрос создания заказа без автризации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .post(ORDER)
                .then();
    }

    @Step("Выполнить запрос на получение заказов пользователя")
    public ValidatableResponse getUserOrder(String token) {
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .get(ORDER)
                .then();
    }

    @Step("Выполнить запрос на получение заказов пользователя без авторизации")
    public ValidatableResponse getOrder() {
        return given()
                .spec(getSpec())
                .get(ORDER)
                .then();
    }
}

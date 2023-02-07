package order;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client{
    private final static String  ORDER = "/api/orders";


    public ValidatableResponse createOrderWithAuth(Order order, String token) {
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .body(order)
                .post(ORDER)
                .then().log().status();
    }
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .post(ORDER)
                .then().log().status();
    }
    public ValidatableResponse getUserOrder(String token) {
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .get(ORDER)
                .then().log().status();
    }
    public ValidatableResponse getOrder() {
        return given()
                .spec(getSpec())
                .get(ORDER)
                .then().log().status();
    }
}

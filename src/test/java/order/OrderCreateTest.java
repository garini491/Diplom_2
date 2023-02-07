package order;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserData;


import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderCreateTest {
    OrderClient orderClient;
    User user;
    UserClient userClient;
    String bearerToken;
    Order order;


    @Before
    public void setUp() {
        order = new Order();
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = UserData.getDefoultUser();
        bearerToken = userClient.register(user).extract().path("accessToken");
    }

    @Test
    @Description("Проверка создания заказа с авторизацией и переданными ингредиентами")
    public void orderCreateWithAuthTest() {
        order.setIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"});
        ValidatableResponse response = orderClient.createOrderWithAuth(order,bearerToken);
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK,statusCode);
        assertTrue(success);
    }
    @Test
    @Description("Проверка создания заказа с авторизацией, без ингредиентов")
    public void orderCreateWithoutIngredientsWithAuthTest() {
        order.setIngredients(new String[]{});
        ValidatableResponse response = orderClient.createOrderWithAuth(order,bearerToken);
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        String expectedMessage = "Ingredient ids must be provided";
        String actualMessage = response.extract().path("message");
        assertFalse(success);
        assertEquals(expectedMessage,actualMessage);
        assertEquals(SC_BAD_REQUEST, statusCode);
    }
    @Test
    @Description("Проверка создания заказа с авторизацией, с некоректным хэшем")
    public void orderCreateNotValidIngredientWithAuthTest() {
        order.setIngredients(new String[]{"12348"});
        ValidatableResponse response = orderClient.createOrderWithAuth(order,bearerToken);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }
    @Test
    @Description("Проверка создания заказа без авторизации и переданными ингредиентами")
    public void orderCreateWithoutAuthTest() {
        order.setIngredients(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"});
        ValidatableResponse response = orderClient.createOrderWithoutAuth(order);
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK,statusCode);
        assertTrue(success);
    }
    @Test
    @Description("Проверка создания заказа с авторизацией, без ингредиентов")
    public void orderCreateWithoutIngredientsWithoutAuthTest() {
        order.setIngredients(new String[]{});
        ValidatableResponse response = orderClient.createOrderWithoutAuth(order);
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        String expectedMessage = "Ingredient ids must be provided";
        String actualMessage = response.extract().path("message");
        assertFalse(success);
        assertEquals(expectedMessage,actualMessage);
        assertEquals(SC_BAD_REQUEST, statusCode);
    }
    @Test
    @Description("Проверка создания заказа с авторизацией, с некоректным хэшем")
    public void orderCreateNotValidIngredientWithoutAuthTest() {
        order.setIngredients(new String[]{"123456"});
        ValidatableResponse response = orderClient.createOrderWithoutAuth(order);
        int statusCode = response.extract().statusCode();
        assertEquals(SC_INTERNAL_SERVER_ERROR, statusCode);
    }

    @After
    public void tearDown() {
        userClient.delete(bearerToken);
    }
}

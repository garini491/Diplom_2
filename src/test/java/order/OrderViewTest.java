package order;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserData;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class OrderViewTest {

    User user;
    Order order;
    UserClient userClient;
    OrderClient orderClient;
    String bearerToken;

    @Before
    public void setUp() {
        order = new Order(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70"});
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = UserData.getDefoultUser();
        bearerToken = userClient.register(user).extract().path("accessToken");
        orderClient.createOrderWithAuth(order,bearerToken);
    }

    @Test
    @Description("Получение заказа конкретного пользователя с авторизацией")
    public void getOrderWithAuth() {
        ValidatableResponse response = orderClient.getUserOrder(bearerToken);
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK,statusCode);
        assertTrue(success);
    }
    @Test
    @Description("Получение заказа конкретного пользователя без авторизации")
    public void getOrderWithoutAuth() {
        ValidatableResponse response = orderClient.getOrder();
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        String expected = "You should be authorised";
        String actual = response.extract().path("message");
        assertEquals(SC_UNAUTHORIZED,statusCode);
        assertFalse(success);
        assertEquals(expected,actual);
    }
    @After
    public void tearDown() {
        userClient.delete(bearerToken);
    }


}

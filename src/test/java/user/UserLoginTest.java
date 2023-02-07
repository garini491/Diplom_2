package user;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class UserLoginTest {
    private User user;
    private UserClient userClient;
    private String bearerToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserData.getDefoultUser();
        bearerToken = userClient.register(user).extract().path("accessToken");
    }
    @Test
    @Description("Авторизация пользователя с корректно заполненными данными")
    public void userValidAuth() {
        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK,statusCode);
        assertTrue(success);
    }
    @Test
    @Description("Авторизация пользователя без email")
    public void userWithoutEmailAuth() {
        user.setEmail("");
        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        boolean success = response.extract().path("success");
        String actualMessage = response.extract().path("message");
        String expectedMessage = "email or password are incorrect";
        int statusCode = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED,statusCode);
        assertFalse(success);
        assertEquals(expectedMessage,actualMessage);
    }
    @Test
    @Description("Авторизация пользователя без пароля")
    public void userWithoutPassAuth() {
        user.setPassword("");
        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        boolean success = response.extract().path("success");
        String actualMessage = response.extract().path("message");
        String expectedMessage = "email or password are incorrect";
        int statusCode = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED,statusCode);
        assertFalse(success);
        assertEquals(expectedMessage,actualMessage);
    }
    @Test
    @Description("Авторизация незарегистрированного пользователя")
    public void userNotRegisteredUserAuth() {
        user.setEmail("test.email>1321@bk.ru");
        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        boolean success = response.extract().path("success");
        String actualMessage = response.extract().path("message");
        String expectedMessage = "email or password are incorrect";
        int statusCode = response.extract().statusCode();
        assertEquals(SC_UNAUTHORIZED,statusCode);
        assertFalse(success);
        assertEquals(expectedMessage,actualMessage);
    }

    @After
    public void tearDown() {
        userClient.delete(bearerToken);
    }
}

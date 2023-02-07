package user;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;


import static org.apache.http.HttpStatus.SC_FORBIDDEN;


import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class UserRegisterTest {
    private User user;
    private UserClient userClient;
    private String bearerToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @Description("Регистрация пользователя с корректно заполненными данными")
    public void userValidRegistration() {
        user = UserData.getDefoultUser();
        ValidatableResponse response = userClient.register(user);
        bearerToken = response.extract().path("accessToken");
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK,statusCode);
        assertTrue(success);

        userClient.delete(bearerToken);
    }
    @Test
    @Description("Регистрация пользователя без email")
    public void userWithoutEmailRegistration() {
        user = UserData.getUserWithoutEmail();
        ValidatableResponse response = userClient.register(user);
        String expectedMessage = "Email, password and name are required fields";
        String actualMessage = response.extract().path("message");
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN,statusCode);
        assertFalse(success);
        assertEquals(expectedMessage,actualMessage);
    }
    @Test
    @Description("Регистрация пользователя без пароля")
    public void userWithoutPassRegistration() {
        user = UserData.getUserWithoutPass();
        ValidatableResponse response = userClient.register(user);
        String expectedMessage = "Email, password and name are required fields";
        String actualMessage = response.extract().path("message");
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN,statusCode);
        assertFalse(success);
        assertEquals(expectedMessage,actualMessage);
    }
    @Test
    @Description("Регистрация пользователя без имени")
    public void userWithoutNameRegistration() {
        user = UserData.getUserWithoutName();
        ValidatableResponse response = userClient.register(user);
        String expectedMessage = "Email, password and name are required fields";
        String actualMessage = response.extract().path("message");
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        assertEquals(SC_FORBIDDEN,statusCode);
        assertFalse(success);
        assertEquals(expectedMessage,actualMessage);
    }
    @Test
    @Description("Регистрация неуникального пользователя")
    public void notUniqueUserRegistration() {
        user = UserData.getDefoultUser();
        bearerToken = userClient.register(user).extract().path("accessToken");
        ValidatableResponse response = userClient.register(user);
        boolean success = response.extract().path("success");
        int statusCode = response.extract().statusCode();
        String expectedMessage = "User already exists";
        String actualMessage = response.extract().path("message");
        assertEquals(SC_FORBIDDEN,statusCode);
        assertFalse(success);
        assertEquals(expectedMessage,actualMessage);

        userClient.delete(bearerToken);
    }
}

package user;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class UserChangeDataTest {
    User user;
    User uniqueUser;
    UserClient userClient;
    String bearerToken;
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserData.getDefoultUser();
        bearerToken = userClient.register(user).extract().path("accessToken");
    }

    @Test
    @Description("Изменение всех данных без авторизации")
    public void userAllUpdateWithoutAuth() {
        user.setEmail("test.mail.updated@yandex.ru");
        user.setName("secondName");
        ValidatableResponse updateResponse = userClient.updateWithoutAuth(user);
        boolean success = updateResponse.extract().path("success");
        String actualMessage = updateResponse.extract().path("message");
        String expectedMessage = "You should be authorised";
        int statusCode = updateResponse.extract().statusCode();
        assertFalse("Изменения не должны были пройти: ",success);
        assertEquals(expectedMessage,actualMessage);
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }
    @Test
    @Description("Изменение email без авторизации")
    public void userEmailUpdateWithoutAuth() {
        user.setEmail("test.mail.updated@yandex.ru");
        ValidatableResponse updateResponse = userClient.updateWithoutAuth(user);
        boolean success = updateResponse.extract().path("success");
        String actualMessage = updateResponse.extract().path("message");
        String expectedMessage = "You should be authorised";
        int statusCode = updateResponse.extract().statusCode();
        assertFalse("Изменения не должны были пройти: ",success);
        assertEquals(expectedMessage,actualMessage);
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }

    @Test
    @Description("Изменение имени без авторизации")
    public void userNameUpdateWithoutAuth() {
        user.setName("secondName");
        ValidatableResponse updateResponse = userClient.updateWithoutAuth(user);
        boolean success = updateResponse.extract().path("success");
        String actualMessage = updateResponse.extract().path("message");
        String expectedMessage = "You should be authorised";
        int statusCode = updateResponse.extract().statusCode();
        assertFalse("Изменения не должны были пройти: ",success);
        assertEquals(expectedMessage,actualMessage);
        assertEquals(SC_UNAUTHORIZED, statusCode);
    }
    @Test
    @Description("Изменение всех данных с авторизацией")
    public void userAllUpdateWithAuth() {
        user.setEmail("test.mail.updated@yandex.ru");
        user.setName("secondName");
        ValidatableResponse updateResponse = userClient.updateWithAuth(user, bearerToken);
        ValidatableResponse userInfoResponse = userClient.getUserInfo(bearerToken);
        boolean success = updateResponse.extract().path("success");
        String newEmail = userInfoResponse.extract().path("user.email");
        String newName = userInfoResponse.extract().path("user.name");
        int statusCode = updateResponse.extract().statusCode();
        assertEquals(SC_OK,statusCode);
        assertTrue("Изменения не прошли: ",success);
        assertEquals("Email не изменен: ",user.getEmail(),newEmail);
        assertEquals("Имя не изменено: ",user.getName(), newName);
    }
    @Test
    @Description("Изменение email с авторизацией")
    public void userEmailUpdateWithAuth() {
        user.setEmail("test.mail.updated@yandex.ru");
        ValidatableResponse updateResponse = userClient.updateWithAuth(user, bearerToken);
        ValidatableResponse userInfoResponse = userClient.getUserInfo(bearerToken);
        boolean success = updateResponse.extract().path("success");
        String newEmail = userInfoResponse.extract().path("user.email");
        int statusCode = updateResponse.extract().statusCode();
        assertEquals(SC_OK,statusCode);
        assertTrue("Изменения не прошли: ",success);
        assertEquals("Email не изменен: ",user.getEmail(),newEmail);
    }

    @Test
    @Description("Изменение имени с авторизацией")
    public void userNameUpdateWithAuth() {
        user.setName("secondName");
        ValidatableResponse updateResponse = userClient.updateWithAuth(user, bearerToken);
        ValidatableResponse userInfoResponse = userClient.getUserInfo(bearerToken);
        boolean success = updateResponse.extract().path("success");
        String newName = userInfoResponse.extract().path("user.name");
        int statusCode = updateResponse.extract().statusCode();
        assertEquals(SC_OK,statusCode);
        assertTrue("Изменения не прошли: ",success);
        assertEquals("Имя не изменено: ",user.getName(), newName);
    }

    @After
    public void tearDown() {
        userClient.delete(bearerToken);
    }
}

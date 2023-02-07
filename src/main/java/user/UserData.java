package user;

public class UserData {
    public static User getDefoultUser() {
        return new User("leviy_email@yandex.ru", "1234", "firstName");
    }

    public static User getUserWithoutEmail() {
        return new User("", "1234", "firstName");
    }

    public static User getUserWithoutName() {
        return new User("test.mail@yandex.ru", "1234", "");
    }

    public static User getUserWithoutPass() {
        return new User("test.mail@yandex.ru", "", "firstName");
    }
}

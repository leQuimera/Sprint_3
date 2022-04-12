package yandex.ru;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Courier() {
    }

    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Courier (String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public Courier setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Courier setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Courier setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public static Courier getRandomCourier() {
        return new Courier()
                .setLogin(RandomStringUtils.randomAlphabetic(10))
                .setPassword(RandomStringUtils.randomAlphabetic(10))
                .setFirstName(RandomStringUtils.randomAlphabetic(10));
    }

    public static Courier getCourierWithoutFirstName(){
        return new Courier()
                .setLogin(RandomStringUtils.randomAlphabetic(10))
                .setPassword(RandomStringUtils.randomAlphabetic(10));
    }

    public static Courier getCourierWithoutLogin() {
        return new Courier()
                .setPassword(RandomStringUtils.randomAlphabetic(10))
                .setFirstName(RandomStringUtils.randomAlphabetic(10));
    }

    public static Courier getCourierWithoutPassword() {
        return new Courier()
                .setLogin(RandomStringUtils.randomAlphabetic(10))
                .setFirstName(RandomStringUtils.randomAlphabetic(10));
    }

    public Courier returnCourierLoginAndPassword() {
        return new Courier(this.getLogin(), this.getPassword());
    }

    @Override
    public String toString() {
        return "Courier{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }
}

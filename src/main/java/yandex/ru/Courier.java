package yandex.ru;

import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

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

    @Step("Create random courier")
    public static Courier createRandomCourier() {
        return new Courier()
                .setLogin(RandomStringUtils.randomAlphabetic(10))
                .setPassword(RandomStringUtils.randomAlphabetic(10))
                .setFirstName(RandomStringUtils.randomAlphabetic(10));
    }

    public static Courier createCourierWithoutFirstName(){
        return new Courier()
                .setLogin(RandomStringUtils.randomAlphabetic(10))
                .setPassword(RandomStringUtils.randomAlphabetic(10));
    }

    public static Courier createCourierWithoutLogin() {
        return new Courier()
                .setPassword(RandomStringUtils.randomAlphabetic(10))
                .setFirstName(RandomStringUtils.randomAlphabetic(10));
    }

    public static Courier createCourierWithoutPassword() {
        return new Courier()
                .setLogin(RandomStringUtils.randomAlphabetic(10))
                .setFirstName(RandomStringUtils.randomAlphabetic(10));
    }

    public Courier returnCourierLoginAndPassword() {
        return new Courier(this.getLogin(), this.getPassword());
    }

    public static Courier returnCourierWithOnlyLogin(Courier courier){
        return new Courier()
                .setLogin(courier.getLogin());
    }

    public static Courier returnCourierWithOnlyPassword(Courier courier){
        return new Courier()
                .setPassword(courier.getPassword());
    }

    public static Courier returnCourierWithOnlyFirstname(Courier courier){
        return new Courier()
                .setPassword(courier.getFirstName());
    }
}

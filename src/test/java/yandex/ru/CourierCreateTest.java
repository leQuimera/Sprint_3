package yandex.ru;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class CourierCreateTest {
    private Courier courier;
    private CourierClient courierClient;

    @Before
    public void setUp() {
        courier = Courier.createRandomCourier();
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        int courierId = courierClient.loginCourier(courier.returnCourierLoginAndPassword()).extract().path("id");
        courierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("A new courier can be created if all required parameters are present")
    public void courierCanBeCreatedTest() {
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        boolean isCourierCreated = response.extract().path("ok");

        assertTrue("Courier is not created", isCourierCreated);
        assertThat("Status code is incorrect", statusCode, equalTo(SC_CREATED));
    }

    @Test
    @DisplayName("Trying to create a courier again with an existing login")
    public void duplicateCourierCannotBeCreatedTest() {
        courierClient.createCourier(courier);
        Courier courierDuplicate = Courier.createCourierWithoutLogin().setLogin(courier.getLogin());
        ValidatableResponse response = courierClient.createCourier(courierDuplicate);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        String expected = "Этот логин уже используется";

        assertThat("Status code is incorrect", statusCode, equalTo(SC_CONFLICT));
        assertEquals("Message is incorrect", expected, errorMessage);
    }
}

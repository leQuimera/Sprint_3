package yandex.ru;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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
    @DisplayName("Successful creation of a courier")
    @Description("A new courier can be created if all required parameters are present (login, password, firstname)")
    public void courierCanBeCreated() {
        ValidatableResponse response = courierClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        boolean isCourierCreated = response.extract().path("ok");

        assertTrue("Courier is not created", isCourierCreated);
        assertEquals("Status code is incorrect", SC_CREATED, statusCode);
    }

    @Test
    @DisplayName("Recreation of a courier")
    @Description("Trying to create a courier again with an existing login")
    public void duplicateCourierCannotBeCreated() {
        courierClient.createCourier(courier);
        Courier courierDuplicate = Courier.createCourierWithoutLogin().setLogin(courier.getLogin());
        ValidatableResponse response = courierClient.createCourier(courierDuplicate);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_CONFLICT, statusCode);

        String errorMessage = response.extract().path("message");
        assertEquals("Message is incorrect", "Этот логин уже используется", errorMessage);
    }
}

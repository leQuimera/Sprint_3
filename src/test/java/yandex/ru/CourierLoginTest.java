package yandex.ru;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {
    private Courier courier;
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courier = Courier.createRandomCourier();
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    @Test
    public void courierCanBeCreatedTest() {
        courierClient.createCourier(courier);
        ValidatableResponse response = courierClient.loginCourier(courier.returnCourierLoginAndPassword());
        int statusCode = response.extract().statusCode();
        int courierId = response.extract().path("id");

        assertThat("Status code is incorrect", statusCode, equalTo(200));
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
    }
}

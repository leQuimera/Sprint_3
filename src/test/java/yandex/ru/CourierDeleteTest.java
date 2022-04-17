package yandex.ru;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierDeleteTest {
    private Courier courier;
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courier = Courier.createRandomCourier();
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Delete courier by Id")
    public void courierCanBeDeleted() {
        courierClient.createCourier(courier);
        ValidatableResponse responseLogin = courierClient.loginCourier(courier.returnCourierLoginAndPassword());
        int courierId = responseLogin.extract().path("id");
        ValidatableResponse responseDelete = courierClient.deleteCourier(courierId);
        int statusCode = responseDelete.extract().statusCode();
        assertEquals("Status code is incorrect", statusCode, SC_OK);

        boolean isCourierDeleted = responseDelete.extract().path("ok");
        assertTrue("Courier was not created", isCourierDeleted);
    }

    @Test
    @DisplayName("Delete non-existent courier by Id")
    public void courierValidationDeleteWithNonexistentId(){
        int courierId = new Random().nextInt();
        ValidatableResponse response = courierClient.deleteCourier(courierId);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect",SC_NOT_FOUND, statusCode);

        String errorMessage = response.extract().path("message");
        assertEquals("Message is incorrect", errorMessage, "Курьера с таким id нет.");
    }

    @Test
    @DisplayName("Removal of the courier without transfer ID")
    public void courierValidationDeleteWithoutId() {
        ValidatableResponse response = courierClient.deleteCourier();
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect",SC_NOT_FOUND, statusCode);

        String errorMessage = response.extract().path("message");
        assertEquals("Message is incorrect", errorMessage,"Недостаточно данных для удаления курьера");
    }

}

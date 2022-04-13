package yandex.ru;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CourierCreateValidationTest {

    private Courier courier;
    private int expectedStatus;
    private String expectedErrorMessage;
    private int courierId;
    private CourierClient courierClient;
    private static final String CURRENT_ERROR_MASSEGE = "Недостаточно данных для создания учетной записи";

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    public CourierCreateValidationTest(Courier courier, int expectedStatus, String expectedErrorMessage){
        this.courier = courier;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {Courier.createCourierWithoutLogin(), SC_BAD_REQUEST, CURRENT_ERROR_MASSEGE},
                {Courier.createCourierWithoutPassword(), SC_BAD_REQUEST, CURRENT_ERROR_MASSEGE},
                {Courier.createCourierWithoutFirstName(), SC_CREATED, null},
        };
    }

    @Test
    public void validationOfCourierCreationTest(){
        ValidatableResponse response = new CourierClient().createCourier(courier);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        if(statusCode == 201){
            courierId = courierClient.loginCourier(courier.returnCourierLoginAndPassword()).extract().path("id");
        }
        assertEquals("Status code is incorrect", expectedStatus, statusCode);
        assertEquals("Message is incorrect", expectedErrorMessage, errorMessage);
    }

}

package yandex.ru;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CourierLoginValidationTest {

    private Courier courierLogin;
    private int expectedStatus;
    private String expectedErrorMessage;
    private CourierClient courierClient;
    private static Courier courier = Courier.createRandomCourier();
    private int courierId;
    private static final String ERROR_MESSAGE_NOT_ENOUGH_DATA = "Недостаточно данных для входа";
    private static final String ERROR_MESSAGE_NOT_EXIST_USER = "Учетная запись не найдена";

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    public CourierLoginValidationTest(Courier courierLogin, int expectedStatus, String expectedErrorMessage) {
        this.courierLogin = courierLogin;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {Courier.returnCourierWithOnlyLogin(courier), SC_BAD_REQUEST, ERROR_MESSAGE_NOT_ENOUGH_DATA},
                {Courier.returnCourierWithOnlyPassword(courier), SC_BAD_REQUEST, ERROR_MESSAGE_NOT_ENOUGH_DATA},
                {Courier.createRandomCourier(), SC_NOT_FOUND, ERROR_MESSAGE_NOT_EXIST_USER},
        };
    }

    @Test
    public void validationOfCourierCreationTest(){
        courierClient.createCourier(courier);
        System.out.println(courierLogin.toString());
        ValidatableResponse errorResponse = new CourierClient().loginCourier(courierLogin);
        System.out.println(errorResponse.toString());
        int statusCode = errorResponse.extract().statusCode();
        String errorMessage = errorResponse.extract().path("message");

        assertEquals("Status code is incorrect", expectedStatus, statusCode);
        assertEquals("Message is incorrect", expectedErrorMessage, errorMessage);
    }
}

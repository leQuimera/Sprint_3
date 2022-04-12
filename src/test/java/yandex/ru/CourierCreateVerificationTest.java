package yandex.ru;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CourierCreateVerificationTest {

    private Courier courier;
    private int courierId;
    private CourierClient courierClient;
    private int expectedStatus;
    private String expectedErrorMessage;
    private static final String CURRENT_ERROR_MASSEGE = "Недостаточно данных для создания учетной записи";

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    public CourierCreateVerificationTest(Courier courier, int expectedStatus, String expectedErrorMessage){
        this.courier = courier;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {Courier.getCourierWithoutLogin(), SC_BAD_REQUEST, CURRENT_ERROR_MASSEGE},
                {Courier.getCourierWithoutPassword(), SC_BAD_REQUEST, CURRENT_ERROR_MASSEGE},
                {Courier.getCourierWithoutFirstName(), SC_CREATED, null},
        };
    }

    @Test
    public void checkValidationOfCourierCreationTest(){
        ValidatableResponse response = new CourierClient().createCourier(courier);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        if(statusCode == 201){
            courierId = courierClient.loginCourier(courier.returnCourierLoginAndPassword()).extract().path("id");
            assertThat("Courier ID is incorrect", courierId, is(not(0)));
        }
        assertEquals("Status code is incorrect", expectedStatus, statusCode);
        assertEquals("Message is incorrect", expectedErrorMessage, errorMessage);
    }

}

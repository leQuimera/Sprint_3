package yandex.ru;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private String[] colors;
    private int trackId;
    private Order order;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        order = Order.createRandomOrderNoColors().setColors(colors);
        orderClient = new OrderClient();
    }

    public OrderCreateTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][]{
                { new String[] {"BLACK"}},
                { new String[] {"GREY"}},
                { new String[] {"BLACK", "GREY"}},
                { new String[] {}}
        };
    }

    @Test
    @DisplayName("Create of an order based on the color of the scooter")
    @Description("Creation of the order with: " +
            "1. Black color of the scooter " +
            "2. Grey color of the scooter " +
            "3. Both colors of the scooter " +
            "4. Without colors of the scooter")
    public void orderCanBeCreatedBasedOnColorTest() {
        ValidatableResponse response = orderClient.createOrder(order);
        int statusCode = response.extract().statusCode();
        trackId = response.extract().path("track");

        assertEquals("Status code is incorrect", SC_CREATED, statusCode);
        assertThat("Track ID is incorrect", trackId, notNullValue());
    }
}

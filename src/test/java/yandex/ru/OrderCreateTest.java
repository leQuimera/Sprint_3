package yandex.ru;

import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
        order = Order.createRandomOrderNoColors(colors);
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
    public void bodyTrackValueIsNotNullWithAnyColor() {
        ValidatableResponse response = orderClient.createOrder(order);
        int statusCode = response.extract().statusCode();
        trackId = response.extract().path("track");

        assertEquals("Некорректный код статуса", 201, statusCode);
        assertThat("Некорректный ID трека", trackId, notNullValue());
    }
}

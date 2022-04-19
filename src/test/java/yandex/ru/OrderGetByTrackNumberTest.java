package yandex.ru;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class OrderGetByTrackNumberTest {
    private OrderClient orderClient;

    @Before
    public void setUp(){
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Find order by track number")
    public void canFindOrderByTrack() {
        int orderTrack = orderClient.createOrder(Order.createRandomOrderNoColors()).extract().path("track");
        ValidatableResponse response = orderClient.getOrderByTrack(orderTrack);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        boolean isEmptyBody = response.extract().contentType().isEmpty();
        assertFalse("Error", isEmptyBody);
    }

    @Test
    @DisplayName("Cannot find order without track number")
    public void canNotFindOrderWithoutTrack() {
        ValidatableResponse response = orderClient.getOrderByTrack();
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_BAD_REQUEST, statusCode);

        String errorMessage = response.extract().path("message");
        assertEquals("Message is incorrect", "Недостаточно данных для поиска", errorMessage);
    }

    @Test
    @DisplayName("Cannot find order with wrong track number")
    public void canNotFindOrderWithWrongTrack() {
        int orderTrack = RandomUtils.nextInt(10000000, 99999999);
        ValidatableResponse response = orderClient.getOrderByTrack(orderTrack);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_NOT_FOUND, statusCode, SC_NOT_FOUND);

        String errorMessage = response.extract().path("message");
        assertEquals("Message is incorrect", "Заказ не найден", errorMessage);
    }

}

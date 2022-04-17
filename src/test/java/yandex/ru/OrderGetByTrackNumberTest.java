package yandex.ru;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;

public class OrderGetByTrackNumberTest {
    private OrderClient orderClient;

    @Before
    public void setUp(){
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Find order by track number")
    public void canFindOrderByTrackTest() {
        int orderTrack = orderClient.createOrder(Order.createRandomOrderNoColors()).extract().path("track");
        ValidatableResponse response = orderClient.getOrderByTrack(orderTrack);
        int statusCode = response.extract().statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo(SC_OK));

        boolean isEmptyBody = response.extract().contentType().isEmpty();
        assertFalse("Error", isEmptyBody);
    }

    @Test
    @DisplayName("Cannot find order without track number")
    public void canNotFindOrderWithoutTrackTest() {
        ValidatableResponse response = orderClient.getOrderByTrack();
        int statusCode = response.extract().statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo(SC_BAD_REQUEST));

        String errorMessage = response.extract().path("message");
        assertThat("Message is incorrect", errorMessage, equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Cannot find order with wrong track number")
    public void canNotFindOrderWithWrongTrackTest() {
        int orderTrack = RandomUtils.nextInt(10000000, 99999999);
        ValidatableResponse response = orderClient.getOrderByTrack(orderTrack);
        int statusCode = response.extract().statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo(SC_NOT_FOUND));

        String errorMessage = response.extract().path("message");
        assertThat("Message is incorrect", errorMessage, equalTo("Заказ не найден"));
    }

}

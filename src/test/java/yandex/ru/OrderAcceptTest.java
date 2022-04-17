package yandex.ru;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class OrderAcceptTest {
    private Courier courier;
    private CourierClient courierClient;
    private OrderClient orderClient;
    int orderId;
    int courierId;
    private final String ERROR_MESSAGE_NOT_ENOUGH_DATA = "Недостаточно данных для поиска";
    private final String ERROR_MESSAGE_ORDER_DOES_NOT_EXIST = "Заказа с таким id не существует";
    private final String ERROR_MESSAGE_COURIER_DOES_NOT_EXIST = "Курьера с таким id не существует";
    private final String ERROR_MESSAGE_ORDER_CURRENTLY_IN_WORK = "Этот заказ уже в работе";

    @Before
    public void setUp(){
        courierClient = new CourierClient();
        orderClient = new OrderClient();
        courier = Courier.createRandomCourier();
        courierClient.createCourier(courier);
    }

    @After
    public void tearDown() {
        courierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Success acceptation of order")
    public void acceptOrderByCourier() {
        courierId = courierClient.loginCourier(courier.returnCourierLoginAndPassword()).extract().path("id");
        int orderTrack = orderClient.createOrder(Order.createRandomOrderNoColors()).extract().path("track");
        orderId = orderClient.getOrderByTrack(orderTrack).extract().path("order.id");
        ValidatableResponse response = orderClient.acceptOrder(courierId, orderId);
        int statusCode = response.extract().statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo(SC_OK));

        boolean isOrderAccepted = response.extract().path("ok");
        assertTrue("Order is accepted", isOrderAccepted);
    }

    @Test
    @DisplayName("Re-acceptance of the order by the courier")
    public void reAcceptanceOfOrderByCourier() {
        courierId = courierClient.loginCourier(courier.returnCourierLoginAndPassword()).extract().path("id");
        int orderTrack = orderClient.createOrder(Order.createRandomOrderNoColors()).extract().path("track");
        orderId = orderClient.getOrderByTrack(orderTrack).extract().path("order.id");
        ValidatableResponse response = orderClient.acceptOrder(courierId, orderId);
        ValidatableResponse duplicateResponse = orderClient.acceptOrder(courierId, orderId);
        int statusCode = duplicateResponse.extract().statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo(SC_CONFLICT));

        String errorMessage = duplicateResponse.extract().path("message");
        assertThat("Message is incorrect", errorMessage, equalTo(ERROR_MESSAGE_ORDER_CURRENTLY_IN_WORK));
    }


    @Test
    @DisplayName("Request with non-existent order ID")
    public void acceptOrderWithUnexcitingOrderId() {
        courierId = courierClient.loginCourier(courier.returnCourierLoginAndPassword()).extract().path("id");
        orderId = RandomUtils.nextInt(10000000, 99999999);
        ValidatableResponse response = orderClient.acceptOrder(courierId, orderId);
        int statusCode = response.extract().statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo(SC_NOT_FOUND));

        String errorMessage = response.extract().path("message");
        assertThat("Message is incorrect", errorMessage, equalTo(ERROR_MESSAGE_ORDER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName("Request with non-existent courier ID")
    public void acceptOrderWithUnexcitingCourierId() {
        courierId = RandomUtils.nextInt(10000000, 99999999);
        int orderTrack = orderClient.createOrder(Order.createRandomOrderNoColors()).extract().path("track");
        orderId = orderClient.getOrderByTrack(orderTrack).extract().path("order.id");
        ValidatableResponse response = orderClient.acceptOrder(courierId, orderId);
        int statusCode = response.extract().statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo(SC_NOT_FOUND));

        String errorMessage = response.extract().path("message");
        assertThat("Message is incorrect", errorMessage, equalTo(ERROR_MESSAGE_COURIER_DOES_NOT_EXIST));
    }

    @Test
    @DisplayName("Request with missing courier ID")
    public void acceptOrderWithoutCourierId() {
        int orderTrack = orderClient.createOrder(Order.createRandomOrderNoColors()).extract().path("track");
        orderId = orderClient.getOrderByTrack(orderTrack).extract().path("order.id");
        ValidatableResponse response = orderClient.acceptOrderWithoutCourierId(orderId);
        int statusCode = response.extract().statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo(SC_NOT_FOUND));

        String errorMessage = response.extract().path("message");
        assertThat("Message is incorrect", errorMessage, equalTo(ERROR_MESSAGE_NOT_ENOUGH_DATA));
    }

    @Test
    @DisplayName("Request with missing order ID")
    public void acceptOrderWithoutOrderId() {
        courierId = courierClient.loginCourier(courier.returnCourierLoginAndPassword()).extract().path("id");
        ValidatableResponse response = orderClient.acceptOrderWithoutOrderId(courierId);
        int statusCode = response.extract().statusCode();
        assertThat("Status code is incorrect", statusCode, equalTo(SC_NOT_FOUND));

        String errorMessage = response.extract().path("message");
        assertThat("Message is incorrect", errorMessage, equalTo(ERROR_MESSAGE_NOT_ENOUGH_DATA));
    }


}

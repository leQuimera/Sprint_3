package yandex.ru;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient{

    @Step("Create an order")
    public ValidatableResponse createOrder(Order order){
        return given()
                .spec(getBaseSpecification())
                .body(order)
                .when()
                .post(BaseEndpoints.ORDER_CREATE_OR_GET)
                .then();
    }

    @Step("Get list of orders")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getBaseSpecification())
                .when()
                .get(BaseEndpoints.ORDER_CREATE_OR_GET)
                .then();
    }

    @Step("Get order by track number")
    public ValidatableResponse getOrderByTrack(int trackId) {
        return given()
                .spec(getBaseSpecification())
                .when()
                .queryParam("t", trackId)
                .get(BaseEndpoints.ORDER_GET_BY_TRACK)
                .then();
    }

    @Step("Try to get order without track number")
    public ValidatableResponse getOrderByTrack() {
        return given()
                .spec(getBaseSpecification())
                .when()
                .get(BaseEndpoints.ORDER_GET_BY_TRACK)
                .then();
    }

    @Step("Accept order by courier")
    public ValidatableResponse acceptOrder(int courierId, int orderId) {
        return given()
                .spec(getBaseSpecification())
                .when()
                .queryParam("courierId", courierId)
                .put(BaseEndpoints.ORDER_ACCEPT + orderId)
                .then();
    }

    @Step("Try to accept order by courier without courier ID")
    public ValidatableResponse acceptOrderWithoutCourierId(int orderId) {
        return given()
                .spec(getBaseSpecification())
                .when()
                .put(BaseEndpoints.ORDER_ACCEPT + orderId)
                .then();
    }

    @Step("Try to accept order by courier without order ID")
    public ValidatableResponse acceptOrderWithoutOrderId(int courierId) {
        return given()
                .spec(getBaseSpecification())
                .when()
                .queryParam("courierId", courierId)
                .put(BaseEndpoints.ORDER_ACCEPT)
                .then();
    }
}

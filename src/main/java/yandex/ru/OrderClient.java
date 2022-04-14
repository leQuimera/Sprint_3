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

}

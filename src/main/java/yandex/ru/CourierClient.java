package yandex.ru;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class CourierClient extends RestAssuredClient{

    @Step("Create a courier")
    public ValidatableResponse createCourier(Courier courier){
        return given()
                .spec(getBaseSpecification())
                .body(courier)
                .when()
                .post(BaseEndpoints.COURIER_CREATE_OR_DELETE)
                .then();
    }

    @Step("Login as a courier")
    public ValidatableResponse loginCourier(Courier courier) {
        return given()
                .spec(getBaseSpecification())
                .body(courier)
                .when()
                .post(BaseEndpoints.COURIER_LOGIN)
                .then();
    }

    @Step("Delete a courier")
    public ValidatableResponse deleteCourier(int courierId) {
        return given()
                .spec(getBaseSpecification())
                .when()
                .delete(BaseEndpoints.COURIER_CREATE_OR_DELETE + courierId)
                .then();
    }

    @Step("Delete a courier without ID")
    public ValidatableResponse deleteCourier() {
        return given()
                .spec(getBaseSpecification())
                .when()
                .delete(BaseEndpoints.COURIER_CREATE_OR_DELETE)
                .then();
    }

}

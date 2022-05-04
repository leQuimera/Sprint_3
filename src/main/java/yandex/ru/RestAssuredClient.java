package yandex.ru;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import static io.restassured.http.ContentType.JSON;

public class RestAssuredClient {

    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

    protected RequestSpecification getBaseSpecification() {
        return new RequestSpecBuilder()
                .setContentType(JSON)
                .setBaseUri(BASE_URL)
                .build();
    }
}

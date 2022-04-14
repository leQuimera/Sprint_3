package yandex.ru;

import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListViewTest {
    private OrderClient orderClient;

    @Before
    public void setUp(){
        orderClient = new OrderClient();
    }

    @Test
    public void viewListOfOrderTest(){
        ValidatableResponse response = orderClient.getOrderList();
        int statusCode = response.extract().statusCode();
        List<Map<String, Object>> orders = response.extract().jsonPath().getList("orders");

        assertThat("Status code is incorrect", statusCode, equalTo(SC_OK));
        assertThat("List is empty",orders,notNullValue());
    }
}

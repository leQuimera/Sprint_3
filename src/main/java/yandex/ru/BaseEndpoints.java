package yandex.ru;

public final class BaseEndpoints {
    public final static String COURIER_LOGIN = "/api/v1/courier/login";
    public final static String COURIER_CREATE_OR_DELETE = "/api/v1/courier/";  //del -> /:id   orders ->/:id/ordersCount
    public final static String ORDER_CREATE_OR_GET = "/api/v1/orders";
    public final static String ORDER_GET_BY_TRACK = "api/v1/orders/track";
    public final static String ORDER_FINISH = "/api/v1/orders/finish/";
    public final static String ORDER_CANCEL = "api/v1/orders/cancel";
    public final static String ORDER_ACCEPT = "api/v1/orders/accept/";
}

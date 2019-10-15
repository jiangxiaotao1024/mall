package service;

import bean.Order;

public interface OrderService {
    String genTradeCode(String memberId);

    String checkTradeCode(String memberId, String tradeCode);

    void saveOrder(Order order);

    Order getOrderByOutTradeNo(String outTradeNo);

    void updateOrder(Order order);
}

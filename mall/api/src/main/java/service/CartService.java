package service;

import bean.Cart;

import java.util.List;

public interface CartService {
    public List<Cart> cartList(String menberId);

    Cart ifCartExist(String memberId, String productSkuId);

    void addToService(Cart cart);

    void updateCart(Cart cart);
    public void flushCartCache(String memberId);

    void checkCart(Cart cart);
}

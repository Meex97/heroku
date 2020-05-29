package unito.progetto.esame.service;



import unito.progetto.esame.model.Cart;
import unito.progetto.esame.model.Client;
import unito.progetto.esame.model.ProductInOrder;
import unito.progetto.esame.model.User;

import java.util.Collection;


public interface CartService {
    Cart getCart(User user);

    void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user);

    void delete(String itemId, User user);

    // void checkout(User user);
    void checkout(Client user);
}

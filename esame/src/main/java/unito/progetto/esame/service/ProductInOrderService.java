package unito.progetto.esame.service;


import unito.progetto.esame.model.ProductInOrder;
import unito.progetto.esame.model.User;


public interface ProductInOrderService {
    void update(String itemId, Integer quantity, User user);
    ProductInOrder findOne(String itemId, User user);
}

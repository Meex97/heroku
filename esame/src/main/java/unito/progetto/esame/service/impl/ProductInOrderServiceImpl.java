package unito.progetto.esame.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unito.progetto.esame.model.ProductInOrder;
import unito.progetto.esame.model.User;
import unito.progetto.esame.repository.ProductInOrderRepository;
import unito.progetto.esame.service.ProductInOrderService;

import java.util.concurrent.atomic.AtomicReference;
import java.util.Optional;

@Service
public class ProductInOrderServiceImpl implements ProductInOrderService {

    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @Override
    @Transactional
    public void update(String itemId, Integer quantity, User user) {
        // var op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst().ifPresent(productInOrder -> {
            productInOrder.setCount(quantity);
            productInOrderRepository.save(productInOrder);
        });

    }

    @Override
    public ProductInOrder findOne(String itemId, User user) {
       // var op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        AtomicReference<ProductInOrder> res = new AtomicReference<>();
        user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst().ifPresent(res::set);
        return res.get();
    }
}

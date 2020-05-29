package unito.progetto.esame.service.impl;


import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unito.progetto.esame.model.*;
import unito.progetto.esame.repository.CartRepository;
import unito.progetto.esame.repository.OrderRepository;
import unito.progetto.esame.repository.ProductInOrderRepository;
import unito.progetto.esame.repository.UserRepository;
import unito.progetto.esame.service.CartService;
import unito.progetto.esame.service.ProductService;
import unito.progetto.esame.service.UserService;

import java.util.*;


@Service
public class CartServiceImpl implements CartService {
    @Autowired
    ProductService productService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductInOrderRepository productInOrderRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserService userService;

    @Override
    public Cart getCart(User user) {
        return user.getCart();
    }

    @Override
    @Transactional
    public void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user) {
        Cart finalCart = user.getCart();
        productInOrders.forEach(productInOrder -> {
            Set<ProductInOrder> set = finalCart.getProducts();
            Optional<ProductInOrder> old = set.stream().filter(e -> e.getProductId().equals(productInOrder.getProductId())).findFirst();
            ProductInOrder prod;
            if (old.isPresent()) {
                prod = old.get();
                prod.setCount(productInOrder.getCount() + prod.getCount());
            } else {
                prod = productInOrder;
                prod.setCart(finalCart);
                finalCart.getProducts().add(prod);
            }
            productInOrderRepository.save(prod);
        });
        cartRepository.save(finalCart);

    }

    @Override
    @Transactional
    public void delete(String itemId, User user) {
        // var op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst().ifPresent(productInOrder -> {
            productInOrder.setCart(null);
            productInOrderRepository.deleteById(productInOrder.getId());
        });
    }



    @Override
    @Transactional
    public void checkout(Client user) {
        // Creat an order
      //  OrderMain order = new OrderMain(user);
       // orderRepository.save(order);

        // clear cart's foreign key & set order's foreign key& decrease stock
      //  System.out.println(user.getCart().getProducts());
        Long prev = -1L;
        List<ProductInOrder> orderList = Lists.newArrayList(user.getCart().getProducts());
        Collections.sort(orderList);
        OrderMain ordermain = null;
        ListIterator<ProductInOrder> iterator = orderList.listIterator();
        while(iterator.hasNext()){
            ProductInOrder prodTmp = iterator.next();
            prodTmp.setCart(null);
            if(prev.intValue() != prodTmp.getIdUtente().intValue()){
                ordermain = new OrderMain(user, prodTmp.getIdUtente().intValue());
                orderRepository.save(ordermain);
                prev= prodTmp.getIdUtente();
            }
            prodTmp.setOrderMain(ordermain);
            productService.decreaseStock(prodTmp.getProductId(), prodTmp.getCount());
            productInOrderRepository.save(prodTmp);
        }



    }
}

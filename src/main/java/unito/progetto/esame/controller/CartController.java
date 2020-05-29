package unito.progetto.esame.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import unito.progetto.esame.form.ItemForm;
import unito.progetto.esame.model.Cart;
import unito.progetto.esame.model.Client;
import unito.progetto.esame.model.ProductInOrder;
import unito.progetto.esame.model.User;
import unito.progetto.esame.repository.ProductInOrderRepository;
import unito.progetto.esame.service.CartService;
import unito.progetto.esame.service.ProductInOrderService;
import unito.progetto.esame.service.ProductService;
import unito.progetto.esame.service.UserService;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;


@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductInOrderService productInOrderService;
    @Autowired
    ProductInOrderRepository productInOrderRepository;

    @PostMapping("")
    public ResponseEntity<Cart> mergeCart(@RequestBody Collection<ProductInOrder> productInOrders, Principal principal) {
        User user = userService.findOne(principal.getName());
        try {
            cartService.mergeLocalCart(productInOrders, user);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("Merge Cart Failed");
        }
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @GetMapping("")
    public Cart getCart(Principal principal) {
        User user = userService.findOne(principal.getName());
        return cartService.getCart(user);
    }


    @Transactional
    @PostMapping("/add")
    public boolean addToCart(@RequestBody ItemForm form, Principal principal) {
        //var productInfo = productService.findOne(form.getProductId());
        try {
            mergeCart(Collections.singleton(new ProductInOrder(productService.findOne(form.getProductId()), form.getQuantity())), principal);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    @PutMapping("/{itemId}")
    public ProductInOrder modifyItem(@PathVariable("itemId") String itemId, @RequestBody Integer quantity, Principal principal) {
        User user = userService.findOne(principal.getName());
         productInOrderService.update(itemId, quantity, user);
        return productInOrderService.findOne(itemId, user);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") String itemId, Principal principal) {
        User user = userService.findOne(principal.getName());
         cartService.delete(itemId, user);
    }


    @PostMapping("/checkout/{email}")
    public ResponseEntity checkout(@PathVariable("email") String email, Principal principal) {
        Client client = userService.findOneClient(email);
        cartService.checkout(client);
        return ResponseEntity.ok(null);
    }


}

package unito.progetto.esame.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import unito.progetto.esame.model.OrderMain;
import unito.progetto.esame.model.ProductInOrder;
import unito.progetto.esame.service.OrderService;
import unito.progetto.esame.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created By Zhu Lin on 3/14/2018.
 */
@RestController
@CrossOrigin
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;

    @GetMapping("/order")
    public List<OrderMain> orderList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size,
                                     Authentication authentication) {
        PageRequest request = PageRequest.of(page - 1, size);
        Page<OrderMain> orderPage;
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            orderPage = orderService.findByBuyerEmail(authentication.getName(), request);
        } else {
            orderPage = orderService.findAll(request);
        }

        List<OrderMain> orderClient = new ArrayList<>();

        orderPage.forEach(product ->{

            orderClient.add(product);

        });
        return orderClient;

        // return orderPage;
    }

    @GetMapping(value = "/orderSupplier/{idUtente}")
    //@PathVariable permette di recuperare i valori inclusi nel URL associato alla richiesta
    public List<OrderMain> findByIdUtente(@PathVariable("idUtente") Long idUtente) {

        System.out.println(idUtente);
        // size definita da
        Page<OrderMain> prod = orderService.findAll(PageRequest.of(0, 50));
        List<OrderMain> orderSupplier = new ArrayList<>();

        prod.forEach(product ->{
            // System.out.println("idUtente " + product.getIdUtente());
            if(product.getIdSeller() == idUtente.intValue() ){
                orderSupplier.add(product);
                // System.out.println("prodotto: "+ product.getProductId()+ " idUtente: "+ product.getIdUtente());
            }
        });
        return orderSupplier;
    }


    @PatchMapping("/order/cancel/{id}")
    public ResponseEntity<OrderMain> cancel(@PathVariable("id") Long orderId, Authentication authentication) {
        OrderMain orderMain = orderService.findOne(orderId);
        if (!authentication.getName().equals(orderMain.getBuyerEmail()) && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderService.cancel(orderId));
    }

    @PatchMapping("/order/finish/{id}")
    public ResponseEntity<OrderMain> finish(@PathVariable("id") Long orderId, Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderService.finish(orderId));
    }

    @GetMapping("/order/{id}")
    public ResponseEntity show(@PathVariable("id") Long orderId, Authentication authentication) {
        boolean isCustomer = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        OrderMain orderMain = orderService.findOne(orderId);
        if (isCustomer && !authentication.getName().equals(orderMain.getBuyerEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Collection<ProductInOrder> items = orderMain.getProducts();
        return ResponseEntity.ok(orderMain);
    }
}

package unito.progetto.esame.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import unito.progetto.esame.model.Cart;


public interface CartRepository extends JpaRepository<Cart, Integer> {
}

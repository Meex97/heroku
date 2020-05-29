package unito.progetto.esame.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import unito.progetto.esame.model.ProductInOrder;

public interface ProductInOrderRepository extends JpaRepository<ProductInOrder, Long> {

}

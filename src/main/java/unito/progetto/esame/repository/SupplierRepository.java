package unito.progetto.esame.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unito.progetto.esame.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String> {

    Supplier findByEmail(String email);
}

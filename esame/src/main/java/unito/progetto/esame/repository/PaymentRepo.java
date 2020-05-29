package unito.progetto.esame.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import unito.progetto.esame.model.Payment;

@Repository
public interface PaymentRepo extends CrudRepository<Payment,String> {

    Payment findByTxnId(String txnId);
}

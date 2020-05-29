package unito.progetto.esame.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import unito.progetto.esame.model.Client;

public interface ClientRepository extends JpaRepository<Client, String> {

    /*@Query("SELECT * FROM Users u WHERE u.idUte = 1")
    Collection<User> findAllActiveUsers();*/
    Client findById(Long id);
    Client findByEmail(String email);
}

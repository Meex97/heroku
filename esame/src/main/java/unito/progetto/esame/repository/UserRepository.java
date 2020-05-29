package unito.progetto.esame.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unito.progetto.esame.model.User;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    //find by id

    User findById(Long id);
    Collection<User> findAllByRole(String role);

}

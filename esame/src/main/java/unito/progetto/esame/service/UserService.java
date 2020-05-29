package unito.progetto.esame.service;


import org.springframework.stereotype.Repository;
import unito.progetto.esame.model.Client;
import unito.progetto.esame.model.Supplier;
import unito.progetto.esame.model.User;

import java.util.Collection;


public interface UserService {
    User findOne(String email);

    User findOneById(Long id);

    Client findOneClient(String email);

    Supplier findOneSupplier(String email);

    Collection<User> findByRole(String role);

    User save(User user);

    Client saveClient(Client client);

    User update(User user);

    Client updateClient(Client client);

    Client updateCredits(int productPrice, Long idUtente);

    Supplier updateSupplier(Supplier supplier);

}

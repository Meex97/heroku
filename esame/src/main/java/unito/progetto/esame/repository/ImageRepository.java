package unito.progetto.esame.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import unito.progetto.esame.model.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findByName(String name);
}

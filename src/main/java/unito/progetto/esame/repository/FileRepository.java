package unito.progetto.esame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import unito.progetto.esame.model.FileModel;

@Transactional
public interface FileRepository extends JpaRepository<FileModel, Long> {

}


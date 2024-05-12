package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rut.pan.entity.Status;

@Repository
public interface IStatusRepository extends JpaRepository<Status, String> {

    Status getStatusById(Long id);

    Status getStatusByStatus(String s);

}

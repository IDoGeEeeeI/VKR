package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.pan.entity.RequestStatus;

public interface IRequestStatusRepository extends JpaRepository<RequestStatus, String> {

}

package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.pan.entity.EmployersRequests;

public interface IEmployerRequestRepository extends JpaRepository<EmployersRequests, String> {

}

package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.pan.entity.Employer;
import rut.pan.entity.EmployersRequests;

import java.util.List;

public interface IEmployerRequestRepository extends JpaRepository<EmployersRequests, String> {

    List<EmployersRequests> findEmployersRequestsByRequestingEmployer(Employer employer);

    List<EmployersRequests> findEmployersRequestsByRequestingEmployerAndSupervisor(Employer employer, Employer supervisor);

}

package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rut.pan.entity.Employer;
import rut.pan.entity.UserDto;

import java.util.List;

@Repository
public interface IEmployerRepository extends JpaRepository<Employer, Long> {
    Employer findEmployerByUser(UserDto userDto);

    Employer findSupervisorById(String id);
    List<Employer> getEmployersBySupervisor(Employer employer);

}

package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rut.pan.entity.Employer;
import rut.pan.entity.UserDto;

import java.util.List;

@Repository
public interface IEmployerRepository extends JpaRepository<Employer, Long> {
    Employer findEmployerByUser(UserDto userDto);

    Employer findSupervisorById(Long id);
    List<Employer> getEmployersBySupervisor(Employer employer);

    @Query("SELECT e FROM Employer e WHERE e != :supervisor and (e.supervisor != :supervisor OR e.supervisor IS NULL)")
    List<Employer> findEmployerBySupervisorIsNotIncludingNull(@Param("supervisor") Employer supervisor);

}

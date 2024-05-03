package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rut.pan.entity.Employer;
import rut.pan.entity.UserDto;

@Repository
public interface IEmployerRepository extends JpaRepository<Employer, Long> {
    Employer findEmployerByUser(UserDto userDto);

}

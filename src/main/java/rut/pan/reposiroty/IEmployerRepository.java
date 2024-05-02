package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rut.pan.entity.Employer;

@Repository
public interface IEmployerRepository extends JpaRepository<Employer, Long> {


}

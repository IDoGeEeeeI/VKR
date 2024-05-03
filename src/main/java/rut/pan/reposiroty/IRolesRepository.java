package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.pan.entity.Roles;

public interface IRolesRepository extends JpaRepository<Roles, String> {

}

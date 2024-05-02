package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import rut.pan.entity.UserDto;

public interface IUserRepository extends JpaRepository<UserDto, Long> {

     UserDto findByLogin(String userLogin);
}

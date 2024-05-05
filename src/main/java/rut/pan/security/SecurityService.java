package rut.pan.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rut.pan.entity.Roles;
import rut.pan.entity.UserDto;
import rut.pan.reposiroty.IUserRepository;

@Component
@Slf4j
public class SecurityService {

    @Autowired
    private IUserRepository iUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationContext authenticationContext;

    public SecurityService(PasswordEncoder passwordEncoder, AuthenticationContext authenticationContext) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationContext = authenticationContext;
    }

    public UserDto createOrUpdateUser(String login, String rawPassword, Roles role) {
        UserDto user = iUserRepository.findByLogin(login);
        if (user == null) {
            user = new UserDto();
            user.setLogin(login);
        } else {
            log.info("Обновляем данные существующего пользователя: {}", login);
        }

        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        return iUserRepository.save(user);
    }

    public void deleteUser(UserDto userDto) {
        iUserRepository.delete(userDto);
    }

    public boolean isUserLoggedIn() {
       return authenticationContext.isAuthenticated();
    }

    public UserDto getUserByLogin(String login) {
        return iUserRepository.findByLogin(login);
    }

    public UserDetails getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class).get();
    }

    public void logout() {
        authenticationContext.logout();
    }
}
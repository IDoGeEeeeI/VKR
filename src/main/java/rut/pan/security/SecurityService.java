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

//    public SecurityService(AuthenticationContext authenticationContext) {
//        this.authenticationContext = authenticationContext;
//    }
    public SecurityService(PasswordEncoder passwordEncoder, AuthenticationContext authenticationContext) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationContext = authenticationContext;
    }

    //todo для дальнейшего создания пользователей внутри(админами)
    public UserDto createUser(String login, String rawPassword, Roles role) {
        UserDto existingUser = iUserRepository.findByLogin(login);
        if (existingUser != null) {
            log.warn("User with login '{}' already exists.", login);
            return null;
        }
        UserDto newUser = new UserDto();
        newUser.setLogin(login);
        newUser.setPassword(passwordEncoder.encode(rawPassword));
        newUser.setRole(role);
        return iUserRepository.save(newUser);
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
package rut.pan.service2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rut.pan.entity.Employer;
import rut.pan.entity.UserDto;
import rut.pan.security.SecurityService;
import rut.pan.service.*;


@Getter
@Component
public class Service2 {

    @SuppressWarnings("checkstyle:StaticVariableName")
    private static Service2 INSTANCE;

    public static Service2 getInstance() {
        return INSTANCE;
    }

    @Component
    @Order(Integer.MIN_VALUE)
    @Slf4j
    private static class Instance {
        @Autowired
        Instance(Service2 services2) {
            Service2.INSTANCE = services2;
            log.info("init instance {}", Service2.class);
        }
    }


    @Autowired
    private SecurityService securityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EmployersRequestService employersRequestService;

    @Transactional
    public Employer saveOrEditEmployer(Employer savedEmployer) {
        UserDto userDto = savedEmployer.getUser();
        UserDto updatedOrNewUser = securityService.createOrUpdateUser(userDto.getLogin(), userDto.getPassword(), userDto.getRole());
        savedEmployer.setUser(updatedOrNewUser);
        return employerService.addEmployer(savedEmployer);
    }

    @Transactional
    public void deleteEmployer(Employer employer) {
        employerService.remove(employer);
        securityService.deleteUser(employer.getUser());
    }

    public Employer getEmployerByAuthenticationUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return Service2.getInstance().getEmployerService().getEmployerByUser(Service2.getInstance().getSecurityService().getUserByLogin(authentication.getName()));
        }
        return null;
    }


}

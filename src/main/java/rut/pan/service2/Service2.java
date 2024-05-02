package rut.pan.service2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import rut.pan.entity.UserDto;
import rut.pan.security.SecurityService;
import rut.pan.service.EmployerService;
import rut.pan.service.TaskService;


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

    public boolean isUserLoggedIn() {
        return securityService.isUserLoggedIn();
    }

    public UserDto getUserByLogin(String login) {
        return securityService.getUserByLogin(login);
    }

    public String test() {
        return "" + taskService.list();
    }


    //todo буду юзать его для взаимодействи на веб слое, так что логику не расписываю в сервисах, а тут буду получать одно, используя другое

}

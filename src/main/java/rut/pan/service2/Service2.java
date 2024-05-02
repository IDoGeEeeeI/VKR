package rut.pan.service2;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import rut.pan.security.SecurityService;
import rut.pan.service.EmployerService;
import rut.pan.service.TaskService;


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


}

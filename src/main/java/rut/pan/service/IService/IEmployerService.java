package rut.pan.service.IService;

import org.springframework.transaction.annotation.Transactional;
import rut.pan.entity.Employer;
import rut.pan.entity.UserDto;

import java.util.List;

public interface IEmployerService {

    List<Employer> list();

    List<Employer> getEmployersBySupervisor(Employer employer);

    List<Employer> getEmployersBySupervisorIsNot(Employer employer);

    Employer findSupervisorByEmployerId(String id);

    Employer getEmployerByUser(UserDto user);

    void remove(Employer employer);

    @Transactional
    public Employer addEmployer(final Employer employer);

    @Transactional
    public Employer editEmployer(final Employer employer);


}

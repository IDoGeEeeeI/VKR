package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rut.pan.entity.Employer;
import rut.pan.entity.UserDto;
import rut.pan.reposiroty.IEmployerRepository;
import rut.pan.service.IService.IEmployerService;

import java.util.List;

@Service
public class EmployerServiceImpl implements IEmployerService {

    @Autowired
    private IEmployerRepository iEmployerRepository;

    @Override
    public List<Employer> list() {
        return iEmployerRepository.findAll();
    }

    @Override
    public List<Employer> getEmployersBySupervisor(Employer employer) {
        return iEmployerRepository.getEmployersBySupervisor(employer);
    }

    @Override
    public List<Employer> getEmployersBySupervisorIsNot(Employer employer) {
        return iEmployerRepository.findEmployerBySupervisorIsNotIncludingNull(employer);
    }

    @Override
    public Employer findSupervisorByEmployerId(String id) {
        return iEmployerRepository.findSupervisorById(Long.valueOf(id));
    }

    @Override
    public Employer getEmployerByUser(UserDto user) {
        return iEmployerRepository.findEmployerByUser(user);
    }

    @Override
    public void remove(final Employer employer) {
        iEmployerRepository.delete(employer);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Employer addEmployer(final Employer employer) {
        return iEmployerRepository.save(employer);
    }

    @Override
    @Transactional
    public Employer editEmployer(final Employer employer) {
        return iEmployerRepository.save(employer);
    }
}

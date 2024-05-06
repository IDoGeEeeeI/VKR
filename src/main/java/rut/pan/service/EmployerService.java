package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rut.pan.entity.Employer;
import rut.pan.entity.UserDto;
import rut.pan.reposiroty.IEmployerRepository;

import java.util.List;

@Service
public class EmployerService {

    @Autowired
    private IEmployerRepository iEmployerRepository;

    public List<Employer> list() {
        return iEmployerRepository.findAll();
    }

    public List<Employer> getEmployersBySupervisor(Employer employer) {
        return iEmployerRepository.getEmployersBySupervisor(employer);
    }

    public Employer findSupervisorByEmployer(String id) {
        return iEmployerRepository.findSupervisorById(id);
    }

    public Employer getEmployerByUser(UserDto user) {
        return iEmployerRepository.findEmployerByUser(user);
    }

    public void remove(final Employer employer) {
        iEmployerRepository.delete(employer);
    }

    @Transactional
    public Employer addEmployer(final Employer employer) {
        return iEmployerRepository.save(employer);
    }

    @Transactional
    public Employer editEmployer(final Employer employer) {
        return iEmployerRepository.save(employer);
    }
}

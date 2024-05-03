package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import rut.pan.entity.Employer;
import rut.pan.entity.Roles;
import rut.pan.reposiroty.IEmployerRepository;

import java.util.List;

@Service
public class EmployerService {

    @Autowired
    private IEmployerRepository iEmployerRepository;

    public List<Employer> list() {
        return iEmployerRepository.findAll();
    }

    public void remove(final Employer employer) {
        iEmployerRepository.delete(employer);
    }

    public Employer addEmployer(final Employer employer) {
        return iEmployerRepository.save(employer);
    }

    public Employer editEmployer(final Employer employer) {
        return iEmployerRepository.save(employer);
    }
}

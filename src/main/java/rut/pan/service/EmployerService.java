package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rut.pan.entity.Employer;
import rut.pan.reposiroty.IEmployerRepository;

import java.util.List;

@Service
public class EmployerService {

    @Autowired
    private IEmployerRepository iEmployerRepository;

    public List<Employer> list() {
        return iEmployerRepository.findAll();
    }
}

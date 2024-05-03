package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rut.pan.entity.Roles;
import rut.pan.reposiroty.IRolesRepository;

import java.util.List;

@Service
public class RolesService {

    @Autowired
    private IRolesRepository iRolesRepository;

    public List<Roles> list() {
        return iRolesRepository.findAll();
    }
}

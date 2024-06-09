package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rut.pan.entity.Roles;
import rut.pan.reposiroty.IRolesRepository;
import rut.pan.service.IService.IRolesService;

import java.util.List;

@Service
public class RolesServiceImpl implements IRolesService {

    @Autowired
    private IRolesRepository iRolesRepository;

    @Override
    public List<Roles> list() {
        return iRolesRepository.findAll();
    }
}

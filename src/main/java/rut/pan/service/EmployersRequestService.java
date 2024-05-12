package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rut.pan.reposiroty.IEmployerRequestRepository;
import rut.pan.reposiroty.IRequestStatusRepository;

@Service
public class EmployersRequestService {

    @Autowired
    private IEmployerRequestRepository iEmployerRequestRepository;

    @Autowired
    private IRequestStatusRepository iRequestStatusRepository;

    //todo

}

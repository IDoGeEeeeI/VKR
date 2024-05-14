package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rut.pan.entity.Employer;
import rut.pan.entity.EmployersRequests;
import rut.pan.entity.RequestStatus;
import rut.pan.reposiroty.IEmployerRequestRepository;
import rut.pan.reposiroty.IRequestStatusRepository;

import java.util.List;

@Service
public class EmployersRequestService {

    @Autowired
    private IEmployerRequestRepository iEmployerRequestRepository;

    @Autowired
    private IRequestStatusRepository iRequestStatusRepository;

    public void saveOrEdit(EmployersRequests requests) {
        iEmployerRequestRepository.save(requests);
    }

    public List<EmployersRequests> getRequestByEmployer(Employer employer) {
        return iEmployerRequestRepository.findEmployersRequestsByRequestingEmployer(employer);
    }

    public List<EmployersRequests> getRequestByEmployerAndSupervisor(Employer employer, Employer supervisor) {
        return iEmployerRequestRepository.findEmployersRequestsByRequestingEmployerAndSupervisor(employer, supervisor);
    }

    public List<RequestStatus> getStatus() {
        return iRequestStatusRepository.findAll();
    }



}

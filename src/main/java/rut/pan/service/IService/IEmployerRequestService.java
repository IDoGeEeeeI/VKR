package rut.pan.service.IService;

import rut.pan.entity.Employer;
import rut.pan.entity.EmployersRequests;
import rut.pan.entity.RequestStatus;

import java.util.List;

public interface IEmployerRequestService {

    void saveOrEdit(EmployersRequests requests);

    List<EmployersRequests> getRequestByEmployer(Employer employer);

    List<EmployersRequests> getRequestByEmployerAndSupervisor(Employer employer, Employer supervisor);

    List<RequestStatus> getStatus();
}

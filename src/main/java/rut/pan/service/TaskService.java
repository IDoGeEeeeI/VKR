package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rut.pan.entity.Employer;
import rut.pan.entity.Task;
import rut.pan.reposiroty.ITaskRepository;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository iTaskTaskRepository;

    public List<Task> list() {
        return iTaskTaskRepository.findAll();
    }

    public List<Task> getListByEmployer(Employer employer) {
        return iTaskTaskRepository.findByEmployer(employer);
    }

    public List<Task> getAllTasksByEmployerId(Integer employerId) {
        return iTaskTaskRepository.getAllTasksByEmployerId(employerId);
    }

}

package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rut.pan.entity.*;
import rut.pan.reposiroty.IPrioritizeRepository;
import rut.pan.reposiroty.IStatusRepository;
import rut.pan.reposiroty.ITaskRepository;
import rut.pan.reposiroty.ITaskTypeRepository;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository iTaskTaskRepository;

    @Autowired
    private ITaskTypeRepository iTaskTypeRepository;

    @Autowired
    private IStatusRepository iStatusRepository;

    @Autowired
    private IPrioritizeRepository iPrioritizeRepository;


    public List<Task> list() {
        return iTaskTaskRepository.findAll();
    }

    public List<Task> getTasksByEmployer(Employer employer) {
        return iTaskTaskRepository.findByEmployer(employer);
    }

    public List<Task> getListByEmployer(Employer employer) {
        return iTaskTaskRepository.findByEmployer(employer);
    }

    public List<Task> getAllTasksByEmployerId(Integer employerId) {
        return iTaskTaskRepository.getAllTasksByEmployerId(employerId);
    }

    public Status getStatusByTask(Task task) {
        return iStatusRepository.getStatusById(task.getStatus().getId());
    }

    public List<Status> getStatus() {
        return iStatusRepository.findAll();
    }

    public List<Prioritize> getPrioritize() {
        return iPrioritizeRepository.findAll();
    }

    public void saveOrEditTask(Task task) {
        if (task.getId() != null && iTaskTaskRepository.existsById(Long.valueOf(task.getId()))) {
            iTaskTaskRepository.save(task);
        } else if (task.getId() == null) {
            iTaskTaskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Task with given ID does not exist");
        }
    }

    public void deleteTask(Task task) {
        iTaskTaskRepository.delete(task);
    }

    public List<TaskType> getTaskTypes() {
        return iTaskTypeRepository.findAll();
    }

    public TaskType getTaskTypeByTag(String name) {
        return iTaskTypeRepository.findTaskTypeByType(name);
    }

    public Task getTaskById(String id) {
        return iTaskTaskRepository.findById(Long.valueOf(id)).get();
    }
}

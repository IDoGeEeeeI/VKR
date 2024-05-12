package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rut.pan.entity.*;
import rut.pan.reposiroty.IPrioritizeRepository;
import rut.pan.reposiroty.IStatusRepository;
import rut.pan.reposiroty.ITaskRepository;
import rut.pan.reposiroty.ITaskTypeRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private ITaskRepository iTaskRepository;

    @Autowired
    private ITaskTypeRepository iTaskTypeRepository;

    @Autowired
    private IStatusRepository iStatusRepository;

    @Autowired
    private IPrioritizeRepository iPrioritizeRepository;


    public List<Task> list() {
        return iTaskRepository.findAll();
    }

    public List<Task> getTasksByEmployer(Employer... employers) {
        List<Integer> employerIds = Arrays.stream(employers)
                .map(Employer::getId)
                .collect(Collectors.toList());
        return iTaskRepository.findByEmployer_IdIn(employerIds);
    }

    public List<Task> getListByEmployer(Employer employer) {
        return iTaskRepository.findByEmployer(employer);
    }

    public List<Task> getAllTasksByEmployerId(Integer employerId) {
        return iTaskRepository.getAllTasksByEmployerId(employerId);
    }

    public List<Status> getAllStatus() {
        return iStatusRepository.findAll();
    }

    public Status getStatusByName(String s) {
        return iStatusRepository.getStatusByStatus(s);
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
        if (task.getId() != null && iTaskRepository.existsById(Long.valueOf(task.getId()))) {
            iTaskRepository.save(task);
        } else if (task.getId() == null) {
            iTaskRepository.save(task);
        } else {
            throw new IllegalArgumentException("Task with given ID does not exist");
        }
    }

    public void deleteTask(Task task) {
        if (task.getId() != null && iTaskRepository.existsById(Long.valueOf(task.getId()))) {
            iTaskRepository.delete(task);
        } else {
            throw new IllegalArgumentException("Task with given ID does not exist");
        }
    }

    public List<TaskType> getTaskTypes() {
        return iTaskTypeRepository.findAll();
    }

    public TaskType getTaskTypeByTag(String name) {
        return iTaskTypeRepository.findTaskTypeByType(name);
    }

    public Task getTaskById(String id) {
        return iTaskRepository.findById(Long.valueOf(id)).get();
    }
}

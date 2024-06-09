package rut.pan.service.IService;

import rut.pan.entity.*;

import java.util.List;

public interface ITaskService {

    List<Task> list();

    List<Task> getTasksByEmployer(Employer... employers);

    List<Task> getListByEmployer(Employer employer);

    List<Task> getAllTasksByEmployerId(Integer employerId);

    void saveOrEditTask(Task task);

    void deleteTask(Task task);

    Task getTaskById(String id);



    List<Status> getAllStatus();

    Status getStatusByName(String s);

    Status getStatusByTask(Task task);

    List<Status> getStatus();

    List<Prioritize> getPrioritize();

    List<TaskType> getTaskTypes();

    TaskType getTaskTypeByTag(String name);

}

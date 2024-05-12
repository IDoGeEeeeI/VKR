package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rut.pan.entity.Employer;
import rut.pan.entity.Task;

import java.util.List;


@Repository
public interface ITaskRepository extends JpaRepository<Task, Long> {

    List<Task> getAllTasksByEmployerId(Integer employerId);

    List<Task> findByEmployer(Employer employer);

    List<Task> findByEmployer_IdIn(List<Integer> employerIds);


}

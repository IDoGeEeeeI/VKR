package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rut.pan.entity.TaskType;

@Repository
public interface ITaskTypeRepository extends JpaRepository<TaskType, String> {

    TaskType findTaskTypeByType(String type);
}

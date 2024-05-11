package rut.pan.reposiroty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rut.pan.entity.Comment;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findCommentByTaskId(Integer task_id);

}

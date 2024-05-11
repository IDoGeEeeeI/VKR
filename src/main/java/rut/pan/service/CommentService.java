package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rut.pan.entity.Comment;
import rut.pan.reposiroty.ICommentRepository;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private ICommentRepository iCommentRepository;

    public List<Comment> getCommentsByTaskId(Integer id) {
        return iCommentRepository.findCommentByTaskId(id);
    }

    public void saveComment(Comment comment) {
        iCommentRepository.save(comment);
    }
}

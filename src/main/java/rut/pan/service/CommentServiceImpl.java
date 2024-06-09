package rut.pan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rut.pan.entity.Comment;
import rut.pan.reposiroty.ICommentRepository;
import rut.pan.service.IService.ICommentServiceService;

import java.util.List;

@Service
public class CommentServiceImpl implements ICommentServiceService {

    @Autowired
    private ICommentRepository iCommentRepository;

    @Override
    public List<Comment> getCommentsByTaskId(Integer id) {
        return iCommentRepository.findCommentByTaskId(id);
    }

    @Override
    public void saveComment(Comment comment) {
        iCommentRepository.save(comment);
    }
}

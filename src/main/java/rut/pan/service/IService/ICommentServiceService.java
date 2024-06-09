package rut.pan.service.IService;

import rut.pan.entity.Comment;

import java.util.List;

public interface ICommentServiceService {

    List<Comment> getCommentsByTaskId(Integer id);

    void saveComment(Comment comment);
}

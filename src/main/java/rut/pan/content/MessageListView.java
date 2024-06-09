package rut.pan.content;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import rut.pan.entity.Comment;
import rut.pan.entity.Employer;
import rut.pan.entity.Task;
import rut.pan.service2.Service2;

public class MessageListView extends Div {

    Employer employer = Service2.getInstance().getEmployerByAuthenticationUser();

    public MessageListView(Task task) {
        MessageList list = new MessageList();
        MessageInput input = new MessageInput();
        input.setWidthFull();
        input.addSubmitListener(submitEvent -> {
            MessageListItem newMessage = new MessageListItem(
                    submitEvent.getValue(),
                    Instant.now(),
                    employer.getName()
                    );
            newMessage.setUserColorIndex(employer.getId());
            List<MessageListItem> items = new ArrayList<>(list.getItems());
            items.add(newMessage);
            list.setItems(items);

            Comment comment = new Comment();
            comment.setText(submitEvent.getValue());
            comment.setDatePosted(Date.from(Instant.now()));
            comment.setTask(task);
            comment.setEmployer(employer);

            Service2.getInstance().getCommentServiceImpl().saveComment(comment);
        });

        List<Comment> comments = Service2.getInstance().getCommentServiceImpl().getCommentsByTaskId(task.getId());
        List<MessageListItem> commList = new ArrayList<>();
        for (Comment comment : comments) {
            MessageListItem message = new MessageListItem(
                    comment.getText(),
                    comment.getDatePosted().toInstant(),
                    comment.getEmployer().getName());
            message.setUserColorIndex(comment.getEmployer().getId());
            commList.add(message);
        }

        list.setItems(commList);

        VerticalLayout chatLayout = new VerticalLayout(list, input);
        chatLayout.expand(list);
        add(chatLayout);
    }
}

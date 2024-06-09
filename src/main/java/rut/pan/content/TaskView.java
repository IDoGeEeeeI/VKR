package rut.pan.content;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.dom.Style;
import lombok.Getter;
import rut.pan.Enums.TaskEnum;
import rut.pan.content.dialogs.AppointDialog;
import rut.pan.entity.Employer;
import rut.pan.entity.Status;
import rut.pan.entity.Task;
import rut.pan.service2.Service2;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class TaskView extends SplitLayout {

    @Getter
    private VerticalLayout leftLayout = new VerticalLayout();
    private Div rightLayout = new Div();

    private final Employer employer;

    @Getter
    private Task rightTask;

    public TaskView(Employer employer) {
        this.employer = employer;
        initializeLayout();
        addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
    }

    private void initializeLayout() {
        leftLayout.setMinWidth("250px");
//        leftLayout.setSpacing(false);
//        leftLayout.setPadding(false);
        rightLayout.setMinWidth("500px");
        addToPrimary(leftLayout);
        addToSecondary(rightLayout);
        createButtons();
        Component component = leftLayout.getChildren().findFirst().get();
        createTaskRightView(Service2.getInstance().getTaskServiceImpl().getTaskById(component.getElement().getProperty("task-id")));
        setHeight("780px");
        setSplitterPosition(15);


    }

    private void createTaskRightView(Task task) {
        rightTask = task;
        rightLayout.removeAll();
        VerticalLayout taskView = new VerticalLayout();

        Div nameTaskDiv = new Div(task.getName());
        nameTaskDiv.getStyle().set("box-shadow", "1px 1px 1px gray");
        nameTaskDiv.getStyle().set("font-size", "2rem");
        nameTaskDiv.setWidthFull();


        HorizontalLayout horizontalLayout1 = new HorizontalLayout();

        Avatar avatarImage = new Avatar();
        avatarImage.addClassName("avatar");
        avatarImage.setHeight("52px");
        avatarImage.setWidth("52px");
        avatarImage.setName(task.getEmployer().getName());

        Div empName = new Div();
        empName.add(task.getEmployer().getName());
        empName.getStyle().set("font-size", "1.2rem");
        empName.getStyle().set("padding-top", "10px");

        horizontalLayout1.add(avatarImage, empName);

        Details infoDetail = new Details("Детали");
        infoDetail.setOpened(true);
        infoDetail.setWidthFull();

        Div infoDiv = new Div();

        HorizontalLayout horizontalLayoutInfo0 = new HorizontalLayout();
        Div cr = new Div("Автор: ");
        cr.getStyle().set("marginLeft", "10px");
        cr.getStyle().set("font-size", "1.2rem");

        Avatar avatarCreatorImage = new Avatar();
        avatarCreatorImage.addClassName("avatar");
        avatarCreatorImage.setHeight("24px");
        avatarCreatorImage.setWidth("24px");
        avatarCreatorImage.setName(task.getCreator().getName());

        Div creatorName = new Div();
        creatorName.add(task.getCreator().getName());
        creatorName.getStyle().set("font-size", "1.2rem");

        horizontalLayoutInfo0.add(cr, avatarCreatorImage, creatorName);

        //иконки добавить
        HorizontalLayout horizontalLayoutInfo1 = new HorizontalLayout();
        Div s = new Div("Статус: " + task.getStatus().getStatus());
        s.getStyle().set("marginLeft", "10px");
        s.getStyle().set("font-size", "1.2rem");
        horizontalLayoutInfo1.add(s);

        HorizontalLayout horizontalLayoutInfo2 = new HorizontalLayout();
        Span t = new Span("Тип: " + task.getTaskType().getType());
        t.getStyle().set("marginLeft", "10px");
        t.getStyle().set("font-size", "1.2rem");
        horizontalLayoutInfo2.add(t);

        HorizontalLayout horizontalLayoutInfo3 = new HorizontalLayout();
        Div p = new Div("Приоритет: " + task.getPrioritizer().getPrioritize());
        p.getStyle().set("marginLeft", "10px");
        p.getStyle().set("font-size", "1.2rem");
        horizontalLayoutInfo3.add(p);

        infoDiv.add(horizontalLayoutInfo0, horizontalLayoutInfo1, horizontalLayoutInfo2, horizontalLayoutInfo3);

        infoDetail.add(infoDiv);

        Details descriptionDetail = new Details("Описание");
        descriptionDetail.setOpened(true);
        descriptionDetail.setWidthFull();
        descriptionDetail.add(new Span(task.getDescription()));
        Details files = new Details("Приложенные фалы");
        files.setOpened(false);
        //todo files - MinIO
//        Upload upload = new Upload();
//        upload.setAcceptedFileTypes("image/jpeg", "image/png");
//        upload.setAutoUpload(true);
//        files.add(upload);
        descriptionDetail.add(files);

        Details commentsDetail = new Details("Комментарии");
        commentsDetail.setOpened(true);
        commentsDetail.setWidthFull();
        commentsDetail.add(new MessageListView(task));

        HorizontalLayout buttonsLayout = createButtonsStatus(task.getStatus().getStatus(), task);

        taskView.add(nameTaskDiv, horizontalLayout1, buttonsLayout, infoDetail, descriptionDetail, commentsDetail);
        rightLayout.add(taskView);

    }

    private HorizontalLayout createButtonsStatus(String status, Task task) {
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Button appoint = new Button("Назначить");
        Button startWork = new Button("Начать работу");
        Button stopWork = new Button("Остановить работу");
        Button agreed = new Button("Решение запроса");
        Button close = new Button("Закрыть запрос");
        Button reopen = new Button("Переоткрыть запрос");

        List<Status> allStatus = Service2.getInstance().getTaskServiceImpl().getAllStatus();

        appoint.addClickListener(e -> {
            AppointDialog appointDialog = new AppointDialog(task,
            yes -> {
                Service2.getInstance().getTaskServiceImpl().saveOrEditTask(task);
                reloadTaskLayout(task);
            });
            appointDialog.open();
        });

        startWork.addClickListener(e -> {
            task.setStatus(allStatus.get(1)); //кринж
            task.setStartDate(Date.from(Instant.now()));
            Service2.getInstance().getTaskServiceImpl().saveOrEditTask(task);

            reloadButtons(buttonsLayout, task);
            reloadTaskLayout(task);
        });

        stopWork.addClickListener(e -> {
            task.setStatus(allStatus.get(0));
            Service2.getInstance().getTaskServiceImpl().saveOrEditTask(task);

            reloadButtons(buttonsLayout, task);
            reloadTaskLayout(task);
        });

        agreed.addClickListener(e -> {
            task.setStatus(allStatus.get(2));
            Service2.getInstance().getTaskServiceImpl().saveOrEditTask(task);

            reloadButtons(buttonsLayout, task);
            reloadTaskLayout(task);
        });

        close.addClickListener(e -> {
            task.setStatus(allStatus.get(4));
            Service2.getInstance().getTaskServiceImpl().saveOrEditTask(task);

            reloadButtons(buttonsLayout, task);
            reloadTaskLayout(task);
        });

        reopen.addClickListener(e -> {
            task.setStatus(allStatus.get(3));
            Service2.getInstance().getTaskServiceImpl().saveOrEditTask(task);

            reloadButtons(buttonsLayout, task);
            reloadTaskLayout(task);
        });

        buttonsLayout.add(appoint);
        if ("OPEN".equals(status) || "REOPENED".equals(status)) {
            buttonsLayout.add(startWork, agreed, close);
        } else if ("IN PROGRESS".equals(task.getStatus().getStatus())) {
            buttonsLayout.add(stopWork, agreed, close);
        } else if ("RESOLVED".equals(status)) {
            buttonsLayout.add(reopen, close);
        } else if ("CLOSED".equals(status)) {
            buttonsLayout.add(agreed, reopen);
        }

        return buttonsLayout;
    }

    public void reloadButtons(HorizontalLayout buttonsLayout, Task task) {
        buttonsLayout.removeAll();
        HorizontalLayout newButtonsLayout = createButtonsStatus(task.getStatus().getStatus(), task);
        buttonsLayout.add(newButtonsLayout);
    }

    private void createButtons() {
        for (Task task : Service2.getInstance().getTaskServiceImpl().list()) {
            Div taskDiv = createDivTask(task);
            taskDiv.addClickListener(e -> {
                buttonClickHandler(task);
            });
            leftLayout.add(taskDiv);
        }
    }

    public void reloadTaskListByFilter(List<Task> tasks) {
        leftLayout.removeAll();
        for (Task task : tasks) {
            Div taskDiv = createDivTask(task);
            taskDiv.addClickListener(e -> {
                buttonClickHandler(task);
            });
            leftLayout.add(taskDiv);
        }
    }

    public void reloadTaskList() {
        leftLayout.removeAll();
        for (Task task : Service2.getInstance().getTaskServiceImpl().list()) {
            Div taskDiv = createDivTask(task);
            taskDiv.addClickListener(e -> {
                buttonClickHandler(task);
            });
            leftLayout.add(taskDiv);
        }
    }

    public void reloadTaskLayout(Task task) {
        createTaskRightView(task);
    }

    public void reloadTaskLayoutDefault() {
        Component component = leftLayout.getChildren().findFirst().get();
        createTaskRightView(Service2.getInstance().getTaskServiceImpl().getTaskById(component.getElement().getProperty("task-id")));
    }

    private void buttonClickHandler(Task task) {
        rightLayout.removeAll();
        createTaskRightView(task);
    }

    private Div createDivTask(Task task) {
        Div div = new Div();
        div.addClassName("task-div");
        div.getElement().setProperty("task-id", task.getId());

        Icon iconComponent = VaadinIcon.CIRCLE.create();
        iconComponent.setColor(TaskEnum.getColorByType(task.getTaskType().getType()));
        iconComponent.getStyle().setAlignSelf(Style.AlignSelf.CENTER);
        iconComponent.getStyle().set("marginLeft", "10px");
        iconComponent.setSize("16px");

        Div iconDiv = new Div();
        iconDiv.setMinWidth("23px");
        iconDiv.add(iconComponent);

        Span textSpan = new Span(task.getName());
        textSpan.getStyle().set("marginLeft", "10px");
        textSpan.setWidth(leftLayout.getWidth());

        Span description = new Span(task.getDescription());
        description.getStyle().set("marginLeft", "10px");
        description.getStyle().set("white-space", "nowrap");
        description.getStyle().set("overflow", "hidden");
        description.getStyle().set("text-overflow", "ellipsis");
        description.getStyle().set("color", "black");
        description.setWidth(leftLayout.getWidth());

        VerticalLayout verticalLayout = new VerticalLayout(textSpan, description);
        verticalLayout.setPadding(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setMargin(false);
        verticalLayout.getStyle().set("font-size", "--lumo-font-size-xs");

        HorizontalLayout layout = new HorizontalLayout(iconDiv, verticalLayout);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setMargin(false);
        layout.setWidth("100%");
        layout.getStyle().set("overflow","hidden");

        div.add(layout);
        div.setWidthFull();
//        div.getStyle().set("background-color", "white");
        div.getStyle().set("justify-content", "left");
        div.setMinHeight("54px");
        div.setWidth("100%");
//        div.getStyle().set("box-shadow", "0 0 0 1px  gray");

        return div;
    }

}

package rut.pan.content.dialogs;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
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
import rut.pan.Enums.TaskEnum;
import rut.pan.content.MessageListView;
import rut.pan.entity.Employer;
import rut.pan.entity.Task;
import rut.pan.service2.Service2;

public class TaskView extends SplitLayout {

    private VerticalLayout leftLayout = new VerticalLayout();
    private Div rightLayout = new Div();

    private final Employer employer;

    public TaskView(Employer employer) {
        this.employer = employer;
        initializeLayout();
        addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
    }

    private void initializeLayout() {
        //todo кнопки создания и тд в фильтровом лейауте
        //todo это для фильтра - Service2.getInstance().getTaskService().getTasksByEmployer(employer)
        leftLayout.setMinWidth("250px");
//        leftLayout.setSpacing(false);
//        leftLayout.setPadding(false);
        rightLayout.setMinWidth("500px");
        addToPrimary(leftLayout);
        addToSecondary(rightLayout);
        createButtons();
        createTaskRightView(null);
        setHeight("780px");
        setSplitterPosition(15);


    }

    private void createTaskRightView(Task task) {
        if (task == null) {
            Component component = leftLayout.getChildren().findFirst().get();
            task = Service2.getInstance().getTaskService().getTaskById(component.getElement().getProperty("task-id"));
        }

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

        Div infoDiv = new Div();
        //todo иконки добавить
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

        infoDiv.add(horizontalLayoutInfo1, horizontalLayoutInfo2, horizontalLayoutInfo3);

        infoDetail.add(infoDiv);

        Details descriptionDetail = new Details("Описание");
        descriptionDetail.setOpened(true);
        descriptionDetail.setWidthFull();
        descriptionDetail.add(new Span(task.getDescription()));
        Details files = new Details("Приложенные фалы");
        //todo files
        files.setOpened(false);
        descriptionDetail.add(files);

        Details commentsDetail = new Details("Комментарии");
        commentsDetail.setOpened(true);
        commentsDetail.setWidthFull();
        commentsDetail.add(new MessageListView(task));


        taskView.add(nameTaskDiv, horizontalLayout1, infoDetail, descriptionDetail, commentsDetail);
        rightLayout.add(taskView);

    }

    private void createButtons() {
//        for (Task task : Service2.getInstance().getTaskService().getTasksByEmployer(employer)) {
        for (Task task : Service2.getInstance().getTaskService().list()) {
            Div taskDiv = createDivTask(task);
            taskDiv.addClickListener(e -> {
                buttonClickHandler(task);
            });
            leftLayout.add(taskDiv);
        }
    }

    public void reloadTaskList() {
        leftLayout.removeAll();
        for (Task task : Service2.getInstance().getTaskService().list()) {
            Div taskDiv = createDivTask(task);
            taskDiv.addClickListener(e -> {
                buttonClickHandler(task);
            });
            leftLayout.add(taskDiv);
        }
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

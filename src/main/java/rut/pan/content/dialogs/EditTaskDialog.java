package rut.pan.content.dialogs;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import rut.pan.entity.*;
import rut.pan.service2.Service2;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EditTaskDialog extends Dialog {

    public EditTaskDialog(Task task, BiConsumer<EditTaskDialog, Task> yes,
                         Consumer<EditTaskDialog> no, Consumer<EditTaskDialog> del) {
        super();
        VerticalLayout verticalLayout = new VerticalLayout();
        add(verticalLayout);
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(false);
        verticalLayout.setPadding(false);
        verticalLayout.setWidthFull();
        verticalLayout.setHeightFull();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout taskLayout = new VerticalLayout();

        TextField name = new TextField();
        name.setLabel("Название");
        name.setWidthFull();
        name.setValue(task.getName());


        ComboBox<TaskType> taskTypeComboBox = new ComboBox<>();
        taskTypeComboBox.setLabel("Тип задачи");
        taskTypeComboBox.setWidthFull();
        taskTypeComboBox.setItemLabelGenerator(TaskType::getType);
        taskTypeComboBox.setItems(Service2.getInstance().getTaskService().getTaskTypes());
        taskTypeComboBox.setValue(task.getTaskType());


        ComboBox<Status> statusComboBox = new ComboBox<>();
        statusComboBox.setLabel("Статус задачи");
        statusComboBox.setWidthFull();
        statusComboBox.setItemLabelGenerator(Status::getStatus);
        statusComboBox.setItems(Service2.getInstance().getTaskService().getStatus());
        statusComboBox.setValue(task.getStatus());

        ComboBox<Prioritize> prioritizeComboBox = new ComboBox<>();
        prioritizeComboBox.setLabel("Приоритет задачи");
        prioritizeComboBox.setWidthFull();
        prioritizeComboBox.setItemLabelGenerator(Prioritize::getPrioritize);
        prioritizeComboBox.setItems(Service2.getInstance().getTaskService().getPrioritize());
        prioritizeComboBox.setValue(task.getPrioritizer());

        ComboBox<Employer> employerComboBox = new ComboBox<>();
        employerComboBox.setLabel("Исполнитель");
        employerComboBox.setWidthFull();
        employerComboBox.setItemLabelGenerator(Employer::getName);
        employerComboBox.setItems(Service2.getInstance().getEmployerService().list());
        employerComboBox.setValue(task.getEmployer());

        ComboBox<Employer> creatorComboBox = new ComboBox<>();
        creatorComboBox.setLabel("Создатель");
        creatorComboBox.setWidthFull();
        creatorComboBox.setItemLabelGenerator(Employer::getName);
        creatorComboBox.setItems(Service2.getInstance().getEmployerService().list());
        creatorComboBox.setValue(task.getCreator());

        TextArea description = new TextArea();
        description.setLabel("Описание");
        description.setWidthFull();
        description.setValue(task.getDescription());

        DatePicker start = new DatePicker();
        start.setLabel("Начало");
        start.setWidthFull();
        start.setValue(task.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        DatePicker end = new DatePicker();
        end.setLabel("Конец");
        end.setWidthFull();
        end.setValue(task.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        start.addValueChangeListener(e -> {
            LocalDate startDate = e.getValue();
            LocalDate endDate = end.getValue();

            if (endDate != null && endDate.isBefore(startDate)) {
                Notification.show("Дата начала не может быть позже даты окончания. Пожалуйста, выберите другую дату.");
                start.setValue(endDate);
            } else {

            }
        });

        end.addValueChangeListener(e -> {
            LocalDate endDate = e.getValue();
            LocalDate startDate = start.getValue();

            if (startDate != null && endDate.isBefore(startDate)) {
                Notification.show("Дата окончания не может быть раньше даты начала. Пожалуйста, выберите другую дату.");
                end.setValue(startDate);
            } else {

            }
        });


        HorizontalLayout dates = new HorizontalLayout();
        dates.add(start, end);

        taskLayout.add(name, taskTypeComboBox, statusComboBox, prioritizeComboBox,
                employerComboBox, creatorComboBox, description, dates);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setMargin(false);
        horizontalLayout.setPadding(true);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        verticalLayout.add(taskLayout, horizontalLayout);

        Button button = new Button("Сохранить");
        button.getElement().setAttribute("ButtonYes", true);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(buttonClickEvent -> {
            if (name.isEmpty() || taskTypeComboBox.isEmpty() || statusComboBox.isEmpty() || prioritizeComboBox.isEmpty() || start.isEmpty() || end.isEmpty()) {
                Notification.show("Пожалуйста, заполните все поля.", 5000, Notification.Position.MIDDLE);
            } else {
                EditTaskDialog.this.close();
                task.setName(name.getValue());
                task.setTaskType(taskTypeComboBox.getValue());
                task.setStatus(statusComboBox.getValue());
                task.setPrioritizer(prioritizeComboBox.getValue());
                task.setEmployer(employerComboBox.getValue());
                task.setCreator(creatorComboBox.getValue());
                task.setDescription(description.getValue());
                task.setStartDate(Date.from(start.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                task.setEndDate(Date.from(end.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

                EditTaskDialog.this.close();
                yes.accept(EditTaskDialog.this, task);
            }
        });

        horizontalLayout.add(button);

        Div interval = new Div();
        interval.setWidth(1, Unit.EM);

        button = new Button("Отмена");
        button.getElement().setAttribute("ButtonNo", true);
        button.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        button.addClickListener(buttonClickEvent -> {
            EditTaskDialog.this.close();
            no.accept(EditTaskDialog.this);
        });

        horizontalLayout.add(interval, button);

        button = new Button("Удалить");
        button.getElement().setAttribute("ButtonDel", true);
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        button.addClickListener(buttonClickEvent -> {
            EditTaskDialog.this.close();
            del.accept(EditTaskDialog.this);
        });

        horizontalLayout.add(interval, button);


        setModal(true);
        setDraggable(true);
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

    }

    public EditTaskDialog(Task task, BiConsumer<EditTaskDialog, Task> yes, Consumer<EditTaskDialog> del) {
        this(task, yes, no -> {}, del);
    }
}

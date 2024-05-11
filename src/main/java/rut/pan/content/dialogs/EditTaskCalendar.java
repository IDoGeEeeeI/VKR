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
import org.vaadin.stefan.fullcalendar.Entry;
import rut.pan.Enums.TaskEnum;
import rut.pan.entity.*;
import rut.pan.service2.Service2;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Диалоговое окно задач в календаре.
 */
public class EditTaskCalendar extends Dialog {

    /**
     * Создать новое окно.
     * @param yes действие на кнопку "Да".
     * @param no действие на кнопку "Нет".
     */
    public EditTaskCalendar(Entry entry, Task task,
                            BiConsumer<EditTaskCalendar, Task> yes,
                            Consumer<EditTaskCalendar> no,
                            Consumer<EditTaskCalendar> del) {
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
        name.setValue(entry.getTitle());
        name.addValueChangeListener(e -> {
            entry.setTitle(e.getValue());
        });

        ComboBox<TaskType> taskTypeComboBox = new ComboBox<>();
        taskTypeComboBox.setLabel("Тип задачи");
        taskTypeComboBox.setWidthFull();
        taskTypeComboBox.addValueChangeListener(e -> {
            entry.setColor(TaskEnum.getColorByType(e.getValue().getType()));
        });
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
        description.setValue(entry.getDescription());
        description.addValueChangeListener(e -> {
            entry.setDescription(e.getValue());
        });

        DatePicker start = new DatePicker();
        start.setLabel("Начало");
        start.setWidthFull();
        start.setValue(entry.getStart().toLocalDate());

        DatePicker end = new DatePicker();
        end.setLabel("Конец");
        end.setWidthFull();
        end.setValue(entry.getEnd().toLocalDate());


        start.addValueChangeListener(e -> {
            LocalDate startDate = e.getValue();
            LocalDate endDate = end.getValue();

            if (endDate != null && endDate.isBefore(startDate)) {
                Notification.show("Дата начала не может быть позже даты окончания. Пожалуйста, выберите другую дату.");
                start.setValue(endDate);
            } else {
                entry.setStart(startDate.atStartOfDay(ZoneOffset.UTC).toInstant());
            }
        });

        end.addValueChangeListener(e -> {
            LocalDate endDate = e.getValue();
            LocalDate startDate = start.getValue();

            if (startDate != null && endDate.isBefore(startDate)) {
                Notification.show("Дата окончания не может быть раньше даты начала. Пожалуйста, выберите другую дату.");
                end.setValue(startDate);
            } else {
                entry.setEnd(endDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant());
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
            EditTaskCalendar.this.close();
            task.setName(entry.getTitle());
            task.setTaskType(
                    Service2.getInstance()
                            .getTaskService()
                            .getTaskTypeByTag(TaskEnum.getTagByColor(entry.getColor()))
            );
            task.setStatus(statusComboBox.getValue());
            task.setPrioritizer(prioritizeComboBox.getValue());
            task.setEmployer(employerComboBox.getValue());
            task.setCreator(creatorComboBox.getValue());
            task.setDescription(entry.getDescription());
            task.setStartDate(Date.from(entry.getStart().toInstant(ZoneOffset.UTC)));
            task.setEndDate(Date.from(entry.getEnd().toInstant(ZoneOffset.UTC)));

            EditTaskCalendar.this.close();
            yes.accept(EditTaskCalendar.this, task);
        });

        horizontalLayout.add(button);

        Div interval = new Div();
        interval.setWidth(1, Unit.EM);

        button = new Button("Отмена");
        button.getElement().setAttribute("ButtonNo", true);
        button.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        button.addClickListener(buttonClickEvent -> {
            EditTaskCalendar.this.close();
            no.accept(EditTaskCalendar.this);
        });

        horizontalLayout.add(interval, button);


        button = new Button("Удалить");
        button.getElement().setAttribute("ButtonDel", true);
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);
        button.addClickListener(buttonClickEvent -> {
            EditTaskCalendar.this.close();
            del.accept(EditTaskCalendar.this);
        });

        horizontalLayout.add(interval, button);

        setModal(true);
        setDraggable(true);
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
    }

    /**
     * Создать новое окно.
     * @param yes действие на кнопку "Да".
     * @param del действие на кнопку "Удалить".
     */
    public EditTaskCalendar(Entry entry, Task task, BiConsumer<EditTaskCalendar, Task> yes, Consumer<EditTaskCalendar> del) {
        this(entry, task, yes, no -> {}, del);
    }
}

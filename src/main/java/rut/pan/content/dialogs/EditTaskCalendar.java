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

import java.time.*;
import java.util.Date;
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
        name.setValue(task.getName());
        name.addValueChangeListener(e -> {
            entry.setTitle(task.getName() + "(" + task.getEmployer().getName() + ")");
        });

        ComboBox<TaskType> taskTypeComboBox = new ComboBox<>();
        taskTypeComboBox.setLabel("Тип задачи");
        taskTypeComboBox.setWidthFull();
        taskTypeComboBox.addValueChangeListener(e -> {
            entry.setColor(TaskEnum.getColorByType(e.getValue().getType()));
        });
        taskTypeComboBox.setItemLabelGenerator(TaskType::getType);
        taskTypeComboBox.setItems(Service2.getInstance().getTaskServiceImpl().getTaskTypes());
        taskTypeComboBox.setValue(task.getTaskType());


        ComboBox<Status> statusComboBox = new ComboBox<>();
        statusComboBox.setLabel("Статус задачи");
        statusComboBox.setWidthFull();
        statusComboBox.setItemLabelGenerator(Status::getStatus);
        statusComboBox.setItems(Service2.getInstance().getTaskServiceImpl().getStatus());
        statusComboBox.setValue(task.getStatus());

        ComboBox<Prioritize> prioritizeComboBox = new ComboBox<>();
        prioritizeComboBox.setLabel("Приоритет задачи");
        prioritizeComboBox.setWidthFull();
        prioritizeComboBox.setItemLabelGenerator(Prioritize::getPrioritize);
        prioritizeComboBox.setItems(Service2.getInstance().getTaskServiceImpl().getPrioritize());
        prioritizeComboBox.setValue(task.getPrioritizer());

        ComboBox<Employer> employerComboBox = new ComboBox<>();
        employerComboBox.setLabel("Исполнитель");
        employerComboBox.setWidthFull();
        employerComboBox.setItemLabelGenerator(Employer::getName);
        employerComboBox.setItems(Service2.getInstance().getEmployerServiceImpl().list());
        employerComboBox.setValue(task.getEmployer());

        ComboBox<Employer> creatorComboBox = new ComboBox<>();
        creatorComboBox.setLabel("Создатель");
        creatorComboBox.setWidthFull();
        creatorComboBox.setItemLabelGenerator(Employer::getName);
        creatorComboBox.setItems(Service2.getInstance().getEmployerServiceImpl().list());
        creatorComboBox.setValue(task.getCreator());
        creatorComboBox.setEnabled(false);


        TextArea description = new TextArea();
        description.setLabel("Описание");
        description.setWidthFull();
        description.setValue(task.getDescription());
        description.addValueChangeListener(e -> {
            entry.setDescription(e.getValue());
        });

        DatePicker start = new DatePicker();
        start.setLabel("Начало");
        start.setWidthFull();
        LocalDate startDate = Instant.ofEpochMilli(task.getStartDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        start.setValue(startDate);

        DatePicker end = new DatePicker();
        end.setLabel("Конец");
        end.setWidthFull();
        LocalDate endDate = Instant.ofEpochMilli(task.getEndDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        end.setValue(endDate);



        start.addValueChangeListener(e -> {
            if (end.getValue() != null && end.getValue().isBefore(e.getValue())) {
                Notification.show("Дата начала не может быть позже даты окончания. Пожалуйста, выберите другую дату.");
                start.setValue(end.getValue());
            } else {
                entry.setStart(e.getValue());
            }
        });

        end.addValueChangeListener(e -> {
            if (start.getValue() != null && e.getValue().isBefore(start.getValue())) {
                Notification.show("Дата окончания не может быть раньше даты начала. Пожалуйста, выберите другую дату.");
                end.setValue(start.getValue());
            } else {
                entry.setEnd(e.getValue());
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
            task.setName(name.getValue());
            task.setTaskType(
                    Service2.getInstance()
                            .getTaskServiceImpl()
                            .getTaskTypeByTag(TaskEnum.getTagByColor(entry.getColor()))
            );
            task.setStatus(statusComboBox.getValue());
            task.setPrioritizer(prioritizeComboBox.getValue());
            task.setEmployer(employerComboBox.getValue());
//            task.setCreator(creatorComboBox.getValue());
            task.setDescription(description.getValue());
            Date startDate1 = Date.from(start.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            task.setStartDate(startDate1);
            Date endDate1 = Date.from(end.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            task.setEndDate(endDate1);

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

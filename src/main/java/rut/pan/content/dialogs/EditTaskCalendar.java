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
import rut.pan.entity.Roles;
import rut.pan.entity.Task;
import rut.pan.entity.TaskType;
import rut.pan.service2.Service2;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
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
    public EditTaskCalendar(Entry entry, Consumer<EditTaskCalendar> yes, Consumer<EditTaskCalendar> no, Consumer<EditTaskCalendar> del) {
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
            entry.setTitle(e.toString());
        });

        ComboBox<TaskType> type = new ComboBox<>();
        type.setLabel("Тип задачи");
        type.setWidthFull();
        type.addValueChangeListener(e -> {
            entry.setColor(TaskEnum.getColorByType(e.getValue().getType()));
        });
        type.setItemLabelGenerator(TaskType::getType);
        type.setItems(Service2.getInstance().getTaskService().getTaskTypes());
        type.setValue(Service2.getInstance().getTaskService().getTaskTypeByTag(TaskEnum.getTagByColor(entry.getColor()))); //изменить!

        TextArea description = new TextArea();
        description.setLabel("Описание");
        description.setWidthFull();
        description.setValue(entry.getDescription());
        description.addValueChangeListener(e -> {
            entry.setDescription(e.toString());
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

        taskLayout.add(name, type, description, dates);

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
            yes.accept(EditTaskCalendar.this);
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
    public EditTaskCalendar(Entry entry, Consumer<EditTaskCalendar> yes, Consumer<EditTaskCalendar> del) {
        this(entry, yes, no -> {}, del);
    }
}

package rut.pan.content;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.vaadin.stefan.fullcalendar.*;
import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;
import rut.pan.Enums.TaskEnum;
import rut.pan.Utils.ConvertDateRu;
import rut.pan.content.dialogs.EditTaskCalendar;
import rut.pan.entity.Employer;
import rut.pan.entity.Task;
import rut.pan.service2.Service2;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarView extends Div {

    private Map<Entry, Task> entryTaskMap = new HashMap<>();


    public CalendarView(Employer employer) {
        FullCalendar calendarContent = FullCalendarBuilder.create().build();
        calendarContent.setHeight("728px"); // 4+ часа потратил

        calendarContent.setLocale(CalendarLocale.RUSSIAN.getLocale());

        List<Task> taskList = Service2.getInstance().getTaskService().getListByEmployer(employer);
        List<Employer> employers = Service2.getInstance().getEmployerService().getEmployersBySupervisor(employer);
        if (!employers.isEmpty()) {
            for (Employer epm : employers) {
                taskList.addAll(Service2.getInstance().getTaskService().getListByEmployer(epm));
            }
        }

        for (Task task : taskList) {
            Entry entry = new Entry();
            entry.setTitle(task.getName() + " (" + task.getEmployer().getName() + ")");

            entry.setColor(TaskEnum.getColorByType(task.getTaskType().getType()));
            entry.setStart(task.getStartDate().toInstant());
            entry.setEnd(task.getEndDate().toInstant());
            entryTaskMap.put(entry, task);
            calendarContent.getEntryProvider().asInMemory().addEntries(entry);
        }
        calendarContent.addEntryClickedListener(e -> {
            InMemoryEntryProvider<Entry> entryProvider = calendarContent.getEntryProvider().asInMemory();
            Entry entry = e.getEntry();
            Task task = entryTaskMap.get(entry);
            EditTaskCalendar editTaskCalendar = new EditTaskCalendar(entry, task,
                    (dialog, updatedTask) -> {
                        Service2.getInstance().getTaskService().saveOrEditTask(updatedTask);
                        entryProvider.refreshAll();
                    },
                    del -> {
                        Service2.getInstance().getTaskService().deleteTask(task);
                        entryTaskMap.remove(entry);
                        entryProvider.refreshAll();

                    });

            editTaskCalendar.open();
            //как залочить скролл у главной вьюхи - хз
        });

        calendarContent.addTimeslotsSelectedListener(event -> {//todo не работает
            Entry entry = new Entry();

            entry.setStart(event.getStart());
            entry.setEnd(event.getEnd());
            entry.setAllDay(event.isAllDay());

        });


        HorizontalLayout calendarButtons = new HorizontalLayout();
        calendarButtons.setWidthFull();
        calendarButtons.addClassName("filter-layout");

        Button left = new Button(VaadinIcon.ANGLE_LEFT.create());
        Button right = new Button(VaadinIcon.ANGLE_RIGHT.create());
        Button today = new Button("Сегодня");

        left.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        right.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        today.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        left.addClickListener(event -> {
            calendarContent.previous();
        });
        right.addClickListener(event -> {
            calendarContent.next();
        });
        today.addClickListener(event -> {
            calendarContent.today();
        });

        Span todayLabel = new Span();
        todayLabel.getStyle().set("text-align", "center");
        todayLabel.setWidthFull();

        Button year = new Button("Год");
        Button month = new Button("Месяц");
        Button week = new Button("Неделя");
        Button day = new Button("День");
        Button list = new Button("Список");

        year.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        month.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        week.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        day.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        list.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        calendarContent.addDatesRenderedListener(event -> {
            LocalDate start = event.getIntervalStart();
            LocalDate end = event.getIntervalEnd();

            if (start.getYear() != end.getYear()) {
                if (start.getMonthValue() == 12 && end.getMonthValue() == 1) {
                    todayLabel.setText(ConvertDateRu.toMonthString(start.getMonth().getValue()) + " " + start.getYear());
                } else {
                    todayLabel.setText(String.valueOf(start.getYear()));
                }
            } else if (start.getMonth() != end.getMonth()) {
                if (Math.abs(ChronoUnit.DAYS.between(start, end)) > 7){
                    todayLabel.setText(ConvertDateRu.toMonthString(start.getMonth().getValue()) + " " + start.getYear());
                } else {
                    todayLabel.setText(start.getDayOfMonth() + " " + ConvertDateRu.toMonthString(start.getMonth().getValue()) +
                            " - " + end.getDayOfMonth() + " " + ConvertDateRu.toMonthString(end.getMonth().getValue()) + " " + end.getYear());
                }
            } else if (start.plusDays(7).equals(end)) {
                todayLabel.setText(ConvertDateRu.toMonthString(start.getMonth().getValue()) +
                        " " + start.getDayOfMonth() + " - " + end.getDayOfMonth() + " " + start.getYear());
            } else if (start.plusDays(1).equals(end)) {
                todayLabel.setText(start.getDayOfMonth() + " " + ConvertDateRu.toMonthString(start.getMonth().getValue()) + " " + start.getYear());
            }
        });

        year.addClickListener(event -> {
            calendarContent.changeView(CalendarViewImpl.DAY_GRID_YEAR);
        });
        month.addClickListener(event -> {
            calendarContent.changeView(CalendarViewImpl.DAY_GRID_MONTH);
        });
        week.addClickListener(event -> {
            calendarContent.changeView(CalendarViewImpl.DAY_GRID_WEEK);
        });
        day.addClickListener(event -> {
            calendarContent.changeView(CalendarViewImpl.DAY_GRID_DAY);
        });
        list.addClickListener(event -> {
            calendarContent.changeView(CalendarViewImpl.LIST_MONTH);
        });


        calendarButtons.add(left, right, today, todayLabel, year, month, week, day, list);

        add(calendarButtons);
        add(calendarContent);
    }

}

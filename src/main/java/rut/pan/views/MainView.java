package rut.pan.views;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import rut.pan.content.*;
import rut.pan.content.dialogs.*;
import rut.pan.entity.Employer;
import rut.pan.entity.EmployersRequests;
import rut.pan.service2.Service2;

import java.util.Collection;

@Slf4j
@PermitAll
@Route("")
class MainView extends VerticalLayout implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!Service2.getInstance().getSecurityService().isUserLoggedIn()) {
            beforeEnterEvent.rerouteTo(LoginView.class);
        }
    }

    private Employer user = null;

    MainView() {

        getUser();


        getStyle().set("height", "100%");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addClassName("super-square");
        mainLayout.setSizeFull();


        HorizontalLayout header = new HorizontalLayout();

        VerticalLayout sidebar = new VerticalLayout();

//        Button homeButton = createButtonWithIcon(VaadinIcon.HOME_O, "Главная");
        Button projectsButton = createButtonWithIcon(VaadinIcon.TASKS, "Задачи");
        Button calendarButton = createButtonWithIcon(VaadinIcon.CALENDAR, "Календарь");
        Button requestButton = createButtonWithIcon(VaadinIcon.QUESTION_CIRCLE_O, "Заявки");


        HorizontalLayout userDiv = new HorizontalLayout();
        userDiv.addClassName("user");
        userDiv.setSpacing(false);

        Avatar avatarImage = new Avatar();
        avatarImage.addClassName("avatar");
        userDiv.add(avatarImage);

        Span text = new Span();
        text.addClassName("user-text");

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setTarget(userDiv);
        contextMenu.setOpenOnClick(true);

        contextMenu.addItem("Профиль", e -> {
            Dialog dialog = new Dialog();
            dialog.getElement().setAttribute("aria-label", "Add note");
            dialog.setHeaderTitle("Профиль");

            dialog.add(createDialogLayout());
            Button closeButton = new Button(new Icon("lumo", "cross"),
                    (eve) -> dialog.close());
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            dialog.getHeader().add(closeButton);
            dialog.setCloseOnOutsideClick(true);
            dialog.setCloseOnEsc(true);

            dialog.open();
        });
        contextMenu.addItem("Выйти", e -> {
            Dialog dialog = new Dialog();

            dialog.setHeaderTitle("Вы действительно хотите выйти ?");

            Button exit = new Button("Выйти", ev -> {
                dialog.close();
                Service2.getInstance().getSecurityService().logout();
            });
            exit.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                    ButtonVariant.LUMO_CONTRAST);
            exit.getStyle().set("margin-right", "auto");
            dialog.getFooter().add(exit);

            Button cancelButton = new Button("Остаться", ev -> dialog.close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            dialog.getFooter().add(cancelButton);

            dialog.open();

        });

        userDiv.add(text);


        if (user != null) {
            text.setText(user.getName());
            avatarImage.setName(user.getName());
        } else {
            text.setText("Дональд Дак");
            avatarImage.setName("УТ");
        }

        TextField searchField = new TextField();
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setPlaceholder("Поиск");


//        sidebar.add(userDiv, searchField, homeButton, projectsButton, calendarButton);
        sidebar.add(userDiv, searchField, projectsButton, calendarButton, requestButton);
        sidebar.addClassName("menu");


        VerticalLayout workLayout = new VerticalLayout();
        workLayout.setWidthFull();
        workLayout.addClassName("work-layout");

        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setWidthFull();
        filterLayout.addClassName("filter-layout");

        Div tasks = new Div();
        tasks.addClassName("tasks-layout");
        tasks.setVisible(false);
        tasks.setEnabled(false);

        workLayout.add(filterLayout);
        workLayout.add(tasks);

        //задачи
        if ("admin".equals(user.getUser().getRole().getRoleName())
                || "manager".equals(user.getUser().getRole().getRoleName())) {
            Button admin = createButtonWithIcon(VaadinIcon.GROUP, "Подчиненные");
            sidebar.add(admin);

            EmployersGridContent grid = new EmployersGridContent(user);
            grid.setWidthFull();
            grid.setHeightFull();
            grid.setItems(Service2.getInstance().getEmployerServiceImpl().getEmployersBySupervisor(user));

            Button add = new Button();
            add.setPrefixComponent(VaadinIcon.PLUS.create());
            add.setText("Добавить нового подчиненного");
            add.getStyle().set("left", "8px");
            add.getStyle().set("top", "4px");
            add.setVisible(false);
            add.setEnabled(false);
            add.addClickListener(e -> {
                EmployerDialog employerDialog = new EmployerDialog(user,
                        (yes, empl) -> {
                            Service2.getInstance().saveOrEditEmployer(empl);
                            grid.setItems(Service2.getInstance().getEmployerServiceImpl().getEmployersBySupervisor(user));
                        });
                employerDialog.open();
            });

            admin.addClickListener(e -> {
                filterLayout.removeAll();
                tasks.removeAll();
                tasks.setVisible(true);
                tasks.setEnabled(true);
                tasks.add(grid);

                add.setVisible(true);
                add.setEnabled(true);
                filterLayout.add(add);
            });


        }
        if ("admin".equals(user.getUser().getRole().getRoleName())) {
            UsersGridContent grid = new UsersGridContent();
            grid.setWidthFull();
            grid.setHeightFull();
            grid.setItems(Service2.getInstance().getEmployerServiceImpl().list());

            Button add = new Button();
            add.setPrefixComponent(VaadinIcon.PLUS.create());
            add.setText("Добавить нового пользователя");
            add.getStyle().set("left", "8px");
            add.getStyle().set("top", "4px");
            add.setVisible(false);
            add.setEnabled(false);
            add.addClickListener(e -> {
                Employer newEmployer = new Employer();

                ListDataProvider<Employer> dataProvider = (ListDataProvider<Employer>) grid.getDataProvider();
                Collection<Employer> currentItems = dataProvider.getItems();

                currentItems.add(newEmployer);
                dataProvider.refreshAll();
            });

            Button admin = createButtonWithIcon(VaadinIcon.USER_STAR, "Админ");
            admin.addClickListener(e -> {
                filterLayout.removeAll();
                tasks.removeAll();
                tasks.setVisible(true);
                tasks.setEnabled(true);
                tasks.add(grid);

                add.setVisible(true);
                add.setEnabled(true);
                filterLayout.add(add);
            });
            sidebar.add(admin);
        }


        projectsButton.addClickListener( e -> {
            filterLayout.removeAll();
            tasks.removeAll();

            TaskView taskView = new TaskView(user);
            tasks.add(taskView);
            tasks.setVisible(true);
            tasks.setEnabled(true);

            Button addFilter = new Button("Добавить фильтры");
            addFilter.setPrefixComponent(VaadinIcon.FORM.create());
            addFilter.getStyle().set("left", "8px");
            addFilter.getStyle().set("top", "4px");
            filterLayout.add(addFilter);

            addFilter.addClickListener(eve -> {
                FilterDialog filterDialog = new FilterDialog(
                        (yes, taskList)-> {
                            taskView.reloadTaskListByFilter(taskList);
                        });
                filterDialog.open();
            });

            Button crateTask = new Button("Создать задачу");
            crateTask.setPrefixComponent(VaadinIcon.FILE_ADD.create());
            crateTask.getStyle().set("left", "8px");
            crateTask.getStyle().set("top", "4px");
            crateTask.addClickListener(event -> {
                AddTaskDialog addTaskDialog = new AddTaskDialog(
                        (dialog, updatedTask) -> {
                            Service2.getInstance().getTaskServiceImpl().saveOrEditTask(updatedTask);
                            taskView.reloadTaskList();
                        });
                addTaskDialog.open();
            });

            filterLayout.add(crateTask);

            Button editTask = new Button("Изменить задачу");
            editTask.setPrefixComponent(VaadinIcon.EDIT.create());
            editTask.getStyle().set("left", "8px");
            editTask.getStyle().set("top", "4px");
            editTask.addClickListener(event -> {
                EditTaskDialog editTaskDialog = new EditTaskDialog(taskView.getRightTask(),
                        (dialog, updatedTask) -> {
                            Service2.getInstance().getTaskServiceImpl().saveOrEditTask(updatedTask);
                            taskView.reloadTaskList();
                        },
                        del -> {
                            Service2.getInstance().getTaskServiceImpl().deleteTask(taskView.getRightTask());
                            taskView.reloadTaskList();
                            taskView.reloadTaskLayoutDefault();
                        });
                editTaskDialog.open();
            });

            filterLayout.add(editTask);


        });
        projectsButton.click();

//        homeButton.addClickListener(e -> {
//
//        });

        calendarButton.addClickListener(e -> {
            filterLayout.removeAll();
            tasks.removeAll();

            CalendarView calendarView = new CalendarView(user);
            tasks.add(calendarView);
            tasks.setVisible(true);
            tasks.setEnabled(true);
        });

        requestButton.addClickListener(e -> {
            filterLayout.removeAll();
            tasks.removeAll();

            EmployerRequestAddView employerRequestAddView = new EmployerRequestAddView(user);
            tasks.add(employerRequestAddView);

            Button createRequest = new Button("Создать заявку");
            createRequest.setPrefixComponent(VaadinIcon.FILE_ADD.create());
            createRequest.getStyle().set("left", "8px");
            createRequest.getStyle().set("top", "4px");
            createRequest.addClickListener(event -> {
                EmployersRequests newEmployersRequests = new EmployersRequests();
                newEmployersRequests.setRequestingEmployer(user);

                ListDataProvider<EmployersRequests> dataProvider = (ListDataProvider<EmployersRequests>) employerRequestAddView.getDataProvider();
                Collection<EmployersRequests> currentItems = dataProvider.getItems();

                currentItems.add(newEmployersRequests);
                dataProvider.refreshAll();
            });
            filterLayout.add(createRequest);

        });


        HorizontalLayout body = new HorizontalLayout(sidebar, workLayout);
        body.setSizeFull();

        mainLayout.add(header, body);
        mainLayout.expand(body);

        add(mainLayout);
    }

    private void getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            user = Service2.getInstance().getEmployerServiceImpl().getEmployerByUser(
                    Service2.getInstance().getSecurityService().getUserByLogin(authentication.getName())//кринж
            );
        }
    }

    private Button createButtonWithIcon(VaadinIcon icon, String text) {
        Button button = new Button();

        Icon iconComponent = icon.create();
        iconComponent.getStyle().set("minWidth", "24px");

        Span textSpan = new Span(text);
        textSpan.getStyle().set("marginLeft", "10px");

        HorizontalLayout layout = new HorizontalLayout(iconComponent, textSpan);
        layout.setDefaultVerticalComponentAlignment(Alignment.START);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setMargin(false);
        layout.setAlignItems(Alignment.START);
        layout.setWidth("144px");
        layout.getStyle().set("font-size", "--lumo-font-size-s");

        button.setIcon(layout);
        button.setWidthFull();
        button.getStyle().set("background-color", "transparent");
        button.getStyle().set("border", "none");
        button.getStyle().set("box-shadow", "none");
        button.getStyle().set("justify-content", "left");
//        button.getStyle().set("overflow", "hidden");
//        button.getStyle().set("text-overflow", "ellipsis");

        return button;
    }

    private VerticalLayout createDialogLayout() {
        if (user != null) {
            TextField nameField = new TextField("Name", user.getName(),
                    "Full name");
            nameField.setReadOnly(true);
            nameField.getStyle().set("padding-top", "0");

            EmailField emailField = new EmailField("Email", user.getEmail());
            emailField.setPlaceholder("email@company.com");
            emailField.setReadOnly(true);


            VerticalLayout fieldLayout = new VerticalLayout(nameField, emailField);
            fieldLayout.setSpacing(false);
            fieldLayout.setPadding(false);
            fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
            fieldLayout.getStyle().set("width", "300px").set("max-width", "100%");

            return fieldLayout;
        }
        return new VerticalLayout();
    }

}

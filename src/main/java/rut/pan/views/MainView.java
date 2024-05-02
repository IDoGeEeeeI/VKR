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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import rut.pan.entity.UserDto;
import rut.pan.service2.Service2;

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

    private UserDto user = null;

    MainView() {

        getUser();

        getStyle().set("height", "100%");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addClassName("super-square");
        mainLayout.setSizeFull();


        HorizontalLayout header = new HorizontalLayout();

        VerticalLayout sidebar = new VerticalLayout();

        Button homeButton = createButtonWithIcon(VaadinIcon.HOME_O, "Главная");
        Button projectsButton = createButtonWithIcon(VaadinIcon.TASKS, "Задачи");
        Button calendarButton = createButtonWithIcon(VaadinIcon.CALENDAR, "Календарь");


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
        //todo для создания пользователей админом
//        contextMenu.addItem("", e -> {
//
//        });
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


        sidebar.add(userDiv, searchField, homeButton, projectsButton, calendarButton);
        sidebar.addClassName("menu");


        VerticalLayout workLayout = new VerticalLayout();
        workLayout.setWidthFull();
        workLayout.addClassName("work-layout");

        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setWidthFull();
        filterLayout.addClassName("filter-layout");
        //todo dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);

        Button addFilter = new Button();
        addFilter.setPrefixComponent(VaadinIcon.FORM.create());
        addFilter.setText("Добавить фильтры");
        addFilter.getStyle().set("left", "8px");
        addFilter.getStyle().set("top", "4px");
        filterLayout.add(addFilter);


        VerticalLayout tasks = new VerticalLayout();
        tasks.addClassName("tasks-layout");

        //блоки задач (авто генерируемые)







        workLayout.add(filterLayout);
        workLayout.add(tasks);

        //задачи
        projectsButton.addClickListener( e -> {
            //todo
        });


        //тестовая штука, просто для показа
        Div div = new Div();
        div.setVisible(false);

        projectsButton.addClickListener(e -> {
            if (!div.isVisible()) {
                div.add(Service2.getInstance().getTaskService().list().toString());
                div.setVisible(true);
            } else {
                div.removeAll();
                div.setVisible(false);
            }

        });

        homeButton.addClickListener(e -> {
            div.remove();
            div.setVisible(false);
        });


        HorizontalLayout body = new HorizontalLayout(sidebar, workLayout, div);
        body.setSizeFull();

        mainLayout.add(header, body);
        mainLayout.expand(body);

        add(mainLayout);
    }

    private void getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            user = Service2.getInstance().getSecurityService().getUserByLogin(authentication.getName());
        }
    }

    private Button createButtonWithIcon(VaadinIcon icon, String text) {
        Button button = new Button();

        Icon iconComponent = icon.create();
        iconComponent.getStyle().set("minWidth", "50px");

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

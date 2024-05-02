package rut.pan.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import rut.pan.service2.Service2;

@Slf4j
@PermitAll
@Route("")
class MainView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private Service2 service2;


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!service2.isUserLoggedIn()) {
            beforeEnterEvent.rerouteTo(LoginView.class);
        }
    }

    MainView() {

        getStyle().set("height", "100%");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addClassName("super-square");
        mainLayout.setSizeFull();


        HorizontalLayout header = new HorizontalLayout();

        VerticalLayout sidebar = new VerticalLayout();

        Button homeButton = new Button("Главная");
        Button projectsButton = new Button("Задачи");
        Button calendarButton = new Button("Календарь");

        homeButton.setWidthFull();
        projectsButton.setWidthFull();
        calendarButton.setWidthFull();

        homeButton.setPrefixComponent(VaadinIcon.HOME_O.create());
        projectsButton.setPrefixComponent(VaadinIcon.TASKS.create());
        calendarButton.setPrefixComponent(VaadinIcon.CALENDAR.create());


        HorizontalLayout userDiv = new HorizontalLayout();
        userDiv.addClassName("user");

        Avatar avatarImage = new Avatar("Admin");
        avatarImage.setImage("icons/defavatar.png");
        userDiv.add(avatarImage);
        Span text = new Span("Дмитрий Панкратов Игоревич");//todo
        text.addClassName("user-text");
        userDiv.add(text);

        TextField searchField = new TextField();
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setPlaceholder("Поиск");


        sidebar.add(userDiv, searchField ,homeButton, projectsButton, calendarButton);
        sidebar.addClassName("menu");


        VerticalLayout workLayout = new VerticalLayout();
        workLayout.setWidthFull();
        workLayout.addClassName("work-layout");

        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setWidthFull();
        filterLayout.addClassName("filter-layout");

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

        });


        //тестовая штука, просто для показа
        Div div = new Div();
        div.setVisible(false);

        projectsButton.addClickListener(e -> {
            if (!div.isVisible()) {
                div.add(Service2.getInstance().test());
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
}

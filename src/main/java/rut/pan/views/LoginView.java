package rut.pan.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import rut.pan.service2.Service2;

@Route("login")
@AnonymousAllowed
@PermitAll
public class LoginView extends Div {

    public LoginView() {
        getStyle().set("display", "flex").set("justify-content", "center")
                .set("padding", "var(--lumo-space-l)");


        LoginForm loginForm = new LoginForm();

        loginForm.getElement().setAttribute("no-autofocus", "");

        loginForm.setAction("login");

        loginForm.addLoginListener(e -> {
            boolean authenticated = Service2.getInstance().getSecurityService().isUserLoggedIn();
            if (authenticated) {
                UI.getCurrent().navigate("defaultPage");
            } else {
                Notification.show("Wrong credentials, please try again.");
            }
        });

        add(loginForm);
    }

}

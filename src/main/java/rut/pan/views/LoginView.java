package rut.pan.views;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

@Route("login")
@AnonymousAllowed
@PermitAll
public class LoginView extends Div {

    public LoginView() {
        getStyle().set("display", "flex").set("justify-content", "center")
                .set("padding", "var(--lumo-space-l)");


        LoginForm loginForm = new LoginForm();
        add(loginForm);

        loginForm.getElement().setAttribute("no-autofocus", "");

        loginForm.setAction("login");

    }

}

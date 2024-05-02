package rut.pan.views;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import rut.pan.service2.Service2;

@Route("login")
@AnonymousAllowed
public class LoginView extends Div {

    @Autowired
    private Service2 service2;

    public LoginView() {
        getStyle().set("display", "flex").set("justify-content", "center")
                .set("padding", "var(--lumo-space-l)");


        LoginForm loginForm = new LoginForm();
        add(loginForm);

        loginForm.getElement().setAttribute("no-autofocus", "");

        loginForm.setAction("login");

    }

}

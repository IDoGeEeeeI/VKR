package rut.pan;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Theme(value = "mytodo")
public class PanApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(PanApplication.class, args);
	}

}

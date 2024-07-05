package rut.pan;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

@Route("test")
@AnonymousAllowed
@PermitAll
public class TestView extends VerticalLayout {

    public TestView() {
        MultiSelectComboBox<String> comboBox = new MultiSelectComboBox<>();
        comboBox.setLabel("Select a value");
        comboBox.setItems("Option 1", "Option 2", "Option 3", "A very long option that does not fit in the combobox");

        // Установить кастомный стиль
        comboBox.getElement().getStyle().set("--vaadin-combo-box-overlay-width", "400px");
        comboBox.setId("filter-combobox");

        // Добавить кастомный рендерер, чтобы убедиться, что текст не обрезается
        comboBox.setRenderer(new TextRenderer<>(item -> {
            return "<div style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>" + item + "</div>";
        }));



        add(comboBox);
    }
}
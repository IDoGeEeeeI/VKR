package rut.pan.content.dialogs;

import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.function.Consumer;

/**
 * Диалоговое окно. Ок/Отмена.
 */
public class OkDialog extends Dialog {

    private final Span span = new Span();

    private final Button button = new Button("ОК");

    /**
     * Отображаемое сообщение.
     * @return Строка.
     */
    public String getMessage() {
        return span.getText();
    }

    /**
     * Отображаемое сообщение.
     * @param message Строка.
     */
    public void setMessage(String message) {
        span.setText(message);
    }

    /**
     * Инициализировать новое окно.
     * @param message отображаемое в окне сообщение.
     * @param consumer действие выполняемое при нажатии кнопки "Ок" или при закрытии окна.
     */
    public OkDialog(String message, Consumer<OkDialog> consumer) {
        super();

        VerticalLayout verticalLayout = new VerticalLayout();
        add(verticalLayout);
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(false);
        verticalLayout.setPadding(false);
        verticalLayout.setWidthFull();
        verticalLayout.setHeightFull();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        span.setWhiteSpace(HasText.WhiteSpace.PRE);
        setMessage(message);
        verticalLayout.add(span);

        button.addClickListener(buttonClickEvent -> {
            OkDialog.this.close();
            if (consumer != null) {
                consumer.accept(OkDialog.this);
            }
        });

        DialogFooter dialogFooter = getFooter();
        verticalLayout = new VerticalLayout(button);
        verticalLayout.setWidthFull();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        dialogFooter.add(verticalLayout);

        setModal(true);
        setDraggable(true);
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
    }

    /**
     * Установить текст на кнопку.
     * @param text текст.
     */
    public void setButtonText(String text) {
        button.setText(text);
    }

    /**
     * Инициализировать новое окно.
     * @param message отображаемое в окне сообщение.
     */
    public OkDialog(String message) {
        this(message, null);
    }

}

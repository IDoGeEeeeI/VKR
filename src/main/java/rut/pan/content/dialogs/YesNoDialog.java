package rut.pan.content.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.function.Consumer;

public class YesNoDialog extends Dialog {

    public YesNoDialog(Consumer<YesNoDialog> yes, Consumer<YesNoDialog> no) {
        super();
        VerticalLayout verticalLayout = new VerticalLayout();
        add(verticalLayout);
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(false);
        verticalLayout.setPadding(false);
        verticalLayout.setWidthFull();
        verticalLayout.setHeightFull();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setMargin(false);
        buttons.setPadding(true);
        buttons.setSpacing(true);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        verticalLayout.add(buttons);


        Button button = new Button("Да");
        button.addClickListener(e -> {
            yes.accept(YesNoDialog.this);
        });
        buttons.add(button);

        button = new Button("Отмена");
        button.addClickListener(buttonClickEvent -> {
            YesNoDialog.this.close();
            no.accept(YesNoDialog.this);
        });
        buttons.add(button);
    }

    public YesNoDialog(Consumer<YesNoDialog> yes) {
        this(yes, no -> {});
    }


}

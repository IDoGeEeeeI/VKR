package rut.pan.content.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import rut.pan.entity.Employer;
import rut.pan.entity.Task;
import rut.pan.service2.Service2;

import java.util.Set;
import java.util.function.Consumer;

public class AppointDialog extends Dialog {

    public AppointDialog(Task task, Consumer<AppointDialog>yes, Consumer<AppointDialog> no) {
        Grid<Employer> grid = new Grid<>(Employer.class, false);
        grid.setItems(Service2.getInstance().getEmployerServiceImpl().list());
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(Employer::getName).setHeader("Исполнитель");
        grid.select(task.getEmployer());//в вебе не отображается, но выбрано, так что Notification не нужен, но оставлю навсякий
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(grid);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setMargin(false);
        horizontalLayout.setPadding(true);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        add(horizontalLayout);


        Button button = new Button("Назначить");
        button.addClickListener(e -> {
            Set<Employer> selectedItems = grid.getSelectedItems();
            if (!selectedItems.isEmpty()) {
                AppointDialog.this.close();
                task.setEmployer(selectedItems.iterator().next());
                yes.accept(AppointDialog.this);
            } else {
                Notification.show("Выберите исполнителя!");
            }
        });
        horizontalLayout.add(button);



        button = new Button("Отмена");
        button.addClickListener(buttonClickEvent -> {
            AppointDialog.this.close();
            no.accept(AppointDialog.this);
        });
        horizontalLayout.add(button);

    }

    public AppointDialog(Task task, Consumer<AppointDialog> yes) {
        this(task, yes, no -> {});
    }
}

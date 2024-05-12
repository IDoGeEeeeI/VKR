package rut.pan.content.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import rut.pan.entity.Employer;
import rut.pan.service2.Service2;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EmployerDialog extends Dialog {

    public EmployerDialog(Employer supe, BiConsumer<EmployerDialog, Employer> yes, Consumer<EmployerDialog> no) {
        Grid<Employer> grid = new Grid<>(Employer.class, false);
        grid.setItems(Service2.getInstance().getEmployerService().getEmployersBySupervisorIsNot(supe));
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(Employer::getName).setHeader("Подчиненные");
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
                Employer employer = selectedItems.iterator().next();
                if (employer.getSupervisor() != null) {
                    YesNoDialog yesNoDialog = new YesNoDialog(
                            yess -> {
                                EmployerDialog.this.close();
                                employer.setSupervisor(supe);
                                yes.accept(EmployerDialog.this, employer);
                            }
                    );
                    yesNoDialog.setHeaderTitle("Внимание, работник уже имеет руководителя");
                    yesNoDialog.open();
                } else {
                    EmployerDialog.this.close();
                    employer.setSupervisor(supe);
                    yes.accept(EmployerDialog.this, employer);
                }
            } else {
                Notification.show("Выберите нового подчиненного!");
            }
        });
        horizontalLayout.add(button);



        button = new Button("Отмена");
        button.addClickListener(buttonClickEvent -> {
            EmployerDialog.this.close();
            no.accept(EmployerDialog.this);
        });
        horizontalLayout.add(button);
    }

    public EmployerDialog(Employer supe, BiConsumer<EmployerDialog, Employer> yes) {
        this(supe, yes, no -> {});
    }
}

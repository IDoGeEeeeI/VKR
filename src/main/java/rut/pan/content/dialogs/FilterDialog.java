package rut.pan.content.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import rut.pan.entity.Employer;
import rut.pan.entity.Task;
import rut.pan.service2.Service2;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FilterDialog extends Dialog {

    public FilterDialog(BiConsumer<FilterDialog, List<Task>> yes, Consumer<FilterDialog> no) {
        super();
        Grid<Employer> grid = new Grid<>(Employer.class, false);
        grid.setItems(Service2.getInstance().getEmployerServiceImpl().list());
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(Employer::getName).setHeader("Исполнитель");
        setCloseOnOutsideClick(false);
        setCloseOnEsc(false);
        add(grid);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setMargin(false);
        horizontalLayout.setPadding(true);
        horizontalLayout.setSpacing(true);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        add(horizontalLayout);


        Button button = new Button("Применить фильтр");
        button.addClickListener(e -> {
            FilterDialog.this.close();
            Set<Employer> selectedEmployers = grid.getSelectedItems();
            Employer[] employersArray = selectedEmployers.toArray(new Employer[selectedEmployers.size()]);
            yes.accept(FilterDialog.this, Service2.getInstance().getTaskServiceImpl().getTasksByEmployer(employersArray));
        });
        horizontalLayout.add(button);



        button = new Button("Отмена");
        button.addClickListener(buttonClickEvent -> {
            FilterDialog.this.close();
            no.accept(FilterDialog.this);
        });
        horizontalLayout.add(button);

    }

    public FilterDialog(BiConsumer<FilterDialog, List<Task>> yes) {
        this(yes, no -> {});
    }
}

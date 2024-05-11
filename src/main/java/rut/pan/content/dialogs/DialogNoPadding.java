package rut.pan.content.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

public class DialogNoPadding extends Div {

    public DialogNoPadding() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Фильтр задач: ");

//        dialog.add(createDialogContent(dialog));
//        dialog.getFooter().add(createFilterButton(dialog));

        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);

        Button button = new Button("Фильтр", e -> dialog.open());

        add(dialog, button);
    }

//    private static Grid<Person> createDialogContent(Dialog dialog) {
//        Grid<Person> grid = new Grid<>(Person.class, false);
//        grid.setItems(DataService.getPeople(50));
//        grid.setSelectionMode(Grid.SelectionMode.MULTI);
//        grid.addColumn(Person::getFullName).setHeader("Name");
//
//        grid.getStyle().set("width", "500px").set("max-width", "100%");
//
//        return grid;
//    }
//
//    private static Button createFilterButton(Dialog dialog) {
//        Button filterButton = new Button("Применить", e -> dialog.close());
//        filterButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//
//        return filterButton;
//    }

}
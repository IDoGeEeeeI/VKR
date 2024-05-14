package rut.pan.content.dialogs;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import rut.pan.entity.Employer;
import rut.pan.entity.EmployersRequests;
import rut.pan.entity.RequestStatus;
import rut.pan.service2.Service2;

public class EmployerRequestDialog extends Dialog {


    public EmployerRequestDialog(Employer employer, Employer supervisor) {
        super();
        Grid<EmployersRequests> grid = new Grid<>();
        grid.setWidthFull();
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.setItems(Service2.getInstance().getEmployersRequestService().getRequestByEmployerAndSupervisor(employer, supervisor));

        grid.addItemClickListener(event -> {
            Editor<EmployersRequests> editor = grid.getEditor();
            if (editor.isOpen()) {
                editor.save();
            } else {
                editor.editItem(event.getItem());
            }
        });

        grid.getEditor().addSaveListener(e -> {
            Service2.getInstance().getEmployersRequestService().saveOrEdit(e.getItem());

        });

        Binder<EmployersRequests> binder = new Binder<>(EmployersRequests.class);
        Editor<EmployersRequests> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        ComboBox<RequestStatus> status = new ComboBox<>();
        status.setItemLabelGenerator(RequestStatus::getDescriptionRu);
        status.setItems(Service2.getInstance().getEmployersRequestService().getStatus());

        grid.addColumn(createAvatarRenderer())
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);

        grid.addColumn(employersRequests -> employersRequests.getRequestingEmployer().getName())
                .setHeader("Имя подчиненного")
                .setResizable(true);
        
        grid.addColumn(EmployersRequests::getRequestName)
                .setHeader("Название")
                .setResizable(true);
        
        grid.addColumn(EmployersRequests::getRequestDescription)
                .setHeader("Описание")
                .setResizable(true);
        
        grid.addColumn(EmployersRequests::getStartDate)
                .setHeader("Дата начала")
                .setResizable(true);
        
        grid.addColumn(EmployersRequests::getEndDate)
                .setHeader("Дата конца")
                .setResizable(true);

        grid.addColumn(employersRequests -> employersRequests.getRequestStatus().getDescriptionRu())
                .setHeader("Статус")
                .setEditorComponent(status)
                .setResizable(true);
        binder.forField(status)
                .bind(EmployersRequests::getRequestStatus, EmployersRequests::setRequestStatus);




        setWidthFull();
        add(grid);
        setModal(true);
        setDraggable(true);
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);

        Button button = new Button("Закрыть");
        button.getElement().setAttribute("ButtonNo", true);
        button.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        button.addClickListener(buttonClickEvent -> {
            if (grid.getEditor().isOpen()) {
                grid.getEditor().save();
            }
            EmployerRequestDialog.this.close();
        });
        FlexLayout wrapper = new FlexLayout(grid, button);
        wrapper.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        wrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        wrapper.setAlignItems(FlexComponent.Alignment.END);
        add(wrapper);
    }

    private static Renderer<EmployersRequests> createAvatarRenderer() {
        return LitRenderer.<EmployersRequests> of(
                        "<vaadin-avatar name=\"${item.getName}\" alt=\"User avatar\"></vaadin-avatar>")
                .withProperty("getName", employersRequests -> employersRequests.getRequestingEmployer().getName());
    }
}

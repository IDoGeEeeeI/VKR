package rut.pan.content;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import rut.pan.entity.Employer;
import rut.pan.entity.Roles;
import rut.pan.service2.Service2;

//todo сделать общую гриду и потом и в задачах использовать
public class GridContent extends Grid<Employer> {

    public GridContent() {
        super();
        setWidthFull();
        setSelectionMode(SelectionMode.SINGLE);

        Binder<Employer> binder = new Binder<>(Employer.class);
        Editor<Employer> editor = this.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

//        addEditableColumns(binder);

        addColumn(new ComponentRenderer<>(employer -> {
            HorizontalLayout layout = new HorizontalLayout();

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            deleteButton.addClickListener(e -> this.remove(employer));

            Button editButton = new Button(new Icon(VaadinIcon.PENCIL));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
            editButton.addClickListener(e -> {
                editor.editItem(employer);
            });

            Button saveButton = new Button(new Icon(VaadinIcon.CHECK), e -> {
                editor.save();
            });
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            Button cancelButton = new Button(new Icon(VaadinIcon.CLOSE), e -> {
                editor.cancel();
            });
            cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            layout.add(editor.isOpen() ? new Component[]{saveButton, cancelButton} : new Component[]{editButton, deleteButton});

            return layout;
        })).setFooter("Всего записей: " + collEmp());

        getEditor().addSaveListener(e -> {
            Service2.getInstance().getEmployerService().editEmployer(e.getItem());
            getListDataView().refreshAll();
            getDataProvider().refreshAll();
        });

        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

//    private void addEditableColumns(Binder<Employer> binder) {
//        TextField nameField = new TextField();
//        ComboBox<Roles> rolesComboBox = new ComboBox<>();
//        TextField emailField = new TextField();
//        TextField captionField = new TextField();
//
//        this.addColumn(createAvatarRenderer())
//                .setAutoWidth(true)
//                .setFlexGrow(0)
//                .setResizable(true);
//        this.addColumn(Employer::getName)
//                .setHeader("Имя")
//                .setEditorComponent(nameField)
//                .setResizable(true);
//        binder.bind(nameField, Employer::getName, Employer::setName);
//
//        rolesComboBox.setItemLabelGenerator(Roles::getRoleName);
//        rolesComboBox.setItems(Service2.getInstance().getRolesService().list());
//        this.addColumn(employer -> employer.getRoles() == null ? "" : employer.getRoles().getRoleName())
//                .setHeader("Должность")
//                .setEditorComponent(rolesComboBox)
//                .setResizable(true)
//                .setSortable(true);
//        binder.bind(rolesComboBox, Employer::getRoles, Employer::setRoles);
//
//        this.addColumn(Employer::getEmail)
//                .setHeader("Email")
//                .setEditorComponent(emailField)
//                .setResizable(true);
//        binder.bind(emailField, Employer::getEmail, Employer::setEmail);
//
//        this.addColumn(Employer::getCaption)
//                .setHeader("Комментарий")
//                .setEditorComponent(captionField)
//                .setResizable(true);
//        binder.bind(captionField, Employer::getCaption, Employer::setCaption);
//
//    }

    public void addNewEmployer() {
        Employer newEmployer = new Employer();

    }

    private int collEmp() {
        return Service2.getInstance().getEmployerService().list().size();
    }

    private static Renderer<Employer> createAvatarRenderer() {
        return LitRenderer.<Employer> of(
                        "<vaadin-avatar name=\"${item.getName}\" alt=\"User avatar\"></vaadin-avatar>")
                .withProperty("getName", Employer::getName);
    }

    private void remove(Employer employer) {
        if (employer == null) {
            return;
        }
        Service2.getInstance().getEmployerService().remove(employer);
        this.getListDataView().refreshAll();

    }

}

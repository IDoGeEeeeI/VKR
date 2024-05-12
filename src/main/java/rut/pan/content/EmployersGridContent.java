package rut.pan.content;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import rut.pan.entity.Employer;
import rut.pan.entity.Roles;
import rut.pan.entity.UserDto;
import rut.pan.service2.Service2;

/**
 * Страница Подчиненные.
 */
public class EmployersGridContent extends Grid<Employer> {

    public EmployersGridContent() {
        super();
        setWidthFull();
        setSelectionMode(SelectionMode.SINGLE);

        Binder<Employer> binder = new Binder<>(Employer.class);
        Editor<Employer> editor = this.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField nameField = new TextField();
        ComboBox<Roles> rolesComboBox = new ComboBox<>();
        ComboBox<Employer> supervisor = new ComboBox<>();

        this.addColumn(createAvatarRenderer())
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);

        this.addColumn(Employer::getName)
                .setHeader("Имя")
                .setEditorComponent(nameField)
                .setResizable(true);

        rolesComboBox.setItemLabelGenerator(Roles::getRoleName);
        rolesComboBox.setItems(Service2.getInstance().getRolesService().list());
        this.addColumn(employer -> employer.getUser() == null
                        ? "" : employer.getUser().getRole() == null ? "" : employer.getUser().getRole().getRoleName())
                .setHeader("Должность")
                .setEditorComponent(rolesComboBox)
                .setResizable(true)
                .setSortable(true);
        binder.forField(rolesComboBox)
                .bind(employer -> {
                    UserDto user = employer.getUser();
                    return user != null ? user.getRole() : null;
                }, (employer, role) -> {
                    UserDto user = employer.getUser();
                    if (user == null) {
                        user = new UserDto();
                        employer.setUser(user);
                    }
                    user.setRole(role);
                });
        this.addColumn(employer -> employer.getSupervisor().getName())
                .setHeader("Руководитель")
                .setEditorComponent(supervisor)
                .setResizable(true);
        binder.forField(supervisor)
                .bind(Employer::getSupervisor, (employer, supe) -> {
                    Employer user = employer.getSupervisor();
                    user.setSupervisor(supe);
                });

        //todo кнопка для просмотра задач подчиненного
    }

    private static Renderer<Employer> createAvatarRenderer() {
        return LitRenderer.<Employer> of(
                        "<vaadin-avatar name=\"${item.getName}\" alt=\"User avatar\"></vaadin-avatar>")
                .withProperty("getName", Employer::getName);
    }


}

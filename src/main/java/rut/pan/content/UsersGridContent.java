package rut.pan.content;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.postgresql.util.PSQLException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import rut.pan.content.dialogs.OkDialog;
import rut.pan.entity.Employer;
import rut.pan.entity.Roles;
import rut.pan.entity.UserDto;
import rut.pan.service2.Service2;

import java.util.Collection;

/**
 * Страница администрирования.
 */
public class UsersGridContent extends Grid<Employer> {

    public UsersGridContent() {
        super();
        setWidthFull();
        setSelectionMode(SelectionMode.SINGLE);

        Binder<Employer> binder = new Binder<>(Employer.class);
        Editor<Employer> editor = this.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        addEditableColumns(binder);

        addColumn(new ComponentRenderer<>(employer -> {
            HorizontalLayout layout = new HorizontalLayout();

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            deleteButton.addClickListener(e -> {
                if (employer.getId() == null) {
                    ListDataProvider<Employer> dataProvider = (ListDataProvider<Employer>) this.getDataProvider();
                    Collection<Employer> currentItems = dataProvider.getItems();
                    currentItems.remove(employer);
                    dataProvider.refreshAll();
                } else {
                    this.remove(employer);
                }
                getListDataView().refreshAll();
            });

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
            UserDto user = e.getItem().getUser();
            if (user == null
                    || (user.getPassword().isEmpty() && user.getLogin().isEmpty() && user.getRole() == null)) {
                OkDialog okDialog = new OkDialog("Заполните обязательные поля!");
                okDialog.open();
            } else {
                try {
                    Service2.getInstance().saveOrEditEmployer(e.getItem());
                } catch (DataIntegrityViolationException ex) {
                    Throwable rootCause = NestedExceptionUtils.getRootCause(ex);
                    if (rootCause instanceof PSQLException sqlEx) {
                        if ("23505".equals(sqlEx.getSQLState())) { //нарушение уникального индекса
                            OkDialog okDialog = new OkDialog("Запись с таким логином уже существует. Пожалуйста, выберите другой логин.");
                            okDialog.open();
                            //fixme устал эту хню делать, не знаю как исправить, там баг, что после этого окна, не доступны кнопки отмена или сохранить
                            return;
                        } else {
                            OkDialog okDialog = new OkDialog("Ошибка при сохранении данных: " + ex.getMessage());
                            okDialog.open();
                            return;

                        }
                    }
                } catch (Exception ex) {
                    OkDialog okDialog = new OkDialog("Неизвестная ошибка: " + ex.getMessage());
                    okDialog.open();
                    return;
                }
            }
            getListDataView().refreshAll();
        });

        getDataProvider().addDataProviderListener(e -> {
            getColumns().get(getColumns().size() - 1).setFooter("Всего записей: " + collEmp());
        });

        addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    private void addEditableColumns(Binder<Employer> binder) {
        TextField nameField = new TextField();
        ComboBox<Roles> rolesComboBox = new ComboBox<>();
        TextField emailField = new TextField();
        TextField captionField = new TextField();
        TextField loginField = new TextField();
        TextField passwordField = new TextField();

        this.addColumn(createAvatarRenderer())
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);

        this.addColumn(employer -> employer.getUser() == null ? "" : employer.getUser().getLogin())
                .setHeader("Login")
                .setEditorComponent(loginField)
                .setResizable(true);
        binder.forField(loginField)
                .bind(employer -> {
                    UserDto user = employer.getUser();
                    return user != null ? user.getLogin() : null;
                }, (employer, login) -> {
                    UserDto user = employer.getUser();
                    if (user == null) {
                        user = new UserDto();
                        employer.setUser(user);
                    }
                    user.setLogin(login);
                });
        this.addColumn(employer -> employer.getUser() == null ? "" : employer.getUser().getPassword())
                .setHeader("Password")
                .setEditorComponent(passwordField)
                .setResizable(true);
        binder.forField(passwordField)
                .bind(employer -> {
                    UserDto user = employer.getUser();
                    return user != null ? user.getPassword() : null;
                }, (employer, password) -> {
                    UserDto user = employer.getUser();
                    if (user == null) {
                        user = new UserDto();
                        employer.setUser(user);
                    }
                    user.setPassword(password);
                });

        this.addColumn(Employer::getName)
                .setHeader("Имя")
                .setEditorComponent(nameField)
                .setResizable(true);
        binder.bind(nameField, Employer::getName, Employer::setName);

        rolesComboBox.setItemLabelGenerator(Roles::getRoleName);
        rolesComboBox.setItems(Service2.getInstance().getRolesServiceImpl().list());
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

        this.addColumn(Employer::getEmail)
                .setHeader("Email")
                .setEditorComponent(emailField)
                .setResizable(true);
        binder.bind(emailField, Employer::getEmail, Employer::setEmail);

        this.addColumn(Employer::getCaption)
                .setHeader("Комментарий")
                .setEditorComponent(captionField)
                .setResizable(true);
        binder.bind(captionField, Employer::getCaption, Employer::setCaption);

    }

    private int collEmp() {
        return Service2.getInstance().getEmployerServiceImpl().list().size();
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
        Service2.getInstance().deleteEmployer(employer);
        this.getListDataView().refreshAll();

    }

}

package rut.pan.content;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import rut.pan.entity.Employer;
import rut.pan.entity.EmployersRequests;
import rut.pan.entity.RequestStatus;
import rut.pan.service2.Service2;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class EmployerRequestAddView extends Grid<EmployersRequests>  {

    public EmployerRequestAddView(Employer employer) {
        super();
        setWidthFull();
        setHeightFull();
        setSelectionMode(SelectionMode.SINGLE);
        setItems(Service2.getInstance().getEmployersRequestService().getRequestByEmployer(employer));


        Binder<EmployersRequests> binder = new Binder<>(EmployersRequests.class);
        Editor<EmployersRequests> editor = getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        ComboBox<RequestStatus> status = new ComboBox<>();
        ComboBox<Employer> sup = new ComboBox<>();
        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        DatePicker start = new DatePicker();
        DatePicker end = new DatePicker();

        status.setItemLabelGenerator(RequestStatus::getDescriptionRu);
        status.setItems(Service2.getInstance().getEmployersRequestService().getStatus());

        sup.setItemLabelGenerator(Employer::getName);
        sup.setItems(employer.getSupervisor());

        this.addColumn(createAvatarRenderer())
                .setAutoWidth(true)
                .setFlexGrow(0)
                .setResizable(true);

        this.addColumn(employersRequests -> employersRequests.getRequestingEmployer().getName())
                .setHeader("Подчиненный")
                .setResizable(true);

        this.addColumn(employersRequests -> employersRequests.getRequestingEmployer().getName())
                .setHeader("Руководитель")
                .setResizable(true);

        
        this.addColumn(EmployersRequests::getRequestName)
                .setHeader("Название")
                .setEditorComponent(nameField)
                .setResizable(true);
        binder.forField(nameField).bind(EmployersRequests::getRequestName, EmployersRequests::setRequestName);
        
        this.addColumn(EmployersRequests::getRequestDescription)
                .setHeader("Описание")
                .setEditorComponent(descriptionField)
                .setResizable(true);
        binder.forField(descriptionField).bind(EmployersRequests::getRequestDescription, EmployersRequests::setRequestDescription);


        this.addColumn(EmployersRequests::getStartDate)
                .setHeader("Дата начала")
                .setEditorComponent(start)
                .setResizable(true);
        
        this.addColumn(EmployersRequests::getEndDate)
                .setHeader("Дата конца")
                .setEditorComponent(end)
                .setResizable(true);

        LocalDateToDateConverter converter = new LocalDateToDateConverter();


        binder.forField(start)
                .withConverter(converter)
                .bind(EmployersRequests::getStartDate, EmployersRequests::setStartDate);

        binder.forField(end)
                .withValidator(endDate -> start.getValue() == null || !endDate.isBefore(start.getValue()), "Дата конца должна быть позже начала!")
                .withConverter(converter)
                .bind(EmployersRequests::getEndDate, EmployersRequests::setEndDate);

        this.addColumn(employersRequests -> employersRequests.getRequestStatus() == null
                    ? null : employersRequests.getRequestStatus().getDescriptionRu())
                .setHeader("Статус")
                .setEditorComponent(status)
                .setResizable(true);
        binder.forField(status)
                .bind(EmployersRequests::getRequestStatus, EmployersRequests::setRequestStatus);

        addColumn(new ComponentRenderer<>(emp -> {
            HorizontalLayout layout = new HorizontalLayout();

            Button editButton = new Button(new Icon(VaadinIcon.PENCIL));
            editButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);
            editButton.addClickListener(e -> {
                editor.editItem(emp);
            });

            Button saveButton = new Button(new Icon(VaadinIcon.CHECK), e -> {
                editor.save();
            });
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            Button cancelButton = new Button(new Icon(VaadinIcon.CLOSE), e -> {
                editor.cancel();
            });
            cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            layout.add(editor.isOpen() ? new Component[]{saveButton, cancelButton} : new Component[]{editButton});

            return layout;
        }));

        this.getEditor().addSaveListener(e -> {
            if (!e.getItem().isEmpty()) {
                e.getItem().setRequestName(nameField.getValue());
                e.getItem().setRequestDescription(descriptionField.getValue());
                e.getItem().setSupervisor(sup.getValue());
                Service2.getInstance().getEmployersRequestService().saveOrEdit(e.getItem());
            }
            getListDataView().refreshAll();
            getEditor().closeEditor();
        });

        this.getEditor().addCancelListener(e -> {
            EmployersRequests item = e.getItem();
            if (item != null && item.isEmpty()) {
                if (getDataProvider() instanceof ListDataProvider) {
                    ListDataProvider<EmployersRequests> listDataProvider = (ListDataProvider<EmployersRequests>) getDataProvider();
                    Collection<EmployersRequests> items = listDataProvider.getItems();
                    items.remove(item);
                    listDataProvider.refreshAll();
                    getListDataView().refreshAll();
                }
            }
        });

        this.getEditor().addCloseListener(e -> {});
        
    }

    public static class LocalDateToDateConverter implements Converter<LocalDate, Date> {

        @Override
        public Result<Date> convertToModel(LocalDate localDate, ValueContext valueContext) {
            return localDate == null ? Result.ok(null) : Result.ok(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        @Override
        public LocalDate convertToPresentation(Date date, ValueContext valueContext) {
            return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    private static Renderer<EmployersRequests> createAvatarRenderer() {
        return LitRenderer.<EmployersRequests> of(
                        "<vaadin-avatar name=\"${item.getName}\" alt=\"User avatar\"></vaadin-avatar>")
                .withProperty("getName", employersRequests -> employersRequests.getRequestingEmployer().getName());
    }
}

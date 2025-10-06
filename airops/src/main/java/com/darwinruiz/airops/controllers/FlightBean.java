package com.darwinruiz.airops.controllers;

import com.darwinruiz.airops.models.Aircraft;
import com.darwinruiz.airops.models.Flight;
import com.darwinruiz.airops.models.Pilot;
import com.darwinruiz.airops.services.CatalogService;
import com.darwinruiz.airops.services.FlightService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class FlightBean implements Serializable {

    @Inject
    CatalogService catalog;

    @Inject
    FlightService flights;

    @Inject
    private Validator validator;

    private Flight flight;
    private ScheduleModel schedule;
    private boolean dialogVisible;
    private Flight selected;
    private boolean detailVisible;

    @PostConstruct
    public void init() {
        flight = new Flight();
        schedule = new DefaultScheduleModel();
        dialogVisible = false;
        reloadSchedule();
    }

    public void newFlight() {
        clearFacesMessages();
        this.flight = new Flight();
        this.dialogVisible = true;   // abrir popup
    }

    public void onEventSelect(SelectEvent<ScheduleEvent<?>> e) {
        Object data = e.getObject().getData();
        if (data instanceof Flight f) {
            this.selected = f;        // ya lo tienes completo
            this.detailVisible = true;
        }
    }

    public void editFlight() {
        try {
            if (selected != null && selected.getId() != null) {
                // Cargar los datos del vuelo seleccionado en el formulario
                this.flight = selected;
                this.dialogVisible = true;

                // Cerrar el diálogo de detalles (se abrirá el de edición)
                this.detailVisible = false;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error",
                            "No se puede editar el vuelo: " + e.getMessage()));
        }
    }

    public void delete() {
        try {
            if (selected != null && selected.getId() != null) {
                // Guardar información del vuelo para el mensaje
                String flightNumber = selected.getFlightNumber();

                // Eliminar el vuelo seleccionado - pasar el objeto completo
                flights.delete(selected);  // ← CAMBIADO: selected en lugar de selected.getId()

                // Recargar el calendario
                reloadSchedule();  // ← CORREGIDO: reloadSchedule() no reloadScheduled()

                // Cerrar el diálogo de detalles
                this.detailVisible = false;
                this.selected = null;

                // Mostrar mensaje de éxito
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Vuelo eliminado",
                                "El vuelo " + flightNumber + " ha sido eliminado exitosamente"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Error",
                                "No se ha seleccionado ningún vuelo para eliminar"));
            }
        } catch (Exception e) {
            // Manejar errores
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al eliminar",
                            "No se pudo eliminar el vuelo: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void save() {
        Set<ConstraintViolation<Flight>> violations = validator.validate(flight);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Flight> violation : violations) {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                String label = getFieldLabel(field);

                FacesContext.getCurrentInstance().addMessage("frmFlights:msgFlight",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                label + ": " + message, null));
            }

            // IMPORTANTE: Marcar que hubo errores de validación
            FacesContext.getCurrentInstance().validationFailed();
            return;
        }



        flights.save(flight);
        reloadSchedule();
        this.dialogVisible = false;
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Vuelo guardado", "Operación exitosa"));
        this.flight = new Flight();
    }

    public void reloadSchedule() {
        DefaultScheduleModel model = new DefaultScheduleModel();
        for (Flight f : flights.flights()) {
            DefaultScheduleEvent<Flight> ev = DefaultScheduleEvent.<Flight>builder()
                    .title(f.getFlightNumber() + " • " + f.getOriginIata() + "→" + f.getDestinationIata())
                    .startDate(f.getDeparture())
                    .endDate(f.getArrival())
                    .data(f)
                    .build();
            model.addEvent(ev);
        }
        this.schedule = model;
    }

    private void clearFacesMessages() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (ctx == null) return;
        for (Iterator<FacesMessage> it = ctx.getMessages(); it.hasNext(); ) {
            it.next();
            it.remove();
        }
    }

    private String getFieldLabel(String fieldName) {
        Map<String, String> labels = new HashMap<>();
        labels.put("flightNumber", "Número de vuelo");
        labels.put("originIata", "Origen (IATA)");
        labels.put("destinationIata", "Destino (IATA)");
        labels.put("departure", "Salida");
        labels.put("arrival", "Llegada");
        labels.put("passengers", "Pasajeros");
        labels.put("pilot", "Piloto");
        labels.put("aircraft", "Aeronave");
        labels.put("createdAt", "Fecha de creación");
        labels.put("updatedAt", "Última actualización");
        labels.put("active", "Activo");

        return labels.getOrDefault(fieldName, fieldName);
    }


    // getters/setters
    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public ScheduleModel getSchedule() {
        return schedule;
    }

    public boolean isDialogVisible() {
        return dialogVisible;
    }

    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }

    public List<Pilot> getPilots() {
        return catalog.pilots();
    }

    public List<Aircraft> getAircraft() {
        return catalog.aircraft();
    }

    public List<Flight> getFlights() {
        return flights.flights();
    }

    public Flight getSelected() {
        return selected;
    }

    public boolean isDetailVisible() {
        return detailVisible;
    }

    public void setDetailVisible(boolean b) {
        this.detailVisible = b;
    }

}

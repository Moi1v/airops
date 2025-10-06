package com.darwinruiz.airops.controllers;

import com.darwinruiz.airops.models.Aircraft;
import com.darwinruiz.airops.models.Pilot;
import com.darwinruiz.airops.services.CatalogService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class AircraftBean implements Serializable {

    @Inject
    CatalogService service;

    @Inject
    private Validator validator;

    private Aircraft selected;
    private boolean dialogVisible;

    @PostConstruct
    public void init() {
        selected = new Aircraft();
        dialogVisible = false;
    }

    public List<Aircraft> getList() {
        return service.aircraft();
    }

    public void newItem() {
        clearFacesMessages();
        selected = new Aircraft();
        dialogVisible = true;
    }

    public void edit(Aircraft a) {
        clearFacesMessages();
        this.selected = a;
        dialogVisible = true;
    }

    public void save() {
        Set<ConstraintViolation<Aircraft>> violations = validator.validate(selected);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Aircraft> violation : violations) {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                String label = getFieldLabel(field);

                FacesContext.getCurrentInstance().addMessage("frmAircraft:frmAircraft",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                label + ": " + message, null));
            }

            // IMPORTANTE: Marcar que hubo errores de validación
            FacesContext.getCurrentInstance().validationFailed();
            return;
        }


        service.save(selected);
        dialogVisible = false;
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Aeronave guardada", "Operación exitosa"));
        selected = new Aircraft();
    }

    public void delete(Aircraft a) {
        service.delete(a);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Aeronave eliminada", null));
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
        labels.put("tailNumber", "Matrícula (Tail Number)");
        labels.put("model", "Modelo");
        labels.put("seats", "Asientos");
        labels.put("maxTakeoffWeightTons", "Peso máximo de despegue (toneladas)");

        return labels.getOrDefault(fieldName, fieldName);
    }


    public Aircraft getSelected() {
        return selected;
    }

    public void setSelected(Aircraft selected) {
        this.selected = selected;
    }

    public boolean isDialogVisible() {
        return dialogVisible;
    }

    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }
}

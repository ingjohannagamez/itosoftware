package com.ito.controller;

import com.ito.prueba.entidad.Perfiles;
import com.ito.prueba.model.PerfilesFacade;
import controller.util.JsfUtil;
import java.io.Serializable;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @Copyrigth [2018] www.ito-software.com
 * @PerfilesController
 * @Descripcion Controlador encargado de administrar los perfiles de los usuarios
 * @author Johann Andres Agamez Ferres
 * @Fecha Creación: 20/10/2018
 * @Fecha ultima modificación: 21/10/2018
 */
@Named(value = "perfilesController")
@ViewScoped
public class PerfilesController extends AbstractController<Perfiles> implements Serializable {

    @EJB
    private PerfilesFacade ejbFacade;

    public PerfilesController() {
        super(Perfiles.class);
    }

    @PostConstruct
    private void init() {
        try {
            this.setItems(this.ejbFacade.findAll());
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error al cargar el listado");
        }
    }

    public void saveNew(ActionEvent event) {
        String msg = ResourceBundle.getBundle("/MyBundle").getString(this.getItemClass().getSimpleName() + "Created");
        persist(AbstractController.PersistAction.CREATE, msg);
    }
    
    public void save(ActionEvent event) {
        String msg = ResourceBundle.getBundle("/MyBundle").getString(this.getItemClass().getSimpleName() + "Updated");
        persist(AbstractController.PersistAction.UPDATE, msg);
    }
    
    public void delete(ActionEvent event) {
        String msg = ResourceBundle.getBundle("/MyBundle").getString(this.getItemClass().getSimpleName() + "Deleted");
        persist(AbstractController.PersistAction.DELETE, msg);
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (this.getSelected() != null) {
            try {
                if (null != persistAction) {
                    switch (persistAction) {
                        case CREATE:
                            ejbFacade.create(this.getSelected());
                            refreshItems();
                            break;
                        case UPDATE:
                            ejbFacade.edit(this.getSelected());
                            break;
                        case DELETE:
                            ejbFacade.remove(this.getSelected());
                            this.getItems().clear();
                            this.setItems(this.ejbFacade.findAll());
                            break;
                        default:
                            throw new Exception();
                    }
                    JsfUtil.addSuccessMessage(successMessage);
                }
            } catch (EJBException ex) {
                Throwable cause = JsfUtil.getRootCause(ex.getCause());
                if (cause != null) {
                    if (cause instanceof ConstraintViolationException) {
                        ConstraintViolationException excp = (ConstraintViolationException) cause;
                        for (ConstraintViolation s : excp.getConstraintViolations()) {
                            JsfUtil.addErrorMessage(s.getMessage());
                        }
                    } else {
                        String msg = cause.getLocalizedMessage();
                        if (msg.length() > 0) {
                            JsfUtil.addErrorMessage(msg);
                        } else {
                            JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/MyBundle").getString("PersistenceErrorOccured"));
            }
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ito.controller;

import com.ito.prueba.entidad.Mercancias;
import com.ito.prueba.model.MercanciasFacade;
import controller.util.JsfUtil;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author pipo0
 */
@Named(value = "mercanciasController")
@ViewScoped
public class MercanciasController extends AbstractController<Mercancias> implements Serializable {
    
    @EJB
    private MercanciasFacade ejbFacade;
    
    public MercanciasController() {
        super(Mercancias.class);
    }
    
    @PostConstruct
    private void init() {
        try {
            this.setItems(this.ejbFacade.findAll());
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error al cargar el listado");
        }
    }
    
}

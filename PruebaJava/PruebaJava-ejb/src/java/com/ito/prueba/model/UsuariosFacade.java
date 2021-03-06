/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ito.prueba.model;

import com.ito.prueba.entidad.Usuarios;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pipo0
 */
@Stateless
public class UsuariosFacade extends AbstractFacade<Usuarios> {

    @PersistenceContext(unitName = "PruebaJava-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuariosFacade() {
        super(Usuarios.class);
    }

    public Usuarios validarAcceso(String username, String password) throws Exception {
        return em.createQuery("SELECT u FROM Usuarios u WHERE u.usuario = :username AND u.contrasena = :password", Usuarios.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
    }

}

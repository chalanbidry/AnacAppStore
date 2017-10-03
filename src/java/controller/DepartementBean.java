/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import ejb.DepartementFacade;
import jpa.Departement;
import util.JsfUtil;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author MJLDH
 */
@Named(value = "departementBean")
@ViewScoped
public class DepartementBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
    @Inject
    private DepartementFacade departementFacade;
    private Departement selectedDepartement;
    private Departement newDepartement;
    private List<Departement> listeDepartements;

    /**
     * Creates a new instance of DepartementBean
     */
    public DepartementBean() {
        
    }

    @PostConstruct
    public void init() {
        listeDepartements = departementFacade.findAll();
        if(listeDepartements.size()!=0)
        {
        System.out.println("---->>>>"+listeDepartements.get(0).getCode());
        }
    }

    public DepartementFacade getDepartementFacade() {
        return departementFacade;
    }

    public void setDepartementFacade(DepartementFacade departementFacade) {
        this.departementFacade = departementFacade;
    }

    public Departement getSelectedDepartement() {
        return selectedDepartement;
    }

    public void setSelectedDepartement(Departement selectedDepartement) {
        this.selectedDepartement = selectedDepartement;
    }

    public List<Departement> getListeDepartements() {
        return listeDepartements;
    }

    public void setListeDepartements(List<Departement> listeDepartements) {
        this.listeDepartements = listeDepartements;
    }

    public Departement getNewDepartement() {
        if (newDepartement == null) {
            newDepartement = new Departement();
        }
        return newDepartement;
    }

    public void setNewDepartement(Departement newDepartement) {
        this.newDepartement = newDepartement;
    }

    public void passItem(Departement item) {
        this.selectedDepartement = item;
    }

    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle("util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            this.departementFacade.create(newDepartement);
            msg = bundle.getString("DepartementCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newDepartement = new Departement();
            this.listeDepartements = this.departementFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("DepartementCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;

        try {
            departementFacade.edit(selectedDepartement);
            this.listeDepartements = this.departementFacade.findAll();
            msg = bundle.getString("DepartementEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("DepartementEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            departementFacade.remove(selectedDepartement);
            msg = bundle.getString("DepartementDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeDepartements.remove(this.selectedDepartement);
            this.listeDepartements = this.departementFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("DepartementDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void reset() {
        newDepartement.reset();
    }
}

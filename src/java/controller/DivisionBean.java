/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import ejb.ServiceFacade;
import ejb.DivisionFacade;
import jpa.Division;
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
import jpa.Service;

/**
 *
 * @author MJLDH
 */
@Named(value = "divisionBean")
@ViewScoped
public class DivisionBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
    @Inject
    private DivisionFacade divisionFacade;
    @Inject
    private ServiceFacade ServiceFacade;
    private Division selectedDivision;
    private Division newDivision;
    private List<Division> listeDivisions;
    private List<Service> listService;

    /**
     * Creates a new instance of DivisionBean
     */
    public DivisionBean() {
        
    }

    @PostConstruct
    public void init() {
        listeDivisions = divisionFacade.findAll();
        if(listeDivisions.size()!=0)
        {
        System.out.println("---->>>>"+listeDivisions.get(0).getCode());
        }
        listService=ServiceFacade.findAll();
    }

    public DivisionFacade getDivisionFacade() {
        return divisionFacade;
    }

    public void setDivisionFacade(DivisionFacade divisionFacade) {
        this.divisionFacade = divisionFacade;
    }

    public Division getSelectedDivision() {
        return selectedDivision;
    }

    public void setSelectedDivision(Division selectedDivision) {
        this.selectedDivision = selectedDivision;
    }

    public List<Division> getListeDivisions() {
        return listeDivisions;
    }

    public void setListeDivisions(List<Division> listeDivisions) {
        this.listeDivisions = listeDivisions;
    }

    public Division getNewDivision() {
        if (newDivision == null) {
            newDivision = new Division();
        }
        return newDivision;
    }

    public void setNewDivision(Division newDivision) {
        this.newDivision = newDivision;
    }

    public void passItem(Division item) {
        this.selectedDivision = item;
    }

    public List<Service> getListService() {
        return listService;
    }

    public void setListService(List<Service> listService) {
        this.listService = listService;
    }

    
    
    
    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            this.divisionFacade.create(newDivision);
            msg = bundle.getString("DivisionCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newDivision = new Division();
            this.listeDivisions = this.divisionFacade.findAll();
        } catch (Exception e) {
            System.out.println("erreur est "+e);
            msg = bundle.getString("DivisionCreateErrorMsg");
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
            divisionFacade.edit(selectedDivision);
            this.listeDivisions = this.divisionFacade.findAll();
            msg = bundle.getString("DivisionEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("DivisionEditErrorMsg");
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
            divisionFacade.remove(selectedDivision);
            msg = bundle.getString("DivisionDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeDivisions.remove(this.selectedDivision);
            this.listeDivisions = this.divisionFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("DivisionDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    
    public List<Division> findListDivisionByService(Service service){
     return divisionFacade.getDivisionBySer(service);
    }

    public void reset() {
        newDivision.reset();
    }
}

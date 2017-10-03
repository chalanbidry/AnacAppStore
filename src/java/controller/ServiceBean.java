/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import ejb.DepartementFacade;
import ejb.ServiceFacade;
import jpa.Service;
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
import jpa.Departement;
import jpa.Utilisateur;

/**
 *
 * @author MJLDH
 */
@Named(value = "serviceBean")
@ViewScoped
public class ServiceBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
    @Inject
    private ServiceFacade serviceFacade;
    @Inject
    private DepartementFacade DepartementFacade;
    private Service selectedService;
    private Service newService;
    private List<Service> listeServices;
    private List<Departement> listDepartement;

    /**
     * Creates a new instance of ServiceBean
     */
    public ServiceBean() {
        
    }

    @PostConstruct
    public void init() {
        listeServices = serviceFacade.findAll();
        if(listeServices.size()!=0)
        {
        System.out.println("---->>>>"+listeServices.get(0).getCode());
        }
        listDepartement=DepartementFacade.findAll();
    }
    
    public List<Service> findListServiceForDBDAF(){
     return serviceFacade.findAll();
    }

    public ServiceFacade getServiceFacade() {
        return serviceFacade;
    }

    public void setServiceFacade(ServiceFacade serviceFacade) {
        this.serviceFacade = serviceFacade;
    }

    public Service getSelectedService() {
        return selectedService;
    }

    public void setSelectedService(Service selectedService) {
        this.selectedService = selectedService;
    }

    public List<Service> getListeServices() {
        return listeServices;
    }

    public void setListeServices(List<Service> listeServices) {
        this.listeServices = listeServices;
    }

    public Service getNewService() {
        if (newService == null) {
            newService = new Service();
        }
        return newService;
    }

    public void setNewService(Service newService) {
        this.newService = newService;
    }

    public void passItem(Service item) {
        this.selectedService = item;
    }

    public List<Departement> getListDepartement() {
        return listDepartement;
    }

    public void setListDepartement(List<Departement> listDepartement) {
        this.listDepartement = listDepartement;
    }

    
    
    
    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            this.serviceFacade.create(newService);
            msg = bundle.getString("ServiceCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newService = new Service();
            this.listeServices = this.serviceFacade.findAll();
        } catch (Exception e) {
            System.out.println("erreur est "+e);
            msg = bundle.getString("ServiceCreateErrorMsg");
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
            serviceFacade.edit(selectedService);
            this.listeServices = this.serviceFacade.findAll();
            msg = bundle.getString("ServiceEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("ServiceEditErrorMsg");
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
            serviceFacade.remove(selectedService);
            msg = bundle.getString("ServiceDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeServices.remove(this.selectedService);
            this.listeServices = this.serviceFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("ServiceDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    
    public List<Service> findServiceByDep(Utilisateur user){
    return serviceFacade.getServiceByDep(user.getFonction().getDepartementDirec());
    }

    public void reset() {
        newService.reset();
    }
}

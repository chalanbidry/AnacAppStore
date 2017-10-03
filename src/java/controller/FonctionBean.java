/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ejb.DivisionFacade;
import ejb.FonctionFacade;
import ejb.ServiceFacade;
import jpa.Fonction;
import util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import jpa.Departement;
import jpa.Division;
import jpa.Service;

/**
 *
 * @author MJLDH
 */
@Named(value = "fonctionBean")
@ViewScoped
public class FonctionBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

    @Inject
    private FonctionFacade fonctionFacade;
    @Inject
    private ServiceFacade serviceFacade;
    @Inject
    private DivisionFacade divisionFacade;
    private Fonction selectedFonction;
    private Fonction newFonction;
    private List<Fonction> listeFonctions;
    private List<Service> listeService;
    private List<Division> listeDivision;
    private Departement selectedDep;
    private boolean isDirecteur;
    private List<Fonction> listFunctionDirecteurAndDg;

    /**
     * Creates a new instance of FonctionBean
     */
    public FonctionBean() {

    }

    @PostConstruct
    public void init() {
        listeFonctions = fonctionFacade.findAll();
        listeService = serviceFacade.findAll();
        listeDivision = divisionFacade.findAll();
        if (listeFonctions.size() != 0) {
            System.out.println("---->>>>" + listeFonctions.get(0).getCode());
        }
        listFunctionDirecteurAndDg=new ArrayList<>();
        findListDirecteruAndDg();
    }

    public List<Fonction> getListFunctionDirecteurAndDg() {
        return listFunctionDirecteurAndDg;
    }

    public void setListFunctionDirecteurAndDg(List<Fonction> listFunctionDirecteurAndDg) {
        this.listFunctionDirecteurAndDg = listFunctionDirecteurAndDg;
    }
    
    

    public FonctionFacade getFonctionFacade() {
        return fonctionFacade;
    }

    public void setFonctionFacade(FonctionFacade fonctionFacade) {
        this.fonctionFacade = fonctionFacade;
    }

    public Fonction getSelectedFonction() {
        return selectedFonction;
    }

    public void setSelectedFonction(Fonction selectedFonction) {
        this.selectedFonction = selectedFonction;
    }

    public List<Fonction> getListeFonctions() {
        return listeFonctions;
    }

    public void setListeFonctions(List<Fonction> listeFonctions) {
        this.listeFonctions = listeFonctions;
    }

    public Fonction getNewFonction() {
        if (newFonction == null) {
            newFonction = new Fonction();
        }
        return newFonction;
    }

    public void setNewFonction(Fonction newFonction) {
        this.newFonction = newFonction;
    }

    public void passItem(Fonction item) {
        this.selectedFonction = item;
    }

    public List<Service> getListeService() {
        return listeService;
    }

    public void setListeService(List<Service> listeService) {
        this.listeService = listeService;
    }

    public Departement getSelectedDep() {
        if (selectedDep == null) {
            selectedDep = new Departement();
        }
        return selectedDep;
    }

    public void setSelectedDep(Departement selectedDep) {
        this.selectedDep = selectedDep;
    }

    public boolean isIsDirecteur() {
        return isDirecteur;
    }

    public void setIsDirecteur(boolean isDirecteur) {
        this.isDirecteur = isDirecteur;
    }

    public List<Division> getListeDivision() {
        return listeDivision;
    }

    public void setListeDivision(List<Division> listeDivision) {
        this.listeDivision = listeDivision;
    }

    public void doCreateFonctionDir(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            newFonction.setChefDivision(false);
            newFonction.setChefService(false);
            newFonction.setDirecteur(true);
            this.fonctionFacade.create(newFonction);
            msg = bundle.getString("FonctionCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newFonction = new Fonction();
            this.listeFonctions = this.fonctionFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("FonctionCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doCreateFonctionSer(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            newFonction.setChefDivision(false);
            newFonction.setChefService(true);
            newFonction.setDirecteur(false);
            newFonction.setDepartementDirec(selectedDep);
            this.fonctionFacade.create(newFonction);
            msg = bundle.getString("FonctionCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newFonction = new Fonction();
            this.listeFonctions = this.fonctionFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("FonctionCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doCreateFonctionDiv(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            newFonction.setChefDivision(true);
            newFonction.setChefService(false);
            newFonction.setDirecteur(false);
            this.fonctionFacade.create(newFonction);
            msg = bundle.getString("FonctionCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newFonction = new Fonction();
            this.listeFonctions = this.fonctionFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("FonctionCreateErrorMsg");
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
            fonctionFacade.edit(selectedFonction);
            this.listeFonctions = this.fonctionFacade.findAll();
            msg = bundle.getString("FonctionEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("FonctionEditErrorMsg");
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
            fonctionFacade.remove(selectedFonction);
            msg = bundle.getString("FonctionDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeFonctions.remove(this.selectedFonction);
            this.listeFonctions = this.fonctionFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("FonctionDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void setItIsDirecteur(String statut) {
        System.out.println("le boolean est " + statut);
        if (statut.equals("true")) {
            isDirecteur = true;
        } else {
            isDirecteur = false;
        }
    }

    public void findListServiceInDep() {
        listeService = serviceFacade.getServiceByDep(selectedDep);
    }
    
    public void findListDivisionInSer() {
        listeDivision = divisionFacade.getDivisionBySer(newFonction.getService());
    }

    public void reset() {
        newFonction.reset();
    }
    
    public void findListDirecteruAndDg(){
     for(Fonction fonction:listeFonctions){
         if(fonction.isDirecteur() || fonction.getCode().equals("DG")){
        listFunctionDirecteurAndDg.add(fonction);
         }
     }
    }
    
   
}

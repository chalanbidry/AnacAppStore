/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import ejb.PrestataireFacade;
import jpa.Prestataire;
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
@Named(value = "prestataireBean")
@ViewScoped
public class prestataireBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
    @Inject
    private PrestataireFacade prestataireFacade;
    private Prestataire selectedPrestataire;
    private Prestataire newPrestataire;
    private List<Prestataire> listePrestataires;

    /**
     * Creates a new instance of PrestataireBean
     */
    public prestataireBean() {
        
    }

    @PostConstruct
    public void init() {
        listePrestataires = prestataireFacade.findAll();
        if(listePrestataires.size()!=0)
        {
        System.out.println("---->>>>"+listePrestataires.get(0));
        }
    }

    public PrestataireFacade getPrestataireFacade() {
        return prestataireFacade;
    }

    public void setPrestataireFacade(PrestataireFacade prestataireFacade) {
        this.prestataireFacade = prestataireFacade;
    }

    public Prestataire getSelectedPrestataire() {
        return selectedPrestataire;
    }

    public void setSelectedPrestataire(Prestataire selectedPrestataire) {
        this.selectedPrestataire = selectedPrestataire;
    }

    public List<Prestataire> getListePrestataires() {
        return listePrestataires;
    }

    public void setListePrestataires(List<Prestataire> listePrestataires) {
        this.listePrestataires = listePrestataires;
    }

    public Prestataire getNewPrestataire() {
        if (newPrestataire == null) {
            newPrestataire = new Prestataire();
        }
        return newPrestataire;
    }

    public void setNewPrestataire(Prestataire newPrestataire) {
        this.newPrestataire = newPrestataire;
    }

    public void passItem(Prestataire item) {
        this.selectedPrestataire = item;
    }

    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle("util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            this.prestataireFacade.create(newPrestataire);
            msg = bundle.getString("PrestataireCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newPrestataire = new Prestataire();
            this.listePrestataires = this.prestataireFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("PrestataireCreateErrorMsg");
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
            prestataireFacade.edit(selectedPrestataire);
            this.listePrestataires = this.prestataireFacade.findAll();
            msg = bundle.getString("PrestataireEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("PrestataireEditErrorMsg");
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
            prestataireFacade.remove(selectedPrestataire);
            msg = bundle.getString("PrestataireDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listePrestataires.remove(this.selectedPrestataire);
            this.listePrestataires = this.prestataireFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("PrestataireDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void reset() {
        newPrestataire.reset();
    }
}

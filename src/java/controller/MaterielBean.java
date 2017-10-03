/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import ejb.CategorieFacade;
import ejb.MaterielFacade;
import jpa.Materiel;
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
import jpa.Categorie;

/**
 *
 * @author MJLDH
 */
@Named(value = "materielBean")
@ViewScoped
public class MaterielBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
    @Inject
    private MaterielFacade materielFacade;
    @Inject
    private CategorieFacade categorieFacade;
    private Materiel selectedMateriel;
    private Materiel newMateriel;
    private List<Materiel> listeMateriels;
    private Categorie selectedCategorie;

    /**
     * Creates a new instance of MaterielBean
     */
    public MaterielBean() {
        
    }

    @PostConstruct
    public void init() {
        listeMateriels = materielFacade.findAll();
        if(listeMateriels.size()!=0)
        {
        System.out.println("---->>>>"+listeMateriels.get(0));
        }
    }

    public MaterielFacade getMaterielFacade() {
        return materielFacade;
    }

    public void setMaterielFacade(MaterielFacade materielFacade) {
        this.materielFacade = materielFacade;
    }

    public Materiel getSelectedMateriel() {
        return selectedMateriel;
    }

    public void setSelectedMateriel(Materiel selectedMateriel) {
        this.selectedMateriel = selectedMateriel;
    }

    public List<Materiel> getListeMateriels() {
        return listeMateriels;
    }

    public void setListeMateriels(List<Materiel> listeMateriels) {
        this.listeMateriels = listeMateriels;
    }

    public Materiel getNewMateriel() {
        if (newMateriel == null) {
            newMateriel = new Materiel();
        }
        return newMateriel;
    }

    public void setNewMateriel(Materiel newMateriel) {
        this.newMateriel = newMateriel;
    }

    public void passItem(Materiel item) {
        this.selectedMateriel = item;
    }

    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
             newMateriel.setCategorie(selectedCategorie);
            this.materielFacade.create(newMateriel);
            msg = bundle.getString("MaterielCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newMateriel = new Materiel();
            this.listeMateriels = this.materielFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("MaterielCreateErrorMsg");
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
            materielFacade.edit(selectedMateriel);
            this.listeMateriels = this.materielFacade.findAll();
            msg = bundle.getString("MaterielEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("MaterielEditErrorMsg");
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
            materielFacade.remove(selectedMateriel);
            msg = bundle.getString("MaterielDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeMateriels.remove(this.selectedMateriel);
            this.listeMateriels = this.materielFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("MaterielDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void reset() {
        newMateriel.reset();
    }

    public Categorie getSelectedCategorie() {
        return selectedCategorie;
    }

    public void setSelectedCategorie(Categorie selectedCategorie) {
        this.selectedCategorie = selectedCategorie;
    }
    
    
    public void findListMaterielByCat(){
     listeMateriels=categorieFacade.findListMaterielForCat(selectedCategorie);
    }
}

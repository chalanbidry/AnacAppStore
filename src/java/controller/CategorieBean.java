package controller;


import ejb.CategorieFacade;
import jpa.Categorie;
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
@Named(value = "categorieBean")
@ViewScoped
public class CategorieBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
    @Inject
    private CategorieFacade categorieFacade;
    private Categorie selectedCategorie;
    private Categorie newCategorie;
    private List<Categorie> listeCategories;

    /**
     * Creates a new instance of CategorieBean
     */
    public CategorieBean() {
        
        
        
        
    }

    @PostConstruct
    public void init() {
        listeCategories = categorieFacade.findAll();
        if(listeCategories.size()!=0)
        {
        System.out.println("---->>>>"+listeCategories.get(0).getCode());
        }
    }

   
    public Categorie getSelectedCategorie() {
        return selectedCategorie;
    }

    public void setSelectedCategorie(Categorie selectedCategorie) {
        this.selectedCategorie = selectedCategorie;
    }

    public List<Categorie> getListeCategories() {
        return listeCategories;
    }

    public void setListeCategories(List<Categorie> listeCategories) {
        this.listeCategories = listeCategories;
    }

    public Categorie getNewCategorie() {
        if (newCategorie == null) {
            newCategorie = new Categorie();
        }
        return newCategorie;
    }

    public void setNewCategorie(Categorie newCategorie) {
        this.newCategorie = newCategorie;
    }

    public void passItem(Categorie item) {
        this.selectedCategorie = item;
    }

    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            this.categorieFacade.create(newCategorie);
            msg = bundle.getString("CategorieCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newCategorie = new Categorie();
            this.listeCategories = this.categorieFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("CategorieCreateErrorMsg");
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
            categorieFacade.edit(selectedCategorie);
            this.listeCategories = this.categorieFacade.findAll();
            msg = bundle.getString("CategorieEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("CategorieEditErrorMsg");
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
            categorieFacade.remove(selectedCategorie);
            msg = bundle.getString("CategorieDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeCategories.remove(this.selectedCategorie);
            this.listeCategories = this.categorieFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("CategorieDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    
   

    public void reset() {
        newCategorie.reset();
    }
}

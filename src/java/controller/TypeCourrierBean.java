/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import ejb.ConnexionFacade;
import ejb.ContextFacade;
import ejb.DocumentFacade;
import ejb.GedFacade2;
import ejb.TypeCourrierFacade;
import ged.DossierCmis;
import ged.ejb.GedFacade;
import jpa.TypeCourrier;
import util.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import jpa.CategorieCourrier;
import jpa.Ged;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;

/**
 *
 * @author MJLDH
 */
@Named(value = "typeCourrierBean")
@ViewScoped
public class TypeCourrierBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
    @Inject
    private TypeCourrierFacade typeCourrierFacade;
    private TypeCourrier selectedTypeCourrier;
    private TypeCourrier newTypeCourrier;
    private List<TypeCourrier> listeTypeCourriers;
     private GedFacade gedFacade;
    @Inject
   private  ContextFacade contextFacade;
    @Inject
   private DocumentFacade documentFacade;
    @Inject
    private GedFacade2 gedFacade2;
    @Inject
   private  ConnexionFacade connexionFacade;
     @Inject
    private ConnexionBean connexionBean;
private String cheminSuivi;
    /**
     * Creates a new instance of TypeCourrierBean
     */
    public TypeCourrierBean() {
        
    }

    @PostConstruct
    public void init() {
         cheminSuivi = contextFacade.getALFRESCO_REPOSITORY_IGECOUR();
        listeTypeCourriers = typeCourrierFacade.findAll();
        if(listeTypeCourriers.size()!=0)
        {
        System.out.println("---->>>>"+listeTypeCourriers.get(0).getId());
        }
    }

   
    public TypeCourrier getSelectedTypeCourrier() {
        return selectedTypeCourrier;
    }

    public void setSelectedTypeCourrier(TypeCourrier selectedTypeCourrier) {
        this.selectedTypeCourrier = selectedTypeCourrier;
    }

    public List<TypeCourrier> getListeTypeCourriers() {
        return listeTypeCourriers;
    }

    public void setListeTypeCourriers(List<TypeCourrier> listeTypeCourriers) {
        this.listeTypeCourriers = listeTypeCourriers;
    }

    public TypeCourrier getNewTypeCourrier() {
        if (newTypeCourrier == null) {
            newTypeCourrier = new TypeCourrier();
        }
        return newTypeCourrier;
    }

    public void setNewTypeCourrier(TypeCourrier newTypeCourrier) {
        this.newTypeCourrier = newTypeCourrier;
    }

    public void passItem(TypeCourrier item) {
        this.selectedTypeCourrier = item;
    }

    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            newTypeCourrier.setGed(creerDossier(newTypeCourrier));
            this.typeCourrierFacade.create(newTypeCourrier);
            msg = bundle.getString("TypeCourrierCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newTypeCourrier = new TypeCourrier();
            this.listeTypeCourriers = this.typeCourrierFacade.findAll();
        } catch (Exception e) {
            System.out.println(" L'erreru est "+e);
            msg = bundle.getString("TypeCourrierCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    
    
    public void doCreateTypeCourrierArrive(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            newTypeCourrier.setCategorieCourrier(CategorieCourrier.CourrierArrive);
            newTypeCourrier.setGed(creerDossier(newTypeCourrier));
            this.typeCourrierFacade.create(newTypeCourrier);
            msg = bundle.getString("TypeCourrierCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newTypeCourrier = new TypeCourrier();
            this.listeTypeCourriers = this.typeCourrierFacade.findAll();
        } catch (Exception e) {
            System.out.println(" L'erreru est "+e);
            msg = bundle.getString("TypeCourrierCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    
    
    
    public Ged creerDossier(TypeCourrier typeCourrier) {
        gedFacade = connexionFacade.initGed(connexionBean.getCurrentUser());
        Ged ged;

//        String name = entity.getNumeroRP().replaceAll("/", "-");
        String name = typeCourrier.getLibelle();
        System.out.println("nom dossier issu du split pour etre name ds ged " + name);
        DossierCmis dossierCmis;

        System.out.println("description du dossier ");
        CmisObject obj;

//        CmisObject obj;
//
        System.out.println("contextBean.getALFRESCO_REPOSITORY() " + contextFacade.getALFRESCO_REPOSITORY_IGECOUR());
//        System.out.println("annee.getValeur() " + annee.getValeur());
        System.out.println("name" + name);

        obj = gedFacade.getCmisObjectByPath(cheminSuivi + "/" +typeCourrier.getCategorieCourrier().getLabel()+"/"+ name);
        System.out.println(" le parent est alors  " + cheminSuivi + "/" +typeCourrier.getCategorieCourrier().getLabel()+"/"+ name);
        if (obj == null) {
            dossierCmis = gedFacade.creerDossier(cheminSuivi + "/" +typeCourrier.getCategorieCourrier().getLabel(),name, "nouveau dossier" + name + " créé en ce jour le " + new Date());
        } else {
            dossierCmis = new DossierCmis((Folder) obj);
        }

        ged = new Ged(dossierCmis.getId(), name, dossierCmis.getId());
        gedFacade2.create(ged);

        return ged;
    }

    public void doEdit(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;

        try {
            typeCourrierFacade.edit(selectedTypeCourrier);
            this.listeTypeCourriers = this.typeCourrierFacade.findAll();
            msg = bundle.getString("TypeCourrierEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("TypeCourrierEditErrorMsg");
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
            typeCourrierFacade.remove(selectedTypeCourrier);
            msg = bundle.getString("TypeCourrierDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeTypeCourriers.remove(this.selectedTypeCourrier);
            this.listeTypeCourriers = this.typeCourrierFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("TypeCourrierDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    
   

    public void reset() {
        newTypeCourrier.reset();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import jpa.CategorieCourrier;


/**
 *
 * @author MJLDH
 */
@Named(value = "categorieCourrierBean")
@ViewScoped
public class CategorieCourrierBean implements Serializable {
    private List<CategorieCourrier> listeCategorieCourrier;
private CategorieCourrier  categorieCourrierArrive;
    /**
     * Creates a new instance of SalleBean
     */
    public CategorieCourrierBean() {
    }

    @PostConstruct
    public void init() {
        listeCategorieCourrier = Arrays.asList(CategorieCourrier.values());
        categorieCourrierArrive=CategorieCourrier.CourrierArrive;
    }

    public CategorieCourrier getCategorieCourrierArrive() {
        return categorieCourrierArrive;
    }

    public void setCategorieCourrierArrive(CategorieCourrier categorieCourrierArrive) {
        this.categorieCourrierArrive = categorieCourrierArrive;
    }

    
    
    
    public List<CategorieCourrier> getListeCategorieCourrier() {
        return listeCategorieCourrier;
    }

    public void setlisteCategorieCourrier(List<CategorieCourrier> listeCategorieCourrier) {
        this.listeCategorieCourrier = listeCategorieCourrier;
    }
    
    
    
    public CategorieCourrier[] getValuesCategorieCourrier() {
        CategorieCourrier[] valuesCategorieCourrier = {CategorieCourrier.CourrierArrive, CategorieCourrier.CourrierDepart,CategorieCourrier.CourrierInterne};
        return valuesCategorieCourrier;
    }

   
}

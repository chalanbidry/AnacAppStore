/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import jpa.Nature;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;


/**
 *
 * @author MJLDH
 */
@Named(value = "sexeBean")
@ViewScoped
public class NatureBean implements Serializable {
    private List<Nature> listeNature;

    /**
     * Creates a new instance of SalleBean
     */
    public NatureBean() {
    }

    @PostConstruct
    public void init() {
        listeNature = Arrays.asList(Nature.values());
    }

    public List<Nature> getListeNature() {
        return listeNature;
    }

    public void setlisteNature(List<Nature> listeNature) {
        this.listeNature = listeNature;
    }
    
    
    
    public Nature[] valuesNature() {
        Nature[] valuesNature = {Nature.CHANGEMENT_PIECE, Nature.REMPLACEMENT_MATERIEL,Nature.REPARATION_MATERIEL};
        return valuesNature;
    }

   
}

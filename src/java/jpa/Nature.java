/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

/**
 *
 * @author SI-MJLDH
 */
public enum Nature {

    CHANGEMENT_PIECE("changement d'une pièce"),
    REMPLACEMENT_MATERIEL("Remplacement du matériel"),
    REPARATION_MATERIEL("Réparation du matériel");
    private final String label;

    private Nature(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label; //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getLabel(){
        return this.label;
    }
    
}

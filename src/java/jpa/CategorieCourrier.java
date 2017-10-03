/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;


public enum CategorieCourrier {

    CourrierDepart("Courrier départ"),
    CourrierArrive("Courrier arrivé"),
    CourrierInterne("Courrier Interne"),
    CourrierDepartInterne("Courrier départ & interne");
    private final String label;

    private CategorieCourrier(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label; 
    }
    
    public String getLabel(){
        return this.label;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

import jpa.Element;

/**
 *
 * @author CHABI Emmanel Landry
 */
public class BonCommandeElement {
    
    public BonCommandeElement(){
    }
    
       private int qte;
    private long PrixUnit;
    private long montant;
    private Element element;

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public long getPrixUnit() {
        return PrixUnit;
    }

    public void setPrixUnit(long PrixUnit) {
        this.PrixUnit = PrixUnit;
    }

    public long getMontant() {
        return montant;
    }

    public void setMontant(long montant) {
        this.montant = montant;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
    
}

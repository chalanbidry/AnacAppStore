package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Exercice implements Serializable {

 
  
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Id
    private String Libelle;
    private boolean courant;
  
    @Version
    private Timestamp version;
  
    


    public Exercice() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
    }

    public boolean isCourant() {
        return courant;
    }

    public void setCourant(boolean courant) {
        this.courant = courant;
    }


    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }


    public String getLibelle() {
        return Libelle;
    }

    public void setLibelle(String Libelle) {
        this.Libelle = Libelle;
    }

  

   

    

    public Timestamp getVersion() {
        return version;
    }

    public void setVersion(Timestamp version) {
        this.version = version;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (Libelle != null ? Libelle.hashCode(): 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Exercice)) {
            return false;
        }
        Exercice other = (Exercice) object;
        if ((this.Libelle == null && other.Libelle != null) || (this.Libelle != null && !this.Libelle.equals(other.Libelle))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return this.libelle;
//    }
    
     
    public void reset() {
        this.Libelle = "";
        this.Libelle = "";
    }

    
//    public String getReferencePV() {
//        return referencePV;
//    }
//
//    public void setReferencePV(String referencePV) {
//        this.referencePV = referencePV;
//    }
//
//    public String getOrientation() {
//        return orientation;
//    }
//
//    public void setOrientation(String orientation) {
//        this.orientation = orientation;
//    }
}

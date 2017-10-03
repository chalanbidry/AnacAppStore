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
public class Prestataire implements Serializable {

    @Id
    private String IFU;
  
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String Nom;
  
    @Version
    private Timestamp version;
  
    


    public Prestataire() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
    }


    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

  

   

    

    public Timestamp getVersion() {
        return version;
    }

    public void setVersion(Timestamp version) {
        this.version = version;
    }

    public String getIFU() {
        return IFU;
    }

    public void setIFU(String IFU) {
        this.IFU = IFU;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (IFU != null ? IFU.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prestataire)) {
            return false;
        }
        Prestataire other = (Prestataire) object;
        if ((this.IFU == null && other.IFU != null) || (this.IFU != null && !this.IFU.equals(other.IFU))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return this.libelle;
//    }
    
     
    public void reset() {
        this.IFU = "";
        this.Nom = "";
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

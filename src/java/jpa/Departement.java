package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Departement implements Serializable {

    @Id
    private String Code;
  
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String Libelle;
    private String actualLastNumCourr;
    @ManyToOne
    private Departement departementParent;
    @ManyToOne
    private Utilisateur secretaireDeDirection;
  
    @Version
    private Timestamp version;
  
    @PrePersist
    public void initDateCreation() {
        dateCreation = new Date();
    }


    public Departement() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
    }

//    @OneToMany
//    private Collection<Partie> parties;

    public String getActualLastNumCourr() {
        return actualLastNumCourr;
    }

    public Utilisateur getSecretaireDeDirection() {
        return secretaireDeDirection;
    }

    public void setSecretaireDeDirection(Utilisateur secretaireDeDirection) {
        this.secretaireDeDirection = secretaireDeDirection;
    }
    
    

    public void setActualLastNumCourr(String actualLastNumCourr) {
        this.actualLastNumCourr = actualLastNumCourr;
    }

    public Departement getDepartementParent() {
        return departementParent;
    }

    public void setDepartementParent(Departement departementParent) {
        this.departementParent = departementParent;
    }

   
    
  

    

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
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
        hash += (Code != null ? Code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departement)) {
            return false;
        }
        Departement other = (Departement) object;
        if ((this.Code == null && other.Code != null) || (this.Code != null && !this.Code.equals(other.Code))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return this.libelle;
//    }
    
     
    public void reset() {
        this.Code = "";
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

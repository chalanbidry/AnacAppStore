package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Fonction implements Serializable {

    @Id
    private String Code;
  
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String Libelle;
    @ManyToOne
    private Service service;
    @ManyToOne
    private Division division;
    @OneToOne
    private Departement departementDirec;
    @Version
    private Timestamp version;
    private boolean Directeur;
    private boolean ChefService;
    private boolean ChefDivision;
  
    


    public Fonction() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
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

    public boolean isDirecteur() {
        return Directeur;
    }

    public void setDirecteur(boolean Directeur) {
        this.Directeur = Directeur;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public boolean isChefService() {
        return ChefService;
    }

    public void setChefService(boolean ChefService) {
        this.ChefService = ChefService;
    }

    public boolean isChefDivision() {
        return ChefDivision;
    }

    public void setChefDivision(boolean ChefDivision) {
        this.ChefDivision = ChefDivision;
    }

    public Departement getDepartementDirec() {
        return departementDirec;
    }

    public void setDepartementDirec(Departement departementDirec) {
        this.departementDirec = departementDirec;
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
        if (!(object instanceof Fonction)) {
            return false;
        }
        Fonction other = (Fonction) object;
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

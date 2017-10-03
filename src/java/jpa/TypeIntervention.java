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
public class TypeIntervention implements Serializable {

    @Id
    private String id;
  
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String libelle;
  
    @Version
    private Timestamp version;
  
    


    public TypeIntervention() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
    }

//    @OneToMany
//    private Collection<Partie> parties;

   
    
  

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypeIntervention)) {
            return false;
        }
        TypeIntervention other = (TypeIntervention) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public void reset() {
        this.id = "";
        this.libelle = "";
    }

}

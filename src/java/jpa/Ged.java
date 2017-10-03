package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
//import jpa.dossier.Annee;
//import jpa.dossier.Document;
//import jpa.dossier.Dossier;
//import jpa.juridiction.Juridiction;

@Entity
public class Ged implements Serializable {

    @Id
    private String refGed;
    private String name;
    private String currentGedRef;
    
    
    @Version
    private Timestamp version;    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
//    @OneToMany(mappedBy = "ged")
//    private List<Juridiction> juridictions;
//    @OneToMany(mappedBy = "ged")
//    private List<Dossier> dossiers;
//    @OneToMany(mappedBy = "ged")
//    private List<Annee> annees;
//    @OneToMany(mappedBy = "ged")
//    private List<Document> documents;

    public Ged() {
    }
    
    public Timestamp getVersion() {
        return version;
    }
    
    @PrePersist
    public void initDateCreation()
    {
        dateCreation = new Date();
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public Ged(String ref, String name, String currentGedRef) {
        System.out.println(" Ged debut ");
        this.refGed = ref;
        this.name = name;
        this.currentGedRef = currentGedRef;
        System.out.println(" Ged fin ");
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (refGed != null ? refGed.hashCode() : 0);
        return hash;
    }

    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ged)) {
            return false;
        }
        Ged other = (Ged) object;
        return !((this.refGed == null && other.refGed != null) || (this.refGed != null && !this.refGed.equals(other.refGed)));
    }

    public String getRefGed() {
        return refGed;
    }

    public void setRefGed(String refGed) {
        this.refGed = refGed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public List<Juridiction> getJuridictions() {
//        return juridictions;
//    }
//
//    public void setJuridictions(List<Juridiction> juridictions) {
//        this.juridictions = juridictions;
//    }
//
//    public List<Dossier> getDossiers() {
//        return dossiers;
//    }
//
//    public void setDossiers(List<Dossier> dossiers) {
//        this.dossiers = dossiers;
//    }

    public String getCurrentGedRef() {
        return currentGedRef;
    }

    public void setCurrentGedRef(String currentGedRef) {
        this.currentGedRef = currentGedRef;
    }

//    public List<Annee> getAnnees() {
//        return annees;
//    }
//
//    public void setAnnees(List<Annee> annees) {
//        this.annees = annees;
//    }
//
//    public List<Document> getDocuments() {
//        return documents;
//    }
//
//    public void setDocuments(List<Document> documents) {
//        this.documents = documents;
//    }
}
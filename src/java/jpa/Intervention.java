package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Intervention implements Serializable {

    @Id
    private String Id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Temporal(TemporalType.DATE)
    private Date dateDepart;
    @Temporal(TemporalType.DATE)
    private Date dateArrive;
    private String Observation;
    @ManyToOne
    private TypeIntervention typeIntervention;
    @ManyToOne
    private Prestataire prestataire;
    @Version
    private Timestamp version;

    public Intervention() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Date getDateArrive() {
        return dateArrive;
    }

    public void setDateArrive(Date dateArrive) {
        this.dateArrive = dateArrive;
    }

    public String getObservation() {
        return Observation;
    }

    public void setObservation(String Observation) {
        this.Observation = Observation;
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
        hash += (Id != null ? Id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Intervention)) {
            return false;
        }
        Intervention other = (Intervention) object;
        if ((this.Id == null && other.Id != null) || (this.Id != null && !this.Id.equals(other.Id))) {
            return false;
        }
        return true;
    }


    public void reset() {
        this.Id = "";
        this.Observation = "";
    }


}

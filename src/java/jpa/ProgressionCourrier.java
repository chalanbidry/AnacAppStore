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
public class ProgressionCourrier implements Serializable {

    @Id
    private String Code;
  
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @ManyToOne
    private Evenement evenement;
    @ManyToOne
    private Utilisateur userSend;
    @ManyToOne
    private Utilisateur userReceive;
    @ManyToOne
    private Courrier courrier;
    private String statut;
    @Version
    private Timestamp version;
    private String reference;
    private String observation;
    @ManyToOne
    private Departement directionConcernee;
    private Service serviceConcerne;
    private Division divisionConcernee;
    private String instructions;
    @Temporal(TemporalType.DATE)
    private Date dateLimite;
    


    public ProgressionCourrier() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
    }

    public Date getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(Date dateLimite) {
        this.dateLimite = dateLimite;
    }

    
    
    
    
    public Service getServiceConcerne() {
        return serviceConcerne;
    }

    public void setServiceConcerne(Service serviceConcerne) {
        this.serviceConcerne = serviceConcerne;
    }

    public Division getDivisionConcernee() {
        return divisionConcernee;
    }

    public void setDivisionConcernee(Division divisionConcernee) {
        this.divisionConcernee = divisionConcernee;
    }
    
    
    

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    

    public Departement getDirectionConcernee() {
        return directionConcernee;
    }

    public void setDirectionConcernee(Departement directionConcernee) {
        this.directionConcernee = directionConcernee;
    }
    
    

    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
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

    public Utilisateur getUserSend() {
        return userSend;
    }

    public void setUserSend(Utilisateur userSend) {
        this.userSend = userSend;
    }

    public Utilisateur getUserReceive() {
        return userReceive;
    }

    public void setUserReceive(Utilisateur userReceive) {
        this.userReceive = userReceive;
    }

    public Courrier getCourrier() {
        return courrier;
    }

    public void setCourrier(Courrier courrier) {
        this.courrier = courrier;
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
        if (!(object instanceof ProgressionCourrier)) {
            return false;
        }
        ProgressionCourrier other = (ProgressionCourrier) object;
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

package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity(name = "Courrier_Utilisateur")
public class CourrierSignataire implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name="courrier_code")
    private  Courrier courrier;
    @Id
    @ManyToOne
    @JoinColumn(name = "utilisateur_login")
    private Utilisateur utilisateur;
    
    private boolean signer;

    

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    public Courrier getCourrier() {
        return courrier;
    }

    public void setCourrier(Courrier courrier) {
        this.courrier = courrier;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public boolean isSigner() {
        return signer;
    }

    public void setSigner(boolean signer) {
        this.signer = signer;
    }

   

   
   
  
   
  
 
    @Version
    private Timestamp version;

    public CourrierSignataire() {
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.courrier);
        hash = 11 * hash + Objects.hashCode(this.utilisateur);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CourrierSignataire other = (CourrierSignataire) obj;
        if (!Objects.equals(this.courrier, other.courrier)) {
            return false;
        }
        if (!Objects.equals(this.utilisateur, other.utilisateur)) {
            return false;
        }
        return true;
    }

   


    public void reset() {
        System.out.println("annuler");;
      
    }


}

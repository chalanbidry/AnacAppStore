package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity(name = "Courrier_Utilisateur_visa")
@IdClass(Courrier_Utilisateur_ID.class)
public class Courrier_Utilisateur_visa implements Serializable {

    @Id
    private String courrierCode;
    @Id
    private String utilisateurLogin;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name ="utilisateurLogin" ,referencedColumnName = "login")
    private Utilisateur userVisa;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name="courrierCode", referencedColumnName = "code")
    private Courrier courrier;
    
    private boolean viser;
    private boolean typeCopieInfo;

    

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    public String getCourrierCode() {
        return courrierCode;
    }

    public void setCourrierCode(String courrierCode) {
        this.courrierCode = courrierCode;
    }

    public String getUtilisateurLogin() {
        return utilisateurLogin;
    }

    public void setUtilisateurLogin(String utilisateurLogin) {
        this.utilisateurLogin = utilisateurLogin;
    }

    public Utilisateur getUserVisa() {
        return userVisa;
    }

    public void setUserVisa(Utilisateur userVisa) {
        this.userVisa = userVisa;
    }

    public Courrier getCourrier() {
        return courrier;
    }

    public void setCourrier(Courrier courrier) {
        this.courrier = courrier;
    }

    public boolean isViser() {
        return viser;
    }

    public void setViser(boolean viser) {
        this.viser = viser;
    }

    public boolean isTypeCopieInfo() {
        return typeCopieInfo;
    }

    public void setTypeCopieInfo(boolean typeCopieInfo) {
        this.typeCopieInfo = typeCopieInfo;
    }

   
   
  
   
  
 
    @Version
    private Timestamp version;

    public Courrier_Utilisateur_visa() {
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
        int hash = 0;
        hash += (courrierCode != null ? courrierCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Courrier_Utilisateur_visa)) {
            return false;
        }
        Courrier_Utilisateur_visa other = (Courrier_Utilisateur_visa) object;
        if ((this.courrierCode == null && other.courrierCode != null) || (this.courrierCode != null && !this.courrierCode.equals(other.courrierCode))) {
            return false;
        }
        return true;
    }


    public void reset() {
        this.courrierCode = "";
      
    }


}

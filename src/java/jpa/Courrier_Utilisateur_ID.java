package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


public class Courrier_Utilisateur_ID {

   private String courrierCode;
   private String utilisateurLogin;

    public Courrier_Utilisateur_ID() {
    }



    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.courrierCode);
        hash = 29 * hash + Objects.hashCode(this.utilisateurLogin);
        return hash;
    }

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

    


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Courrier_Utilisateur_ID other = (Courrier_Utilisateur_ID) obj;
        if (!Objects.equals(this.courrierCode, other.courrierCode)) {
            return false;
        }
        if (!Objects.equals(this.utilisateurLogin, other.utilisateurLogin)) {
            return false;
        }
        return true;
    }

    


}

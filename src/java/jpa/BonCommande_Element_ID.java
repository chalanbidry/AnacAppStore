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


public class BonCommande_Element_ID {

   private String elementCode;
   private String bonCommandeCode;

    public BonCommande_Element_ID() {
    }



    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.elementCode);
        hash = 29 * hash + Objects.hashCode(this.bonCommandeCode);
        return hash;
    }

    public String getElementCode() {
        return elementCode;
    }

    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }

    public String getBonCommandeCode() {
        return bonCommandeCode;
    }

    public void setBonCommandeCode(String bonCommandeCode) {
        this.bonCommandeCode = bonCommandeCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BonCommande_Element_ID other = (BonCommande_Element_ID) obj;
        if (!Objects.equals(this.elementCode, other.elementCode)) {
            return false;
        }
        if (!Objects.equals(this.bonCommandeCode, other.bonCommandeCode)) {
            return false;
        }
        return true;
    }

    


}

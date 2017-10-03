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
public class Application implements Serializable {

    @Id
    private String Code;
  
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String appliName;
    @Version
    private Timestamp version;
   
  
    


    public Application() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
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

    public String getAppliName() {
        return appliName;
    }

    public void setAppliName(String appliName) {
        this.appliName = appliName;
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
        if (!(object instanceof Application)) {
            return false;
        }
        Application other = (Application) object;
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
        this.appliName = "";
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

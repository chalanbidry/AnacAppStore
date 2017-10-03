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

@Entity
@IdClass(BonCommande_Element_ID.class)
public class BonCommande_Element implements Serializable {

    @Id
    private String bonCommandeCode;
    @Id
    private String elementCode;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name ="bonCommandeCode" ,referencedColumnName = "code")
    private Boncommande bonCommande;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name="elementCode", referencedColumnName = "code")
    private Element element;
    
    private int qte;
    private long PrixUnit;
    private long montant;
    

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
   
    @Temporal(TemporalType.DATE)
    private Date date;
    private String libelle;
   
  
 
    @Version
    private Timestamp version;

    public BonCommande_Element() {
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getBonCommandeCode() {
        return bonCommandeCode;
    }

    public void setBonCommandeCode(String bonCommandeCode) {
        this.bonCommandeCode = bonCommandeCode;
    }

    public String getElementCode() {
        return elementCode;
    }

    public void setElementCode(String elementCode) {
        this.elementCode = elementCode;
    }

    public Boncommande getBonCommande() {
        return bonCommande;
    }

    public void setBonCommande(Boncommande bonCommande) {
        this.bonCommande = bonCommande;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public long getPrixUnit() {
        return PrixUnit;
    }

    public void setPrixUnit(long PrixUnit) {
        this.PrixUnit = PrixUnit;
    }

    public long getMontant() {
        return montant;
    }

    public void setMontant(long montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        hash += (bonCommandeCode != null ? bonCommandeCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BonCommande_Element)) {
            return false;
        }
        BonCommande_Element other = (BonCommande_Element) object;
        if ((this.bonCommandeCode == null && other.bonCommandeCode != null) || (this.bonCommandeCode != null && !this.bonCommandeCode.equals(other.bonCommandeCode))) {
            return false;
        }
        return true;
    }


    public void reset() {
        this.bonCommandeCode = "";
      
    }


}

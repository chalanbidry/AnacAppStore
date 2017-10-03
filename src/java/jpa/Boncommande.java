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

@Entity
public class Boncommande implements Serializable {

    @Id
    private String Code;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String objet;
    @Temporal(TemporalType.DATE)
    private Date dateDevisOrPF;
    @ManyToOne
    private LigneBudgetaire ligneBudgetaire;
   
    @OneToOne
    private Demande demande;
    private String beneficiaire;
    @OneToMany
    private List<BonCommande_Element> bonAndElement;
    private long TVA;
    private long NetApayerTTC;
    private long totalHT;
    private String TTCLettre;
    @ManyToOne
    private Exercice exercice;
    
  
    @ManyToOne
    private Utilisateur utilisateuurSend;
    @OneToMany
    private List<Utilisateur> utilisateursChecked;
    @Version
    private Timestamp version;

    public Boncommande() {
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

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public Date getDateDevisOrPF() {
        return dateDevisOrPF;
    }

    public void setDateDevisOrPF(Date dateDevisOrPF) {
        this.dateDevisOrPF = dateDevisOrPF;
    }

    public LigneBudgetaire getLigneBudgetaire() {
        return ligneBudgetaire;
    }

    public void setLigneBudgetaire(LigneBudgetaire ligneBudgetaire) {
        this.ligneBudgetaire = ligneBudgetaire;
    }

   

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public Utilisateur getUtilisateuurSend() {
        return utilisateuurSend;
    }

    public void setUtilisateuurSend(Utilisateur utilisateuurSend) {
        this.utilisateuurSend = utilisateuurSend;
    }

    public List<Utilisateur> getUtilisateursChecked() {
        return utilisateursChecked;
    }

    public void setUtilisateursChecked(List<Utilisateur> utilisateursChecked) {
        this.utilisateursChecked = utilisateursChecked;
    }

    public List<BonCommande_Element> getBonAndElement() {
        return bonAndElement;
    }

    public void setBonAndElement(List<BonCommande_Element> bonAndElement) {
        this.bonAndElement = bonAndElement;
    }

    public long getTVA() {
        return TVA;
    }

    public void setTVA(long TVA) {
        this.TVA = TVA;
    }

    public long getNetApayerTTC() {
        return NetApayerTTC;
    }

    public void setNetApayerTTC(long NetApayerTTC) {
        this.NetApayerTTC = NetApayerTTC;
    }

    public long getTotalHT() {
        return totalHT;
    }

    public void setTotalHT(long totalHT) {
        this.totalHT = totalHT;
    }

    public String getTTCLettre() {
        return TTCLettre;
    }

    public void setTTCLettre(String TTCLettre) {
        this.TTCLettre = TTCLettre;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
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
        if (!(object instanceof Boncommande)) {
            return false;
        }
        Boncommande other = (Boncommande) object;
        if ((this.Code == null && other.Code != null) || (this.Code != null && !this.Code.equals(other.Code))) {
            return false;
        }
        return true;
    }


    public void reset() {
        this.Code = "";
      
    }


}

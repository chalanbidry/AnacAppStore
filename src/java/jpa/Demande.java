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
public class Demande implements Serializable {

    @Id
    private String Code;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String objet;
    @Temporal(TemporalType.DATE)
    private Date date;
    private String fichier;
    @OneToOne
    private Exercice exercice;
    private String travauxARealiser;
    private String infrastructure;
    private String natureDesTravaux;
    @OneToOne
    private Intervention intervention;
    @OneToOne
    private Prestataire fournisseur;
    @ManyToOne
    private Utilisateur utilisateurSend;
     @ManyToOne
    private Materiel materiel;
    @OneToMany
    private List<Utilisateur> utilisateursChecked;
    @Version
    private Timestamp version;

   
    private Ged ged;
    public Demande() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public Intervention getIntervention() {
        return intervention;
    }

    public void setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }

    public Utilisateur getUtilisateurSend() {
        return utilisateurSend;
    }

    public void setUtilisateurSend(Utilisateur utilisateurSend) {
        this.utilisateurSend = utilisateurSend;
    }

    public List<Utilisateur> getUtilisateursChecked() {
        return utilisateursChecked;
    }

    public void setUtilisateursChecked(List<Utilisateur> utilisateursChecked) {
        this.utilisateursChecked = utilisateursChecked;
    }

    public String getTravauxARealiser() {
        return travauxARealiser;
    }

    public void setTravauxARealiser(String travauxARealiser) {
        this.travauxARealiser = travauxARealiser;
    }

    public String getInfrastructure() {
        return infrastructure;
    }

    public void setInfrastructure(String infrastructure) {
        this.infrastructure = infrastructure;
    }

    public String getNatureDesTravaux() {
        return natureDesTravaux;
    }

    public void setNatureDesTravaux(String natureDesTravaux) {
        this.natureDesTravaux = natureDesTravaux;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Prestataire getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Prestataire fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Ged getGed() {
        return ged;
    }

    public void setGed(Ged ged) {
        this.ged = ged;
    }

    public Materiel getMateriel() {
        return materiel;
    }

    public void setMateriel(Materiel materiel) {
        this.materiel = materiel;
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
        if (!(object instanceof Demande)) {
            return false;
        }
        Demande other = (Demande) object;
        if ((this.Code == null && other.Code != null) || (this.Code != null && !this.Code.equals(other.Code))) {
            return false;
        }
        return true;
    }


    public void reset() {
        this.Code = "";
      
    }


}

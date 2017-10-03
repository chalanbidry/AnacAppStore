package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Utilisateur implements Serializable {

    private String Matricule;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String nom;
    private String prenom;
    private String name;
    @OneToOne
    private Fonction fonction;
    @Version
    private Timestamp version;
    @Id
    private String login;
    private String password;
    @OneToMany
    private List<Demande> demandesChecked;
     @OneToMany
    private List<Application> listApplications;
       @OneToMany
    private List<Publication> listPublicationReceive;
    private String dn;
    private boolean admin;
    private boolean ChefService;
     private boolean Dg;
     private boolean SecretaireAdministratif;
     private boolean Directeur;
     private boolean secretaireDeDirection;
     private boolean secretaireParticulier;
    private String CheminSignature;
    @OneToMany(mappedBy = "utilisateur")
    private List<CourrierSignataire> courrierSignataires;

    public Utilisateur() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
    }

    @PrePersist
    public void initDateCreation() {
        dateCreation = new Date();
    }

    public boolean isSecretaireParticulier() {
        return secretaireParticulier;
    }

    public void setSecretaireParticulier(boolean secretaireParticulier) {
        this.secretaireParticulier = secretaireParticulier;
    }
    
    
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isSecretaireDeDirection() {
        return secretaireDeDirection;
    }

    public void setSecretaireDeDirection(boolean secretaireDeDirection) {
        this.secretaireDeDirection = secretaireDeDirection;
    }
    
    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Demande> getDemandesChecked() {
        return demandesChecked;
    }

    public void setDemandesChecked(List<Demande> demandesChecked) {
        this.demandesChecked = demandesChecked;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getMatricule() {
        return Matricule;
    }

    public void setMatricule(String Matricule) {
        this.Matricule = Matricule;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fonction getFonction() {
        return fonction;
    }

    public void setFonction(Fonction fonction) {
        this.fonction = fonction;
    }

    public Timestamp getVersion() {
        return version;
    }

    public void setVersion(Timestamp version) {
        this.version = version;
    }

    public boolean isChefService() {
        return ChefService;
    }

    public void setChefService(boolean ChefService) {
        this.ChefService = ChefService;
    }

    public String getCheminSignature() {
        return CheminSignature;
    }

    public void setCheminSignature(String CheminSignature) {
        this.CheminSignature = CheminSignature;
    }

    public boolean isDg() {
        return Dg;
    }

    public void setDg(boolean Dg) {
        this.Dg = Dg;
    }

    public boolean isSecretaireAdministratif() {
        return SecretaireAdministratif;
    }

    public void setSecretaireAdministratif(boolean SecretaireAdministratif) {
        this.SecretaireAdministratif = SecretaireAdministratif;
    }

    public boolean isDirecteur() {
        return Directeur;
    }

    public void setDirecteur(boolean Directeur) {
        this.Directeur = Directeur;
    }

    public List<Application> getListApplications() {
        return listApplications;
    }

    public void setListApplications(List<Application> listApplications) {
        this.listApplications = listApplications;
    }

    public List<Publication> getListPublicationReceive() {
        return listPublicationReceive;
    }

    public void setListPublicationReceive(List<Publication> listPublicationReceive) {
        this.listPublicationReceive = listPublicationReceive;
    }

    public List<CourrierSignataire> getCourrierSignataires() {
        return courrierSignataires;
    }

    public void setCourrierSignataires(List<CourrierSignataire> courrierSignataires) {
        this.courrierSignataires = courrierSignataires;
    }

    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (login != null ? login.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login))) {
            return false;
        }
        return true;
    }

    public void reset() {
        this.login = "";

    }

}

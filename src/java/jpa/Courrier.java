package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
public class Courrier implements Serializable {

    @Id
    private String Code;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Temporal(TemporalType.DATE)
    private Date dateCourrier;
    private String contenu;
    @ManyToOne
    private TypeCourrier typeCourrier;

    @OneToOne
    private Utilisateur initiateur;
    private String titre;
    private String Objet;
    private boolean pieceJointe;
    private List<String> destinataires;
    private String teldestinataire;
    private boolean withObjet;
    private boolean withTitre;
    private boolean withTelDesti;
    private String numCour;
    private String typeSignature;
    @ManyToOne
    private Courrier courrierInstiguateur;
    @ManyToOne
    private Exercice exercice;
    private String numCourrierArrive;
    @Temporal(TemporalType.DATE)
    private Date dateCorrespondance;
    @Temporal(TemporalType.DATE)
    private Date dateArrivee;
    private String Expediteur;

    @OneToMany
    private List<Courrier_Utilisateur_visa> courAndVisaUser;
    @OneToMany(mappedBy = "courrier", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<CourrierSignataire> courrierSignataires;
    private Ged ged;

    @Version
    private Timestamp version;

    public Courrier() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
    }

    public String getTypeSignature() {
        return typeSignature;
    }

    public void setTypeSignature(String typeSignature) {
        this.typeSignature = typeSignature;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public boolean isWithTelDesti() {
        return withTelDesti;
    }

    public void setWithTelDesti(boolean withTelDesti) {
        this.withTelDesti = withTelDesti;
    }

    public Ged getGed() {
        return ged;
    }

    public void setGed(Ged ged) {
        this.ged = ged;
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

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public TypeCourrier getTypeCourrier() {
        return typeCourrier;
    }

    public void setTypeCourrier(TypeCourrier typeCourrier) {
        this.typeCourrier = typeCourrier;
    }

    public Utilisateur getInitiateur() {
        return initiateur;
    }

    public void setInitiateur(Utilisateur initiateur) {
        this.initiateur = initiateur;
    }

    public List<String> getDestinataires() {
        return destinataires;
    }

    public void setDestinataires(List<String> destinataires) {
        this.destinataires = destinataires;
    }

    public String getTeldestinataire() {
        return teldestinataire;
    }

    public void setTeldestinataire(String teldestinataire) {
        this.teldestinataire = teldestinataire;
    }

    public Courrier getCourrierInstiguateur() {
        return courrierInstiguateur;
    }

    public void setCourrierInstiguateur(Courrier courrierInstiguateur) {
        this.courrierInstiguateur = courrierInstiguateur;
    }

    public Date getDateCourrier() {
        return dateCourrier;
    }

    public void setDateCourrier(Date dateCourrier) {
        this.dateCourrier = dateCourrier;
    }

    public boolean isWithObjet() {
        return withObjet;
    }

    public void setWithObjet(boolean withObjet) {
        this.withObjet = withObjet;
    }

    public boolean isWithTitre() {
        return withTitre;
    }

    public void setWithTitre(boolean withTitre) {
        this.withTitre = withTitre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getObjet() {
        return Objet;
    }

    public void setObjet(String Objet) {
        this.Objet = Objet;
    }

    public boolean isPieceJointe() {
        return pieceJointe;
    }

    public void setPieceJointe(boolean pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    public Timestamp getVersion() {
        return version;
    }

    public void setVersion(Timestamp version) {
        this.version = version;
    }

    public List<Courrier_Utilisateur_visa> getCourAndVisaUser() {
        return courAndVisaUser;
    }

    public void setCourAndVisaUser(List<Courrier_Utilisateur_visa> courAndVisaUser) {
        this.courAndVisaUser = courAndVisaUser;
    }

    public List<CourrierSignataire> getCourrierSignataires() {
        return courrierSignataires;
    }

    public void setCourrierSignataires(List<CourrierSignataire> courrierSignataires) {
        this.courrierSignataires = courrierSignataires;
    }

    public String getNumCour() {
        return numCour;
    }

    public void setNumCour(String numCour) {
        this.numCour = numCour;
    }

    public String getNumCourrierArrive() {
        return numCourrierArrive;
    }

    public void setNumCourrierArrive(String numCourrierArrive) {
        this.numCourrierArrive = numCourrierArrive;
    }

   

    public Date getDateCorrespondance() {
        return dateCorrespondance;
    }

    public void setDateCorrespondance(Date dateCorrespondance) {
        this.dateCorrespondance = dateCorrespondance;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public String getExpediteur() {
        return Expediteur;
    }

    public void setExpediteur(String Expediteur) {
        this.Expediteur = Expediteur;
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
        if (!(object instanceof Courrier)) {
            return false;
        }
        Courrier other = (Courrier) object;
        if ((this.Code == null && other.Code != null) || (this.Code != null && !this.Code.equals(other.Code))) {
            return false;
        }
        return true;
    }

    public void reset() {
        this.Code = "";

    }

}

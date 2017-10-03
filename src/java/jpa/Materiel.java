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
public class Materiel implements Serializable {

    @Id
    private String numero;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private String nomMateriel;
    @ManyToOne
    private Categorie Categorie;
    @Version
    private Timestamp version;

    public Materiel() {
//        System.out.println("----------------- suis ds le constructeur de dossier---------------------");
    }

//    @OneToMany
//    private Collection<Partie> parties;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNomMateriel() {
        return nomMateriel;
    }

    public void setNomMateriel(String nomMateriel) {
        this.nomMateriel = nomMateriel;
    }

    public Categorie getCategorie() {
        return Categorie;
    }

    public void setCategorie(Categorie Categorie) {
        this.Categorie = Categorie;
    }

  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numero != null ? numero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Materiel)) {
            return false;
        }
        Materiel other = (Materiel) object;
        if ((this.numero == null && other.numero != null) || (this.numero != null && !this.numero.equals(other.numero))) {
            return false;
        }
        return true;
    }

    public void reset() {
        this.numero = "";
        this.nomMateriel = "";
    }

}

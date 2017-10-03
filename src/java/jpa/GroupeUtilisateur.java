/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

/**
 *
 * @author SI-MJLDH
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"GROUPE_CN","UTILISATEUR_LOGIN"}))
public class GroupeUtilisateur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;    
    @ManyToOne
    private Utilisateur utilisateur;
    @ManyToOne
    private Groupe groupe;
    @Version
    private Timestamp version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    public GroupeUtilisateur() {
    }

    public GroupeUtilisateur(Utilisateur utilisateur, Groupe groupe) {
        this.utilisateur = utilisateur;
        this.groupe = groupe;
    }
    
    
    @PrePersist
    public void initDateCreation() {
        dateCreation = new Date();
//        id = utilisateur.getLogin()+"_"+groupe.getCn();
    }
    
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }
    
    public Date getDateCreation() {
        return dateCreation;
    }
    
    public Timestamp getVersion() {
        return version;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupeUtilisateur)) {
            return false;
        }
        GroupeUtilisateur other = (GroupeUtilisateur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

   
    
}

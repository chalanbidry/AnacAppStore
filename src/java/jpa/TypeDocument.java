package jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.Version;

@Entity
public class TypeDocument implements Serializable {

    @Id
    private String id;
    @Column(unique = true)
    private String libelle;
    @JsonIgnore //@JsonManagedReference
    @OneToMany(mappedBy = "typeDocument")
    private List<Document> document;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datecreation;
    @Version
    private Timestamp version;
    @ManyToOne
    private Application application;
    @PrePersist
    public void  init(){
        this.datecreation=new Date();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public List<Document> getDocument() {
        return document;
    }

    public void setDocument(List<Document> document) {
        this.document = document;
    }

    public Timestamp getVersion() {
        return version;
    }

    public void setVersion(Timestamp version) {
        this.version = version;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
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
        if (!(object instanceof TypeDocument)) {
            return false;
        }
        TypeDocument other = (TypeDocument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.libelle;
    }
    
    public void reset() {
        this.id = "";
        this.libelle = "";
    }

}

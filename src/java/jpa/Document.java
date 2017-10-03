package jpa;

//mport com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
//import forseti.jpa.Ged;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
public class Document implements Serializable {

    @Id
    private String id;
    private String submittedFileName;
    private String fileName;
    private String mimeType;
    private String taille;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    private boolean sens;
    @ManyToOne
    private Demande demande;
      @ManyToOne
    private Courrier courrier;

    @Version
    private Timestamp version;
    //@JsonBackReference
    @ManyToOne
    private TypeDocument typeDocument;
    private Ged ged;
    

    private String refDocument;
    private String description;

    @Transient
    private byte[] contenu;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public TypeDocument getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(TypeDocument typeDocument) {
        this.typeDocument = typeDocument;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public Courrier getCourrier() {
        return courrier;
    }

    public void setCourrier(Courrier courrier) {
        this.courrier = courrier;
    }

    
    
    

    @PrePersist
    public void initDateCreation() {
        dateCreation = new Date();
    }

    public boolean isSens() {
        return sens;
    }

    public void setSens(boolean sens) {
        this.sens = sens;
    }

    

    public String getSubmittedFileName() {
        return submittedFileName;
    }

    public void setSubmittedFileName(String submittedFileName) {
        this.submittedFileName = submittedFileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

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

    public Ged getGed() {
        return ged;
    }

    public void setGed(Ged ged) {
        this.ged = ged;
    }

    public String getRefDocument() {
        return refDocument;
    }

    public void setRefDocument(String refDocument) {
        this.refDocument = refDocument;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getContenu() {
        return contenu;
    }

    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
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
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "{id : " + this.id + " - fileName : " + this.fileName + " - description : " + this.description + "}";
    }

    public void reset() {
        this.id = "";
        this.submittedFileName = "";
        this.sens = false;
    }

   

    
    
}

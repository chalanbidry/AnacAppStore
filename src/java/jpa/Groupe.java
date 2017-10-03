/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author SI-MJLDH
 */
@Entity
public class Groupe implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String cn;
    private String description;
    private String dn;
    private String id;
  @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;

    public Groupe() {
    }
    
    public Groupe(String cn, String description, String dn) {
        this.cn = cn;
        this.description = description;
        this.dn = dn;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    @PrePersist
    public void initDateCreation()
    {
        dateCreation = new Date();
    }
    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cn != null ? cn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Groupe)) {
            return false;
        }
        Groupe other = (Groupe) object;
        if ((this.cn == null && other.cn != null) || (this.cn != null && !this.cn.equals(other.cn))) {
            return false;
        }
        return true;
    }

    public void reset() {
        cn = null;
        description = null;
        dn = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
}

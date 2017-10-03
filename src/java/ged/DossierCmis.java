/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ged;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * représente un dossier, c'est à dire un objet du type "cmis:folder"; c'est un wrapper
 * pour la classe <code>org.apache.chemistry.opencmis.client.api.Folder</code>
 * 
 * @author xess
 */
public class DossierCmis {
    
    Folder dossier;
    
    public static final String typeCmis = "cmis:folder";
       

    public DossierCmis(Folder dossier) {
        this.dossier = dossier;
    }

    public Folder getDossier() {
        return dossier;
    }
    
    public List<Object> listerContenu()  {
        ArrayList<Object> content = new ArrayList<>();
        ItemIterable<CmisObject> enfants = dossier.getChildren();
        enfants.forEach(content::add);
        return content.stream().filter(e -> e instanceof Folder || e instanceof Document).
                map( e -> e instanceof Folder ? new DossierCmis((Folder)e) : new DocumentCmis((Document)e)).
                    collect(Collectors.toList());        
    }

    public void setDossier(Folder dossier) {
        this.dossier = dossier;
    }   

    public String getId() {
        return dossier.getId();
    }   

    public String getNom() {
        return dossier.getName();
    }

    
    public Date getDateCreation() {
        return dossier.getCreationDate().getTime();
    }
    
    public String getCreateur() {
        return dossier.getCreatedBy();
    }

    public Date getDerniereDateModif() {
        return dossier.getLastModificationDate().getTime();
    }

    public String getDernierModificateur() {
        return dossier.getLastModifiedBy();
    }   
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", getId()).
                append("nom", getNom()).
                append("createur", getCreateur()).
                append("modificateur", getDernierModificateur()).
                append("modifié-le ", String.format("%1$tF %1$tT", getDerniereDateModif())).
                build();
    }
}


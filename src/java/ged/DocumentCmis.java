/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ged;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * représente le document d'un entrepôt CMIS, un wrapper pour la classe  
 * <code>org.apache.chemistry.opencmis.client.api.Document</code>.
 * @author xess
 */
public class DocumentCmis {
    
    private final Document document;
    public static final String typeCmis = "cmis:document";

    public DocumentCmis(Document document) {
        this.document = document;
    }   

    public String getId() {
        return document.getId();
    }
    
    public String getNom() {
        return document.getName();        
    }

    public Date getDateCreation() {
        return document.getCreationDate().getTime();
    }

    public Date getDerniereDateModif() {
        return document.getLastModificationDate().getTime();
    }
    
    public String getDernierModificateur() {
        return document.getLastModifiedBy();
    }

    public String getCreateur() {
        return document.getCreatedBy();
    }

    public String getMimeType() {
        return document.getContentStreamMimeType();
    }
    
    /**
     * flux de lecture du contenu du document.
     * Il faudra fermer convenablement ce flux après utilisation.
     * @return <code>java.io.InputStream</code>
     */
    public InputStream contenu() {        
        InputStream input = document.getContentStream().getStream();
        return input;
    }
    
    /**
     * retourne le contenu de document sous forme de tableau.
     * @return un tableau d'octets.
     * @throws IOException 
     */
    public byte[] contenuOctets() throws IOException {        
        byte[] buffer = new byte[1024];
        try(InputStream input = contenu();
                ByteArrayOutputStream output = new ByteArrayOutputStream();) {
            int count;
            while((count =input.read(buffer))!= -1) 
                output.write(buffer, 0, count);
            return output.toByteArray();            
        }
    }
    
    /**
     * encode et retourne le contenu du document sous forme de String Base64.
     * @return String encodé Base64.
     * @throws IOException 
     */
    public String contenuBase64() throws IOException {                        
        return Base64.getEncoder().encodeToString(contenuOctets());       
    }
    
    public String getVersion() {
        return document.getVersionLabel();
    }   
    
    public String getDescription() {
        return document.getDescription();
    }
    
    public List<DossierCmis> getParents() {
        List<DossierCmis> parents = new ArrayList<>();
        document.getParents().forEach(a -> parents.add(new DossierCmis(a)));
        return parents;
    }
    
    public Document getDocument() {
        return document;
    }
    
    /**
     * Vérifier si le document est une copie pour modification PWC (private working copy)
     * @return 
     */
    public boolean estUneCopiePourModif() {
        return document.isPrivateWorkingCopy();
    }
    
    /**
     * Supprime uniquement la version concernée du document.
     */
    public void supprimerLaVersion() {
        document.delete(false);
    }
    
    /**
     * Supprime toutes les versions du document.
     */
    public void supprimerTout() {
        document.deleteAllVersions();
    }
    
    public String verrouillerParQui() {
        return document.getVersionSeriesCheckedOutBy();
    }
        
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", getId()).
                append("nom", getNom()).
                append("type-mime", getMimeType()).
                append("createur", getCreateur()).
                append("modificateur", getDernierModificateur()).
                append("modifié-le ", String.format("%1$tF %1$tT", getDerniereDateModif())).
                build();
    }
}


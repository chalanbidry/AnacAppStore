/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ged;

import org.apache.chemistry.opencmis.commons.PropertyIds;

/**
 * classe utilitaire qui définit des infos et propriétés usuelles de l'API que sont:<br/>
 * <ul>
 * <li>cmis:name</li>
 * <li>cmis:objectId</li>
 * <li>cmis:folder</li>
 * <li>cmis:document</li>
 * <li>cmis:creationDate</li>
 * <li>cmis:createdBy</li>
 * <li>cmis:lastModifiedBy</li>
 * <li>cmis:lastModificationDate</li>
 * <li>cmis:objectTypeId</li>
 * </ul>
 * @author xess
 */

public final class ProprieteCmis {
    public static final String NOM_OBJET = PropertyIds.NAME;  //"cmis:name";
    public static final String ID_OBJET = PropertyIds.OBJECT_ID; //"cmis:objectId";
    public static final String DOSSIER = "cmis:folder";
    public static final String DOCUMENT = "cmis:document";
    public static final String DATE_CREATION = PropertyIds.CREATION_DATE; //"cmis:creationDate";
    public static final String CREATEUR = PropertyIds.CREATED_BY;//"cmis:createdBy";
    public static final String DERNIER_MODIFICATEUR = PropertyIds.LAST_MODIFIED_BY;//"cmis:lastModifiedBy";
    public static final String DATE_DERNIERE_MODIF = PropertyIds.LAST_MODIFICATION_DATE;//"cmis:lastModificationDate";
    public static final String ID_TYPE_OBJET = PropertyIds.OBJECT_TYPE_ID; // cmis:objectTypeId
    
    
    /**
     * retourne la liste des propriétés disponibles.
     * @return un tableau de String.
     */
    public String[] getListePropriete() {
        //TODO
        throw new RuntimeException();        
    }
}

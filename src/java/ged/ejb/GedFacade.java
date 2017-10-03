/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ged.ejb;

import ged.CmisOperationException;
import ged.DocumentCmis;
import ged.DossierCmis;
import ged.ProprieteCmis;
import util.JsfUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.QueryStatement;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisVersioningException;
import org.apache.log4j.Logger;

/**
 * Fournit l'ensemble de la logique de communication avec la plate-forme de
 * gestion de documents grâce au protocole CMIS. Testé avec Alfresco 5.0b Est
 * basé sur les API open CMIS d'apache chemistry.
 *
 * @author xess
 */
public class GedFacade {

    // regex pour vérifier si c'est un chemin
    Pattern pathPattern = Pattern.compile("^(/.*)+/?");

    /**
     * Assure que le Bean a été correctement initialisé et prêt a recevoir des
     * appels. un intercepteur se sert de cette variable pour interdire les
     * appels de la méthode en cas d'echec ou de non completion de
     * l'initialisation.
     */
    boolean ready = false;

    Session sessionCmis;
    /**
     * requête CMIS pour retrouver un dossier.
     */
    public static final String requeteTrouverObjet = "SELECT * FROM ? WHERE cmis:name = ?";
    static Logger logger = Logger.getLogger("ged.ejb.GedFacade");

    public GedFacade(Session sessionCmis) {
        this.sessionCmis = sessionCmis;
    }

    private GedFacade() {
    }

    /**
     * @return true si le Bean est prêt à recevoir des appels.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * verify if the string is a path value.
     *
     * @param value the string to match with.
     * @return true if <code>path</code> is a valid path
     */
    public boolean isPath(String value) {
        return pathPattern.matcher(value).matches();
    }

    public CmisObject getObject(ObjectId id) {
        CmisObject obj = sessionCmis.getObject(id);
        obj.refresh();
        return obj;
    }

    public CmisObject getCmisObjectById(String id) {
        CmisObject obj = sessionCmis.getObject(id);
        obj.refresh();
        return obj;
    }

    public CmisObject getCmisObjectByPath(String name) {
           System.out.println("entrer dans getCmisObjectByPath et le name est "+name);
        try {
            CmisObject obj = sessionCmis.getObjectByPath(name);
            obj.refresh();
            return obj;
        } catch (org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException e) {
            return null;
        }
    }

    /**
     * retrouve un document ou un dossier dans l'entrepôt grâce au chemin ou à
     * l'identifiant.
     *
     * @param objet recherché
     * @return un objet objet DocumentCmis ou DossierCmis ou null suivant le
     * type de l'objet.
     */
    public Object trouverObjet(String objet) {
        Object result = null;
        try {
            CmisObject cmisObj;
            if (isPath(objet)) {
                cmisObj = getCmisObjectByPath(objet);
            } else {
                cmisObj = getCmisObjectById(objet);
            }
            if (cmisObj instanceof Document) {
                result = new DocumentCmis((Document) cmisObj);
            } else if (cmisObj instanceof Folder) {
                result = new DossierCmis((Folder) cmisObj);
            }
            logger.debug("succès de la recherche pour " + objet);
        } catch (CmisObjectNotFoundException | ClassCastException e) {
            logger.error("echec de la recherche ", e);
        }
        return result;
    }

    /**
     * recherche un dossier par son nom "cmis:name". La liste d'objets qui sont
     * soit du type "cmis:folder", soit "cmis:document"
     *
     * @param nom le nom i.e propriété cmis:name de l'objet recherché
     * @param typeObjet type de l'objet recherché. DOSSIER("cmis:folder") ou
     * DOCUMENT("cmis:document").
     * @return une liste de dossiers ou de documents. ou <code>null</code> si
     * aucun objet n'est trouvé.
     */
    public List<Object> trouverObjetParNom(TypeObjet typeObjet, String nom) {
        String type = typeObjet == TypeObjet.DOCUMENT ? ProprieteCmis.DOCUMENT : ProprieteCmis.DOSSIER;
        QueryStatement stmt = sessionCmis.createQueryStatement(requeteTrouverObjet);
        stmt.setType(1, type);
        stmt.setString(2, nom);
        System.out.println(stmt.toQueryString());
        logger.debug("requete de recherche obtenue " + stmt.toQueryString());
        ItemIterable<QueryResult> result = stmt.query(false);
        if (result.getTotalNumItems() == 0) {
            logger.debug("aucun item trouvé pour la recherche");
            return null;
        }
        List<Object> list = new ArrayList<>();
        for (QueryResult qr : result) {
            String id = qr.getPropertyValueByQueryName(ProprieteCmis.ID_OBJET);
            list.add(trouverObjet(id));
        }
        logger.debug("resultat recherche :" + list);
        return list;
    }

    /**
     * Crée un sous-dossier, sous le dossier dont l'identifiant ou bien le
     * chemin est donné en paramètre. retourne une exception si un problème un
     * problème qui empêche la création est rencontré.
     *
     * @param parent l'identifiant ou le chemin du dossier parent.
     * @param nomNouveauDossier nom du dossier à créer (numéro RG).
     * @param description description of the folder. Peut être null.
     * @return une instance DossierCmis.
     */
    public DossierCmis creerDossier(String parent, String nomNouveauDossier, String description) {
        Folder fd, fdNew;
        System.out.println("entrer dans creerDossier");
        if (isPath(parent)) {
            fd = (Folder) getCmisObjectByPath(parent);
        } else {
            fd = (Folder) getCmisObjectById(parent);
        }

        if (fd == null) {
            logger.warn("le dossier parent n'existe pas " + parent);
            throw new IllegalArgumentException("le dossier parent n'existe pas");
        } else {
             String pathNew=fd.getPath()+"/"+nomNouveauDossier;
            if(getCmisObjectByPath(pathNew)!=null) {
                CmisObject cmisTest;
               Iterable<CmisObject> itera=fd.getChildren();
               int i=1;
               for(CmisObject cmi:itera){
                 i++;
               }
                 nomNouveauDossier=nomNouveauDossier+(String.valueOf(i++));
                 System.out.println("le dossier est alors "+nomNouveauDossier);
            }
        }
        Map<String, String> newFolderProps = new HashMap<>();
        newFolderProps.put(ProprieteCmis.ID_TYPE_OBJET, ProprieteCmis.DOSSIER);
        newFolderProps.put(ProprieteCmis.NOM_OBJET, nomNouveauDossier);
        newFolderProps.put(PropertyIds.DESCRIPTION, description);
        Folder dossier = fd.createFolder(newFolderProps);
        logger.debug("dossier créé avec succès :" + dossier);
        return new DossierCmis(dossier);
    }

    /**
     * Crée un nouveau document dans l'entrepôt.
     *
     * @param parent le chemin ou le id du dossier parent.
     * @param nomDocument le nom du document créé.
     * @param dataStream le contenu du document. Peut être null.
     * @param mimeType le type de media du document. Peut être null.
     * @param desc description of the document. Peut être null.
     * @return un objet document CMIS
     */
    public DocumentCmis creerDocument(String parent, String nomDocument,
            InputStream dataStream, String mimeType, String desc) {
        System.out.println("rentrer creer document ");
        logger.debug(String.format("création du document %s dans le dossier %s", nomDocument, parent));
        DocumentCmis doc;
        Folder dossierParent;
        if (isPath(parent)) {
            dossierParent = (Folder) getCmisObjectByPath(parent);
        } else {
            dossierParent = (Folder) getCmisObjectById(parent);
        }
        if (dossierParent == null) {
            logger.warn("le dossier parent n'existe pas " + parent);
            throw new IllegalArgumentException("le dossier parent n'existe pas");
        }
        ContentStream content = sessionCmis.getObjectFactory().createContentStream(nomDocument, -1, mimeType, dataStream);
        Map<String, String> properties = new HashMap<>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, ProprieteCmis.DOCUMENT);
        properties.put(PropertyIds.NAME, nomDocument);
        properties.put(PropertyIds.DESCRIPTION, desc);
        Document dcmt = dossierParent.createDocument(properties, content, VersioningState.MAJOR);
        doc = new DocumentCmis(dcmt);
        logger.debug("succès création document " + doc);
        return doc;
    }

    /**
     * Retire la dernière version d'un document en vue d'une modification hors
     * ligne. Le document obtenu est PWC (private working document). Jusqu'à la
     * validation des modifications apportés à cette ce document, cette
     * opération ne sera plus autorisée. le document ainsi verouillé, aucun
     * autre utilisateur ne pourra le modifier. si le nom du fichier original
     * est monfichier.txt, le nom de la copie est par défaut monfichier (Working
     * Copy).txt
     *
     * @param pathOrId peut être un chemin de document ou un id.
     * @return soit le document trouvé, soit null.
     */
    public DocumentCmis verrouillerPourModification(String pathOrId) throws CmisVersioningException {
        DocumentCmis doc = null;
        System.out.println("pathOrId " + pathOrId);
        try {
            Document d = ((DocumentCmis) trouverObjet(pathOrId)).getDocument();
            // recuperer la dernière version pour modification
            Document pwc = (Document) getCmisObjectById(d.checkOut().getId());
            doc = new DocumentCmis(pwc);
            logger.info(String.format("document %s verrouillé par %s", doc.getNom(), doc.verrouillerParQui()));
        } catch (CmisObjectNotFoundException | ClassCastException e) {
            logger.error("echec de verrouillage de dossier", e);
        }
        return doc;
    }

    public DocumentCmis[] retirerListeVersions(String pathOrId) {
        List<DocumentCmis> docs = new ArrayList<>();
        try {
            Document d;
            if (isPath(pathOrId)) {
                d = (Document) getCmisObjectByPath(pathOrId);
            } else {
                d = (Document) getCmisObjectById(pathOrId);
            }
            // recuperer la dernière version pour modification
            d.getAllVersions().forEach(e -> docs.add(new DocumentCmis(e)));
        } catch (CmisObjectNotFoundException | ClassCastException e) {
            logger.error("probleme pendant l'extraction des versions du document", e);
        }
        return docs.toArray(new DocumentCmis[1]);
    }

    /**
     * enregistre un document qui auparavant a été retiré pour modification.
     *
     * @param idCopie l'id du document précédemment verrouillé pour
     * modification.
     * @param nouveauNom le nouveau nom à donner au document. <code>null</code>
     * pour garder le meme nom.
     * @param nouveauContenu le contenu avec les modifications
     * @param mimeType le type du média de document
     * @param commentaire le commentaire de modification
     * @return la dernière version nouvellement enregistrée du document avec les
     * modifications.
     */
    public DocumentCmis enregistrerCopieModifiee(String idCopie, String nouveauNom,
            InputStream nouveauContenu, String mimeType, String commentaire) {
        System.out.println("enregistrerCopieModifiee debut ");
        DocumentCmis newDoc = null;
        Map<String, String> properties = new HashMap<>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, ProprieteCmis.DOCUMENT);
        if (!(nouveauNom == null || nouveauNom.isEmpty())) {
            properties.put(PropertyIds.NAME, nouveauNom);
        }
        ContentStream cstream = sessionCmis.getObjectFactory().createContentStream(nouveauNom, -1, mimeType, nouveauContenu);

        try {
            Document pwc = ((DocumentCmis) trouverObjet(idCopie)).getDocument();

            ObjectId newId = pwc.checkIn(true, properties, cstream, commentaire);
            System.out.println("newId " + newId);
            newDoc = new DocumentCmis((Document) getObject(newId));

            logger.info(String.format("document %s modifié par %s", newDoc.getNom(), newDoc.getDernierModificateur()));

        } catch (CmisBaseException e) {
            logger.error("Echec de mise à jour du document" + nouveauNom, e);
            System.out.println("Echec de mise à jour du document " + nouveauNom + " " + e);
        }
        System.out.println("enregistrerCopieModifiee fin " + newDoc);
        return newDoc;
    }

    /**
     * Annuler le verrou auparavant créé sur un document, pour une modification
     * hors-ligne.
     *
     * @param cheminOuId chemin complet de la copie.
     * @throws forseti.ged.CmisOperationException
     */
    public void annulerVerrou(String cheminOuId) throws CmisOperationException {
        try {
            Document d = ((DocumentCmis) trouverObjet(cheminOuId)).getDocument();
            if (d.isPrivateWorkingCopy()) {
                d.cancelCheckOut();
            } else {
                logger.warn("Pas de verrou sur " + d.getName() + " opération annulée");
            }
        } catch (ClassCastException e) {
            throw new CmisOperationException("l'argument n'est pas un document valide", e);
        }
    }

    private void testInit() {
        SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
        Map<String, String> parameters = new HashMap<>();
        //User credentials.
        parameters.put(SessionParameter.USER, "admin");
        parameters.put(SessionParameter.PASSWORD, "boyses");

        // Connection settings.
        parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameters.put(SessionParameter.ATOMPUB_URL, "http://localhost:8081/alfresco/api/-default-/public/cmis/versions/1.1/atom");  //URL to your CMIS server.
        parameters.put(SessionParameter.AUTH_HTTP_BASIC, "true");
        parameters.put(SessionParameter.COOKIES, "true");
        //Create session.
        //Alfresco only provides one repository.
        Repository repository = sessionFactory.getRepositories(parameters).get(0);
        sessionCmis = repository.createSession();
//        sessionCmis.
    }

    /**
     * supprimer le document et toutes ses versions.
     *
     * @param pathOrId
     * @throws CmisOperationException
     */
    public void supprimerDocument(String pathOrId) throws CmisOperationException {
        supprimerDocument(pathOrId, true);
    }

    private void supprimerDocument(String pathOrId, boolean toutesLesVersions) throws CmisOperationException {
        try {
            Document d = (Document) trouverObjet(pathOrId);
            d.delete(toutesLesVersions);
            logger.warn("document " + d.getName() + " supprimé avec succès");
        } catch (ClassCastException e) {
            logger.error("echec de la suppression de document", e);
            throw new CmisOperationException("l'objet spécifié n'est pas un document");
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Début des tests ===");
        GedFacade facade = new GedFacade();
        facade.testInit();
        DocumentCmis d = (DocumentCmis) facade.trouverObjet("/FORSETI_COM/TPI-LOM/INFO.txt");
        //List<?> dnom = (List<? super DocumentCmis>) facade.trouverObjetParNom(TypeObjet.DOCUMENT,"testDatas_03092015.sql");

        //System.out.println("recherche par nom " + dnom);
        System.out.println("recherche par chemin/id " + d);
        try {
            DocumentCmis dd = facade.verrouillerPourModification("/FORSETI_COM/TPI-LOM/INFO.txt");
            System.out.println("document verrouillé " + dd);
        } catch (CmisVersioningException e) {
            System.out.println("document déjà verrouillé " + e);
        }

        //FileInputStream fin = new FileInputStream("C:\\xess\\data\\mydoc\\INFO.txt");
        //DocumentCmis dm = facade.enregistrerCopieModifiee(dd.getId(), null, fin, dd.getMimeType(),
        //        "version modifiée " + String.format("%1$tF, %1$tT", new Date()));
        //System.out.println("modification effectuée. resultat " + dm);
        //facade.annulerVerrou(dd.getId());
        /*
         byte[] b = d.contenuOctets();
         System.out.println("taille contenu lu: "+b.length);
         try (FileOutputStream fout = new FileOutputStream("C:\\Users\\xess\\Desktop\\bib\\testDatas_03092015_d2.sql")) {
         fout.write(b);
         }
         */
        System.out.println("=== fin des test ===");
    }
}

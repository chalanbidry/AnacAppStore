/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import ged.DocumentCmis;
import ged.ejb.GedFacade;
import jpa.Document;
import jpa.Demande;

import util.JsfUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import jpa.Courrier;
import jpa.Ged;
import jpa.Utilisateur;
import org.apache.log4j.Logger;

/**
 *
 * @author Gildasdarex
 */
@Stateless
public class DocumentFacade extends AbstractFacade<Document> {
    
    Logger logger = Logger.getLogger(DocumentFacade.class);
    
    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
    
    private Utilisateur currentProcureur;
    
    private GedFacade gedFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public DocumentFacade() {
        super(Document.class);
    }
    
    public String giveId() {
        
        int size = findAll().size();
        String id = "", inc = "";
        if (size != 0 && size != 9999) {
            int taille = String.valueOf(size + 1).length();
            for (int i = 1; i <= (4 - taille); i++) {
                inc = inc + "0";
            }
            inc = inc + (String.valueOf(size + 1));
            
            id = "2101" + JsfUtil.getCurrentYear() + inc;
        } else if (size != 9999) {
            inc = "1";
            id = "2101" + JsfUtil.getCurrentYear() + "000" + inc;
        }
        return id;
    }
    
    @Override
    public void create(Document entity) {
        try {
            //System.out.println("cle primaire " + contextBean.getJURIDICTIONCLEPRIMAIRE());
            entity.setId(giveId());

//            int levelDocumentByType = this.getCompteurDocumentsByType(entity.getTypeDocument().getId()).size();
//            levelDocumentByType += 1;
//        String idForFileName = compteurFileFacade.getFileNameCompteur(Document.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE());
//            entity.setFileName("" + entity.getTypeDocument().getId() + "N° " + levelDocumentByType + "_" + JsfUtil.convertDate(new Date(), "EEEEE dd MMM yyyy 'A' HH:mm:ss"));
            // String idForFileName=compteurFileFacade.getFileNameCompteur(Document.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE());
            //entity.setFileName("Factum_"+idForFileName);
            getEntityManager().persist(entity);
        } catch (Exception e) {
            JsfUtil.logError(logger, "erreur document facade ");
        }
        
    }
    
    public void verrouillerDocument(Document documentCourant) throws IOException {
        
        System.out.println("downloadDocument : " + documentCourant.getId());
        String ref = documentCourant.getGed().getRefGed();
        System.out.println("Document ref : " + ref);
        //DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
        DocumentCmis doc = (DocumentCmis) gedFacade.verrouillerPourModification(ref);
        System.out.println("Document : " + doc);
        
        Ged ged = new Ged(doc.getId(), doc.getNom(), doc.getId());
        getEntityManager().persist(ged);
        
        documentCourant.setGed(ged);
        edit(documentCourant);
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.reset();
        
        response.setContentType(doc.getDocument().getContentStreamMimeType());
        response.setHeader("Content-Disposition", "attachment;filename=" + doc.getDocument().getName());
        response.setContentLength((int) doc.getDocument().getContentStream().getLength());
        BufferedInputStream buf = new BufferedInputStream(doc.getDocument().getContentStream().getStream());
        
        response.getOutputStream().write(doc.contenuOctets());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        facesContext.responseComplete();
        facesContext.renderResponse();
        buf.close();
    }

    /*
     Renauld
     */
    /**
     * Document par annees
     *
     * @param annee
     * @return Liste des documents par annee.
     */
    public List<Document> getListeDocumentsAnnee(String annee) {
//        System.out.println("dans selection........");
        List<Document> documents;
        Query jpql = em.createQuery("SELECT d FROM Document d WHERE d.progressionDossier.annee = :annee");
//        Query jpql = em.createQuery("SELECT d, max(d.progressionDossier.dateCreation) FROM Document d WHERE  d.progressionDossier.annee=:annee AND d.progressionDossier.statut.code NOT IN ('ARC') GROUP BY d.progressionDossier.dossier.id,d.id,d.progressionDossier.dateCreation HAVING  max(d.progressionDossier.dateCreation)=d.progressionDossier.dateCreation");
        jpql.setParameter("annee", annee);
        documents = jpql.getResultList();
        
        return documents;
    }

    /**
     * Renvoie la liste des documents par annee par service d'un juge ou d'un
     * greffier donné.
     *
     * @author Zansouyé
     * @param datefin
     * @param datedebut
     * @param service
     * @return
     */
    public List<Document> getListeDocumentsParPeriode(Date datedebut, Date datefin) {
        List<Document> documents;
        Query jpql = em.createQuery("SELECT d FROM Document d WHERE d.progressionDossier.dossier.dateSaisine BETWEEN :datedebut AND :datefin AND d.progressionDossier.serviceDestinataire = :service");
        jpql.setParameter("datedebut", datedebut);
        jpql.setParameter("datefin", datefin);
        
        documents = jpql.getResultList();
        if (documents == null) {
            documents = new ArrayList<>();
        }
        return documents;
    }

    /**
     * Document par annees
     *
     * @param annee
     * @return Liste des documents par annee. version Armel
     */
    public List<Document> getListeDocumentsAnneeByType(String annee) {
        List<Document> documents;
        Query jpql = em.createQuery("SELECT d FROM Document d WHERE d.progressionDossier.dossier.annee.valeur=:annee  and d.typeDocument.id='_DECISIONT_'");
        jpql.setParameter("annee", annee);
        documents = jpql.getResultList();
        
        return documents;
    }

    /**
     * Ged du dossier
     *
     * @param dossier
     * @return Liste des documents du dossier.
     */
//    A revoir
    public List<Document> getListeDocumentsDossier(Demande demande) {
        List<Document> documents;
        Query jpql = em.createQuery("SELECT d FROM Document d WHERE  d.progressionDossier.dossier=:dossier");
        jpql.setParameter("demande", demande);
        documents = jpql.getResultList();
        if (documents == null) {
            documents = new ArrayList<>();
        }
        return documents;
    }
    
    public List<Document> getCompteurDocumentsByType(String typeDocument) {
        List<Document> documents;
        Query jpql = em.createQuery("SELECT d FROM Document d WHERE  d.typeDocument.id=:typeDocument");
        jpql.setParameter("typeDocument", typeDocument);
        documents = jpql.getResultList();
        
        return documents;
    }

//       a revoir 
//      public List<Document> getDocByLib(String typeDocument,Dossier dossier) {
//        List<Document> documents;
//        Query jpql = em.createQuery("SELECT d FROM Document d WHERE  d.typeDocument.libelle=:typeDocument and d.dossier=:dossier");
//        jpql.setParameter("typeDocument", typeDocument);
//         jpql.setParameter("dossier", dossier);
//        documents = jpql.getResultList();
//        if(documents==null)documents=new ArrayList<>();
//        System.out.println("documents--->"+ documents);
//        return documents;
//    }  
    public List<Document> getListeDocumentsByDemmande(Demande demande) {
//        System.out.println("dans selection........");
        List<Document> documents;
        Query jpql = em.createQuery("SELECT d FROM Document d WHERE d.demande = :demande");
//        Query jpql = em.createQuery("SELECT d, max(d.progressionDossier.dateCreation) FROM Document d WHERE  d.progressionDossier.annee=:annee AND d.progressionDossier.statut.code NOT IN ('ARC') GROUP BY d.progressionDossier.dossier.id,d.id,d.progressionDossier.dateCreation HAVING  max(d.progressionDossier.dateCreation)=d.progressionDossier.dateCreation");
        jpql.setParameter("demande", demande);
        documents = jpql.getResultList();
        
        return documents;
    }
    
    public List<Document> getListeDocumentsByCourrier(Courrier courrier) {
//        System.out.println("dans selection........");
        List<Document> documents;
        Query jpql = em.createQuery("SELECT d FROM Document d WHERE d.courrier = :courrier");
//        Query jpql = em.createQuery("SELECT d, max(d.progressionDossier.dateCreation) FROM Document d WHERE  d.progressionDossier.annee=:annee AND d.progressionDossier.statut.code NOT IN ('ARC') GROUP BY d.progressionDossier.dossier.id,d.id,d.progressionDossier.dateCreation HAVING  max(d.progressionDossier.dateCreation)=d.progressionDossier.dateCreation");
        jpql.setParameter("courrier", courrier);
        documents = jpql.getResultList();
        
        return documents;
    }
    
    public Document getDocumentsCourrierByCourrier(Courrier courrier) {
//        System.out.println("dans selection........");
        List<Document> documents;
        Query jpql = em.createQuery("SELECT d FROM Document d WHERE d.courrier = :courrier and d.typeDocument.id=:idType");
//        Query jpql = em.createQuery("SELECT d, max(d.progressionDossier.dateCreation) FROM Document d WHERE  d.progressionDossier.annee=:annee AND d.progressionDossier.statut.code NOT IN ('ARC') GROUP BY d.progressionDossier.dossier.id,d.id,d.progressionDossier.dateCreation HAVING  max(d.progressionDossier.dateCreation)=d.progressionDossier.dateCreation");
        jpql.setParameter("courrier", courrier);
        jpql.setParameter("idType", "210120179");
        documents = jpql.getResultList();
        if (documents.isEmpty()) {
            return null;
        }
        
        return documents.get(0);
    }
}

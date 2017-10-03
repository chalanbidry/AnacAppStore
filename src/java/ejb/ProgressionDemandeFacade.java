/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.ProgressionDemande;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Demande;
import jpa.Fonction;
import jpa.Service;
import jpa.Utilisateur;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class ProgressionDemandeFacade extends AbstractFacade<ProgressionDemande> {

    Logger logger = Logger.getLogger(ProgressionDemandeFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgressionDemandeFacade() {
        super(ProgressionDemande.class);
    }

    public String giveCode() {

        ProgressionDemande lastProgressionDemande = getLastProgressionDemande();

        String id = "", inc = "";
        if (lastProgressionDemande != null) {
            String idLast = lastProgressionDemande.getCode();
            String lastPart = idLast.substring(8);
            int position = Integer.valueOf(lastPart);
            if (position != 999999) {
                int taille = String.valueOf(position + 1).length();
                for (int i = 1; i <= (6 - taille); i++) {
                    inc = inc + "0";
                }
                inc = inc + (String.valueOf(position + 1));

                id = "1101" + JsfUtil.getCurrentYear() + inc;
            }
        } else {
            inc = "1";
            id = "1101" + JsfUtil.getCurrentYear() + "00000" + inc;
        }
        return id;
    }

    @Override
    public void create(ProgressionDemande entity) {
        entity.setCode(giveCode());
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public List<ProgressionDemande> getAllUsers() {
        Query q = getEntityManager().createQuery("select U from ProgressionDemande U order by U.nomPrenoms");
        // set parameters
        List<ProgressionDemande> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

    public List<ProgressionDemande> getAllProgressionDemandeByUser(Utilisateur user) {
        Query q = getEntityManager().createQuery("select d from ProgressionDemande d where d.utilisateurSend=:user order by d.dateCreation DESC");
        q.setParameter("user", user);
        List<ProgressionDemande> suggestions = q.getResultList();

        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public List<ProgressionDemande> getAllProgressionDemandeByUserDate(Utilisateur user, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("select d from ProgressionDemande d where d.utilisateurSend=:user AND d.date BETWEEN :date1 AND :date2 order by d.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<ProgressionDemande> suggestions = q.getResultList();
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public ProgressionDemande getLastProgressionDemande() {
        Query q = getEntityManager().createQuery("select d from ProgressionDemande d where d.dateCreation >= ALL (SELECT d1.dateCreation FROM ProgressionDemande d1)");
        // set parameters
        List<ProgressionDemande> suggestions = q.getResultList();
        if (suggestions.isEmpty()) {
            return null;
        } else {
            return suggestions.get(0);
        }

    }

    /**
     * Renvoie la dernière progression du dossier
     *
     *
     * @param demande
     * @return Une ligne de progression Dossiers
     */
    public ProgressionDemande getLastProgressionDemande(Demande demande) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionDemande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionDemande p2 WHERE p2.demande = p1.demande)AND p1.demande=:demande");
        q.setParameter("demande", demande);
        List<ProgressionDemande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p.get(0);
        }
    }

    /**
     * Renvoie la dernière progression du dossier
     *
     *
     * @param user
     * @return Une ligne de progression Dossiers
     */
    public List<Demande> getDemandeCreesAndYear(Utilisateur user) {
        System.out.println("le user est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1.demande FROM ProgressionDemande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionDemande p2 WHERE p2.demande = p1.demande)AND p1.demande.utilisateurSend=:user and p1.userReceive=:user and p1.evenement.Code=:code ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("code", "Dem001");
        List<Demande> p;
        p = q.getResultList();
        System.out.println("le resultat est " + p);
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }

        return p;
    }

    public List<ProgressionDemande> getListAllProgressionFromDemande(Demande demande) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionDemande p1 WHERE p1.demande=:demande ORDER BY p1.dateCreation ASC");
        q.setParameter("demande", demande);
        List<ProgressionDemande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    /**
     * Renvoie la dernière progression du dossier
     *
     *
     * @param demande
     * @param user
     * @return Une ligne de progression Dossiers
     */
    public boolean getLastProgressionDemandeForUser(Demande demande, Utilisateur user) {
        Query q;
     if(user.getFonction().getCode().equals("CSARH")){
         q = getEntityManager().createQuery("SELECT p1 FROM ProgressionDemande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionDemande p2 WHERE p2.demande = p1.demande)AND p1.demande=:demande and p1.userReceive =:user and p1.evenement.Code =:code1 OR p1.evenement.Code=:code2 ");
        q.setParameter("demande", demande);
        q.setParameter("user", user);
         q.setParameter("code1", "Dem002");
         q.setParameter("code2", "Dem0011");
        }else{
            q = getEntityManager().createQuery("SELECT p1 FROM ProgressionDemande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionDemande p2 WHERE p2.demande = p1.demande)AND p1.demande=:demande and p1.userReceive =:user and p1.evenement.Code =:code ");
        q.setParameter("demande", demande);
        q.setParameter("user", user);
         q.setParameter("code", "Dem002");
        
     }
       
        List<ProgressionDemande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public List<ProgressionDemande> getLastProgressionAllDemande() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionDemande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionDemande p2 WHERE p2.demande = p1.demande)AND p1.evenement.Code=:even ORDER BY p1.dateCreation DESC");
        q.setParameter("even", "Dem003");
        List<ProgressionDemande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }

    public List<ProgressionDemande> getLastProgressionAllDemandeSendDG() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionDemande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionDemande p2 WHERE p2.demande = p1.demande)AND p1.evenement.Code=:even ORDER BY p1.dateCreation DESC");
        q.setParameter("even", "Dem004");
        List<ProgressionDemande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }

    public List<ProgressionDemande> getLastProgressionAllDemandeSendCSFC() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionDemande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionDemande p2 WHERE p2.demande = p1.demande) and p1.statut!=:statut AND p1.demande  IN (SELECT p3.demande from ProgressionDemande p3 where p3.evenement.Code=:even) ORDER BY p1.dateCreation DESC");
        q.setParameter("even", "Dem005");
        q.setParameter("statut", "Terminée");
        List<ProgressionDemande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }

    public List<ProgressionDemande> getLastProgressionAllDemandeSendCSFCAndNonFinsh(Fonction fonction) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionDemande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionDemande p2 WHERE p2.demande = p1.demande)AND p1.statut != :statut and p1.userReceive.fonction=:function ORDER BY p1.dateCreation DESC");
        q.setParameter("statut", "Terminée");
        q.setParameter("function", fonction);
        List<ProgressionDemande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }
    
     public List<ProgressionDemande> getLastProgressionAllDemandeSendByMeAndNonFinsh(Fonction fonction) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionDemande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionDemande p2 WHERE p2.demande = p1.demande)AND p1.statut != :statut and p1.demande.utilisateurSend.fonction=:function ORDER BY p1.dateCreation DESC");
        q.setParameter("statut", "Terminée");
        q.setParameter("function", fonction);
        List<ProgressionDemande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }
    

    public List<Demande> getListAllProgressionFromDemandeTermine(Utilisateur user, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("SELECT p1.demande FROM ProgressionDemande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionDemande p2 WHERE p2.demande = p1.demande)AND p1.userReceive=:user and p1.statut=:statut and p1.evenement.Code=:code and p1.dateCreation BETWEEN :date1 and :date2 ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("statut", "Terminée");
        q.setParameter("code", "Dem0010");
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<Demande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }
    
     public List<Demande> getListAllProgressionFromDemandeCreatByMe(Utilisateur user, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("SELECT p1.demande FROM ProgressionDemande p1 WHERE  p1.demande.utilisateurSend=:user and p1.statut!=:statut and p1.evenement.Code=:code and p1.dateCreation BETWEEN :date1 and :date2 ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("statut", "Terminée");
        q.setParameter("code", "Dem001");
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<Demande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }
      public List<Demande> getListAllProgressionFromDemandeCreatInService(Service service, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("SELECT p1.demande FROM ProgressionDemande p1 WHERE  p1.demande.utilisateurSend.fonction.service=:service and p1.statut!=:statut and p1.evenement.Code=:code and p1.dateCreation BETWEEN :date1 and :date2 ORDER BY p1.dateCreation DESC");
        q.setParameter("service", service);
        q.setParameter("statut", "Terminée");
        q.setParameter("code", "Dem001");
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<Demande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

}

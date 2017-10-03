/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.ProgressionBonCommande;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Boncommande;
import jpa.Demande;
import jpa.Utilisateur;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class ProgressionBonCommandeFacade extends AbstractFacade<ProgressionBonCommande> {

    Logger logger = Logger.getLogger(ProgressionBonCommandeFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgressionBonCommandeFacade() {
        super(ProgressionBonCommande.class);
    }

    public String giveCode() {

        ProgressionBonCommande lastProgressionBonCommande = getLastProgressionBonCommande();

        String id = "", inc = "";
        if (lastProgressionBonCommande != null) {
            String idLast = lastProgressionBonCommande.getCode();
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
    public void create(ProgressionBonCommande entity) {
        entity.setCode(giveCode());
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public List<ProgressionBonCommande> getAllUsers() {
        Query q = getEntityManager().createQuery("select U from ProgressionBonCommande U order by U.nomPrenoms");
        // set parameters
        List<ProgressionBonCommande> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

    public List<ProgressionBonCommande> getAllProgressionBonCommandeByUser(Utilisateur user) {
        Query q = getEntityManager().createQuery("select d from ProgressionBonCommande d where d.utilisateurSend=:user order by d.dateCreation");
        q.setParameter("user", user);
        List<ProgressionBonCommande> suggestions = q.getResultList();

        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public List<ProgressionBonCommande> getAllProgressionBonCommandeByUserDate(Utilisateur user, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("select d from ProgressionBonCommande d where d.utilisateurSend=:user AND d.date BETWEEN :date1 AND :date2 order by d.dateCreation");
        q.setParameter("user", user);
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<ProgressionBonCommande> suggestions = q.getResultList();
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public ProgressionBonCommande getLastProgressionBonCommande() {
        Query q = getEntityManager().createQuery("select d from ProgressionBonCommande d where d.dateCreation >= ALL (SELECT d1.dateCreation FROM ProgressionBonCommande d1)");
        // set parameters
        List<ProgressionBonCommande> suggestions = q.getResultList();
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
    public ProgressionBonCommande getLastProgressionBonCommande(Demande demande) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionBonCommande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionBonCommande p2 WHERE p2.demande = p1.demande)AND p1.demande=:demande");
        q.setParameter("demande", demande);
        List<ProgressionBonCommande> p;
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
        System.out.println("le user est "+user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1.demande FROM ProgressionBonCommande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionBonCommande p2 WHERE p2.demande = p1.demande)AND p1.demande.utilisateurSend=:user and p1.userReceive=:user and p1.evenement.Code=:code");
        q.setParameter("user", user);
        q.setParameter("code","Dem001");
        List<Demande> p;
        p = q.getResultList();
        System.out.println("le resultat est "+p);
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
          
        return p;
    }
    
    
    public List<ProgressionBonCommande> getListAllProgressionFromDemande(Boncommande bonCommande){
    Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionBonCommande p1 WHERE p1.boncommande=:bonCommande ORDER BY p1.dateCreation ASC");
        q.setParameter("bonCommande", bonCommande);
        List<ProgressionBonCommande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
          p=new ArrayList<>();    
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
    public boolean getLastProgressionBonCommandeForUser(Demande demande,Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionBonCommande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionBonCommande p2 WHERE p2.demande = p1.demande)AND p1.demande=:demande and p1.userReceive =:user and p1.evenement.Code =:code ");
        q.setParameter("demande", demande);
         q.setParameter("user", user);
         q.setParameter("code","Dem002");
        List<ProgressionBonCommande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    
    
    
    
     public List<ProgressionBonCommande> getLastProgressionAllBon() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionBonCommande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionBonCommande p2 WHERE p2.boncommande = p1.boncommande)AND p1.evenement.Code=:even");
        q.setParameter("even", "Dem006");
        List<ProgressionBonCommande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }
     
      public List<ProgressionBonCommande> getLastProgressionAllBonSendDAF() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionBonCommande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionBonCommande p2 WHERE p2.boncommande = p1.boncommande)AND p1.evenement.Code=:even");
        q.setParameter("even", "Dem007");
        List<ProgressionBonCommande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }
      
       public List<ProgressionBonCommande> getLastProgressionAllBonSendDG() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionBonCommande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionBonCommande p2 WHERE p2.boncommande = p1.boncommande)AND p1.evenement.Code=:even");
        q.setParameter("even", "Dem008");
        List<ProgressionBonCommande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }
       
         public List<ProgressionBonCommande> getLastProgressionAllBonSendCSFC() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionBonCommande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionBonCommande p2 WHERE p2.boncommande = p1.boncommande)AND p1.evenement.Code=:even");
        q.setParameter("even", "Dem009");
        List<ProgressionBonCommande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }
     
     
       public List<ProgressionBonCommande> getLastProgressionAllDemandeSendDG() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionBonCommande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionBonCommande p2 WHERE p2.demande = p1.demande)AND p1.evenement.Code=:even");
        q.setParameter("even", "Dem004");
        List<ProgressionBonCommande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }
       
       
       
       public List<ProgressionBonCommande> getLastProgressionAllDemandeSendCSFC() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionBonCommande p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionBonCommande p2 WHERE p2.demande = p1.demande)AND p1.evenement.Code=:even");
        q.setParameter("even", "Dem005");
        List<ProgressionBonCommande> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }

}

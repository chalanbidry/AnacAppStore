/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Demande;

import jpa.Boncommande;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Departement;
import jpa.Service;
import jpa.Utilisateur;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class DemandeFacade extends AbstractFacade<Demande> {

    Logger logger = Logger.getLogger(DemandeFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DemandeFacade() {
        super(Demande.class);
    }

    public String giveCode() {

        Demande lastDemande = getLastDemande();
        String code = "";
        if (lastDemande != null) {
            String CodeLast = lastDemande.getCode();
            int position = Integer.valueOf(CodeLast);

            if (position != 999999) {
                int taille = String.valueOf(position + 1).length();
                for (int i = 1; i <= (6 - taille); i++) {
                    code = code + "0";
                }
                code = code + (String.valueOf(position + 1));

            }
            return code;
        } else {
            code = "000001";
            return code;
        }
    }

    @Override
    public void create(Demande entity) {
        entity.setCode(giveCode());
        getEntityManager().persist(entity);
    }
    
    public String createAndGidCode(Demande entity) {
        String code=giveCode();
        entity.setCode(code);
        getEntityManager().persist(entity);
        return code;
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public List<Demande> getAllUsers() {
        Query q = getEntityManager().createQuery("select U from Demande U order by U.nomPrenoms");
        // set parameters
        List<Demande> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

    public List<Demande> getAllDemandeByUser(Utilisateur user) {
        Query q = getEntityManager().createQuery("select d from Demande d where d.utilisateurSend=:user order by d.dateCreation");
        q.setParameter("user", user);
        List<Demande> suggestions = q.getResultList();

        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public List<Demande> getAllDemandeByUserDate(Utilisateur user, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("select d from Demande d where d.utilisateurSend=:user AND d.date BETWEEN :date1 AND :date2 order by d.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<Demande> suggestions = q.getResultList();
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public Demande getLastDemande() {
        Query q = getEntityManager().createQuery("select d from Demande d where d.dateCreation >= ALL (SELECT d1.dateCreation FROM Demande d1)");
        // set parameters
        List<Demande> suggestions = q.getResultList();
        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            return null;
        }
        // return the suggestions
        return suggestions.get(0);

    }

    public List<Demande> getAllDemandeInDir(Service service, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("select d from Demande d where d.utilisateurSend.fonction.service=:service and d.dateCreation BETWEEN :date1 and :date2 ORDER BY d.dateCreation DESC");
        q.setParameter("service", service);
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<Demande> suggestions = q.getResultList();
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }
    
    public boolean alredyBonCommande(Demande demande){
     Query q = getEntityManager().createQuery("select bc from Boncommande bc where bc.demande=:demande");
        q.setParameter("demande", demande);
        List<Boncommande> suggestions =new ArrayList<>();
        suggestions= q.getResultList();
        if(!suggestions.isEmpty()){
         return true;
        }
        return false;
    }
}

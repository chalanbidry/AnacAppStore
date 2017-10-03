/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Evenement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Utilisateur;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class EvenementFacade extends AbstractFacade<Evenement> {

    Logger logger = Logger.getLogger(EvenementFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EvenementFacade() {
        super(Evenement.class);
    }

    public String giveCode() {

        Evenement lastEvenement = getLastEvenement();
        String CodeLast = lastEvenement.getCode();
        int position = Integer.valueOf(CodeLast);
        String code = "";
        if (lastEvenement != null && position != 999999) {
            int taille = String.valueOf(position + 1).length();
            for (int i = 1; i <= (6 - taille); i++) {
                code = code + "0";
            }
            code = code + (String.valueOf(position + 1));

          
        } else if (position != 999999) {
           code="000001";
        }
        return code;
    }

    @Override
    public void create(Evenement entity) {
entity.setCode(giveCode());
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public List<Evenement> getAllUsers() {
        Query q = getEntityManager().createQuery("select U from Evenement U order by U.nomPrenoms");
        // set parameters
        List<Evenement> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

    public List<Evenement> getAllEvenementByUser(Utilisateur user) {
        Query q = getEntityManager().createQuery("select d from Evenement d where d.utilisateurSend=:user order by d.dateCreation");
        q.setParameter("user", user);
        List<Evenement> suggestions = q.getResultList();

        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public List<Evenement> getAllEvenementByUserDate(Utilisateur user, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("select d from Evenement d where d.utilisateurSend=:user AND d.date BETWEEN :date1 AND :date2 order by d.dateCreation");
        q.setParameter("user", user);
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<Evenement> suggestions = q.getResultList();
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public Evenement getLastEvenement() {
        Query q = getEntityManager().createQuery("select d from Evenement d where d.dateCreation >= ALL (SELECT d1.dateCreation FROM Evenement d1)");
        // set parameters
        List<Evenement> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions.get(0);

    }
}

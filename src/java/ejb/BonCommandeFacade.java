/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Boncommande;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Demande;
import jpa.Departement;
import jpa.Service;
import jpa.Utilisateur;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class BonCommandeFacade extends AbstractFacade<Boncommande> {

    Logger logger = Logger.getLogger(BonCommandeFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BonCommandeFacade() {
        super(Boncommande.class);
    }

    public String giveCode() {

        Boncommande lastBoncommande = getLastBonCommande();
        String code = "";
        if (lastBoncommande != null) {
            String CodeLast = lastBoncommande.getCode();
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
    public void create(Boncommande entity) {
        entity.setCode(giveCode());
        getEntityManager().persist(entity);
    }

    public String createGivCode(Boncommande entity) {
        String code = giveCode();
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
    public Boncommande getLastBonCommande() {
        Query q = getEntityManager().createQuery("select bc from Boncommande bc where bc.dateCreation >= ALL (SELECT bc1.dateCreation FROM Boncommande bc1)");
        // set parameters
        List<Boncommande> suggestions = q.getResultList();
        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            return null;
        }
        // return the suggestions
        return suggestions.get(0);

    }

    public Boncommande findBonFromDemande(Demande demande) {
        Query q = getEntityManager().createQuery("select bn from Boncommande bn WHERE bn.demande=:demande");
        q.setParameter("demande", demande);
        List<Boncommande> suggestions = q.getResultList();
        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            return null;
        }
        // return the suggestions
        return suggestions.get(0);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.CourrierSignataire;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Courrier;
import jpa.Departement;
import jpa.CourrierSignataire;
import jpa.Utilisateur;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class CourrierSignataireFacade extends AbstractFacade<CourrierSignataire> {

    Logger logger = Logger.getLogger(CourrierSignataireFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CourrierSignataireFacade() {
        super(CourrierSignataire.class);
    }

   

    @Override
    public void create(CourrierSignataire entity) {
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @param departement
     * @return liste des utilisateurs
     */
    public List<CourrierSignataire> getAllCourrierSignataireByCourrier(Courrier courrier) {
        Query q = getEntityManager().createQuery("select cu from Courrier_Utilisateur cu WHERE cu.courrier=:courrier");
        // set parameters
        q.setParameter("courrier", courrier);
        List<CourrierSignataire> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
    
    
    public List<CourrierSignataire> getAllCourrierSignataireByCourrierNotSign(Courrier courrier) {
        Query q = getEntityManager().createQuery("select cu from Courrier_Utilisateur cu WHERE cu.courrier=:courrier and cu.signer=:sign");
        // set parameters
        q.setParameter("courrier", courrier);
        q.setParameter("sign", false);
        List<CourrierSignataire> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
    
    
    
     public CourrierSignataire findWithTwoObjectId(Utilisateur user,Courrier courrier) {
        Query q = getEntityManager().createQuery("select cu from Courrier_Utilisateur cu WHERE cu.courrier=:courrier and cu.utilisateur=:user");
        // set parameters
        q.setParameter("courrier", courrier);
        q.setParameter("user", user);
        List<CourrierSignataire> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
           return null;
        }

        // return the suggestions
        return suggestions.get(0);

    }
     
      public boolean findWithTwoObjectIdBool(Utilisateur user,Courrier courrier) {
        Query q = getEntityManager().createQuery("select cu from Courrier_Utilisateur cu WHERE cu.courrier=:courrier and cu.utilisateur=:user");
        // set parameters
        q.setParameter("courrier", courrier);
        q.setParameter("user", user);
        List<CourrierSignataire> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
           return false;
        }else{
         return true;
         }

        // return the suggestions
      

    }
    
    
    

}

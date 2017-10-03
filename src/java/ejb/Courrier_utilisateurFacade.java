/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Courrier_Utilisateur_visa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Courrier;
import jpa.Departement;
import jpa.Courrier_Utilisateur_visa;
import jpa.Utilisateur;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class Courrier_utilisateurFacade extends AbstractFacade<Courrier_Utilisateur_visa> {

    Logger logger = Logger.getLogger(Courrier_utilisateurFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Courrier_utilisateurFacade() {
        super(Courrier_Utilisateur_visa.class);
    }

   

    @Override
    public void create(Courrier_Utilisateur_visa entity) {
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @param departement
     * @return liste des utilisateurs
     */
    public List<Courrier_Utilisateur_visa> getAllCourrier_Utilisateur_visaByCourrier(Courrier courrier) {
        Query q = getEntityManager().createQuery("select cu from Courrier_Utilisateur_visa cu WHERE cu.courrier=:courrier");
        // set parameters
        q.setParameter("courrier", courrier);
        List<Courrier_Utilisateur_visa> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
    
    
    
     public Courrier_Utilisateur_visa findWithTwoObjectId(Utilisateur user,Courrier courrier) {
        Query q = getEntityManager().createQuery("select cu from Courrier_Utilisateur_visa cu WHERE cu.courrier=:courrier and cu.userVisa=:user");
        // set parameters
        q.setParameter("courrier", courrier);
        q.setParameter("user", user);
        List<Courrier_Utilisateur_visa> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            return null;
        }

        // return the suggestions
        return suggestions.get(0);

    }
     
     
      public boolean findWithTwoObjectIdBool(Utilisateur user,Courrier courrier) {
        Query q = getEntityManager().createQuery("select cu from Courrier_Utilisateur_visa cu WHERE cu.courrier=:courrier and cu.userVisa=:user");
        // set parameters
        q.setParameter("courrier", courrier);
        q.setParameter("user", user);
        List<Courrier_Utilisateur_visa> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
           return false;
        }else{
          return true;
        }

        // return the suggestions

    }
    
    
    

}

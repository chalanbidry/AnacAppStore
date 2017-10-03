/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Departement;
import java.util.ArrayList;
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
public class DepartementFacade extends AbstractFacade<Departement> {

    Logger logger = Logger.getLogger(DepartementFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartementFacade() {
        super(Departement.class);
    }

   

    @Override
    public void create(Departement entity) {
//        entity.setId(compteurFacade.getNext(Departement.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public List<Departement> getAllUsers() {
        Query q = getEntityManager().createQuery("select U from Departement U order by U.nomPrenoms");
        // set parameters
        List<Departement> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
    
    
    public List<Departement> getAllDepBySD(Utilisateur user) {
        Query q = getEntityManager().createQuery("select dep from Departement dep where dep.secretaireDeDirection=:user");
        // set parameters
        q.setParameter("user", user);
        List<Departement> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
    
    
    
    
     
    

}

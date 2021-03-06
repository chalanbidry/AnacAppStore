/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Exercice;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class ExerciceFacade extends AbstractFacade<Exercice> {

    Logger logger = Logger.getLogger(ExerciceFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExerciceFacade() {
        super(Exercice.class);
    }

   

    @Override
    public void create(Exercice entity) {
//        entity.setId(compteurFacade.getNext(Exercice.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public Exercice getCourrantExo() {
        Query q = getEntityManager().createQuery("select E from Exercice E where E.courant=:exo");
        // set parameters
        q.setParameter("exo", true);
        List<Exercice> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            return null;
        }

        // return the suggestions
        return suggestions.get(0);

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.TypeIntervention;
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
public class TypeInterventionFacade extends AbstractFacade<TypeIntervention> {

    Logger logger = Logger.getLogger(TypeInterventionFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypeInterventionFacade() {
        super(TypeIntervention.class);
    }

   

    @Override
    public void create(TypeIntervention entity) {
//        entity.setId(compteurFacade.getNext(TypeIntervention.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public List<TypeIntervention> getAllUsers() {
        Query q = getEntityManager().createQuery("select U from TypeIntervention U order by U.nomPrenoms");
        // set parameters
        List<TypeIntervention> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

}

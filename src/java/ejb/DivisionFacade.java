/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Division;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Service;
import jpa.Division;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class DivisionFacade extends AbstractFacade<Division> {

    Logger logger = Logger.getLogger(DivisionFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DivisionFacade() {
        super(Division.class);
    }

   

    @Override
    public void create(Division entity) {
//        entity.setId(compteurFacade.getNext(Division.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @param service
     * @return liste des utilisateurs
     */
//    public List<Division> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from Division U order by U.nomPrenoms");
//        // set parameters
//        List<Division> suggestions = q.getResultList();
//
//        // avoid returing null to managed beans
//        if (suggestions == null) {
//            suggestions = new ArrayList<>();
//        }
//
//        // return the suggestions
//        return suggestions;
//
//    }
     public List<Division> getDivisionBySer(Service service) {
        Query q = getEntityManager().createQuery("select F FROM Division F where F.service=:service");
        q.setParameter("service", service);
        List<Division> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

}

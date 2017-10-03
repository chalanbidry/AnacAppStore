/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Service;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Departement;
import jpa.Service;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class ServiceFacade extends AbstractFacade<Service> {

    Logger logger = Logger.getLogger(ServiceFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ServiceFacade() {
        super(Service.class);
    }

   

    @Override
    public void create(Service entity) {
//        entity.setId(compteurFacade.getNext(Service.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @param departement
     * @return liste des utilisateurs
     */
//    public List<Service> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from Service U order by U.nomPrenoms");
//        // set parameters
//        List<Service> suggestions = q.getResultList();
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
     public List<Service> getServiceByDep(Departement departement) {
        Query q = getEntityManager().createQuery("select F FROM Service F where F.departement=:departement");
        q.setParameter("departement", departement);
        List<Service> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Application;
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
public class ApplicationFacade extends AbstractFacade<Application> {

    Logger logger = Logger.getLogger(ApplicationFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ApplicationFacade() {
        super(Application.class);
    }

   

    @Override
    public void create(Application entity) {
//        entity.setId(compteurFacade.getNext(Application.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @param departement
     * @return liste des utilisateurs
     */
//    public List<Application> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from Application U order by U.nomPrenoms");
//        // set parameters
//        List<Application> suggestions = q.getResultList();
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
     public List<Application> getApplicationByService(Service service) {
        Query q = getEntityManager().createQuery("select F FROM Application F where F.service=:service");
        q.setParameter("service", service);
        List<Application> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
     
     
     
     public List<Service> getServiceByDep(Departement departement) {
        Query q = getEntityManager().createQuery("select S FROM  Service S where S.departement=:departement");
        q.setParameter("departement", departement);
        List<Service> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

     
         public List<Application> getAllDirecteurs() {
        Query q = getEntityManager().createQuery("select f FROM  Application f where f.Directeur=:dir");
        q.setParameter("dir", true);
        List<Application> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
     
}

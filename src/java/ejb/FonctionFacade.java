/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Fonction;
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
public class FonctionFacade extends AbstractFacade<Fonction> {

    Logger logger = Logger.getLogger(FonctionFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FonctionFacade() {
        super(Fonction.class);
    }

   

    @Override
    public void create(Fonction entity) {
//        entity.setId(compteurFacade.getNext(Fonction.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @param departement
     * @return liste des utilisateurs
     */
//    public List<Fonction> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from Fonction U order by U.nomPrenoms");
//        // set parameters
//        List<Fonction> suggestions = q.getResultList();
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
     public List<Fonction> getFonctionByService(Service service) {
        Query q = getEntityManager().createQuery("select F FROM Fonction F where F.service=:service");
        q.setParameter("service", service);
        List<Fonction> suggestions = q.getResultList();

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

     
         public List<Fonction> getAllDirecteurs() {
        Query q = getEntityManager().createQuery("select f FROM  Fonction f where f.Directeur=:dir");
        q.setParameter("dir", true);
        List<Fonction> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
     
}

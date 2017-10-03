/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Publication;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import jpa.Departement;
import jpa.Service;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class PublicationFacade extends AbstractFacade<Publication> {

    Logger logger = Logger.getLogger(PublicationFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    
    public String giveCode() {

        List<Publication> listLast = findLastPublication();
        if (listLast.isEmpty()) {
            return "2101" + JsfUtil.getCurrentYear() + "1";
        } else {
            String lastId = listLast.get(0).getCode();
            int numOrdre = Integer.parseInt(lastId.substring(8));
            return "2101" + JsfUtil.getCurrentYear() + (numOrdre + 1);
        }
    }
    
    
    public PublicationFacade() {
        super(Publication.class);
    }

   

    @Override
    public void create(Publication entity) {
          entity.setCode(giveCode());
          entity.setDateCreation(new Date());
        getEntityManager().persist(entity);
    }
    
    public String creatPublicationAndGiveCode(Publication entity){
    String code = giveCode();
        entity.setCode(code);
        entity.setDateCreation(new Date());
        getEntityManager().persist(entity);
        return code;
    }

    /**
     * trouve les exercices publications de l'utilisateur
     *
     *
     * @param departement
     * @return liste des utilisateurs
     */
//    public List<Publication> getAllUsers() {
//        Query q = getEntityManager().createQuery("sPect U from Publication U order by U.nomPrenoms");
//        // set parameters
//        List<Publication> suggestions = q.getResultList();
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
     public List<Publication> getPublicationByService(Service service) {
        Query q = getEntityManager().createQuery("sPect F FROM Publication F where F.service=:service");
        q.setParameter("service", service);
        List<Publication> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
     
     
     
     public List<Service> getServiceByDep(Departement departement) {
        Query q = getEntityManager().createQuery("sPect S FROM  Service S where S.departement=:departement");
        q.setParameter("departement", departement);
        List<Service> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

     
         public List<Publication> getAllDirecteurs() {
        Query q = getEntityManager().createQuery("sPect f FROM  Publication f where f.Directeur=:dir");
        q.setParameter("dir", true);
        List<Publication> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
         
           public List<Publication> findLastPublication() {
        Query q = getEntityManager().createQuery("SELECT P FROM Publication P WHERE P.dateCreation >= ALL(SELECT P1.dateCreation FROM Publication P1)");
        return q.getResultList();
    }
     
}

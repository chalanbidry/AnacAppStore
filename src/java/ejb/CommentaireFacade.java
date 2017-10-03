/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Commentaire;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Departement;
import jpa.Publication;
import jpa.Service;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class CommentaireFacade extends AbstractFacade<Commentaire> {

    Logger logger = Logger.getLogger(CommentaireFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommentaireFacade() {
        super(Commentaire.class);
    }

    public String giveCode() {

        List<Commentaire> listLast = findLastComment();
        if (listLast.isEmpty()) {
            return "2101" + JsfUtil.getCurrentYear() + "1";
        } else {
            String lastId = listLast.get(0).getCode();
            int numOrdre = Integer.parseInt(lastId.substring(8));
            return "2101" + JsfUtil.getCurrentYear() + (numOrdre + 1);
        }
    }

    @Override
    public void create(Commentaire entity) {
          entity.setCode(giveCode());
          entity.setDateCreation(new Date());
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices commentaires de l'utilisateur
     *
     *
     * @param pub
     * @param departement
     * @return liste des utilisateurs
     */
//    public List<Commentaire> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from Commentaire U order by U.nomPrenoms");
//        // set parameters
//        List<Commentaire> suggestions = q.getResultList();
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
     public List<Commentaire> getCommentaireByPub(Publication pub) {
        Query q = getEntityManager().createQuery("select c FROM Commentaire c where c.publication=:pub");
        q.setParameter("pub", pub);
        List<Commentaire> suggestions = q.getResultList();

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

     
         public List<Commentaire> getAllDirecteurs() {
        Query q = getEntityManager().createQuery("select f FROM  Commentaire f where f.Directeur=:dir");
        q.setParameter("dir", true);
        List<Commentaire> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
         
               public List<Commentaire> findLastComment() {
        Query q = getEntityManager().createQuery("SELECT C FROM Commentaire C WHERE C.dateCreation >= ALL(SELECT C1.dateCreation FROM Commentaire C1)");
        return q.getResultList();
    }
     
}

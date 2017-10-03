/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Utilisateur;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Application;
import jpa.Departement;
import jpa.Division;
import jpa.Fonction;
import jpa.Publication;
import jpa.Service;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur> {

    Logger logger = Logger.getLogger(UtilisateurFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UtilisateurFacade() {
        super(Utilisateur.class);
    }

  

    @Override
    public void create(Utilisateur entity) {
//        entity.setId(compteurFacade.getNext(Utilisateur.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public List<Utilisateur> getAllUsers() {
        Query q = getEntityManager().createQuery("select U from Utilisateur U order by U.nomPrenoms");
        // set parameters
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
     public List<Utilisateur> getAllUsersNotGroup() {
        Query q = getEntityManager().createQuery("select U from Utilisateur U where U NOT IN (SELECT Gu.utilisateur FROM GroupeUtilisateur Gu)");
        // set parameters
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }
     
        public Utilisateur getFonctionChef(Service service) {
        Query q = getEntityManager().createQuery("select U from Utilisateur U where U.fonction.service=:service and U.ChefService=:chefService");
        q.setParameter("service", service);
         q.setParameter("chefService", true);
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            return null;
        }else{
         return suggestions.get(0);
        }

    }
        
        public Utilisateur getFonctionDirecteur(Service service) {
        Query q = getEntityManager().createQuery("select U from Utilisateur U where U.fonction.departementDirec=:dep and U.fonction.Directeur=:isDirecteur");
        q.setParameter("dep", service.getDepartement());
         q.setParameter("isDirecteur", true);
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            return null;
        }else{
         return suggestions.get(0);
        }
        

    }
        
        
        public Utilisateur getFonctionDG() {
        Query q = getEntityManager().createQuery("select U from Utilisateur U where U.Dg=:dg");
         q.setParameter("dg", true);
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            return null;
        }else{
         return suggestions.get(0);
        }
        }
     
     public Utilisateur getUserByFonction(Fonction fonction) {
        Query q = getEntityManager().createQuery("select U from Utilisateur U where U.fonction=:fonction");
        q.setParameter("fonction", fonction);
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            return null;
        }else{
         return suggestions.get(0);
        }

    }
     
     
     public boolean getIfPassExist(String pass) {
        Query q = getEntityManager().createQuery("select U from Utilisateur U where U.password=:password");
        q.setParameter("password", pass);
        List<Utilisateur> suggestions = q.getResultList();
        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            return false;
        }else{
         return true;
        }

    }
     
     
     
      public List<Publication> getAllPubSend(Utilisateur user) {
        Query q = getEntityManager().createQuery("select Pub from Publication Pub where Pub.userEmetteur=:user ORDER BY Pub.dateCreation DESC");
        q.setParameter("user", user);
        List<Publication> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions==null) {
            suggestions=new ArrayList<>();
        }
        return  suggestions;

    }
    
     
      
      public List<Utilisateur> getAllUserChefServiceUntilMe(Utilisateur user,Departement direction) {
        Query q = getEntityManager().createQuery("select User from Utilisateur User where User.fonction.departementDirec=:direction and User.fonction.ChefService = :chef and User!= :user");
        q.setParameter("user", user);
        q.setParameter("direction", direction);
        q.setParameter("chef", true);
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions==null) {
            suggestions=new ArrayList<>();
        }
        return  suggestions;

    }
      
      
       public List<Utilisateur> getAllUserDirecteurUntilMe(Utilisateur user) {
        Query q = getEntityManager().createQuery("select User from Utilisateur User where User.fonction.Directeur= :directeur and User!= :user");
        q.setParameter("user", user);
        q.setParameter("directeur", true);
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions==null) {
            suggestions=new ArrayList<>();
        }
        return  suggestions;

    }
       
       
       
       public Utilisateur getUserDirecteurFromDirection(Departement dep) {
        Query q = getEntityManager().createQuery("select User from Utilisateur User where User.fonction.Directeur= :directeur and User.fonction.departementDirec = :dep");
        q.setParameter("dep", dep);
        q.setParameter("directeur", true);
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions==null) {
            suggestions=new ArrayList<>();
        }
        return suggestions.get(0) ;

    }
       
        public Utilisateur getUserChefServiceByService(Service ser) {
        Query q = getEntityManager().createQuery("select User from Utilisateur User where User.fonction.ChefService= :Cservice and User.fonction.service = :ser");
        q.setParameter("ser", ser);
        q.setParameter("Cservice", true);
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions==null) {
            suggestions=new ArrayList<>();
        }
        return suggestions.get(0) ;

    }
        
        
        public Utilisateur getUserChefDivisionByDivision(Division div) {
        Query q = getEntityManager().createQuery("select User from Utilisateur User where User.fonction.ChefDivision= :Cdivision and User.fonction.division = :div");
        q.setParameter("div", div);
        q.setParameter("Cdivision", true);
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions==null) {
            suggestions=new ArrayList<>();
        }
        return suggestions.get(0) ;

    }
        
         public List<Utilisateur> getUserSecretairesDirecction() {
        Query q = getEntityManager().createQuery("select User from Utilisateur User where User.secretaireDeDirection=:sec");
        q.setParameter("sec", true);
        List<Utilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions==null) {
            suggestions=new ArrayList<>();
        }
        return suggestions ;

    }
        
        
        
       
       
       
       
       
       
       
       
       
   
   

}

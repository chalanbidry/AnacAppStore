/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Groupe;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Departement;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class GroupeFacade extends AbstractFacade<Groupe> {

    Logger logger = Logger.getLogger(GroupeFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupeFacade() {
        super(Groupe.class);
    }

   

    @Override
    public void create(Groupe entity) {
//        entity.setId(compteurFacade.getNext(Groupe.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @param departement
     * @return liste des utilisateurs
     */
//    public List<Groupe> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from Groupe U order by U.nomPrenoms");
//        // set parameters
//        List<Groupe> suggestions = q.getResultList();
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
     public List<Groupe> getGroupeByDep(Departement departement) {
        Query q = getEntityManager().createQuery("select F FROM Groupe F where F.departement=:departement");
        q.setParameter("departement", departement);
        List<Groupe> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

}

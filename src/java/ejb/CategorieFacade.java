/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Categorie;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Materiel;

import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class CategorieFacade extends AbstractFacade<Categorie> {

    Logger logger = Logger.getLogger(CategorieFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
    public String giveCode() {

        List<Categorie> listLast = findLastCategorie();
        if (listLast.isEmpty()) {
            return "2101" + JsfUtil.getCurrentYear() + "1";
        } else {
            String lastId = listLast.get(0).getCode();
            int numOrdre = Integer.parseInt(lastId.substring(8));
            return "2101" + JsfUtil.getCurrentYear() + (numOrdre + 1);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategorieFacade() {
        super(Categorie.class);
    }

   

    @Override
    public void create(Categorie entity) {
 entity.setCode(giveCode());
 entity.setDateCreation(new Date());
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
//    public List<Categorie> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from Categorie U order by U.nomPrenoms");
//        // set parameters
//        List<Categorie> suggestions = q.getResultList();
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
    
     public List<Categorie> findLastCategorie() {
        Query q = getEntityManager().createQuery("SELECT cat FROM Categorie cat WHERE cat.dateCreation >= ALL(SELECT cat1.dateCreation FROM Categorie cat1)");
        return q.getResultList();
    }
     
     
       public List<Materiel> findListMaterielForCat(Categorie categorie) {
        Query q = getEntityManager().createQuery("SELECT mat FROM Materiel mat WHERE mat.Categorie=:cat");
        q.setParameter("cat",categorie);
        return q.getResultList();
    }

}

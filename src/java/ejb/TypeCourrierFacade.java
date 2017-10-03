/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.TypeCourrier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.CategorieCourrier;
import jpa.Materiel;

import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class TypeCourrierFacade extends AbstractFacade<TypeCourrier> {

    Logger logger = Logger.getLogger(TypeCourrierFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
    public String giveId() {

        List<TypeCourrier> listLast = findLastTypeCourrier();
        if (listLast.isEmpty()) {
            return "2101" + JsfUtil.getCurrentYear() + "1";
        } else {
            String lastId = listLast.get(0).getId();
            int numOrdre = Integer.parseInt(lastId.substring(8));
            return "2101" + JsfUtil.getCurrentYear() + (numOrdre + 1);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypeCourrierFacade() {
        super(TypeCourrier.class);
    }

   

    @Override
    public void create(TypeCourrier entity) {
 entity.setId(giveId());
 entity.setDateCreation(new Date());
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
//    public List<TypeCourrier> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from TypeCourrier U order by U.nomPrenoms");
//        // set parameters
//        List<TypeCourrier> suggestions = q.getResultList();
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
    
     public List<TypeCourrier> findLastTypeCourrier() {
        Query q = getEntityManager().createQuery("SELECT typC FROM TypeCourrier typC WHERE typC.dateCreation >= ALL(SELECT typC1.dateCreation FROM TypeCourrier typC1)");
        return q.getResultList();
    }
     
     
       public List<Materiel> findListMaterielForCat(TypeCourrier typeCourrier) {
        Query q = getEntityManager().createQuery("SELECT mat FROM Materiel mat WHERE mat.TypeCourrier=:cat");
        q.setParameter("cat",typeCourrier);
        return q.getResultList();
    }
       
        public List<TypeCourrier> findListTypeCourrierByCate(CategorieCourrier categorieCourrier) {
        Query q = getEntityManager().createQuery("SELECT typCou FROM TypeCourrier typCou WHERE TypCou.categorieCourrier=:cat");
        q.setParameter("cat",categorieCourrier);
        return q.getResultList();
    }

}

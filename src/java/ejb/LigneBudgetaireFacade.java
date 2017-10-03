/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.LigneBudgetaire;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Exercice;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class LigneBudgetaireFacade extends AbstractFacade<LigneBudgetaire> {

    Logger logger = Logger.getLogger(LigneBudgetaire.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
    public String giveId() {

        List<LigneBudgetaire> listLast = findLastLigne();
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

    public LigneBudgetaireFacade() {
        super(LigneBudgetaire.class);
    }

   

    @Override
    public void create(LigneBudgetaire entity) {
//        entity.setId(compteurFacade.getNext(Exercice.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
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
    public LigneBudgetaire getCourrantExo() {
        Query q = getEntityManager().createQuery("select E from Exercice E where E.courant=:exo");
        // set parameters
        q.setParameter("exo", true);
        List<LigneBudgetaire> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            return null;
        }

        // return the suggestions
        return suggestions.get(0);

    }
    
     public List<LigneBudgetaire> findLastLigne() {
        Query q = getEntityManager().createQuery("SELECT ligne FROM LigneBudgetaire ligne  WHERE ligne.dateCreation >= ALL(SELECT ligne1.dateCreation FROM LigneBudgetaire ligne1)");
        return q.getResultList();
    }
     
      public List<LigneBudgetaire> findLigneByExo(Exercice exo) {
        Query q = getEntityManager().createQuery("SELECT ligne FROM  LigneBudgetaire ligne where ligne.exercice =:exo");
        q.setParameter("exo", exo);
        return q.getResultList();
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import controller.ConnexionBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Element;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class ElementFacade extends AbstractFacade<Element> {

    Logger logger = Logger.getLogger(ElementFacade.class);
    @Inject
    ConnexionBean connexionBean;
    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ElementFacade() {
        super(Element.class);
    }

    public String giveCode() {

        List<Element> listLast = findLastElement();
        if (listLast.isEmpty()) {
            return "2101" + JsfUtil.getCurrentYear() + "1";
        } else {
            String lastId = listLast.get(0).getCode();
            int numOrdre = Integer.parseInt(lastId.substring(8));
            return "2101" + JsfUtil.getCurrentYear() + (numOrdre + 1);
        }
    }

    @Override
    public void create(Element entity) {
        entity.setCode(giveCode());
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
//    public List<Element> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from Element U order by U.nomPrenoms");
//        // set parameters
//        List<Element> suggestions = q.getResultList();
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
    public List<Element> findLastElement() {
        Query q = getEntityManager().createQuery("SELECT el FROM Element el WHERE el.dateCreation >= ALL(SELECT el1.dateCreation FROM Element el1)");
        return q.getResultList();
    }

}

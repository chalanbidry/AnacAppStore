/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.BonCommande_Element;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Boncommande;
import jpa.Departement;
import jpa.Service;
import jpa.Utilisateur;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class BonCommande_ElementFacade extends AbstractFacade<BonCommande_Element> {

    Logger logger = Logger.getLogger(BonCommande_ElementFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BonCommande_ElementFacade() {
        super(BonCommande_Element.class);
    }

    public String giveCode() {

        BonCommande_Element lastBonCommande_Element = getLastBonCommande();
        String code = "";
        if (lastBonCommande_Element != null) {
            String CodeLast = lastBonCommande_Element.getBonCommandeCode();
            int position = Integer.valueOf(CodeLast);

            if (position != 999999) {
                int taille = String.valueOf(position + 1).length();
                for (int i = 1; i <= (6 - taille); i++) {
                    code = code + "0";
                }
                code = code + (String.valueOf(position + 1));

            }
            return code;
        } else {
            code = "000001";
            return code;
        }
    }

    @Override
    public void create(BonCommande_Element entity) {
   
        getEntityManager().persist(entity);
    }

    

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public BonCommande_Element getLastBonCommande() {
        Query q = getEntityManager().createQuery("select bc from BonCommande_Element bc where bc.dateCreation >= ALL (SELECT bc1.dateCreation FROM BonCommande_Element bc1)");
        // set parameters
        List<BonCommande_Element> suggestions = q.getResultList();
        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            return null;
        }
        // return the suggestions
        return suggestions.get(0);

    }
    
    
     
    public List<BonCommande_Element> getListBonCommande_Element(Boncommande bc) {
        Query q = getEntityManager().createQuery("select bc_e from BonCommande_Element bc_e where bc_e.bonCommande=:bonCommande");
        // set parameters
        q.setParameter("bonCommande", bc);
        List<BonCommande_Element> suggestions = q.getResultList();
        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            suggestions=new ArrayList<>();
        }
        // return the suggestions
        return suggestions;

    }

}

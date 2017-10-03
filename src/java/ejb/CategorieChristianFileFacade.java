/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.CategorieChristianFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Utilisateur;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class CategorieChristianFileFacade extends AbstractFacade<CategorieChristianFile> {

    Logger logger = Logger.getLogger(CategorieChristianFileFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategorieChristianFileFacade() {
        super(CategorieChristianFile.class);
    }

    public String giveCode() {

        CategorieChristianFile lastEvenement = getLastCategorie();

        String code = "";
        if (lastEvenement != null) {
            String CodeLast = lastEvenement.getCode();
            int position = Integer.valueOf(CodeLast);
            if (position != 999999) {

                int taille = String.valueOf(position + 1).length();
                for (int i = 1; i <= (6 - taille); i++) {
                    code = code + "0";
                }
                code = code + (String.valueOf(position + 1));

            }
        } else  {
            code = "000001";
        }
        return code;
    }

    @Override
    public void create(CategorieChristianFile entity) {
        entity.setCode(giveCode());
        getEntityManager().persist(entity);
    }

    public String createAndGidCode(CategorieChristianFile entity) {
        String code = giveCode();
        entity.setCode(code);
        entity.setDateCreation(new Date());
        getEntityManager().persist(entity);
        return code;
    }

    public CategorieChristianFile getLastCategorie() {
        Query q = getEntityManager().createQuery("select d from CategorieChristianFile d where d.dateCreation >= ALL (SELECT d1.dateCreation FROM CategorieChristianFile d1)");
        // set parameters
        List<CategorieChristianFile> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions.isEmpty()) {
            System.out.println("C'est nuel");
            return null;
        }else{
        // return the suggestions
        return suggestions.get(0);
        }

    }
}

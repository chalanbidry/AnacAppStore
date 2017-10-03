/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Courrier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.CategorieCourrier;
import jpa.Departement;
import jpa.Exercice;
import jpa.Materiel;
import jpa.TypeCourrier;
import jpa.Utilisateur;

import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class CourrierFacade extends AbstractFacade<Courrier> {

    Logger logger = Logger.getLogger(CourrierFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    public String giveCode() {

        List<Courrier> listLast = findLastCourrier();
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

    public CourrierFacade() {
        super(Courrier.class);
    }

    @Override
    public void create(Courrier entity) {
        entity.setCode(giveCode());
        entity.setDateCreation(new Date());
        getEntityManager().persist(entity);
    }

    public String createAndGidCode(Courrier entity) {
        String code = giveCode();
        entity.setCode(code);
        getEntityManager().persist(entity);
        return code;
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
//    public List<Courrier> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from Courrier U order by U.nomPrenoms");
//        // set parameters
//        List<Courrier> suggestions = q.getResultList();
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
    public List<Courrier> findLastCourrier() {
        Query q = getEntityManager().createQuery("SELECT cat FROM Courrier cat WHERE cat.dateCreation >= ALL(SELECT cat1.dateCreation FROM Courrier cat1)");
        return q.getResultList();
    }

    public List<Courrier> findListCourrierInitie(Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT cour FROM Courrier cour WHERE cour.initiateur=:user ORDER BY cour.dateCreation DESC");
        q.setParameter("user", user);
        return q.getResultList();
    }

    public List<Courrier> findListCourrierRecherche(Departement direction, CategorieCourrier categorieCourrier, TypeCourrier typeCourrier) {
        Query q = getEntityManager().createQuery("SELECT p1.courrier FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier)and p1.evenement.Code=:code1 and p1.courrier.typeCourrier.categorieCourrier=:cat and p1.courrier.typeCourrier=:type and p1.courrier.initiateur.fonction.departementDirec=:direction");
        q.setParameter("code1", "Cou0010");
        q.setParameter("direction", direction);
        q.setParameter("cat", categorieCourrier);
        q.setParameter("type", typeCourrier);
        return q.getResultList();
    }

    public List<Courrier> findListCourrierRechercheByDateArchive(TypeCourrier typeCourrier, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("SELECT p1.courrier FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier)and p1.evenement.Code=:code1 and p1.courrier.typeCourrier=:type and p1.dateCreation BETWEEN :date1 and :date2");
        q.setParameter("code1", "Cou0010");
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        q.setParameter("type", typeCourrier);
        return q.getResultList();
    }
    
    public List<Courrier> findListCourrierArriRechercheByDateArchive(TypeCourrier typeCourrier, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("SELECT p1.courrier FROM ProgressionCourrier p1 WHERE p1.evenement.Code=:even and p1.courrier.typeCourrier=:type and p1.dateCreation BETWEEN :date1 and :date2  ORDER BY p1.dateCreation DESC");
        q.setParameter("even", "Cou0014");
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        q.setParameter("type", typeCourrier);
        return q.getResultList();
    }
    
    
    public List<Courrier> findListCourrierArriRechercheByDateArchiveInDirection(TypeCourrier typeCourrier, Date date1, Date date2,Departement departement) {
        Query q = getEntityManager().createQuery("SELECT p1.courrier FROM ProgressionCourrier p1 WHERE p1.evenement.Code=:even1 and p1.courrier.typeCourrier=:type and p1.dateCreation BETWEEN :date1 and :date2 and p1.courrier IN (select p2.courrier FROM ProgressionCourrier p2 where p2.evenement.Code=:even2 and p2.directionConcernee=p1.directionConcernee) and p1.directionConcernee=:departement ORDER BY p1.dateCreation DESC ");
        q.setParameter("even1", "Cou0016");
          q.setParameter("even2", "Cou0022");
          q.setParameter("departement", departement);
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        q.setParameter("type", typeCourrier);
        return q.getResultList();
    }
    
    

    public List<Materiel> findListMaterielForCat(Courrier courrier) {
        Query q = getEntityManager().createQuery("SELECT mat FROM Materiel mat WHERE mat.Courrier=:cat");
        q.setParameter("cat", courrier);
        return q.getResultList();
    }

    public String findNextNumCourArriCourrier(Exercice exo) {
        Query q = getEntityManager().createQuery("SELECT cour FROM Courrier cour WHERE cour.dateCreation >= ALL(SELECT cour1.dateCreation FROM Courrier cour1 where cour1.typeCourrier.categorieCourrier=:cateArrive and cour1.exercice.Libelle=:exo) and cour.typeCourrier.categorieCourrier=:cateArrive  and cour.exercice.Libelle=:exo");
        q.setParameter("cateArrive", CategorieCourrier.CourrierArrive);
        q.setParameter("exo", exo.getLibelle());
        List<Courrier> courriers = q.getResultList();
        String code = "";
        if (!courriers.isEmpty()) {

            int position = Integer.valueOf(courriers.get(0).getNumCourrierArrive());

            if (position != 9999) {
                int taille = String.valueOf(position + 1).length();
                for (int i = 1; i <= (4 - taille); i++) {
                    code = code + "0";
                }
                code = code + (String.valueOf(position + 1));

            }
            return code;
        } else {
            code = "0001";
            return code;
        }

    }

}

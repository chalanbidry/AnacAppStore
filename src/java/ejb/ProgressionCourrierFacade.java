/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.ProgressionCourrier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.PostPersist;
import jpa.Courrier;
import jpa.Departement;
import jpa.Evenement;
import jpa.Fonction;
import jpa.Service;
import jpa.Utilisateur;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class ProgressionCourrierFacade extends AbstractFacade<ProgressionCourrier> {

    Logger logger = Logger.getLogger(ProgressionCourrierFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProgressionCourrierFacade() {
        super(ProgressionCourrier.class);
    }

    public String giveCode() {

        ProgressionCourrier lastProgressionCourrier = getLastProgressionCourrier();

        String id = "", inc = "";
        if (lastProgressionCourrier != null) {
            String idLast = lastProgressionCourrier.getCode();
            String lastPart = idLast.substring(8);
            int position = Integer.valueOf(lastPart);
            if (position != 999999) {
                int taille = String.valueOf(position + 1).length();
                for (int i = 1; i <= (6 - taille); i++) {
                    inc = inc + "0";
                }
                inc = inc + (String.valueOf(position + 1));

                id = "1101" + JsfUtil.getCurrentYear() + inc;
            }
        } else {
            inc = "1";
            id = "1101" + JsfUtil.getCurrentYear() + "00000" + inc;
        }
        return id;
    }

    @Override
    public void create(ProgressionCourrier entity) {
        entity.setCode(giveCode());
        getEntityManager().persist(entity);
//        getEntityManager().flush();
//        getEntityManager().refresh(entity);
    }

    @PostPersist
    public void refresh(ProgressionCourrier entity) {
        getEntityManager().refresh(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public List<ProgressionCourrier> getAllUsers() {
        Query q = getEntityManager().createQuery("select U from ProgressionCourrier U order by U.nomPrenoms");
        // set parameters
        List<ProgressionCourrier> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

    public List<ProgressionCourrier> getAllProgressionCourrierByUser(Utilisateur user) {
        Query q = getEntityManager().createQuery("select d from ProgressionCourrier d where d.utilisateurSend=:user order by d.dateCreation DESC");
        q.setParameter("user", user);
        List<ProgressionCourrier> suggestions = q.getResultList();

        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public List<ProgressionCourrier> getAllProgressionCourrierByUserDate(Utilisateur user, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("select d from ProgressionCourrier d where d.utilisateurSend=:user AND d.date BETWEEN :date1 AND :date2 order by d.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<ProgressionCourrier> suggestions = q.getResultList();
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public ProgressionCourrier getLastProgressionCourrier() {
        Query q = getEntityManager().createQuery("select d from ProgressionCourrier d where d.dateCreation >= ALL (SELECT d1.dateCreation FROM ProgressionCourrier d1)");
        // set parameters
        List<ProgressionCourrier> suggestions = q.getResultList();
        if (suggestions.isEmpty()) {
            return null;
        } else {
            return suggestions.get(0);
        }

    }

    /**
     * Renvoie la dernière progression du dossier
     *
     *
     * @param courrier
     * @return Une ligne de progression Dossiers
     */
    public ProgressionCourrier getLastProgressionCourrier(Courrier courrier) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier)AND p1.courrier=:courrier");
        q.setParameter("courrier", courrier);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p.get(0);
        }
    }

    public ProgressionCourrier getLastProgressionCourrierSendToUser(Courrier courrier, Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier and p2.userReceive=p1.userReceive)AND p1.courrier=:courrier and p1.userReceive=:user");
        q.setParameter("courrier", courrier);
        q.setParameter("user", user);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p.get(0);
        }
    }

    /**
     * Renvoie la dernière progression du dossier
     *
     *
     * @param user
     * @return Une ligne de progression Dossiers
     */
    public List<Courrier> getCourrierCreesAndYear(Utilisateur user) {
        System.out.println("le user est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1.demande FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.demande = p1.demande)AND p1.demande.utilisateurSend=:user and p1.userReceive=:user and p1.evenement.Code=:code ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("code", "Dem001");
        List<Courrier> p;
        p = q.getResultList();
        System.out.println("le resultat est " + p);
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }

        return p;
    }

    public List<ProgressionCourrier> getListAllProgressionFromCourrier(Courrier courrier) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.courrier=:courrier ORDER BY p1.dateCreation ASC");
        q.setParameter("courrier", courrier);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        for (ProgressionCourrier pc : p) {
            System.out.println("les evennements sont ..." + pc.getCode() + " user : " + pc.getUserSend().getFonction().getCode() + " " + pc.getEvenement().getLibelle() + "/n");
        }
        return p;
    }

    public List<ProgressionCourrier> getListAllLastProgressionCourrierInDireection(Courrier courrier) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier and p2.directionConcernee = p1.directionConcernee) and p1.courrier = :courrier and p1.directionConcernee IS NOT NULL");
        q.setParameter("courrier", courrier);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        for (ProgressionCourrier pc : p) {
            System.out.println("les evennements sont ..." + pc.getCode() + " user : " + pc.getUserSend().getFonction().getCode() + " " + pc.getEvenement().getLibelle() + "/n");
        }
        return p;
    }

    public boolean getIfAlredyTraitByDG(Courrier courrier) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.courrier=:courrier and p1.evenement.Code=:even");
        q.setParameter("courrier", courrier);
        q.setParameter("even", "Cou0016");
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
           return false;
        }

        return true;
    }

    /**
     * Renvoie la dernière progression du dossier
     *
     *
     * @param demande
     * @param user
     * @return Une ligne de progression Dossiers
     */
    public boolean getLastProgressionCourrierForUser(Courrier demande, Utilisateur user) {
        Query q;
        if (user.getFonction().getCode().equals("CSARH")) {
            q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.demande = p1.demande)AND p1.demande=:demande and p1.userReceive =:user and p1.evenement.Code =:code1 OR p1.evenement.Code=:code2 ");
            q.setParameter("demande", demande);
            q.setParameter("user", user);
            q.setParameter("code1", "Dem002");
            q.setParameter("code2", "Dem0011");
        } else {
            q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.demande = p1.demande)AND p1.demande=:demande and p1.userReceive =:user and p1.evenement.Code =:code ");
            q.setParameter("demande", demande);
            q.setParameter("user", user);
            q.setParameter("code", "Dem002");

        }

        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public List<ProgressionCourrier> getLastProgressionAllCourrier() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.demande = p1.demande)AND p1.evenement.Code=:even ORDER BY p1.dateCreation DESC");
        q.setParameter("even", "Dem003");
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }

    public List<ProgressionCourrier> getLastProgressionAllCourrierSendDG() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.demande = p1.demande)AND p1.evenement.Code=:even ORDER BY p1.dateCreation DESC");
        q.setParameter("even", "Dem004");
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }

    public List<ProgressionCourrier> getLastProgressionAllCourrierSendCSFC() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.demande = p1.demande) and p1.statut!=:statut AND p1.demande  IN (SELECT p3.demande from ProgressionCourrier p3 where p3.evenement.Code=:even) ORDER BY p1.dateCreation DESC");
        q.setParameter("even", "Dem005");
        q.setParameter("statut", "Terminée");
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }

    public List<ProgressionCourrier> getLastProgressionAllCourrierSendCSFCAndNonFinsh(Fonction fonction) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.demande = p1.demande)AND p1.statut != :statut and p1.userReceive.fonction=:function ORDER BY p1.dateCreation DESC");
        q.setParameter("statut", "Terminée");
        q.setParameter("function", fonction);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }

    public List<ProgressionCourrier> getLastProgressionAllCourrierSendByMeAndNonFinsh(Fonction fonction) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.demande = p1.demande)AND p1.statut != :statut and p1.demande.utilisateurSend.fonction=:function ORDER BY p1.dateCreation DESC");
        q.setParameter("statut", "Terminée");
        q.setParameter("function", fonction);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return null;
        } else {
            return p;
        }
    }

    public List<Courrier> getListAllProgressionFromCourrierTermine(Utilisateur user, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("SELECT p1.demande FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.demande = p1.demande)AND p1.userReceive=:user and p1.statut=:statut and p1.evenement.Code=:code and p1.dateCreation BETWEEN :date1 and :date2 ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("statut", "Terminée");
        q.setParameter("code", "Dem0010");
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<Courrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<Courrier> getListAllProgressionFromCourrierCreatByMe(Utilisateur user, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("SELECT p1.demande FROM ProgressionCourrier p1 WHERE  p1.demande.utilisateurSend=:user and p1.statut!=:statut and p1.evenement.Code=:code and p1.dateCreation BETWEEN :date1 and :date2 ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("statut", "Terminée");
        q.setParameter("code", "Dem001");
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<Courrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<Courrier> getListAllProgressionFromCourrierCreatInService(Service service, Date date1, Date date2) {
        Query q = getEntityManager().createQuery("SELECT p1.demande FROM ProgressionCourrier p1 WHERE  p1.demande.utilisateurSend.fonction.service=:service and p1.statut!=:statut and p1.evenement.Code=:code and p1.dateCreation BETWEEN :date1 and :date2 ORDER BY p1.dateCreation DESC");
        q.setParameter("service", service);
        q.setParameter("statut", "Terminée");
        q.setParameter("code", "Dem001");
        q.setParameter("date1", date1);
        q.setParameter("date2", date2);
        List<Courrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> getListAllProgressionFromReception(Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier)AND p1.statut != :statut and p1.userReceive=:user  ORDER BY p1.dateCreation DESC");
        q.setParameter("statut", "Nouveau Courrier");
//        q.setParameter("code", "Cou002");
        q.setParameter("user", user);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> getListAllProgressionFromBoitEnvoi(Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE  p1.statut = :statut and p1.userReceive=:user and p1.evenement.Code=:code ORDER BY p1.dateCreation DESC");
        q.setParameter("statut", "Nouveau Courrier");
        q.setParameter("code", "Cou001");
        q.setParameter("user", user);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> getListAllProgressionFromSendForVisa(Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier) AND  p1.statut != :statut and p1.userReceive=:user and p1.evenement.Code IN(:code1,:code2,:code3,:code4,:code5) ORDER BY p1.dateCreation DESC");
        q.setParameter("statut", "Nouveau Courrier");
        q.setParameter("code1", "Cou002");
        q.setParameter("code2", "Cou003");
        q.setParameter("code3", "Cou007");
        q.setParameter("code4", "Cou0012");
        q.setParameter("code5", "Cou0013");
        q.setParameter("user", user);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> getListAllProgressionAlredyVisa(Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE  p1.statut != :statut and p1.evenement.Code IN (:code1,:code2,:code3) and p1.userSend=:user ORDER BY p1.dateCreation DESC");
        q.setParameter("statut", "Nouveau Courrier");
        q.setParameter("code1", "Cou002");
        q.setParameter("code2", "Cou007");
        q.setParameter("code3", "Cou008");
        q.setParameter("user", user);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> getListAllProgressionFromSendForSign(Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier) AND  p1.statut != :statut and p1.userReceive=:user and p1.evenement.Code IN(:code1,:code2,:code3)  ORDER BY p1.dateCreation DESC");
        q.setParameter("statut", "Nouveau Courrier");
        q.setParameter("code1", "Cou008");
        q.setParameter("code2", "Cou0012");
        q.setParameter("code3", "Cou0013");
        q.setParameter("user", user);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> getListAllProgressionFromGiveDGForSign(Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier) AND  p1.statut != :statut and p1.userReceive=:user and p1.evenement.Code IN(:code1,:code2,:code3)  ORDER BY p1.dateCreation DESC");
        q.setParameter("statut", "Nouveau Courrier");
        q.setParameter("code1", "Cou0011");
        q.setParameter("code2", "Cou0012");
        q.setParameter("code3", "Cou0013");
        q.setParameter("user", user);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> getListPgAttenteArchive(Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier) AND   p1.userReceive=:user and p1.evenement.Code IN(:code1,:code2,:code3)  ORDER BY p1.dateCreation DESC");
        q.setParameter("code1", "Cou009");
        q.setParameter("code2", "Cou0012");
        q.setParameter("code3", "Cou0013");
        q.setParameter("user", user);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> getListPgAttenteReInsertion(Utilisateur user) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier) AND   p1.userReceive=:user and p1.courrier.typeSignature=:typeSign  ORDER BY p1.dateCreation DESC");
        q.setParameter("typeSign", "Physique");
        q.setParameter("user", user);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> getListPgCourrierArriveSave() {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.evenement.Code=:code1 or p1.evenement.Code=:code2  ORDER BY p1.dateCreation DESC");
        q.setParameter("code1", "Cou0014");
        q.setParameter("code2", "Cou0020");
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public boolean getIfCourrierArriveIsSend(Courrier courrier) {
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier) and p1.courrier=:courrier and (p1.evenement.Code=:code1 or p1.evenement.Code=:code2)");
        q.setParameter("code1", "Cou0014");
        q.setParameter("code2", "Cou0020");
        q.setParameter("courrier", courrier);
        List<ProgressionCourrier> p;
        p = q.getResultList();

        return p.isEmpty();
    }

    public List<ProgressionCourrier> findListCourrierArrReceiveByDG(Utilisateur user) {
        System.out.println("uers est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier) AND   p1.userReceive=:user and p1.evenement.Code=:even  ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("even", "Cou0015");
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public int findNbrCourrierArrReceiveFromDG(Utilisateur user, Evenement even) {
        System.out.println("uers est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier and p2.directionConcernee=:direc ) AND   p1.userReceive=:user and p1.evenement=:even  and p1.directionConcernee=:direc  ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("direc", user.getFonction().getDepartementDirec());
        q.setParameter("even", even);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p.size();
    }

    public int findNbrCourrierArrReceiveFromDirecteur(Utilisateur user, Evenement even) {
        System.out.println("uers est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier and p2.serviceConcerne=:ser ) AND   p1.userReceive=:user and p1.evenement=:even  and  p1.serviceConcerne=:ser  ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("ser", user.getFonction().getService());
        q.setParameter("even", even);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p.size();
    }

    public int findNbrCourrierArrReceiveFromChefService(Utilisateur user, Evenement even) {
        System.out.println("uers est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier and p2.divisionConcernee=:div ) AND   p1.userReceive=:user and p1.evenement=:even  and  p1.divisionConcernee=:div  ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("div", user.getFonction().getDivision());
        q.setParameter("even", even);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p.size();
    }

    public int findNbrCourrierArrReceiveFromSAOrSC(Utilisateur user, Evenement even) {
        System.out.println("uers est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier) AND   p1.userReceive=:user and p1.evenement=:even   ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("even", even);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p.size();
    }
    
    
    
    public int findNbrCourrierArrReceiveFromSCOrCreateByMe(Utilisateur user, Evenement even1,Evenement even2) {
        System.out.println("uers est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier) AND   p1.userReceive=:user and (p1.evenement=:even1 or p1.evenement=:even2)   ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("even1", even1);
        q.setParameter("even2", even2);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        System.out.println("on a "+p);
        return p.size();
    }
    
    
    
    

    public List<ProgressionCourrier> findListCourrierArrReceiveFromDG(Utilisateur user, Evenement even1, Evenement evenAR) {
        System.out.println("uers est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier and p2.directionConcernee=:direc ) AND  p1.userReceive=:user and ( p1.evenement=:even1 OR p1.evenement=:evenAR)  and p1.directionConcernee=:direc  ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("direc", user.getFonction().getDepartementDirec());
        q.setParameter("even1", even1);
        q.setParameter("evenAR", evenAR);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> findListCourrierArrReceiveFromDirecteur(Utilisateur user, Evenement even1, Evenement evenAR) {
        System.out.println("uers est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier and p2.serviceConcerne=:ser ) AND  p1.userReceive=:user and ( p1.evenement=:even1 OR p1.evenement=:evenAR)  and p1.serviceConcerne=:ser  ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("ser", user.getFonction().getService());
        q.setParameter("even1", even1);
        q.setParameter("evenAR", evenAR);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> findListCourrierArrReceiveFromChefService(Utilisateur user, Evenement even1, Evenement evenAR) {
        System.out.println("uers est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier and p2.divisionConcernee=:div ) AND  p1.userReceive=:user and ( p1.evenement=:even1 OR p1.evenement=:evenAR)  and p1.divisionConcernee=:div  ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("div", user.getFonction().getDivision());
        q.setParameter("even1", even1);
        q.setParameter("evenAR", evenAR);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public boolean findIfCourrierrIsAlredyAR(Utilisateur user, Courrier courrier) {
        System.out.println("uers est " + user.getLogin());
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE p1.dateCreation >= ALL (SELECT p2.dateCreation FROM ProgressionCourrier p2 WHERE p2.courrier = p1.courrier and p2.directionConcernee=:direc and p2.userReceive=p1.userReceive ) AND  p1.userReceive=:user and  p1.evenement.Code IN (:even1,:even2,:even3) and p1.courrier=:cour  and p1.directionConcernee=:direc  ORDER BY p1.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("cour", courrier);
        q.setParameter("direc", user.getFonction().getDepartementDirec());
        q.setParameter("even1", "Cou0016");
        q.setParameter("even2", "Cou0017");
        q.setParameter("even3", "Cou0018");
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            return true;
        }
        return false;
    }

    public ProgressionCourrier findUserInstrute(Utilisateur user, Courrier courrier) {
        String even = "";
        if (user.isDirecteur()) {
            even = "Cou0016";
        }
        if (user.isChefService()) {
            even = "Cou0017";
        }
        if (user.getFonction().isChefDivision()) {
            even = "Cou0018";
        }
        Query q = getEntityManager().createQuery("SELECT p1 FROM ProgressionCourrier p1 WHERE  p1.userReceive=:user and  p1.evenement.Code =:even and p1.courrier=:cour ");
        q.setParameter("user", user);
        q.setParameter("cour", courrier);
        q.setParameter("even", even);

        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        System.out.println("le pO est " + "et la taille est " + p.size() + " et " + "even = " + even + " " + p.get(0).getCode());
        return p.get(0);
    }

    public List<ProgressionCourrier> findPGForTypeUser(Courrier courrier, String codeFonction) {

        Query q = getEntityManager().createQuery("SELECT DISTINCT p1 FROM ProgressionCourrier p1 WHERE  p1.userSend.fonction.Code=:code  and   p1.courrier=:courrier ORDER BY p1.dateCreation ASC");
        q.setParameter("code", codeFonction);
        q.setParameter("courrier", courrier);

        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }

    public List<ProgressionCourrier> findPGForUserSend(Courrier courrier, Utilisateur user) {

        Query q = getEntityManager().createQuery("SELECT DISTINCT p1 FROM ProgressionCourrier p1 WHERE  p1.userSend=:user  and   p1.courrier=:courrier ORDER BY p1.dateCreation ASC");
        q.setParameter("user", user);
        q.setParameter("courrier", courrier);

        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }
    
    
    public List<Courrier> findCourrierAttenteTraitement(Departement direction) {

        Query q = getEntityManager().createQuery("select p1.courrier FROM ProgressionCourrier p1 where p1.evenement.Code=:even1 and p1.courrier NOT IN (Select p2.courrier from ProgressionCourrier p2 where p2.evenement.Code=:even2 and p2.directionConcernee=p1.directionConcernee) and p1.directionConcernee=:direction ORDER BY p1.dateCreation DESC ");
        q.setParameter("direction", direction);
        q.setParameter("even1", "Cou0016");
        q.setParameter("even2", "Cou0021");
        List<Courrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }
    
    
    
    public List<ProgressionCourrier> findCourrierInDirectionNotArchive(Departement direction) {

        Query q = getEntityManager().createQuery("select p1 FROM ProgressionCourrier p1 where p1.evenement.Code=:even1 and p1.courrier NOT IN (Select p2.courrier from ProgressionCourrier p2 where p2.evenement.Code=:even2 and p2.directionConcernee=p1.directionConcernee) and p1.directionConcernee=:direction ORDER BY p1.dateCreation DESC ");
        q.setParameter("direction", direction);
        q.setParameter("even1", "Cou0016");
        q.setParameter("even2", "Cou0022");
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if (p.isEmpty()) {
            p = new ArrayList<>();
        }
        return p;
    }
    
    
    
    
    public boolean getIfCourrierSendWithDelai(Courrier courrier) {
        Query q = getEntityManager().createQuery("SELECT p1 From ProgressionCourrier p1 where p1.evenement.Code=:code1 and p1.courrier=:courrier and  p1.dateLimite IS NOT NULL");
        q.setParameter("code1", "Cou0017");
        q.setParameter("courrier", courrier);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if(p.isEmpty()){
         return false;
        }else{
        return true;
        }
    }
    
    public boolean getIfCourrierAlredyTermine(Courrier courrier) {
        Query q = getEntityManager().createQuery("SELECT p1 From ProgressionCourrier p1 where p1.evenement.Code=:code1 and p1.courrier=:courrier");
        q.setParameter("code1", "Cou0021");
        q.setParameter("courrier", courrier);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if(p.isEmpty()){
         return false;
        }else{
        return true;
        }
    }
    
    
    
    
    public ProgressionCourrier getPgFromDelai(Courrier courrier) {
        Query q = getEntityManager().createQuery("SELECT p1 From ProgressionCourrier p1 where p1.evenement.Code=:code1 and p1.courrier=:courrier and  p1.dateLimite IS NOT NULL");
        q.setParameter("code1", "Cou0017");
        q.setParameter("courrier", courrier);
        List<ProgressionCourrier> p;
        p = q.getResultList();
        if(p.isEmpty()){
         p=new ArrayList<>();
        }
        return p.get(0);
    }
    
    
    
    

}

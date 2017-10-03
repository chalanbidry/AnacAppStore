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
import jpa.Notification;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Application;
import jpa.Courrier;
import jpa.Demande;
import jpa.Utilisateur;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class NotificationFacade extends AbstractFacade<Notification> {

    Logger logger = Logger.getLogger(NotificationFacade.class);
    @Inject
    ConnexionBean connexionBean;
    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificationFacade() {
        super(Notification.class);
    }

    public String giveCode() {

        List<Notification> listLast = findLastNotification();
        if (listLast.isEmpty()) {
            return "2101" + JsfUtil.getCurrentYear() + "1";
        } else {
            String lastId = listLast.get(0).getCode();
            int numOrdre = Integer.parseInt(lastId.substring(8));
            return "2101" + JsfUtil.getCurrentYear() + (numOrdre + 1);
        }
    }

    @Override
    public void create(Notification entity) {
        entity.setCode(giveCode());
        getEntityManager().persist(entity);
    }
    
    public void createNotificationDemande(Utilisateur sender,Utilisateur receiver,String libelle,Demande demande,Application application){
        Notification notif=new Notification();
       notif.setDemande(demande);
        notif.setDateCreation(new Date());
        notif.setLibelle(libelle);
        notif.setReceiver(receiver);
        notif.setSender(sender);
        notif.setStatut("NotSee");
        notif.setType("notif");
        notif.setApplication(application);
        create(notif);
    }
    
    public void createNotificationCourrier(Utilisateur sender,Utilisateur receiver,String libelle,Courrier courrier,String note,Application application){
        Notification notif=new Notification();
       notif.setCourrier(courrier);
        notif.setDateCreation(new Date());
        notif.setLibelle(libelle);
        notif.setReceiver(receiver);
        notif.setSender(sender);
        notif.setNote(note);
        notif.setStatut("NotSee");
        notif.setType("notif");
        notif.setApplication(application);
        create(notif);
    }
    
    public void createCorrectionCourrier(Utilisateur sender,Utilisateur receiver,String libelle,Courrier courrier,String note,Application application){
        Notification notif=new Notification();
       notif.setCourrier(courrier);
        notif.setDateCreation(new Date());
        notif.setLibelle(libelle);
        notif.setReceiver(receiver);
        notif.setSender(sender);
        notif.setNote(note);
        notif.setStatut("NotSee");
        notif.setType("correction");
        notif.setApplication(application);
        create(notif);
    }
    
    
    public void createCorrectionDemande(Utilisateur sender,Utilisateur receiver,String libelle,Demande demande,Application application){
        Notification notif=new Notification();
       notif.setDemande(demande);
        notif.setDateCreation(new Date());
        notif.setLibelle(libelle);
        notif.setReceiver(receiver);
        notif.setSender(sender);
        notif.setStatut("NotSee");
        notif.setType("correction");
        notif.setApplication(application);
        create(notif);
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
    public List<Notification> findLastNotification() {
        Query q = getEntityManager().createQuery("SELECT noti FROM Notification noti WHERE noti.dateCreation >= ALL(SELECT not1.dateCreation FROM Notification not1)");
        return q.getResultList();
    }
    
    public List<Notification> trouverNotifNotSee(Utilisateur receiver,Application application){
      Query q=getEntityManager().createQuery("SELECT noti FROM Notification noti WHERE noti.receiver=:receiver and noti.statut=:statut and noti.type=:type and noti.application=:application   ORDER BY noti.dateCreation DESC");
      q.setParameter("receiver", receiver);
      q.setParameter("statut", "NotSee");
       q.setParameter("type", "notif");
       q.setParameter("application", application);
      List<Notification> suggestions = q.getResultList();
        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }
    
    public List<Notification> trouverCorrectionNotSee(Utilisateur receiver,Application application){
      Query q=getEntityManager().createQuery("SELECT noti FROM Notification noti WHERE noti.receiver=:receiver and noti.statut=:statut and noti.type=:type and noti.application=:application   ORDER BY noti.dateCreation DESC");
      q.setParameter("receiver", receiver);
      q.setParameter("statut", "NotSee");
       q.setParameter("type", "correction");
       q.setParameter("application", application);
      List<Notification> suggestions = q.getResultList();
        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }
    
    
    public List<Notification> trouverAllNotifCorrecByCourrier(Courrier courrier){
      Query q=getEntityManager().createQuery("SELECT noti FROM Notification noti WHERE noti.courrier=:courrier and noti.note!=:note");
      q.setParameter("courrier", courrier);
         q.setParameter("note", "");
      List<Notification> suggestions = q.getResultList();
        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

}

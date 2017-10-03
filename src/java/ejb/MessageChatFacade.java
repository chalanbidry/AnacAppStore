/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Departement;
import jpa.MessageChat;
import jpa.MessageChat;
import jpa.Utilisateur;
import org.apache.log4j.Logger;

/**
 *
 * @author utilisateur
 */
@Stateless
public class MessageChatFacade extends AbstractFacade<MessageChat> {

    Logger logger = Logger.getLogger(MessageChatFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MessageChatFacade() {
        super(MessageChat.class);
    }

    @Override
    public void create(MessageChat entity) {
        int size = findAll().size();
        entity.setCode("Mess_" + String.valueOf(size));
        entity.setDateCreation(new Date());

//        entity.setId(compteurFacade.getNext(Fonction.class.getSimpleName(), contextBean.getJURIDICTIONCLEPRIMAIRE()));
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @param departement
     * @return liste des utilisateurs
     */
//    public List<Fonction> getAllUsers() {
//        Query q = getEntityManager().createQuery("select U from Fonction U order by U.nomPrenoms");
//        // set parameters
//        List<Fonction> suggestions = q.getResultList();
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
    public List<MessageChat> getMessageReceive(Utilisateur user) {
        Query q = getEntityManager().createQuery("select M FROM MessageChat M where M.recepteur=:user and M.statut=:statut ORDER BY M.dateCreation DESC");
        q.setParameter("user", user);
        q.setParameter("statut", "Non lu");
        List<MessageChat> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

    public List<MessageChat> getAllMessage(Utilisateur user) {
        Query q = getEntityManager().createQuery("select M FROM MessageChat M where M.recepteur=:user or M.expediteur=:user ORDER BY M.dateCreation DESC");
        q.setParameter("user", user);

        List<MessageChat> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

    public List<MessageChat> getAllMessageInTwo(Utilisateur user1, Utilisateur user2) {
        Query q = getEntityManager().createQuery("select M FROM MessageChat M where (M.recepteur=:user1 and M.expediteur=:user2) or (M.recepteur=:user2 and M.expediteur=:user1) ORDER BY M.dateCreation ASC");
        q.setParameter("user1", user1);
        q.setParameter("user2", user2);
        List<MessageChat> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions;

    }

    public int nbrmsgSendToUser(Utilisateur user1, Utilisateur user2) {
        Query q = getEntityManager().createQuery("select M FROM MessageChat M where (M.expediteur=:user1 and M.recepteur=:user2) and M.statut=:statut");
        q.setParameter("user1", user1);
        q.setParameter("user2", user2);
        q.setParameter("statut", "Non lu");
        List<MessageChat> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions.size();

    }
    
    
    public void updateLu(Utilisateur user1, Utilisateur user2) {
        Query q = getEntityManager().createQuery("update MessageChat M set M.statut=:statut where  M.expediteur=:user1 and M.recepteur=:user2");
        q.setParameter("user1", user1);
        q.setParameter("user2", user2);
        q.setParameter("statut", "Lu");
        q.executeUpdate();


    }

    public void viderChat() {
        Query q = getEntityManager().createQuery("DELETE FROM MessageChat");
        q.executeUpdate();
    }

}

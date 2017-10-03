/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ejb.CommentaireFacade;
import ejb.FonctionFacade;
import ejb.GroupeFacade;
import ejb.GroupeUtilisateurFacade;
import ejb.MessageChatFacade;
import ejb.PublicationFacade;
import ejb.UtilisateurFacade;
import java.io.IOException;
import jpa.Utilisateur;
import util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;

import jpa.Commentaire;
import jpa.MessageChat;
import jpa.Publication;
import org.primefaces.context.RequestContext;
import pojo.EncrypteDecrypte;
import pojo.notificationEndPoint;

/**
 *
 * @author MJLDH
 */
@Named(value = "socialBean")
@SessionScoped
public class SocialBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

    @Inject
    private UtilisateurFacade utilisateurFacade;
    @Inject
    private ConnexionBean connexionBean;
    @Inject
    private FonctionFacade fonctionFacade;
    @Inject
    private PublicationFacade publicationFacade;
    @Inject
    private CommentaireFacade commentaireFacade;
    @Inject
    private GroupeUtilisateurFacade groupeUtilisateurFacade;
    @Inject
    private GroupeFacade groupeFacade;
    @Inject
    private MessageChatFacade messageChatFacade;
    private Utilisateur selectedUtilisateur;
    private Utilisateur newUtilisateur;
    private List<Utilisateur> listeUtilisateurs;
    private boolean isUpdatePassword;
    private String passwordTempo1;
    private String passwordTempo2;
    private String oldPassword;
    private Utilisateur userchating;
    private String msg;
    private String[][] messagesTab;
    private List<MessageChat> listMessageInTwo;
    private Publication newPublication;
    private Commentaire newCommentaire;
    
    @ViewScoped
    private List<Publication> listPublication;
      private List<Publication> listPublicationSend;
      @ViewScoped
    private List<Commentaire> listCommentaire;
    private List<Utilisateur> listUserSelectForPublication;

    /**
     * Creates a new instance of UtilisateurBean
     */
    public SocialBean() {

    }

    @PostConstruct
    public void init() {
        listeUtilisateurs = utilisateurFacade.findAll();
        listeUtilisateurs.remove(connexionBean.getCurrentUser());
        listPublicationSend=utilisateurFacade.getAllPubSend(connexionBean.getCurrentUser());
      
        listPublication=findPublicReceive();
        
      
        if (listeUtilisateurs.size() != 0) {
            System.out.println("---->>>>" + listeUtilisateurs.get(0).getLogin());
        }
        isUpdatePassword = false;
        recupererId();
        listMessageInTwo = new ArrayList<>();
      
        listCommentaire = new ArrayList<>();
        listUserSelectForPublication = new ArrayList<>();
    }
    
    public List<Publication> findPublicReceive(){
       Utilisateur user=utilisateurFacade.find(connexionBean.getCurrentUser().getLogin());
       return user.getListPublicationReceive();
    }

    public String getMsg() {
        if (msg == null) {
            msg = "";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MessageChat> getListMessageInTwo() {
        return listMessageInTwo;
    }

    public List<Publication> getListPublicationSend() {
        return listPublicationSend;
    }

    public void setListPublicationSend(List<Publication> listPublicationSend) {
        this.listPublicationSend = listPublicationSend;
    }

    public void setListMessageInTwo(List<MessageChat> listMessageInTwo) {
        this.listMessageInTwo = listMessageInTwo;
    }

    public Publication getNewPublication() {
        if(newPublication==null){
         newPublication=new Publication();
        }
        return newPublication;
    }

    public void setNewPublication(Publication newPublication) {
        this.newPublication = newPublication;
    }

    public Commentaire getNewCommentaire() {
        if(newCommentaire==null){
        newCommentaire=new Commentaire();
        }
        return newCommentaire;
    }

    public void setNewCommentaire(Commentaire newCommentaire) {
        this.newCommentaire = newCommentaire;
    }

    public List<Publication> getListPublication() {
        return listPublication;
    }

    public void setListPublication(List<Publication> listPublication) {
        this.listPublication = listPublication;
    }

    public List<Commentaire> getListCommentaire() {
        return listCommentaire;
    }

    public void setListCommentaire(List<Commentaire> listCommentaire) {
        this.listCommentaire = listCommentaire;
    }

    public List<Utilisateur> getListUserSelectForPublication() {
        return listUserSelectForPublication;
    }

    public void setListUserSelectForPublication(List<Utilisateur> listUserSelectForPublication) {
        this.listUserSelectForPublication = listUserSelectForPublication;
    }

    public UtilisateurFacade getUtilisateurFacade() {
        return utilisateurFacade;
    }

    public void setUtilisateurFacade(UtilisateurFacade utilisateurFacade) {
        this.utilisateurFacade = utilisateurFacade;
    }

    public Utilisateur getSelectedUtilisateur() {
        return selectedUtilisateur;
    }

    public void setSelectedUtilisateur(Utilisateur selectedUtilisateur) {
        this.selectedUtilisateur = selectedUtilisateur;
    }

    public List<Utilisateur> getListeUtilisateurs() {
        return listeUtilisateurs;
    }

    public void setListeUtilisateurs(List<Utilisateur> listeUtilisateurs) {
        this.listeUtilisateurs = listeUtilisateurs;
    }

    public Utilisateur getNewUtilisateur() {
        if (newUtilisateur == null) {
            newUtilisateur = new Utilisateur();
        }
        return newUtilisateur;
    }

    public void setNewUtilisateur(Utilisateur newUtilisateur) {
        this.newUtilisateur = newUtilisateur;
    }

    public void passItem(Utilisateur item) {
        this.selectedUtilisateur = item;
    }

    public boolean isIsUpdatePassword() {
        return isUpdatePassword;
    }

    public void setIsUpdatePassword(boolean isUpdatePassword) {
        this.isUpdatePassword = isUpdatePassword;
    }

    public String getPasswordTempo1() {
        return passwordTempo1;
    }

    public void setPasswordTempo1(String passwordTempo1) {
        this.passwordTempo1 = passwordTempo1;
    }

    public String getPasswordTempo2() {
        return passwordTempo2;
    }

    public void setPasswordTempo2(String passwordTempo2) {
        this.passwordTempo2 = passwordTempo2;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public Utilisateur getUserchating() {
        return userchating;
    }

    public void setUserchating(Utilisateur userchating) {
        this.userchating = userchating;
    }

    public void passUserChat(Utilisateur user) {
        userchating = user;
        listMessageInTwo = messageChatFacade.getAllMessageInTwo(connexionBean.getCurrentUser(), userchating);
        messageChatFacade.updateLu(user, connexionBean.getCurrentUser());
    }

    public void recupererId() {
        try {
            String login = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
            System.out.println(" Id ---" + login);
            if (login != null) {
                selectedUtilisateur = utilisateurFacade.find(login);
            }

        } catch (Exception e) {
            System.out.println("Erreur " + e);
        }

    }

    public void passUser(Utilisateur user) {
        System.out.println("le user ests " + user.getLogin());
        selectedUtilisateur = user;
    }

    public void setItIsUpdatePassword(String statut) {
        System.out.println("le boolean est " + statut);
        if (statut.equals("true")) {
            isUpdatePassword = true;
        } else {
            isUpdatePassword = false;
        }
    }

    public void doEditUser(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;

        try {
            if (isUpdatePassword) {
                System.out.println("1");
                String oldGetPassword = selectedUtilisateur.getPassword();
                System.out.println("1 old0 >" + oldGetPassword);
                if (EncrypteDecrypte.sha256(oldPassword).equals(oldGetPassword) && passwordTempo1.equals(passwordTempo2)) {
                    System.out.println("2");
                    selectedUtilisateur.setPassword(EncrypteDecrypte.sha256(passwordTempo2));
                    System.out.println("3");
                    utilisateurFacade.edit(selectedUtilisateur);
                    System.out.println("4");
                    this.listeUtilisateurs = this.utilisateurFacade.findAll();
                    msg = bundle.getString("UtilisateurEditSuccessMsg");
                    JsfUtil.addSuccessMessage(msg);
                } else {
                    msg = bundle.getString("UtilisateurEditPasswordErrorMsg");
                    JsfUtil.addErrorMessage(msg);
                }

            } else {
                utilisateurFacade.edit(selectedUtilisateur);
                this.listeUtilisateurs = this.utilisateurFacade.findAll();
                msg = bundle.getString("UtilisateurEditSuccessMsg");
                JsfUtil.addSuccessMessage(msg);

            }
            connexionBean.init();
        } catch (Exception e) {
            System.out.println("Errreur est " + e);
            msg = bundle.getString("UtilisateurEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void sendMessage(ActionEvent event) {
        System.out.println("bonjour -->" + msg);
        MessageChat messageChat = new MessageChat();
        messageChat.setLibelle(msg);
        messageChat.setExpediteur(connexionBean.getCurrentUser());
        messageChat.setRecepteur(userchating);
        messageChat.setStatut("Non lu");
        messageChatFacade.create(messageChat);

        try {
            if (!msg.equals("")) {
                notificationEndPoint.sendMessageChat(connexionBean.getCurrentUser(), userchating, msg);
//            RequestContext.getCurrentInstance().execute("sendMessage()");
                msg = "";
            }
        } catch (IOException ex) {
            Logger.getLogger(SocialBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void seeMessage(MessageChat message) {
        message.setStatut("Lu");
        messageChatFacade.edit(message);
    }

    public void deleteChat() {
        String msg;
        try {
            messageChatFacade.viderChat();
            msg = "Messagerie vidée avec succès";
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            System.out.println("Erreur est " + e);
            msg = "Echec de l'opération";
            JsfUtil.addErrorMessage(msg);
        }

    }

    public int nbrMsgInstance(Utilisateur user) {

        return messageChatFacade.nbrmsgSendToUser(user, connexionBean.getCurrentUser());
    }

    public void doPublication(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;

        try {

            newPublication.setUserEmetteur(connexionBean.getCurrentUser());
            String codePublication = publicationFacade.creatPublicationAndGiveCode(newPublication);
              Publication pubTampon=publicationFacade.find(codePublication);
            for(Utilisateur userPublish: listUserSelectForPublication){
             userPublish.getListPublicationReceive().add(pubTampon);
             utilisateurFacade.editMany(userPublish);
        
            }
            listPublicationSend=utilisateurFacade.getAllPubSend(connexionBean.getCurrentUser());
              listPublication=connexionBean.getCurrentUser().getListPublicationReceive();
            msg = bundle.getString("publicationSuccessMsg");
            JsfUtil.addSuccessMessage(msg);

            connexionBean.init();
        } catch (Exception e) {
            System.out.println("Errreur est " + e);
            msg = bundle.getString("publicationErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    
    
     public void doCommentaire(ActionEvent event,Publication pub) {

        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;

        try {

            newCommentaire.setUserEmetteur(connexionBean.getCurrentUser());
            newCommentaire.setPublication(pub);
             commentaireFacade.create(newCommentaire);
          
            
            msg = bundle.getString("publicationSuccessMsg");
            JsfUtil.addSuccessMessage(msg);

            connexionBean.init();
        } catch (Exception e) {
            System.out.println("Errreur est " + e);
            msg = bundle.getString("publicationErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
     
     public List<Commentaire> findListCommentForPub(Publication pub){
     return commentaireFacade.getCommentaireByPub(pub);
     }
    
    
//    public void reset() {
//        newUtilisateur.reset();
//    }
}

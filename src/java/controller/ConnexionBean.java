/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ejb.ConnexionFacade;
import ejb.NotificationFacade;
import ejb.UtilisateurFacade;
import java.io.IOException;
import util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import static javax.ws.rs.client.Entity.entity;
import jpa.Application;
import jpa.Notification;
import jpa.Utilisateur;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.log4j.Logger;
import pojo.notificationEndPoint;

/**
 *
 * @author Patrick
 */
@Named(value = "connexionBean")
@SessionScoped
public class ConnexionBean implements Serializable {

    Logger logger = Logger.getLogger(ConnexionBean.class);
    @Inject
    UtilisateurFacade utilisateurFacade;
    @Inject
    ConnexionFacade connexionFacade;
    @Inject
    NotificationFacade notificationFacade;
    private Utilisateur userChefService;
    private Utilisateur userDirecteur;
     private Utilisateur userDG;
    private Utilisateur currentUser;
    private Application currentAppli;
    private static List<Utilisateur> listConnectes = new ArrayList<>();
    private static List<Utilisateur> listNonConnectes;
   

    public ConnexionBean() {
    }

    @PostConstruct
    public void init() {
        JsfUtil.logInfo(logger, "------- ConnexionBean PostConstruct init debut---------------");
        FacesContext ctx = FacesContext.getCurrentInstance();
        String login = ctx.getExternalContext().getUserPrincipal().getName();
        currentUser = utilisateurFacade.find(login);
        if(listConnectes.contains(currentUser))
        {
       logoutFromInit();
        }
        if (!currentUser.isChefService() && !currentUser.isDg() && !currentUser.isSecretaireDeDirection()) {
            userChefService = utilisateurFacade.getFonctionChef(currentUser.getFonction().getService());
        }
        
         if (!currentUser.isDirecteur() && !currentUser.isDg() && !currentUser.isSecretaireDeDirection()) {
            userDirecteur = utilisateurFacade.getFonctionDirecteur(currentUser.getFonction().getService());
        }
         
         if(!currentUser.isDg()){
          userDG= utilisateurFacade.getFonctionDG();
         }
        
        JsfUtil.logInfo(logger, "le user est " + currentUser.getPrenom());
         if(!listConnectes.contains(currentUser)){
        listConnectes.add(currentUser);
        listNonConnectes = findLisNonConnecte();
         }
        JsfUtil.logInfo(logger, "------- ConnexionBean PostConstruct init fin---------------");
//        if (currentUser.isAdmin() || currentUser.getFonction().getCode().equals("CSFC")) {
            connexionFacade.initGed(currentUser);
//        }
      
    }

    public Utilisateur getUserDG() {
        return userDG;
    }

    public void setUserDG(Utilisateur userDG) {
        this.userDG = userDG;
    }
    
    

    public List<Utilisateur> getListConnectes() {
        return listConnectes;
    }

    public void setListConnectes(List<Utilisateur> listConnectes) {
        this.listConnectes = listConnectes;
    }

    public List<Utilisateur> getListNonConnectes() {
        return listNonConnectes;
    }

    public void setListNonConnectes(List<Utilisateur> listNonConnectes) {
        this.listNonConnectes = listNonConnectes;
    }

    public String convertDate(Date d, String format) {
        return JsfUtil.convertDate(d, format);
    }

    public Long getDureeEnJours(Date debut, Date fin) {
        return JsfUtil.getDaysBetween(debut, fin);
    }

    public Utilisateur getUserDirecteur() {
        return userDirecteur;
    }

    public void setUserDirecteur(Utilisateur userDirecteur) {
        this.userDirecteur = userDirecteur;
    }
    
    
    

    public String arrondir(Double nbre) {
        try {
            return arrondir(nbre, 2);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String arrondir(Double nbre, int dec) {
        try {
            return String.format("%.2f", nbre);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String convertDate(Date d) {
        return JsfUtil.convertDate(d);
    }

    public String convertDateHeure(Date d) {
        return JsfUtil.convertDateHeure(d);
    }

    public Utilisateur getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Utilisateur currentUser) {
        this.currentUser = currentUser;
    }

    public Utilisateur getUserChefService() {
        return userChefService;
    }

    public void setUserChefService(Utilisateur userChefService) {
        this.userChefService = userChefService;
    }

    public Application getCurrentAppli() {
        return currentAppli;
    }

    public void setCurrentAppli(Application currentAppli) {
        this.currentAppli = currentAppli;
    }
    
    

    public String logout() {
        listConnectes.remove(currentUser);
        //ResourceBundle bundle = ResourceBundle.getBundle("util.Bundle", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        //Log.trace(sessionBean.class, bundle.getString("GoodByeDialogTitle"), currentUser, "logout", currentTPI, currentExercer, null);
        System.out.println("--------Debut déconnexion----------------");
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
////        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("---------Fin déconnexion---------------");
        return "/pageAllConnexion/index?faces-redirect=true";
//        return "/photo/index.xhtml";
    }
    
    public String logoutFromInit(){
     return "/index?faces-redirect=true";
    }
    
    public String chooseAppli(Application appli){
        currentAppli=appli;
        System.out.println(" appli est "+currentAppli.getAppliName());
      
      return "/index.xhtml?faces-redirect=true";
         
    }

    public List<Utilisateur> findLisNonConnecte() {
        List<Utilisateur> listNonCon = new ArrayList<>();
        for (Utilisateur userNonConnect : utilisateurFacade.findAll()) {
            if (!listConnectes.contains(userNonConnect) && userNonConnect != currentUser) {
                listNonCon.add(userNonConnect);
            }
        }
        return listNonCon;
    }
    
    public String changeAppli(){
     return "/pageAllConnexion/index.xhtml?faces-redirect=true";
    }
}

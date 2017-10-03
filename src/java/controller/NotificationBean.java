/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import ejb.NotificationFacade;
import util.JsfUtil;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import jpa.Application;
import jpa.Courrier;
import jpa.Notification;
import org.apache.log4j.Logger;

/**
 *
 * @author MJLDH
 */
@Named(value = "notificationBean")
@ViewScoped
public class NotificationBean implements Serializable {

    Logger logger = Logger.getLogger(NotificationBean.class);
    
   
     @Inject
    private ConnexionBean connexionBean;
     @Inject
    private NotificationFacade notificationFacade;
    private List<Notification> notifs;
    private List<Notification> alerte;

    /**
     * Creates a new instance of NotificationBean
     */
    public NotificationBean() {
    }

    @PostConstruct
    public void init() {

//        for (int i = 0; i < 2; i++) {
//            test(i);
//        }
        
    }

    public List<Notification> getNotifs() {
        return notifs;
    }

    public void setNotifs(List<Notification> notifs) {
        this.notifs = notifs;
    }

   

    public List<Notification> getAlerte() {
        return alerte;
    }

    public void setAlerte(List<Notification> alerte) {
        this.alerte = alerte;
    }

    public List<Notification> someNotification(Application application) {
        notifs = notificationFacade.trouverNotifNotSee(connexionBean.getCurrentUser(),application);
        return  notifs.stream().limit(10).collect(Collectors.toList());
    }
    
    public List<Notification> someCorrection(Application application){
    alerte=notificationFacade.trouverCorrectionNotSee(connexionBean.getCurrentUser(),application);
    return  alerte.stream().limit(10).collect(Collectors.toList());
    }
    
    public List<Notification> someCourrierByCourrier(Courrier courrier){
      return notificationFacade.trouverAllNotifCorrecByCourrier(courrier);
    }

   

    

  
}

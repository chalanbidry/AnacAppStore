/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ejb.ApplicationFacade;
import ejb.DepartementFacade;
import ejb.FonctionFacade;
import ejb.GroupeFacade;
import ejb.GroupeUtilisateurFacade;
import ejb.UtilisateurFacade;
import jpa.Utilisateur;
import util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import jpa.Departement;
import jpa.Fonction;
import jpa.GroupeUtilisateur;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import jpa.Application;
import jpa.Groupe;
import jpa.Service;
import pojo.EncrypteDecrypte;

/**
 *
 * @author MJLDH
 */
@Named(value = "utilisateurBean")
@ViewScoped
public class UtilisateurBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

    @Inject
    private UtilisateurFacade utilisateurFacade;
    @Inject
    private FonctionFacade fonctionFacade;
    @Inject
    private GroupeUtilisateurFacade groupeUtilisateurFacade;
    @Inject
    private GroupeFacade groupeFacade;
    @Inject
    private ApplicationFacade applicationFacade;
      @Inject
    private DepartementFacade departementFacade;
    private Utilisateur selectedUtilisateur;
    private Utilisateur newUtilisateur;
    private List<Utilisateur> listeUtilisateurs;
    private String passwordTempo1;
    private String passwordTempo2;
    private Departement SelectedDepartement;
    private boolean isChefService, isDirecteur;
    private GroupeUtilisateur newGroupUtilisateur;
    private List<Groupe> listGroupes;
    private List<GroupeUtilisateur> listeGroupeUstilisateurs;
    private List<Utilisateur> listeUtilisateursNot;
    private Service serviceSelected;
    private List<Application> listAppliNotUserIn;
    private Application selectedApplication;
    private boolean secAdministrative;
   

    /**
     * Creates a new instance of UtilisateurBean
     */
    public UtilisateurBean() {

    }

    @PostConstruct
    public void init() {
        listeUtilisateurs = utilisateurFacade.findAll();
        if (listeUtilisateurs.size() != 0) {
            System.out.println("---->>>>" + listeUtilisateurs.get(0).getLogin());
        }
        listGroupes = groupeFacade.findAll();
        listeGroupeUstilisateurs = groupeUtilisateurFacade.findAll();
        listeUtilisateursNot = utilisateurFacade.getAllUsersNotGroup();
        listAppliNotUserIn = new ArrayList<>();
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

    public boolean isIsChefService() {
        return isChefService;
    }

    public void setIsChefService(boolean isChefService) {
        this.isChefService = isChefService;
    }

    public GroupeUtilisateur getNewGroupUtilisateur() {
        if (newGroupUtilisateur == null) {
            newGroupUtilisateur = new GroupeUtilisateur();
        }
        return newGroupUtilisateur;
    }

    public void setNewGroupUtilisateur(GroupeUtilisateur newGroupUtilisateur) {
        this.newGroupUtilisateur = newGroupUtilisateur;
    }

    public List<Groupe> getListGroupes() {
        return listGroupes;
    }

    public void setListGroupes(List<Groupe> listGroupes) {
        this.listGroupes = listGroupes;
    }

    public List<GroupeUtilisateur> getListeGroupeUstilisateurs() {
        return listeGroupeUstilisateurs;
    }

    public void setListeGroupeUstilisateurs(List<GroupeUtilisateur> listeGroupeUstilisateurs) {
        this.listeGroupeUstilisateurs = listeGroupeUstilisateurs;
    }

    public List<Utilisateur> getListeUtilisateursNot() {
        return listeUtilisateursNot;
    }

    public void setListeUtilisateursNot(List<Utilisateur> listeUtilisateursNot) {
        this.listeUtilisateursNot = listeUtilisateursNot;
    }

    public Service getServiceSelected() {
        if (serviceSelected == null) {
            serviceSelected = new Service();
        }
        return serviceSelected;
    }

    public void setServiceSelected(Service serviceSelected) {
        this.serviceSelected = serviceSelected;
    }

    public boolean isIsDirecteur() {
        return isDirecteur;
    }

    public void setIsDirecteur(boolean isDirecteur) {
        this.isDirecteur = isDirecteur;
    }

    public Application getSelectedApplication() {
        return selectedApplication;
    }

    public void setSelectedApplication(Application selectedApplication) {
        this.selectedApplication = selectedApplication;
    }

    public boolean isSecAdministrative() {
        return secAdministrative;
    }

    public void setSecAdministrative(boolean secAdministrative) {
        this.secAdministrative = secAdministrative;
    }

    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            if (passwordTempo1.equals(passwordTempo2)) {
                newUtilisateur.setPassword(EncrypteDecrypte.sha256(passwordTempo2));
//                System.out.println("le password est "+EncrypteDecrypte.DecryptText(EncrypteDecrypte.sha256(passwordTempo2)));
                newUtilisateur.setName(newUtilisateur.getNom() + " " + newUtilisateur.getPrenom());
                this.utilisateurFacade.create(newUtilisateur);
                this.listeUtilisateurs = this.utilisateurFacade.findAll();

                msg = bundle.getString("UtilisateurCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
            } else {
                msg = bundle.getString("UtilisateurCreateErrorMsgNotSamePass");
                JsfUtil.addErrorMessage(msg);
            }
        } catch (Exception e) {
            msg = bundle.getString("UtilisateurCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doCreateDG(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            if (passwordTempo1.equals(passwordTempo2)) {
                newUtilisateur.setPassword(EncrypteDecrypte.sha256(passwordTempo2));
//                System.out.println("le password est "+EncrypteDecrypte.DecryptText(EncrypteDecrypte.sha256(passwordTempo2)));
                newUtilisateur.setName(newUtilisateur.getNom() + " " + newUtilisateur.getPrenom());
                newUtilisateur.setFonction(fonctionFacade.find("DG"));
                newUtilisateur.setDg(true);
                this.utilisateurFacade.create(newUtilisateur);
                this.listeUtilisateurs = this.utilisateurFacade.findAll();

                msg = bundle.getString("UtilisateurCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
            } else {
                msg = bundle.getString("UtilisateurCreateErrorMsgNotSamePass");
                JsfUtil.addErrorMessage(msg);
            }
        } catch (Exception e) {
            msg = bundle.getString("UtilisateurCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doCreateSecretaire(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            if (passwordTempo1.equals(passwordTempo2)) {
                newUtilisateur.setPassword(EncrypteDecrypte.sha256(passwordTempo2));
//                System.out.println("le password est "+EncrypteDecrypte.DecryptText(EncrypteDecrypte.sha256(passwordTempo2)));
                newUtilisateur.setName(newUtilisateur.getNom() + " " + newUtilisateur.getPrenom());
                if (secAdministrative) {
                    newUtilisateur.setFonction(fonctionFacade.find("SA"));
                    newUtilisateur.setSecretaireAdministratif(true);
                    newUtilisateur.setSecretaireDeDirection(false);
                    newUtilisateur.setSecretaireParticulier(false);
                } else {
                    newUtilisateur.setFonction(fonctionFacade.find("SP"));
                    newUtilisateur.setSecretaireAdministratif(false);
                    newUtilisateur.setSecretaireDeDirection(false);
                    newUtilisateur.setSecretaireParticulier(true);
                }

                newUtilisateur.setSecretaireAdministratif(true);
                this.utilisateurFacade.create(newUtilisateur);
                this.listeUtilisateurs = this.utilisateurFacade.findAll();

                msg = bundle.getString("UtilisateurCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
            } else {
                msg = bundle.getString("UtilisateurCreateErrorMsgNotSamePass");
                JsfUtil.addErrorMessage(msg);
            }
        } catch (Exception e) {
            msg = bundle.getString("UtilisateurCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    
    public List<Utilisateur> findUserSecDirection(){
      return utilisateurFacade.getUserSecretairesDirecction();
    }
    

    public void doCreateSecretaireDirection(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            if (passwordTempo1.equals(passwordTempo2)) {
                newUtilisateur.setPassword(EncrypteDecrypte.sha256(passwordTempo2));
//                System.out.println("le password est "+EncrypteDecrypte.DecryptText(EncrypteDecrypte.sha256(passwordTempo2)));
                newUtilisateur.setName(newUtilisateur.getNom() + " " + newUtilisateur.getPrenom());
                newUtilisateur.setFonction(null);
                newUtilisateur.setSecretaireAdministratif(false);
                newUtilisateur.setSecretaireDeDirection(true);
                newUtilisateur.setSecretaireParticulier(false);
                this.utilisateurFacade.create(newUtilisateur);
                this.listeUtilisateurs = this.utilisateurFacade.findAll();

                msg = bundle.getString("UtilisateurCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
            } else {
                msg = bundle.getString("UtilisateurCreateErrorMsgNotSamePass");
                JsfUtil.addErrorMessage(msg);
            }
        } catch (Exception e) {
            msg = bundle.getString("UtilisateurCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEdit(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;

        try {
            utilisateurFacade.edit(selectedUtilisateur);
            this.listeUtilisateurs = this.utilisateurFacade.findAll();
            msg = bundle.getString("UtilisateurEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("UtilisateurEditErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDel(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            utilisateurFacade.remove(selectedUtilisateur);
            msg = bundle.getString("UtilisateurDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeUtilisateurs.remove(this.selectedUtilisateur);
            this.listeUtilisateurs = this.utilisateurFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("UtilisateurDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doLiaison(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            groupeUtilisateurFacade.create(newGroupUtilisateur);

            msg = bundle.getString("GroupUsrCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            listeGroupeUstilisateurs = groupeUtilisateurFacade.findAll();

        } catch (Exception e) {
            msg = bundle.getString("UtilisateurCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
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

    public Departement getSelectedDepartement() {
        return SelectedDepartement;
    }

    public void setSelectedDepartement(Departement SelectedDepartement) {
        this.SelectedDepartement = SelectedDepartement;
    }

    public List<Fonction> findListFonctionByService() {
        if (!isDirecteur) {
            return fonctionFacade.getFonctionByService(serviceSelected);
        } else {
            return fonctionFacade.getAllDirecteurs();
        }
    }

    public List<Service> findListServiceByDepartement() {
        return fonctionFacade.getServiceByDep(SelectedDepartement);
    }

    public List<Application> getListAppliNotUserIn() {
        return listAppliNotUserIn;
    }

    public void setListAppliNotUserIn(List<Application> listAppliNotUserIn) {
        this.listAppliNotUserIn = listAppliNotUserIn;
    }

    public void setItIsChefService(String statut) {
        System.out.println("le boolean est " + statut);
        if (statut.equals("true")) {
            isChefService = true;
        } else {
            isChefService = false;
        }
    }

    public void setItIsDirecteur(String statut) {
        System.out.println("le boolean est " + statut);
        if (statut.equals("true")) {
            isDirecteur = true;
        } else {
            isDirecteur = false;
        }
    }

    public void reset() {
        newUtilisateur.reset();
    }

    public void allAppliNotUser() {
        listAppliNotUserIn.clear();
        for (Application application : applicationFacade.findAll()) {
            if (!selectedUtilisateur.getListApplications().contains(application)) {
                listAppliNotUserIn.add(application);
            }
        }
    }

    public void doLiaisonAppli(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            selectedUtilisateur.getListApplications().add(selectedApplication);
            utilisateurFacade.edit(selectedUtilisateur);

            msg = bundle.getString("GroupUsrCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            System.out.println("Erreur " + e);
            msg = bundle.getString("UtilisateurCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }
    
    
    
    public void doLiaisonSecDep(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            SelectedDepartement.setSecretaireDeDirection(selectedUtilisateur);
            departementFacade.edit(SelectedDepartement);

            msg = bundle.getString("LiaisonSecDep");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            System.out.println("Erreur " + e);
            msg = bundle.getString("EchecMessage");
            JsfUtil.addErrorMessage(msg);
        }
    }
    
    
    
    
    
    

    public Utilisateur findSA() {
        return utilisateurFacade.getUserByFonction(fonctionFacade.find("SA"));
    }
}

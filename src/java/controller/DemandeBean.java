/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ejb.BonCommandeFacade;
import ejb.BonCommande_ElementFacade;
import ejb.ConnexionFacade;
import ejb.ContextFacade;
import ejb.DemandeFacade;
import ejb.DocumentFacade;
import ejb.ElementFacade;
import ejb.EvenementFacade;
import ejb.ExerciceFacade;
import ejb.FonctionFacade;
import ejb.GedFacade2;
import ejb.MessageChatFacade;
import ejb.NotificationFacade;
import ejb.ProgressionBonCommandeFacade;
import ejb.ProgressionDemandeFacade;
import ejb.TypeDocumentFacade;
import ejb.UtilisateurFacade;
import ejb.CategorieFacade;
import ejb.LigneBudgetaireFacade;
import ged.DocumentCmis;
import ged.DossierCmis;
import ged.ejb.GedFacade;
import java.io.IOException;
import jpa.Demande;
import util.JsfUtil;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import jpa.BonCommande_Element;
import jpa.Boncommande;
import jpa.Departement;
import jpa.Element;
import jpa.Evenement;
import jpa.Exercice;
import jpa.Fonction;
import jpa.ProgressionBonCommande;
import jpa.ProgressionDemande;
import jpa.Service;
import jpa.Utilisateur;
import pojo.BonCommandeElement;
import javax.jms.*;
import javax.servlet.http.Part;
import jpa.Document;
import jpa.Ged;
import jpa.MessageChat;
import jpa.Notification;
import jpa.TypeDocument;
import jpa.Materiel;
import jpa.Categorie;
import jpa.LigneBudgetaire;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.primefaces.context.RequestContext;
import pojo.Nombre;
import pojo.notificationEndPoint;

/**
 *
 * @author MJLDH
 */
@Named(value = "demandeBean")
@ViewScoped
public class DemandeBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

    @Inject
    private DemandeFacade demandeFacade;
    @Inject
    private ConnexionBean connexionBean;
    @Inject
    private MaterielBean materielBean;
    @Inject
    private ExerciceFacade exerciceFacade;
    @Inject
    private UtilisateurFacade utilisateurFacade;
    @Inject
    private FonctionFacade fonctionFacade;
    @Inject
    private ProgressionDemandeFacade progressionDemandeFacade;
    @Inject
    private EvenementFacade evenementFacade;
    @Inject
    private ElementFacade elementFacade;
    @Inject
    private BonCommandeFacade bonCommandeFacade;
    @Inject
    private BonCommande_ElementFacade bonCommande_ElementFacade;
    @Inject
    private NotificationFacade notificationFacade;
    @Inject
    private ProgressionBonCommandeFacade progressionBonCommandeFacade;
    @Inject
    ConnexionFacade connexionFacade;
    private GedFacade gedFacade;
    @Inject
    ContextFacade contextFacade;
    @Inject
    DocumentFacade documentFacade;
    @Inject
    private GedFacade2 gedFacade2;
    @Inject
    private TypeDocumentFacade typeDocumentFacade;
    @Inject
    private MessageChatFacade messageChatFacade;
    @Inject
    private CategorieFacade categorieFacade;
    @Inject
    private LigneBudgetaireFacade ligneBudgetaireFacade;

    private Demande selectedDemande;
    private Demande newDemande;
    private List<Demande> listeDemandes;
    private List<Exercice> listExercice;
    private String id;
    private List<Demande> listeDemandesByUser;
    private Date date1;
    private Date date2;
    private ProgressionDemande newProgressionDemande;
    private ProgressionBonCommande newProgressionBonCommande;
    private List<Demande> listeDemandesInDirection;
    private List<Demande> listDemandeWaitAuto;
    private boolean envoi, autorise, SendDG, SendCSFC, sendBonDG, sendBonCSFC;
    private Utilisateur chefService;
    private List<ProgressionDemande> listPrDemSendDAF;
    private Service serviceSelected;
    private ProgressionDemande selectedProgressionDemande;
    private Boncommande newBonDeCommande;
    private Boncommande selectedBonDeCommande;
    private BonCommande_Element selectBonCommandeElement;
    private List<String> listeElementTotal;
    private String ElementTampon;
    private List<BonCommandeElement> listCommandeElement;
    private BonCommandeElement newCommandeElement;
    private Element selectElement;
    private Element newElement;
    private List<Element> listElement;
    private long total;
    private List<Boncommande> listBonCommande;
    private List<BonCommande_Element> listBonCommande_Element;
    private List<ProgressionBonCommande> listPrBonCommande;
    private List<ProgressionBonCommande> listPrBonCommandeSendDAF;
    private String cheminSuivi;
    private List<TypeDocument> listTypeDoc;
    private Part fileAdd;
    private TypeDocument selectedTypeDocument;
    private Document SelectedDocument;
    private String messageCorrection;
    private List<MessageChat> listMessNonLu;
    private Categorie selectedCategorie;
    private List<Materiel> listeMateriels;
    private Materiel selectedMateriel;
    private String typeAjout;
    private LigneBudgetaire newLigneBudgetaire;
    private LigneBudgetaire selectedLigneBudgetaire;
    private Exercice selectedExercice;
    private List<LigneBudgetaire> listeLigneBudgetaire;
    private List<LigneBudgetaire> listeResteBudgetaaire;
    private long montantRealloue;
    private LigneBudgetaire ligneGiverealoction;

    /**
     * Creates a new instance of DemandeBean
     */
    public DemandeBean() {

    }

    @PostConstruct
    public void init() {
        cheminSuivi = contextFacade.getALFRESCO_REPOSITORY();
        listeDemandes = demandeFacade.findAll();
        if (listeDemandes.size() != 0) {
            System.out.println("---->>>>" + listeDemandes.get(0).getCode());
        }
        listeDemandesByUser = demandeFacade.getAllDemandeByUser(connexionBean.getCurrentUser());
        System.out.println("listeDemandesByUser --->" + listeDemandesByUser);
        listExercice = exerciceFacade.findAll();
        recupererId();
        recupererCode();
        recupererIdBonCommande();
        LocalDateTime datedujour = LocalDate.now().atStartOfDay();
        LocalDateTime debut = datedujour.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime fin = datedujour.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1).minusNanos(1);
        date1 = JsfUtil.convertirEnDate(debut);
        date2 = JsfUtil.convertirEnDate(fin);
        listDemandeWaitAuto = new ArrayList<>();
        if (connexionBean.getCurrentUser() != null) {
            findListDemandeInDir();
        }
        if (!connexionBean.getCurrentUser().isSecretaireDeDirection() && connexionBean.getCurrentUser().getFonction().getCode().equals("DAF")) {
            listPrDemSendDAF = progressionDemandeFacade.getLastProgressionAllDemande();
        }
        System.out.println("la date1 " + date1 + " et date2 " + date2);
        listCommandeElement = new ArrayList<>();
        listElement = elementFacade.findAll();
        total = 0;

        listBonCommande = bonCommandeFacade.findAll();
        listPrBonCommande = progressionBonCommandeFacade.getLastProgressionAllBon();
        listPrBonCommandeSendDAF = progressionBonCommandeFacade.getLastProgressionAllBonSendDAF();
//        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
        listTypeDoc = new ArrayList<>();

        if (!connexionBean.getCurrentUser().isSecretaireDeDirection() && !connexionBean.getCurrentUser().getFonction().getCode().equals("CSFC")) {
            listTypeDoc.add(typeDocumentFacade.find("210120176"));
            listTypeDoc.add(typeDocumentFacade.find("210120177"));
        } else {
            listTypeDoc = typeDocumentFacade.getAllTypeByApp(connexionBean.getCurrentAppli());
        }
        listMessNonLu = new ArrayList<>();
        listeLigneBudgetaire = ligneBudgetaireFacade.findLigneByExo(exerciceFacade.getCourrantExo());
        listeResteBudgetaaire = new ArrayList<>();
    }

    public LigneBudgetaire getLigneGiverealoction() {
        return ligneGiverealoction;
    }

    public void setLigneGiverealoction(LigneBudgetaire ligneGiverealoction) {
        this.ligneGiverealoction = ligneGiverealoction;
    }

    public List<LigneBudgetaire> getListeResteBudgetaaire() {
        return listeResteBudgetaaire;
    }

    public void setListeResteBudgetaaire(List<LigneBudgetaire> listeResteBudgetaaire) {
        this.listeResteBudgetaaire = listeResteBudgetaaire;
    }

    public long getMontantRealloue() {
        return montantRealloue;
    }

    public void setMontantRealloue(long montantRealloue) {
        this.montantRealloue = montantRealloue;
    }

    public LigneBudgetaire getSelectedLigneBudgetaire() {
        return selectedLigneBudgetaire;
    }

    public void setSelectedLigneBudgetaire(LigneBudgetaire selectedLigneBudgetaire) {
        this.selectedLigneBudgetaire = selectedLigneBudgetaire;
    }

    public Exercice getSelectedExercice() {
        return selectedExercice;
    }

    public void setSelectedExercice(Exercice selectedExercice) {
        this.selectedExercice = selectedExercice;
    }

    public LigneBudgetaire getNewLigneBudgetaire() {
        if (newLigneBudgetaire == null) {
            newLigneBudgetaire = new LigneBudgetaire();
        }
        return newLigneBudgetaire;
    }

    public void setNewLigneBudgetaire(LigneBudgetaire newLigneBudgetaire) {
        this.newLigneBudgetaire = newLigneBudgetaire;
    }

    public List<LigneBudgetaire> getListeLigneBudgetaire() {
        return listeLigneBudgetaire;
    }

    public void setListeLigneBudgetaire(List<LigneBudgetaire> listeLigneBudgetaire) {
        this.listeLigneBudgetaire = listeLigneBudgetaire;
    }

    public List<MessageChat> getListMessNonLu() {
        return listMessNonLu;
    }

    public void setListMessNonLu(List<MessageChat> listMessNonLu) {
        this.listMessNonLu = listMessNonLu;
    }

    public DemandeFacade getDemandeFacade() {
        return demandeFacade;
    }

    public void setDemandeFacade(DemandeFacade demandeFacade) {
        this.demandeFacade = demandeFacade;
    }

    public Demande getSelectedDemande() {

        return selectedDemande;
    }

    public List<Materiel> getListeMateriels() {
        return listeMateriels;
    }

    public void setListeMateriels(List<Materiel> listeMateriels) {
        this.listeMateriels = listeMateriels;
    }

    public Categorie getSelectedCategorie() {
        return selectedCategorie;
    }

    public void setSelectedCategorie(Categorie selectedCategorie) {
        this.selectedCategorie = selectedCategorie;
    }

    public BonCommandeElement getNewCommandeElement() {
        if (newCommandeElement == null) {
            newCommandeElement = new BonCommandeElement();
        }
        return newCommandeElement;
    }

    public Part getFileAdd() {
        return fileAdd;
    }

    public void setFileAdd(Part fileAdd) {
        this.fileAdd = fileAdd;
    }

    public void setNewCommandeElement(BonCommandeElement newCommandeElement) {
        this.newCommandeElement = newCommandeElement;
    }

    public void setSelectedDemande(Demande selectedDemande) {
        this.selectedDemande = selectedDemande;
    }

    public List<Demande> getListeDemandes() {
        return listeDemandes;
    }

    public void setListeDemandes(List<Demande> listeDemandes) {
        this.listeDemandes = listeDemandes;
    }

    public List<TypeDocument> getListTypeDoc() {
        return listTypeDoc;
    }

    public void setListTypeDoc(List<TypeDocument> listTypeDoc) {
        this.listTypeDoc = listTypeDoc;
    }

    public Demande getNewDemande() {
        if (newDemande == null) {
            newDemande = new Demande();
        }
        return newDemande;
    }

    public void setNewDemande(Demande newDemande) {
        this.newDemande = newDemande;
    }

    public void passItem(Demande item) {
        System.out.println("la demande est " + item);
        this.selectedDemande = item;
        this.selectedBonDeCommande = bonCommandeFacade.findBonFromDemande(selectedDemande);
    }

    public void passItemNotif(Notification item) {
        System.out.println("la demande est " + item);
        this.selectedDemande = item.getDemande();
        item.setStatut("See");
        notificationFacade.edit(item);
        this.selectedBonDeCommande = bonCommandeFacade.findBonFromDemande(selectedDemande);

    }

    public TypeDocument getSelectedTypeDocument() {
        if (selectedTypeDocument == null) {
            selectedTypeDocument = new TypeDocument();
        }
        return selectedTypeDocument;
    }

    public void setSelectedTypeDocument(TypeDocument selectedTypeDocument) {
        this.selectedTypeDocument = selectedTypeDocument;
    }

    public void passItemBon(Boncommande item) {
        System.out.println("la demande est " + item);
        this.selectedBonDeCommande = item;

    }

    public List<Exercice> getListExercice() {
        return listExercice;
    }

    public void setListExercice(List<Exercice> listExercice) {
        this.listExercice = listExercice;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public List<Demande> getListeDemandesByUser() {
//        if (newDemande == null) {
//            newDemande = new Demande();
//        }
        return listeDemandesByUser;
    }

    public boolean isSendBonCSFC() {
        return sendBonCSFC;
    }

    public void setSendBonCSFC(boolean sendBonCSFC) {
        this.sendBonCSFC = sendBonCSFC;
    }

    public void setListeDemandesByUser(List<Demande> listeDemandesByUser) {
        this.listeDemandesByUser = listeDemandesByUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProgressionDemande getNewProgressionDemande() {
        return newProgressionDemande;
    }

    public Element getSelectElement() {
        if (selectElement == null) {
            selectElement = new Element();
        }
        return selectElement;
    }

    public List<ProgressionBonCommande> getListPrBonCommande() {
        return listPrBonCommande;
    }

    public void setListPrBonCommande(List<ProgressionBonCommande> listPrBonCommande) {
        this.listPrBonCommande = listPrBonCommande;
    }

    public void setSelectElement(Element selectElement) {
        this.selectElement = selectElement;
    }

    public List<Demande> getListeDemandesInDirection() {
        return listeDemandesInDirection;
    }

    public void setListeDemandesInDirection(List<Demande> listeDemandesInDirection) {
        this.listeDemandesInDirection = listeDemandesInDirection;
    }

    public void setNewProgressionDemande(ProgressionDemande newProgressionDemande) {
        this.newProgressionDemande = newProgressionDemande;
    }

    public boolean isEnvoi() {
        return envoi;
    }

    public void setEnvoi(boolean envoi) {
        this.envoi = envoi;
    }

    public List<Demande> getListDemandeWaitAuto() {
        return listDemandeWaitAuto;
    }

    public void setListDemandeWaitAuto(List<Demande> listDemandeWaitAuto) {
        this.listDemandeWaitAuto = listDemandeWaitAuto;
    }

    public boolean isAutorise() {
        return autorise;
    }

    public void setAutorise(boolean autorise) {
        this.autorise = autorise;
    }

    public Utilisateur getChefService() {
        return chefService;
    }

    public void setChefService(Utilisateur chefService) {
        this.chefService = chefService;
    }

    public Materiel getSelectedMateriel() {
        if (selectedMateriel == null) {
            selectedMateriel = new Materiel();
        }
        return selectedMateriel;
    }

    public void setSelectedMateriel(Materiel selectedMateriel) {
        this.selectedMateriel = selectedMateriel;
    }

    public Service getServiceSelected() {
        return serviceSelected;
    }

    public BonCommande_Element getSelectBonCommandeElement() {
        return selectBonCommandeElement;
    }

    public void setSelectBonCommandeElement(BonCommande_Element selectBonCommandeElement) {
        this.selectBonCommandeElement = selectBonCommandeElement;
    }

    public void setServiceSelected(Service serviceSelected) {
        this.serviceSelected = serviceSelected;
    }

    public List<Element> getListElement() {
        return listElement;
    }

    public void setListElement(List<Element> listElement) {
        this.listElement = listElement;
    }

    public ProgressionDemande getSelectedProgressionDemande() {
        return selectedProgressionDemande;
    }

    public void setSelectedProgressionDemande(ProgressionDemande selectedProgressionDemande) {
        this.selectedProgressionDemande = selectedProgressionDemande;
    }

    public boolean isSendDG() {
        return SendDG;
    }

    public void setSendDG(boolean SendDG) {
        this.SendDG = SendDG;
    }

    public boolean isSendCSFC() {
        return SendCSFC;
    }

    public void setSendCSFC(boolean SendCSFC) {
        this.SendCSFC = SendCSFC;
    }

    public boolean isSendBonDG() {
        return sendBonDG;
    }

    public void setSendBonDG(boolean sendBonDG) {
        this.sendBonDG = sendBonDG;
    }

    public Boncommande getNewBonDeCommande() {
        if (newBonDeCommande == null) {
            newBonDeCommande = new Boncommande();
        }
        return newBonDeCommande;
    }

    public void setNewBonDeCommande(Boncommande newBonDeCommande) {
        this.newBonDeCommande = newBonDeCommande;
    }

    public List<String> getListeElementTotal() {
        return listeElementTotal;
    }

    public void setListeElementTotal(List<String> listeElementTotal) {
        this.listeElementTotal = listeElementTotal;
    }

    public String getElementTampon() {
        return ElementTampon;
    }

    public void setElementTampon(String ElementTampon) {
        this.ElementTampon = ElementTampon;
    }

    public Element getNewElement() {
        if (newElement == null) {
            newElement = new Element();
        }
        return newElement;
    }

    public void setNewElement(Element newElement) {
        this.newElement = newElement;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<Boncommande> getListBonCommande() {
        return listBonCommande;
    }

    public void setListBonCommande(List<Boncommande> listBonCommande) {
        this.listBonCommande = listBonCommande;
    }

    public List<BonCommande_Element> getListBonCommande_Element() {
        return listBonCommande_Element;
    }

    public void setListBonCommande_Element(List<BonCommande_Element> listBonCommande_Element) {
        this.listBonCommande_Element = listBonCommande_Element;
    }

    public Document getSelectedDocument() {
        return SelectedDocument;
    }

    public void setSelectedDocument(Document SelectedDocument) {
        this.SelectedDocument = SelectedDocument;
    }

    public String getMessageCorrection() {
        return messageCorrection;
    }

    public void setMessageCorrection(String messageCorrection) {
        this.messageCorrection = messageCorrection;
    }

    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            newProgressionDemande = new ProgressionDemande();
            newDemande.setUtilisateurSend(connexionBean.getCurrentUser());
            newDemande.setDateCreation(new Date());
            System.out.println("selectedMateriele " + selectedMateriel);
            newDemande.setMateriel(selectedMateriel);
            String code = this.demandeFacade.createAndGidCode(newDemande);
            newDemande.setGed(creerDossier(demandeFacade.find(code)));
            demandeFacade.edit(newDemande);
            Evenement even = evenementFacade.find("Dem001");
            System.out.println("1");
            newProgressionDemande.setDateCreation(new Date());
            newProgressionDemande.setDemande(newDemande);
            newProgressionDemande.setEvenement(even);
            newProgressionDemande.setUserSend(connexionBean.getCurrentUser());
            newProgressionDemande.setUserReceive(connexionBean.getCurrentUser());
            newProgressionDemande.setStatut("Nouvelle Demande");
            System.out.println("2");
            this.progressionDemandeFacade.create(newProgressionDemande);
            msg = bundle.getString("DemandeCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newDemande = new Demande();
            this.listeDemandes = this.demandeFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("DemandeCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
            System.out.println("Erreur " + e);
        }
    }

    public void doEdit(ActionEvent event) {

        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;

        try {
            demandeFacade.edit(selectedDemande);
            this.listeDemandes = this.demandeFacade.findAll();
            msg = bundle.getString("DemandeEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("DemandeEditErrorMsg");
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
            demandeFacade.remove(selectedDemande);
            msg = bundle.getString("DemandeDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeDemandes.remove(this.selectedDemande);
            this.listeDemandes = this.demandeFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("DemandeDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void recupererId() {
        try {
            id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
            System.out.println(" Id ---" + id);
            if (id != null) {
                selectedDemande = demandeFacade.find(id);
                findAllSigns();
            }
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//        response.sendRedirect("GestionDossier/index.xhtml");
        } catch (Exception e) {
            System.out.println("Erreur " + e);
        }

    }

    public void recupererIdBonCommande() {
        try {
            id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
            System.out.println(" Id ---" + id);
            if (id != null) {
                selectedBonDeCommande = bonCommandeFacade.find(id);
                listBonCommande_Element = bonCommande_ElementFacade.getListBonCommande_Element(selectedBonDeCommande);
                System.out.println("la lise est listBonCommande_Element  " + listBonCommande_Element);
                findAllSigns();
            }
//        FacesContext context = FacesContext.getCurrentInstance();
//        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//        response.sendRedirect("GestionDossier/index.xhtml");
        } catch (Exception e) {
            System.out.println("Erreur " + e);
        }

    }

    public List<BonCommandeElement> getListCommandeElement() {
        return listCommandeElement;
    }

    public void setListCommandeElement(List<BonCommandeElement> listCommandeElement) {
        this.listCommandeElement = listCommandeElement;
    }

    public void findListDemandeInDate() {
        listeDemandesByUser = demandeFacade.getAllDemandeByUserDate(connexionBean.getCurrentUser(), date1, date2);
    }

    public ProgressionDemande findLastProgressionDemande() {
        if (selectedDemande != null) {
            return progressionDemandeFacade.getLastProgressionDemande(selectedDemande);
        }
        return null;
    }

    public String getTypeAjout() {
        return typeAjout;
    }

    public void setTypeAjout(String typeAjout) {
        this.typeAjout = typeAjout;
    }

    public void reset() {
        newDemande.reset();
    }

    public List<Demande> findListDemandeAndYear() {
        return progressionDemandeFacade.getDemandeCreesAndYear(connexionBean.getCurrentUser());
    }

    public List<ProgressionDemande> findListEvenement() {

        if (selectedDemande != null) {
            return progressionDemandeFacade.getListAllProgressionFromDemande(selectedDemande);
        }
        List<ProgressionDemande> listDefault = new ArrayList<>();
        return listDefault;
    }

    public List<ProgressionBonCommande> findListEvenementBon() {

        if (selectedDemande != null) {
            return progressionBonCommandeFacade.getListAllProgressionFromDemande(selectedBonDeCommande);
        }
        List<ProgressionBonCommande> listDefault = new ArrayList<>();
        return listDefault;
    }

    public ProgressionBonCommande getNewProgressionBonCommande() {
        return newProgressionBonCommande;
    }

    public void setNewProgressionBonCommande(ProgressionBonCommande newProgressionBonCommande) {
        this.newProgressionBonCommande = newProgressionBonCommande;
    }

    public void findListDemandeInDir() {
        if (connexionBean.getCurrentUser() != null && !connexionBean.getCurrentUser().isSecretaireDeDirection()) {
            listeDemandesInDirection = demandeFacade.getAllDemandeInDir(connexionBean.getCurrentUser().getFonction().getService(), date1, date2);
            for (Demande demande : listeDemandesInDirection) {
                if (findIfWaitAuto(demande)) {
                    listDemandeWaitAuto.add(demande);
                }
            }
        }

    }

    public List<Demande> findListDemandeTermine() {
        return progressionDemandeFacade.getListAllProgressionFromDemandeTermine(connexionBean.getCurrentUser(), date1, date2);
    }

    public List<Demande> findListDemandeByMe() {
        if (connexionBean.getCurrentUser().isChefService()) {
            return progressionDemandeFacade.getListAllProgressionFromDemandeCreatInService(connexionBean.getCurrentUser().getFonction().getService(), date1, date2);

        } else {
            return progressionDemandeFacade.getListAllProgressionFromDemandeCreatByMe(connexionBean.getCurrentUser(), date1, date2);
        }
    }

    public List<Document> findListDocx() {
        return documentFacade.getListeDocumentsByDemmande(selectedDemande);
    }

    public boolean findIfWaitAuto(Demande demande) {
        if (connexionBean.getCurrentUser() != null && demande != null) {
            return progressionDemandeFacade.getLastProgressionDemandeForUser(demande, connexionBean.getCurrentUser());
        }
        return false;
    }

    public void findAllSigns() {
        for (ProgressionDemande pgd : progressionDemandeFacade.getListAllProgressionFromDemande(selectedDemande)) {
            if (pgd.getEvenement().getCode().equals("Dem002")) {
                envoi = true;
            }
            if (pgd.getEvenement().getCode().equals("Dem003")) {
                autorise = true;
                findChefService(selectedDemande.getUtilisateurSend().getFonction().getService());
            }

            if (pgd.getEvenement().getCode().equals("Dem004")) {
                SendDG = true;

            }
            if (pgd.getEvenement().getCode().equals("Dem005")) {
                SendCSFC = true;

            }

        }

        for (ProgressionBonCommande pbc : progressionBonCommandeFacade.getListAllProgressionFromDemande(selectedBonDeCommande)) {
            if (pbc.getEvenement().getCode().equals("Dem008")) {
                sendBonDG = true;
            }
            if (pbc.getEvenement().getCode().equals("Dem009")) {
                sendBonCSFC = true;
            }
        }
        System.out.println(" even trouve " + envoi);
    }

    public void doEnvoi(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            Evenement even = evenementFacade.find("Dem002");
            newProgressionDemande = new ProgressionDemande();
            newProgressionDemande.setDateCreation(new Date());
            newProgressionDemande.setDemande(selectedDemande);
            newProgressionDemande.setEvenement(even);
            newProgressionDemande.setUserReceive(connexionBean.getUserChefService());
            newProgressionDemande.setUserSend(connexionBean.getCurrentUser());
            newProgressionDemande.setStatut("Nouvelle Demande");
            progressionDemandeFacade.create(newProgressionDemande);
            notificationFacade.createNotificationDemande(connexionBean.getCurrentUser(), connexionBean.getUserChefService(), "Une demande vient de vous être addressée par " + connexionBean.getCurrentUser().getName(), selectedDemande, connexionBean.getCurrentAppli());
            notificationEndPoint.sendNotif(connexionBean.getCurrentUser(), connexionBean.getUserChefService(), "Une demande vient de vous être addressée par " + connexionBean.getCurrentUser().getName());
            msg = bundle.getString("DemandeEnvoiSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeDemandes.remove(this.selectedDemande);
            this.listeDemandes = this.demandeFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("DemandeEnvoiErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEnvoiBon(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            System.out.println("1");
            Fonction fonc = fonctionFacade.find("DAF");
            Utilisateur userDaf = utilisateurFacade.getUserByFonction(fonc);
            System.out.println("2");
            Evenement even = evenementFacade.find("Dem007");
            newProgressionBonCommande = new ProgressionBonCommande();
            newProgressionBonCommande.setDateCreation(new Date());
            newProgressionBonCommande.setBoncommande(selectedBonDeCommande);
            newProgressionBonCommande.setEvenement(even);
            System.out.println("5");
            newProgressionBonCommande.setUserReceive(userDaf);
            System.out.println("6");
            newProgressionBonCommande.setUserSend(connexionBean.getCurrentUser());
            System.out.println("7");
            progressionBonCommandeFacade.create(newProgressionBonCommande);
            System.out.println("4");
            listPrBonCommande = progressionBonCommandeFacade.getLastProgressionAllBon();
            notificationFacade.createNotificationDemande(connexionBean.getCurrentUser(), userDaf, "Le bon de commmande cencernantla demmande " + selectedBonDeCommande.getDemande().getCode() + " vient de vous être addressé par " + connexionBean.getCurrentUser().getName(), selectedBonDeCommande.getDemande(), connexionBean.getCurrentAppli());
            notificationEndPoint.sendNotif(connexionBean.getCurrentUser(), userDaf, "Le bon de commmande cencernantla demmande " + selectedBonDeCommande.getDemande().getCode() + " vient de vous être addressé par " + connexionBean.getCurrentUser().getName());
            msg = bundle.getString("BonEnvoiSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listBonCommande.remove(this.selectedBonDeCommande);
            this.listBonCommande = this.bonCommandeFacade.findAll();
        } catch (Exception e) {
            System.out.println("Erreur " + e);
            msg = bundle.getString("BonEnvoiErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doAutorisation(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            Fonction fonc;
            Evenement even;
            if (connexionBean.getCurrentUser().getFonction().getCode().equals("CSISP") || connexionBean.getCurrentUser().getFonction().getCode().equals("CSARH")) {
                fonc = fonctionFacade.find("DAF");
                even = evenementFacade.find("Dem003");
            } else {
                fonc = fonctionFacade.find("CSARH");
                even = evenementFacade.find("Dem0011");
            }
            Utilisateur userDaf = utilisateurFacade.getUserByFonction(fonc);

            newProgressionDemande = new ProgressionDemande();
            newProgressionDemande.setDateCreation(new Date());
            newProgressionDemande.setDemande(selectedDemande);
            newProgressionDemande.setEvenement(even);
            newProgressionDemande.setUserReceive(userDaf);
            newProgressionDemande.setUserSend(connexionBean.getCurrentUser());
            newProgressionDemande.setStatut("En cours de traitement");
            progressionDemandeFacade.create(newProgressionDemande);
            notificationFacade.createNotificationDemande(connexionBean.getCurrentUser(), userDaf, "Une demande vient de vous être addressée par " + connexionBean.getCurrentUser().getName(), selectedDemande, connexionBean.getCurrentAppli());
            notificationEndPoint.sendNotif(connexionBean.getCurrentUser(), userDaf, "Une demande vient de vous être addressée par " + connexionBean.getCurrentUser().getName());
            findListDemandeInDir();
            if (connexionBean.getCurrentUser().getFonction().getCode().equals("CSISP") || connexionBean.getCurrentUser().getFonction().getCode().equals("CSARH")) {
                msg = bundle.getString("DemandeAutoriseSuccessMsg");
            } else {
                msg = bundle.getString("DemandeAutoriseCSARHSuccessMsg");
            }

            JsfUtil.addSuccessMessage(msg);
            listDemandeWaitAuto.remove(selectedDemande);
            this.listeDemandes.remove(this.selectedDemande);
            this.listeDemandes = this.demandeFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("DemandeAutoriseErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doAutorisationByDG(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            Fonction fonc = fonctionFacade.find("CSFC");
            Utilisateur userCSFC = utilisateurFacade.getUserByFonction(fonc);
            Evenement even = evenementFacade.find("Dem005");
            newProgressionDemande = new ProgressionDemande();
            newProgressionDemande.setDateCreation(new Date());
            newProgressionDemande.setDemande(selectedDemande);
            newProgressionDemande.setEvenement(even);
            newProgressionDemande.setUserReceive(userCSFC);
            newProgressionDemande.setUserSend(connexionBean.getCurrentUser());
            newProgressionDemande.setStatut("En cours de traitement");
            progressionDemandeFacade.create(newProgressionDemande);
            notificationFacade.createNotificationDemande(connexionBean.getCurrentUser(), userCSFC, "Une demande vient de vous être addressée par " + connexionBean.getCurrentUser().getName(), selectedDemande, connexionBean.getCurrentAppli());
            notificationEndPoint.sendNotif(connexionBean.getCurrentUser(), userCSFC, "Une demande vient de vous être addressée par " + connexionBean.getCurrentUser().getName());
            msg = bundle.getString("DemandeAutoriseSuccessDGMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeDemandes.remove(this.selectedDemande);
            this.listeDemandes = this.demandeFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("DemandeAutoriseErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEnvoiDG(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle("util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            if (selectedProgressionDemande != null) {
                selectedDemande = selectedProgressionDemande.getDemande();
            }
            Fonction fonc = fonctionFacade.find("DG");
            Utilisateur userDg = utilisateurFacade.getUserByFonction(fonc);
            Evenement even = evenementFacade.find("Dem004");
            newProgressionDemande = new ProgressionDemande();
            newProgressionDemande.setDateCreation(new Date());
            newProgressionDemande.setDemande(selectedDemande);
            newProgressionDemande.setEvenement(even);
            newProgressionDemande.setUserReceive(userDg);
            newProgressionDemande.setUserSend(connexionBean.getCurrentUser());
            newProgressionDemande.setStatut("En cours de traitement");
            progressionDemandeFacade.create(newProgressionDemande);
            notificationFacade.createNotificationDemande(connexionBean.getCurrentUser(), userDg, "Une demande vient de vous être addressée par " + connexionBean.getCurrentUser().getName(), selectedDemande, connexionBean.getCurrentAppli());
            notificationEndPoint.sendNotif(connexionBean.getCurrentUser(), userDg, "Une demande vient de vous être addressée par " + connexionBean.getCurrentUser().getName());
            msg = bundle.getString("DemandeAutoriseSuccessSndDGMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeDemandes.remove(this.selectedDemande);
            this.listeDemandes = this.demandeFacade.findAll();
        } catch (Exception e) {
            System.out.println("Errur >>>" + e);
            msg = bundle.getString("DemandeAutoriseErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void findChefService(Service service) {
        chefService = utilisateurFacade.getFonctionChef(service);
    }

    public List<ProgressionDemande> getDemandeByService(Service service) {
        List<ProgressionDemande> listTemmpon = new ArrayList<>();
        listPrDemSendDAF = progressionDemandeFacade.getLastProgressionAllDemande();
        if (listPrDemSendDAF != null) {
            for (ProgressionDemande Pd : listPrDemSendDAF) {
                if (Pd.getUserSend().getFonction().getService().equals(service)) {
                    listTemmpon.add(Pd);
                }
            }
        }
        return listTemmpon;
    }

    public void passService(Service service) {
        serviceSelected = service;
    }

    public Boncommande getSelectedBonDeCommande() {
        return selectedBonDeCommande;
    }

    public void setSelectedBonDeCommande(Boncommande selectedBonDeCommande) {
        this.selectedBonDeCommande = selectedBonDeCommande;
    }

    public void passProgressionDemande(ProgressionDemande PgDe) {
        selectedProgressionDemande = PgDe;
    }

    public List<ProgressionDemande> findListSendDG() {
        return progressionDemandeFacade.getLastProgressionAllDemandeSendDG();
    }

    public List<ProgressionDemande> findListSendCSFC() {
        return progressionDemandeFacade.getLastProgressionAllDemandeSendCSFC();
    }

    public List<ProgressionDemande> findListSendCSFCandNoFinish() {
        return progressionDemandeFacade.getLastProgressionAllDemandeSendCSFCAndNonFinsh(connexionBean.getCurrentUser().getFonction());
    }

    public List<ProgressionDemande> findListSendByMeAndfinishNot() {
        return progressionDemandeFacade.getLastProgressionAllDemandeSendByMeAndNonFinsh(connexionBean.getCurrentUser().getFonction());
    }

    public void test() {
        System.out.println("la demande est " + selectedDemande.getCode());
    }

    public void passElement(Element element) {
        newElement = element;
    }

    public void ajouterElement() {
        if (newElement.getLibelle() != null) {
            if (selectElement == null) {
                newElement.setDateCreation(new Date());
                elementFacade.create(newElement);
            }
            if (selectElement != null && !selectElement.getLibelle().equals(newElement.getLibelle())) {
                newElement.setDateCreation(new Date());
                elementFacade.create(newElement);
            }
            newCommandeElement.setElement(newElement);
            newCommandeElement.setMontant(newCommandeElement.getPrixUnit() * newCommandeElement.getQte());
            listCommandeElement.add(newCommandeElement);
            total += newCommandeElement.getMontant();
            newElement = new Element();
            selectElement = new Element();
            newCommandeElement = new BonCommandeElement();

        }
        listElement = elementFacade.findAll();
    }

    public void deleteItem(BonCommandeElement element) {
        total -= element.getMontant();
        listCommandeElement.remove(element);

    }

    public void doCreatBonCommande(ActionEvent even) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle("util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            System.out.println("la demande est " + selectedDemande);
            Evenement evenement = evenementFacade.find("Dem006");
            newProgressionDemande = new ProgressionDemande();
            newProgressionDemande.setDateCreation(new Date());
            newProgressionDemande.setDemande(newBonDeCommande.getDemande());
            newProgressionDemande.setEvenement(evenement);
            newProgressionDemande.setUserReceive(connexionBean.getCurrentUser());
            newProgressionDemande.setUserSend(connexionBean.getCurrentUser());
            newProgressionDemande.setStatut("En cours de traitement");
            progressionDemandeFacade.create(newProgressionDemande);
            newBonDeCommande.setDateCreation(new Date());

            String code = bonCommandeFacade.createGivCode(newBonDeCommande);

            Boncommande boncommandeTampon = bonCommandeFacade.find(code);
            List<BonCommande_Element> listBonComEle = new ArrayList<>();

            for (BonCommandeElement bcE : listCommandeElement) {
                BonCommande_Element bC_E = new BonCommande_Element();
                bC_E.setBonCommande(boncommandeTampon);
                bC_E.setBonCommandeCode(code);
                bC_E.setDateCreation(new Date());
                bC_E.setElement(bcE.getElement());
                bC_E.setElementCode(bcE.getElement().getCode());
                bC_E.setMontant(bcE.getMontant());
                bC_E.setPrixUnit(bcE.getPrixUnit());
                bC_E.setQte(bcE.getQte());
                bonCommande_ElementFacade.create(bC_E);
                listBonComEle.add(bC_E);

            }

            boncommandeTampon.setBonAndElement(listBonComEle);
            boncommandeTampon.setTotalHT(total);
            boncommandeTampon.setTVA(18);
            boncommandeTampon.setNetApayerTTC(total + ((total * 18) / 100));
            bonCommandeFacade.edit(boncommandeTampon);

            Boncommande boncommandeTampon2 = bonCommandeFacade.find(code);
            System.out.println("le bon est 1");
            long MtRestant = boncommandeTampon2.getLigneBudgetaire().getMontantRestant();
            System.out.println("le bon est 2");
            long MtRestant2 = MtRestant - (total + ((total * 18) / 100));
            System.out.println("le bon est 3");
            boncommandeTampon2.getLigneBudgetaire().setMontantRestant(MtRestant2);
            System.out.println("le bon est 3");
            ligneBudgetaireFacade.edit(boncommandeTampon2.getLigneBudgetaire());
            System.out.println("le bon est 4");
            Evenement even1 = evenementFacade.find("Dem006");
            newProgressionBonCommande = new ProgressionBonCommande();
            newProgressionBonCommande.setDateCreation(new Date());
            newProgressionBonCommande.setBoncommande(boncommandeTampon);
            newProgressionBonCommande.setEvenement(even1);
            newProgressionBonCommande.setUserReceive(connexionBean.getCurrentUser());
            newProgressionBonCommande.setUserSend(connexionBean.getCurrentUser());
            progressionBonCommandeFacade.create(newProgressionBonCommande);
            listPrBonCommande = progressionBonCommandeFacade.getLastProgressionAllBon();

            msg = bundle.getString("BonSaveSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            System.out.println("Errur >>>" + e);
            msg = bundle.getString("BonSaveErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }

    }

    public List<ProgressionBonCommande> findListBonSendFromService(Service service) {
        List<ProgressionBonCommande> listPgBCom = new ArrayList<>();
        System.out.println("listPrBonCommandeSendDAF -->" + listPrBonCommandeSendDAF);
        if (service != null && listPrBonCommandeSendDAF != null) {
            for (ProgressionBonCommande PrBc : listPrBonCommandeSendDAF) {
                System.out.println("service -->" + service.getCode());
                System.out.println("Autre -->" + PrBc.getBoncommande().getDemande().getUtilisateurSend().getFonction().getService().getCode());
                if (PrBc.getBoncommande().getDemande().getUtilisateurSend().getFonction().getService().getCode().equals(service.getCode())) {
                    listPgBCom.add(PrBc);
                    System.out.println("Ok");
//                listPrBonCommandeSendDAF.remove(PrBc);
                }
            }
        }
        return listPgBCom;

    }

    public void passDemande() {
        newBonDeCommande.setDemande(selectedDemande);
    }

    public void passLigneBc() {
        newBonDeCommande.setLigneBudgetaire(selectedLigneBudgetaire);
    }

    public void doAutoBonByDAF(ActionEvent event, Boncommande bonCommande) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            Fonction fonc = fonctionFacade.find("DG");
            Utilisateur userDg = utilisateurFacade.getUserByFonction(fonc);
            Evenement even = evenementFacade.find("Dem008");
            newProgressionBonCommande = new ProgressionBonCommande();
            newProgressionBonCommande.setDateCreation(new Date());
            newProgressionBonCommande.setBoncommande(bonCommande);
            newProgressionBonCommande.setEvenement(even);
            newProgressionBonCommande.setUserReceive(userDg);
            newProgressionBonCommande.setUserSend(connexionBean.getCurrentUser());
            progressionBonCommandeFacade.create(newProgressionBonCommande);
            notificationFacade.createNotificationDemande(connexionBean.getCurrentUser(), userDg, "Le bon de commmande cencernantla demmande " + bonCommande.getDemande().getCode() + " vient de vous être addressé par " + connexionBean.getCurrentUser().getName(), bonCommande.getDemande(), connexionBean.getCurrentAppli());
            notificationEndPoint.sendNotif(connexionBean.getCurrentUser(), userDg, "Le bon de commmande cencernantla demmande " + bonCommande.getDemande().getCode() + " vient de vous être addressé par " + connexionBean.getCurrentUser().getName());
            RequestContext requestContext = RequestContext.getCurrentInstance();
            requestContext.execute("$('#listBonCommandeDialog').modal('hide')");
            msg = bundle.getString("BonEnvoiSuccessMsg");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            msg = bundle.getString("BonEnvoiErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doAutoBonByDG(ActionEvent event, Boncommande bonCommande) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            Fonction fonc = fonctionFacade.find("CSFC");
            Utilisateur usercsfc = utilisateurFacade.getUserByFonction(fonc);
            Evenement even = evenementFacade.find("Dem009");
            newProgressionBonCommande = new ProgressionBonCommande();
            newProgressionBonCommande.setDateCreation(new Date());
            newProgressionBonCommande.setBoncommande(bonCommande);
            newProgressionBonCommande.setEvenement(even);
            newProgressionBonCommande.setUserReceive(usercsfc);
            newProgressionBonCommande.setUserSend(connexionBean.getCurrentUser());
            progressionBonCommandeFacade.create(newProgressionBonCommande);
            notificationFacade.createNotificationDemande(connexionBean.getCurrentUser(), usercsfc, "Le bon de commmande cencernantla demmande " + bonCommande.getDemande().getCode() + " vient de vous être addressé par " + connexionBean.getCurrentUser().getName(), bonCommande.getDemande(), connexionBean.getCurrentAppli());
            notificationEndPoint.sendNotif(connexionBean.getCurrentUser(), usercsfc, "Le bon de commmande cencernantla demmande " + bonCommande.getDemande().getCode() + " vient de vous être addressé par " + connexionBean.getCurrentUser().getName());
            msg = bundle.getString("BonEnvoiSuccessMsg");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            msg = bundle.getString("BonEnvoiErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public List<ProgressionBonCommande> findListBonSendDG() {
        return progressionBonCommandeFacade.getLastProgressionAllBonSendDG();
    }

    public List<ProgressionBonCommande> findListBonSendCSFC() {
        return progressionBonCommandeFacade.getLastProgressionAllBonSendCSFC();
    }

    public Ged creerDossier(Demande demande) {
        gedFacade = connexionFacade.initGed(connexionBean.getCurrentUser());
        Ged ged;

//        String name = entity.getNumeroRP().replaceAll("/", "-");
        String name = demande.getCode();
        System.out.println("nom dossier issu du split pour etre name ds ged " + name);
        DossierCmis dossierCmis;

        System.out.println("description du dossier ");
        CmisObject obj;

//        CmisObject obj;
//
        System.out.println("contextBean.getALFRESCO_REPOSITORY() " + contextFacade.getALFRESCO_REPOSITORY());
//        System.out.println("annee.getValeur() " + annee.getValeur());
        System.out.println("name" + name);

        obj = gedFacade.getCmisObjectByPath(cheminSuivi + "/" + name);
        System.out.println(" le parent est alors  " + cheminSuivi);
        if (obj == null) {

            dossierCmis = gedFacade.creerDossier(cheminSuivi, "Demande_" + demande.getCode(), "nouveau dossier" + name + " créé en ce jour le " + new Date());
        } else {
            dossierCmis = new DossierCmis((Folder) obj);
        }

        ged = new Ged(dossierCmis.getId(), name, dossierCmis.getId());
        gedFacade2.create(ged);

        return ged;
    }

    public void doCreateDocument(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            System.out.println("1-----------------");

            System.out.println("3-----------------");
            gedFacade = connexionFacade.initGed(connexionBean.getCurrentUser());
            System.out.println("lid du dossier --" + selectedDemande.getCode());
            // documentFacade.create(listDocument, listFile, dossierFacade.find(iddossier), selectedParties, audienceFacade.find(idaudience), "DOS-003", "PIECE");
            controlSizeFile(fileAdd);

//            documentFacade.createDocument(listDocumentToPersit, listFile, selectedFolderByGet, selectedParties, "DIC-022", "PIECE");
            Document newDocumentToPersit = new Document();
            newDocumentToPersit.setFileName(fileAdd.getSubmittedFileName());
            newDocumentToPersit.setMimeType(fileAdd.getContentType());
            newDocumentToPersit.setTaille("" + fileAdd.getSize());
            newDocumentToPersit.setSens(true);
            newDocumentToPersit.setTypeDocument(selectedTypeDocument);
            newDocumentToPersit.setDemande(selectedDemande);
//             documentFacade.create(newDocumentToPersit);

            System.out.println("----------------------------------");
            System.out.println("doc name: " + newDocumentToPersit.getFileName());
            System.out.println("doc ged: " + newDocumentToPersit.getGed());
            System.out.println("doc mimeType: " + newDocumentToPersit.getMimeType());
            System.out.println("doc getTaille: " + newDocumentToPersit.getTaille());
            System.out.println("doc Type: " + newDocumentToPersit.getTypeDocument().getLibelle());
            System.out.println("----------------------------------");

            Ged ged;
            String name = newDocumentToPersit.getFileName();
            String[] ext = name.split("\\.");
            if (ext.length > 0) {
                name = ext[ext.length - 1];
            } else {
                name = "";
            }

            name = JsfUtil.getFileName(name, newDocumentToPersit.getTypeDocument().getLibelle());
            DocumentCmis documentCmis;

            //dossierCmis = gedFacade.creerDossier(authenticateBean.getConfig().getREPOSITORY().getValeur(), name, entity.getLibelleCourt());
            System.out.println("----------------------------------");
            System.out.println("selectedDossier.getGed().getRefGed(): " + selectedDemande.getGed().getRefGed());
            System.out.println("name: " + name);
            System.out.println("part.getInputStream(): " + fileAdd.getInputStream());
            System.out.println("document.getMimeType(): " + newDocumentToPersit.getMimeType());

            System.out.println("----------------------------------");

            documentCmis = gedFacade.creerDocument(selectedDemande.getGed().getRefGed(), fileAdd.getSubmittedFileName(), fileAdd.getInputStream(),
                    newDocumentToPersit.getMimeType(), "nouveau fichier");
            ged = new Ged(documentCmis.getId(), name, documentCmis.getId());
            gedFacade2.create(ged);

            Document docToPersit = new Document();
//            String localName = newDocumentToPersit.getFileName() + "." + JsfUtil.convertDate(new Date(), "EEEEE dd MMM yyyy 'A' HH:mm:ss");
            System.out.println("le fichier " + fileAdd.getSubmittedFileName());
            docToPersit.setFileName(fileAdd.getSubmittedFileName());
            docToPersit.setGed(ged);
            docToPersit.setMimeType(newDocumentToPersit.getMimeType());
            docToPersit.setSens(true);
            docToPersit.setSubmittedFileName(name);
            docToPersit.setTaille(newDocumentToPersit.getTaille());
            docToPersit.setTypeDocument(newDocumentToPersit.getTypeDocument());
            docToPersit.setDemande(selectedDemande);

            docToPersit.setDescription("fichier " + selectedTypeDocument.getLibelle() + " enregistré ce " + JsfUtil.convertDate(new Date(), "EEEEE dd MMM yyyy 'A' HH:mm:ss"));
            documentFacade.create(docToPersit);

            System.out.println("4-----------------");
            msg = bundle.getString("ProgressionCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            System.out.println("0-----------------");
            msg = bundle.getString("CreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
            System.err.println("exception ==> " + e);
        }

    }

    public void controlSizeFile(Part myFile) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        // System.out.println("fichier est: "+file1);

        try {
            Long tailleFichierEnMo = (myFile.getSize() / (1024 * 1024));
            System.out.println("la taille du fichier convertie en Mo est: " + tailleFichierEnMo);

            String tailleFichier = JsfUtil.convertFileSize(myFile.getSize());
            System.out.println("la taille du fichier formatée est: " + tailleFichier);

            if (tailleFichierEnMo > 40) {
                //msg = bundle.getString("WarningHeavingMsg");
//                JsfUtil.addErrorMessage("Taille "+ tailleFichier+"! Fichier trop lourd. Veuillez limiter à 10Mo");
                JsfUtil.addWarningMessage("Fichier " + myFile.getName() + "de taille " + tailleFichier + "est trop lourd! Veuillez limiter à 40Mo");
                throw new Exception();
                // System.out.println("msg---" + msg);
            }
//            else {
//                throw new Exception("Fichier trop!Veuillez limiter la taille du fichier " + myFile.getName());
//            }
        } catch (Exception e) {
            System.out.println("Erreur " + e);
            //msg = bundle.getString("ProgressionPiecesCreateErrorMsg");
            //JsfUtil.addErrorMessage(msg);
        }
    }

    public void doFinish(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            Evenement even = evenementFacade.find("Dem0010");
            newProgressionDemande = new ProgressionDemande();
            newProgressionDemande.setDateCreation(new Date());
            newProgressionDemande.setDemande(selectedDemande);
            newProgressionDemande.setEvenement(even);
            newProgressionDemande.setUserReceive(connexionBean.getCurrentUser());
            newProgressionDemande.setUserSend(connexionBean.getCurrentUser());
            newProgressionDemande.setStatut("Terminée");
            progressionDemandeFacade.create(newProgressionDemande);
            notificationFacade.createNotificationDemande(connexionBean.getCurrentUser(), selectedDemande.getUtilisateurSend(), " Monsieur de CSFC vient de terminer le processus de votre demande du " + connexionBean.convertDate(selectedDemande.getDate(), "dd/MM/yyyy"), selectedDemande, connexionBean.getCurrentAppli());
            notificationEndPoint.sendNotif(connexionBean.getCurrentUser(), selectedDemande.getUtilisateurSend(), "Monsieur de CSFC vient de terminer le processus de votre demande du " + connexionBean.convertDate(selectedDemande.getDate()));
            msg = bundle.getString("TerminerSuccessMsg");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            msg = bundle.getString("CreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void test2(Document doc) {
        System.out.println("le document estt " + doc.getFileName());
    }

    public void doSendCorrec(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            notificationFacade.createCorrectionDemande(connexionBean.getCurrentUser(), selectedDemande.getUtilisateurSend(), messageCorrection, selectedDemande, connexionBean.getCurrentAppli());
            notificationEndPoint.sendMessageCorrection(connexionBean.getCurrentUser(), selectedDemande.getUtilisateurSend(), messageCorrection);

            msg = bundle.getString("messageSendSuccessMsg");

            JsfUtil.addSuccessMessage(msg);
            this.listeDemandes.remove(this.selectedDemande);
            this.listeDemandes = this.demandeFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("DemandeAutoriseErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doCreateLigne(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            newLigneBudgetaire.setExercice(exerciceFacade.getCourrantExo());
            newLigneBudgetaire.setMontantRestant(newLigneBudgetaire.getMontant());
            ligneBudgetaireFacade.create(newLigneBudgetaire);
            msg = bundle.getString("LigneCreatSuccessMsg");

            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            System.out.println("Erreur " + e);
            msg = bundle.getString("LigneCreatErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void recupererCode() {
        try {
            String code = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
            System.out.println(" code ---" + code);
            if (code != null) {
                newBonDeCommande = new Boncommande();
                selectedDemande = demandeFacade.find(code);
                newBonDeCommande.setDemande(selectedDemande);
            }

        } catch (Exception e) {
            System.out.println("Erreur " + e);
        }

    }

    public List<MessageChat> findListNonLu() {
        return messageChatFacade.getMessageReceive(connexionBean.getCurrentUser());

    }

    public boolean alredyBcomm(Demande demande) {
        return demandeFacade.alredyBonCommande(demande);
    }

    public String converssion(long total) {
        String lettre = "";
        try {
            lettre = Nombre.CALCULATE.getValue(total, "FCFA");
        } catch (Exception ex) {
            Logger.getLogger(DemandeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lettre;
    }

    public void findListMaterielByCat() {

        listeMateriels = categorieFacade.findListMaterielForCat(selectedCategorie);

    }

    public void passTypeAjout(String type) {
        typeAjout = type;
        if (type.equals("Matériel")) {
            materielBean.setSelectedCategorie(selectedCategorie);
        }
    }

    public void verifierMateriel() {

        RequestContext.getCurrentInstance().execute("notif();");
    }

    public List<LigneBudgetaire> findListBudgetaireByExo() {
        return ligneBudgetaireFacade.findLigneByExo(selectedExercice);
    }

    public void passLigne(LigneBudgetaire ligne) {
        selectedLigneBudgetaire = ligne;
        listeResteBudgetaaire.clear();
        listeResteBudgetaaire.addAll(listeLigneBudgetaire);
        listeResteBudgetaaire.remove(selectedLigneBudgetaire);
        System.out.println("listeResteBudgetaaire-->" + listeResteBudgetaaire);
    }

    public void doCreateRealocation(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            long restant, nouvelleAlloué;
            restant = ligneGiverealoction.getMontantRestant() - montantRealloue;
            if (restant >= 0) {
                nouvelleAlloué = ligneGiverealoction.getMontant() - montantRealloue;
                ligneGiverealoction.setMontantRestant(restant);
                ligneGiverealoction.setMontant(nouvelleAlloué);
                ligneBudgetaireFacade.editMany(ligneGiverealoction);
                restant = selectedLigneBudgetaire.getMontantRestant() + montantRealloue;
                nouvelleAlloué = selectedLigneBudgetaire.getMontant() + montantRealloue;
                selectedLigneBudgetaire.setMontant(nouvelleAlloué);
                selectedLigneBudgetaire.setMontantRestant(restant);
                ligneBudgetaireFacade.editMany(selectedLigneBudgetaire);

                msg = bundle.getString("LigneReallouSuccessMsg");

                JsfUtil.addSuccessMessage(msg);
            } else {
                msg = bundle.getString("LigneReallouMoreErrorMsg");
                JsfUtil.addErrorMessage(msg);
            }
        } catch (Exception e) {
            System.out.println("Erreur " + e);
            msg = bundle.getString("LigneReallouErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

}

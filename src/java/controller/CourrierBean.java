/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.zxing.EncodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import ejb.ConnexionFacade;
import ejb.ContextFacade;
import ejb.CourrierFacade;
import ejb.CourrierSignataireFacade;
import ejb.Courrier_utilisateurFacade;
import ejb.DocumentFacade;
import ejb.EvenementFacade;
import ejb.ExerciceFacade;
import ejb.GedFacade2;
import ejb.NotificationFacade;
import ejb.ProgressionCourrierFacade;
import ejb.TypeCourrierFacade;
import ejb.TypeDocumentFacade;
import ejb.UtilisateurFacade;
import ejb.DepartementFacade;
import ejb.FonctionFacade;
import ged.DocumentCmis;
import ged.DossierCmis;
import ged.ejb.GedFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jpa.Courrier;
import util.JsfUtil;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Lob;
import javax.servlet.http.Part;
import jpa.CategorieCourrier;
import jpa.CourrierSignataire;
import jpa.Courrier_Utilisateur_visa;
import jpa.Departement;
import jpa.Division;
import jpa.Document;
import jpa.Evenement;
import jpa.Fonction;
import jpa.Ged;
import jpa.Notification;
import jpa.ProgressionCourrier;
import jpa.Service;
import jpa.TypeCourrier;
import jpa.TypeDocument;
import jpa.Utilisateur;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import pojo.EncrypteDecrypte;

import pojo.NotificationCourrierEndPoint;
import pojo.PDFMerger;
import pojo.Qrcode;

/**
 *
 * @author MJLDH
 */
@Named(value = "courrierBean")
@ViewScoped
public class CourrierBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

    @Inject
    private CourrierFacade courrierFacade;
    @Inject
    private FonctionFacade fonctionFacade;
    @Inject
    private UtilisateurFacade utilisateurFacade;
    @Inject
    private TypeCourrierFacade typeCourrierFacade;
    @Inject
    private EvenementFacade evenementFacade;
    @Inject
    private ConnexionBean connexionBean;
    @Inject
    private GedBean gedBean;
    @Inject
    private ProgressionCourrierFacade progressionCourrierFacade;
    @Inject
    private ExerciceFacade exerciceFacade;
    @Inject
    private NotificationFacade notificationFacade;
    @Inject
    private Courrier_utilisateurFacade courrier_utilisateurFacade;
    @Inject
    private CourrierSignataireFacade courrierSignataireFacade;
    private Courrier selectedCourrier;
    private ProgressionCourrier selectedProgressionCourrier;
    private Courrier newCourrier;
    private List<Courrier> listeCourriers;
    @Lob
    private String contenu;
    private GedFacade gedFacade;
    @Inject
    ContextFacade contextFacade;
    @Inject
    DocumentFacade documentFacade;
    @Inject
    private GedFacade2 gedFacade2;
    @Inject
    private DepartementFacade departementFacade;
    private Fonction fonctionSelect;
    private List<Fonction> listSignataire;
    private List<TypeCourrier> listTypeCourrier;
    private CategorieCourrier categorieCourrierSelected;
    private Date today;
    private List<ProgressionCourrier> allcourrierInit;
    private boolean listPageEnvoiActive;
    private boolean listDocActive;
    private ProgressionCourrier lastPgCourrierForCour;
    private List<String> listDestinataire;
    private String destinataire;
    private List<Courrier_Utilisateur_visa> listCourrier_ChefDiv;
    private List<Courrier_Utilisateur_visa> listCourrier_ChefSer;
    private List<Courrier_Utilisateur_visa> listCourrier_Dir;

    private String noteForCopie;
    private boolean typeCopieInfo;
    private Utilisateur userSelectForCopie;
    private boolean typeCopieTraitement;
    private boolean withNote;
    private Notification notifiationSelected;
    private Utilisateur signataireSelected;
    private String cheminSuivi;
    @Inject
    private ConnexionFacade connexionFacade;
    private List<TypeDocument> listTypeDoc;
    private Part fileAdd;
    private TypeDocument selectedTypeDocument;
    private Document SelectedDocument;
    private String description, observation;
    @Inject
    private TypeDocumentFacade typeDocumentFacade;
    private List<ProgressionCourrier> listCourrierSelected;
    @ManagedProperty("#{param.PgCr}")
    String PgCr;
    private String idDBListSelect;
    private Departement selectedDirection;
    private TypeCourrier selectedTypeCourrier;
    private List<Courrier> listCourrierFindForRecherhe;
    private List<Document> listDocInCour;
    private String codeCourriSelect;
    private String passwordQRcode;
    private List<InputStream> listFilAdd;
    private List<Part> listPartSaved;
    private String filePath;
    private boolean showFileScan;
    private boolean showAllPJ;
    private boolean typeRechercheByDate;
    private boolean showRelierTrb;
    private Date date1;
    private Date date2;
    private String nextNumCourrierArrive;
    private String instructions;
    private boolean instructionChecked;
    private List<Departement> departementConcernes;
    private List<Service> servicesConcernes;
    private List<Division> divisionsConcernes;
    private Courrier courrierInstigateur;
    private List<Departement> listDepToSD;
    private boolean sendWithDelai;
    private Date dateLimite;
    private boolean delaiPasse;

    /**
     * Creates a new instance of CourrierBean
     */
    public CourrierBean() {

    }

    @PostConstruct
    public void init() {
        System.out.println("le PgCr est " + PgCr);
        cheminSuivi = contextFacade.getALFRESCO_REPOSITORY_IGECOUR();
        listeCourriers = courrierFacade.findAll();
        listDepToSD = new ArrayList<>();;
        if (listeCourriers.size() != 0) {
            System.out.println("---->>>>" + listeCourriers.get(0).getCode());
        }
        if (!connexionBean.getCurrentUser().isSecretaireDeDirection() && (connexionBean.getCurrentUser().getFonction().getCode().equals("SA") || connexionBean.getCurrentUser().getFonction().getCode().equals("SC"))) {
            nextNumCourrierArrive = courrierFacade.findNextNumCourArriCourrier(exerciceFacade.getCourrantExo());
        }

        if (connexionBean.getCurrentUser().isSecretaireDeDirection()) {
            listDepToSD = findListDepBySD();
        }

        listSignataire = new ArrayList<>();
        listTypeCourrier = new ArrayList<>();
        today = new Date();
        allcourrierInit = progressionCourrierFacade.getListAllProgressionFromBoitEnvoi(connexionBean.getCurrentUser());
        listPageEnvoiActive = true;
        listDocActive = false;
        listDestinataire = new ArrayList<>();
        listCourrier_ChefDiv = new ArrayList<>();
        listCourrier_ChefSer = new ArrayList<>();
        listCourrier_Dir = new ArrayList<>();
        servicesConcernes = new ArrayList<>();
        listTypeDoc = typeDocumentFacade.getAllTypeByApp(connexionBean.getCurrentAppli());
        listCourrierSelected = new ArrayList<>();
        receiveParam();
        receiveCodeQr();
        receiveCodeCourrierInstigateur();
        departementConcernes = new ArrayList<>();
        listCourrierFindForRecherhe = new ArrayList<>();
        listDocInCour = new ArrayList<>();
        listFilAdd = new ArrayList<>();
        listPartSaved = new ArrayList<>();
        divisionsConcernes = new ArrayList<>();
        filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/tamponFileConcaten");
        showFileScan = false;
        showAllPJ = false;
        typeRechercheByDate = false;
        showRelierTrb = false;
        instructionChecked = false;
        sendWithDelai = false;
        delaiPasse = false;
    }

    public boolean isDelaiPasse() {
        return delaiPasse;
    }

    public void setDelaiPasse(boolean delaiPasse) {
        this.delaiPasse = delaiPasse;
    }

    public Date getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(Date dateLimite) {
        this.dateLimite = dateLimite;
    }

    public boolean isSendWithDelai() {
        return sendWithDelai;
    }

    public void setSendWithDelai(boolean sendWithDelai) {
        this.sendWithDelai = sendWithDelai;
    }

    public List<Departement> getListDepToSD() {
        return listDepToSD;
    }

    public void setListDepToSD(List<Departement> listDepToSD) {
        this.listDepToSD = listDepToSD;
    }

    public List<Departement> findListDepBySD() {
        return departementFacade.getAllDepBySD(connexionBean.getCurrentUser());
    }

    public Courrier getCourrierInstigateur() {
        return courrierInstigateur;
    }

    public void setCourrierInstigateur(Courrier courrierInstigateur) {
        this.courrierInstigateur = courrierInstigateur;
    }

    public List<Division> getDivisionsConcernes() {
        return divisionsConcernes;
    }

    public void setDivisionsConcernes(List<Division> divisionsConcernes) {
        this.divisionsConcernes = divisionsConcernes;
    }

    public List<Service> getServicesConcernes() {
        return servicesConcernes;
    }

    public void setServicesConcernes(List<Service> servicesConcernes) {
        this.servicesConcernes = servicesConcernes;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public List<Departement> getDepartementConcernes() {
        return departementConcernes;
    }

    public void setDepartementConcernes(List<Departement> departementConcernes) {
        this.departementConcernes = departementConcernes;
    }

    public boolean isInstructionChecked() {
        return instructionChecked;
    }

    public void setInstructionChecked(boolean instructionChecked) {
        this.instructionChecked = instructionChecked;
    }

    public String getNextNumCourrierArrive() {
        return nextNumCourrierArrive;
    }

    public void setNextNumCourrierArrive(String nextNumCourrierArrive) {
        this.nextNumCourrierArrive = nextNumCourrierArrive;
    }

    public boolean isShowRelierTrb() {
        return showRelierTrb;
    }

    public void setShowRelierTrb(boolean showRelierTrb) {
        this.showRelierTrb = showRelierTrb;
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

    public boolean isTypeRechercheByDate() {
        return typeRechercheByDate;
    }

    public void setTypeRechercheByDate(boolean typeRechercheByDate) {
        this.typeRechercheByDate = typeRechercheByDate;
    }

    public boolean isShowFileScan() {
        return showFileScan;
    }

    public void setShowFileScan(boolean showFileScan) {
        this.showFileScan = showFileScan;
    }

    public boolean isShowAllPJ() {
        return showAllPJ;
    }

    public void setShowAllPJ(boolean showAllPJ) {
        this.showAllPJ = showAllPJ;
    }

    public void showPJ() {
        showAllPJ = true;
    }

    public List<InputStream> getListFilAdd() {
        return listFilAdd;
    }

    public void setListFilAdd(List<InputStream> listFilAdd) {
        this.listFilAdd = listFilAdd;
    }

    public List<Part> getListPartSaved() {
        return listPartSaved;
    }

    public void setListPartSaved(List<Part> listPartSaved) {
        this.listPartSaved = listPartSaved;
    }

    public String getPasswordQRcode() {
        return passwordQRcode;
    }

    public void setPasswordQRcode(String passwordQRcode) {
        this.passwordQRcode = passwordQRcode;
    }

    public String getCodeCourriSelect() {
        return codeCourriSelect;
    }

    public void setCodeCourriSelect(String codeCourriSelect) {
        this.codeCourriSelect = codeCourriSelect;
    }

    public List<Document> getListDocInCour() {
        return listDocInCour;
    }

    public void setListDocInCour(List<Document> listDocInCour) {
        this.listDocInCour = listDocInCour;
    }

    public TypeCourrier getSelectedTypeCourrier() {
        return selectedTypeCourrier;
    }

    public void setSelectedTypeCourrier(TypeCourrier selectedTypeCourrier) {
        this.selectedTypeCourrier = selectedTypeCourrier;
    }

    public Departement getSelectedDirection() {
        return selectedDirection;
    }

    public void setSelectedDirection(Departement selectedDirection) {
        this.selectedDirection = selectedDirection;
    }

    public String getIdDBListSelect() {
        return idDBListSelect;
    }

    public void setIdDBListSelect(String idDBListSelect) {
        this.idDBListSelect = idDBListSelect;
    }

    public String getPgCr() {
        return PgCr;
    }

    public void setPgCr(String PgCr) {
        this.PgCr = PgCr;
    }

    public boolean isListDocActive() {
        return listDocActive;
    }

    public void setListDocActive(boolean listDocActive) {
        this.listDocActive = listDocActive;
    }

    public Notification getNotifiationSelected() {
        return notifiationSelected;
    }

    public void setNotifiationSelected(Notification notifiationSelected) {
        this.notifiationSelected = notifiationSelected;
    }

    public boolean isWithNote() {
        return withNote;
    }

    public void setWithNote(boolean withNote) {
        this.withNote = withNote;
    }

    public List<TypeDocument> getListTypeDoc() {
        return listTypeDoc;
    }

    public void setListTypeDoc(List<TypeDocument> listTypeDoc) {
        this.listTypeDoc = listTypeDoc;
    }

    public Part getFileAdd() {
        return fileAdd;
    }

    public void setFileAdd(Part fileAdd) {
        this.fileAdd = fileAdd;
    }

    public TypeDocument getSelectedTypeDocument() {
        return selectedTypeDocument;
    }

    public void setSelectedTypeDocument(TypeDocument selectedTypeDocument) {
        this.selectedTypeDocument = selectedTypeDocument;
    }

    public Document getSelectedDocument() {
        return SelectedDocument;
    }

    public void setSelectedDocument(Document SelectedDocument) {
        this.SelectedDocument = SelectedDocument;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Utilisateur getSignataireSelected() {
        return signataireSelected;
    }

    public void setSignataireSelected(Utilisateur signataireSelected) {
        this.signataireSelected = signataireSelected;
    }

    public ProgressionCourrier getLastPgCourrierForCour() {
        if (lastPgCourrierForCour == null) {
            lastPgCourrierForCour = new ProgressionCourrier();
        }
        return lastPgCourrierForCour;
    }

    public boolean isTypeCopieTraitement() {
        return typeCopieTraitement;
    }

    public void setTypeCopieTraitement(boolean typeCopieTraitement) {
        this.typeCopieTraitement = typeCopieTraitement;
    }

    public void setLastPgCourrierForCour(ProgressionCourrier lastPgCourrierForCour) {
        this.lastPgCourrierForCour = lastPgCourrierForCour;
    }

    public ProgressionCourrier getSelectedProgressionCourrier() {
        return selectedProgressionCourrier;
    }

    public void setSelectedProgressionCourrier(ProgressionCourrier selectedProgressionCourrier) {
        this.selectedProgressionCourrier = selectedProgressionCourrier;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getContenu() {
        return contenu;
    }

    public boolean isListPageEnvoiActive() {
        return listPageEnvoiActive;
    }

    public void setListPageEnvoiActive(boolean listPageEnvoiActive) {
        this.listPageEnvoiActive = listPageEnvoiActive;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Courrier getSelectedCourrier() {
        return selectedCourrier;
    }

    public void setSelectedCourrier(Courrier selectedCourrier) {
        this.selectedCourrier = selectedCourrier;
    }

    public List<Courrier> getListeCourriers() {
        return listeCourriers;
    }

    public void setListeCourriers(List<Courrier> listeCourriers) {
        this.listeCourriers = listeCourriers;
    }

    public Courrier getNewCourrier() {
        if (newCourrier == null) {
            newCourrier = new Courrier();
        }
        return newCourrier;
    }

    public void setNewCourrier(Courrier newCourrier) {
        this.newCourrier = newCourrier;
    }

    public void passItem(ProgressionCourrier pc) {
        this.selectedCourrier = pc.getCourrier();
        selectedProgressionCourrier = pc;
        listCourrier_ChefDiv = new ArrayList<>();
        listCourrier_ChefSer = new ArrayList<>();
        listCourrier_Dir = new ArrayList<>();
        if (pc.getCourrier().getTypeCourrier().getCategorieCourrier() != CategorieCourrier.CourrierArrive) {
            findAllCourUserFromDataBase();
        }
        System.out.println("le courrier " + selectedCourrier.getCode());
    }

    public void reNullCourrier() {
        selectedCourrier = null;
    }

    public void passItemForSend(ProgressionCourrier pc, Utilisateur userSA) {
        this.selectedCourrier = pc.getCourrier();
        signataireSelected = userSA;
    }

    public List<Fonction> getListSignataire() {
        return listSignataire;
    }

    public void setListSignataire(List<Fonction> listSignataire) {
        this.listSignataire = listSignataire;
    }

    public Fonction getFonctionSelect() {

        return fonctionSelect;
    }

    public void setFonctionSelect(Fonction fonctionSelect) {
        this.fonctionSelect = fonctionSelect;
    }

    public List<TypeCourrier> getListTypeCourrier() {
        return listTypeCourrier;
    }

    public void setListTypeCourrier(List<TypeCourrier> listTypeCourrier) {
        this.listTypeCourrier = listTypeCourrier;
    }

    public CategorieCourrier getCategorieCourrierSelected() {
        return categorieCourrierSelected;
    }

    public String getNoteForCopie() {
        return noteForCopie;
    }

    public void setNoteForCopie(String noteForCopie) {
        this.noteForCopie = noteForCopie;
    }

    public boolean isTypeCopieInfo() {
        return typeCopieInfo;
    }

    public void setTypeCopieInfo(boolean typeCopieInfo) {
        this.typeCopieInfo = typeCopieInfo;
    }

    public void setCategorieCourrierSelected(CategorieCourrier categorieCourrierSelected) {
        this.categorieCourrierSelected = categorieCourrierSelected;
    }

    public List<ProgressionCourrier> getAllcourrierInit() {
        return allcourrierInit;
    }

    public void setAllcourrierInit(List<ProgressionCourrier> allcourrierInit) {
        this.allcourrierInit = allcourrierInit;
    }

    public List<String> getListDestinataire() {
        return listDestinataire;
    }

    public void setListDestinataire(List<String> listDestinataire) {
        this.listDestinataire = listDestinataire;
    }

    public Utilisateur getUserSelectForCopie() {
        return userSelectForCopie;
    }

    public void setUserSelectForCopie(Utilisateur userSelectForCopie) {
        this.userSelectForCopie = userSelectForCopie;
    }

    public List<ProgressionCourrier> getListCourrierSelected() {
        return listCourrierSelected;
    }

    public void setListCourrierSelected(List<ProgressionCourrier> listCourrierSelected) {
        this.listCourrierSelected = listCourrierSelected;
    }

    public List<Courrier> getListCourrierFindForRecherhe() {
        return listCourrierFindForRecherhe;
    }

    public void setListCourrierFindForRecherhe(List<Courrier> listCourrierFindForRecherhe) {
        this.listCourrierFindForRecherhe = listCourrierFindForRecherhe;
    }

    public String getCountryParam(FacesContext fc) {

        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();
        return params.get("contenu");

    }

    public List<Utilisateur> findListUserVisaDefault() {
        List<Utilisateur> list = new ArrayList<>();
        Utilisateur userConnect = connexionBean.getCurrentUser();
        if (userConnect.getFonction().isChefDivision()) {
            list.add(userConnect);
            list.add(connexionBean.getUserChefService());
            list.add(connexionBean.getUserDirecteur());
        }
        if (userConnect.getFonction().isChefService()) {
            list.add(userConnect);
            list.add(connexionBean.getUserDirecteur());
        }
        return list;
    }

    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
//            if (!connexionBean.getCurrentUser().isChefService()) {
            List<Utilisateur> listSigna = new ArrayList<>();
            ProgressionCourrier newProgression;
            System.out.println(" le contenu est " + newCourrier.getContenu());

            newCourrier.setDateCreation(new Date());
            newCourrier.setInitiateur(connexionBean.getCurrentUser());
            if (newCourrier.isWithTitre()) {
                newCourrier.setTitre(newCourrier.getTypeCourrier().getLibelle());
            }
            if (newCourrier.isWithObjet()) {
                newCourrier.setDestinataires(listDestinataire);
            }
            for (Fonction fonction : listSignataire) {
                listSigna.add(utilisateurFacade.getUserByFonction(fonction));
            }
            newCourrier.setExercice(exerciceFacade.getCourrantExo());
            if (courrierInstigateur != null) {
                newCourrier.setCourrierInstiguateur(courrierInstigateur);
            }
            String code = this.courrierFacade.createAndGidCode(newCourrier);
            Courrier courrierTampon = courrierFacade.find(code);
            courrierTampon.setGed(creerDossier(courrierTampon));
            CourrierSignataire courrierSignataire = new CourrierSignataire();
            List<CourrierSignataire> courSina = new ArrayList<>();
            for (Utilisateur user : listSigna) {
                courrierSignataire.setCourrier(courrierTampon);
                courrierSignataire.setUtilisateur(user);
                courrierSignataire.setDateCreation(today);
                courrierSignataire.setSigner(false);
                courrierSignataireFacade.create(courrierSignataire);
                courSina.add(courrierSignataire);
            }
            courrierTampon.setCourrierSignataires(courSina);
            courrierFacade.edit(courrierTampon);
            Evenement even = evenementFacade.find("Cou001");
            newProgression = new ProgressionCourrier();
            newProgression.setCourrier(courrierTampon);
            newProgression.setDateCreation(new Date());
            newProgression.setEvenement(even);
            newProgression.setStatut("Nouveau Courrier");
            newProgression.setUserSend(connexionBean.getCurrentUser());
            newProgression.setUserReceive(connexionBean.getCurrentUser());
            newProgression.setReference(findReference());
            progressionCourrierFacade.create(newProgression);
            for (Utilisateur user : findListUserVisaDefault()) {
                Courrier_Utilisateur_visa newCour_User = new Courrier_Utilisateur_visa();
                newCour_User.setCourrier(courrierTampon);
                newCour_User.setCourrierCode(code);
                newCour_User.setDateCreation(today);
                newCour_User.setUserVisa(null);
                newCour_User.setUserVisa(user);
                newCour_User.setUtilisateurLogin(user.getLogin());
                newCour_User.setViser(false);
                courrier_utilisateurFacade.create(newCour_User);
            }
            msg = bundle.getString("CourrierCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newCourrier = new Courrier();
            this.listeCourriers = this.courrierFacade.findAll();
//            } else {
//                msg = bundle.getString("CourrierCreateNotAutoErrorMsg");
//                JsfUtil.addWarningMessage(msg);
//            }
        } catch (Exception e) {
            System.out.println("e-->" + e);
            msg = bundle.getString("CourrierCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doCreateCourrierArrive(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
//            if (!connexionBean.getCurrentUser().isChefService()) {

            ProgressionCourrier newProgression;

            newCourrier.setDateCreation(new Date());
            newCourrier.setExercice(exerciceFacade.getCourrantExo());
            newCourrier.setNumCourrierArrive(nextNumCourrierArrive);
            String code = this.courrierFacade.createAndGidCode(newCourrier);
            Courrier courrierTampon = courrierFacade.find(code);
            courrierTampon.setGed(creerDossier(courrierTampon));
            courrierFacade.edit(courrierTampon);
            Evenement even;
            if (!connexionBean.getCurrentUser().getFonction().getCode().equals("SC")) {
                even = evenementFacade.find("Cou0014");
                newProgression = new ProgressionCourrier();
                newProgression.setCourrier(courrierTampon);
                newProgression.setDateCreation(new Date());
                newProgression.setEvenement(even);
                newProgression.setStatut("Nouveau Courrier");
                newProgression.setUserSend(connexionBean.getCurrentUser());
                newProgression.setUserReceive(connexionBean.getCurrentUser());
            } else {
                even = evenementFacade.find("Cou0020");
                newProgression = new ProgressionCourrier();
                newProgression.setCourrier(courrierTampon);
                newProgression.setDateCreation(new Date());
                newProgression.setEvenement(even);
                newProgression.setStatut("Nouveau Courrier");
                newProgression.setUserSend(connexionBean.getCurrentUser());
                newProgression.setUserReceive(findUserByonction(fonctionFacade.find("SA")));
            }

            progressionCourrierFacade.create(newProgression);

            File fichier = new File(filePath + "\\" + fileAdd.getSubmittedFileName());
            selectedTypeDocument = typeDocumentFacade.find("210120179");
//             creation du cdocument dans la ged
            gedFacade = connexionFacade.initGed(connexionBean.getCurrentUser());
            System.out.println("lid du dossier --" + courrierTampon.getCode());
            // documentFacade.create(listDocument, listFile, dossierFacade.find(iddossier), selectedParties, audienceFacade.find(idaudience), "DOS-003", "PIECE");
            controlSizeFileWithFile(fichier);

//            documentFacade.createDocument(listDocumentToPersit, listFile, selectedFolderByGet, selectedParties, "DIC-022", "PIECE");
            Document newDocumentToPersit = new Document();
            newDocumentToPersit.setFileName(fichier.getName());
            newDocumentToPersit.setMimeType(fileAdd.getContentType());
            newDocumentToPersit.setTaille("" + fichier.length());
            newDocumentToPersit.setSens(true);
            newDocumentToPersit.setTypeDocument(selectedTypeDocument);
            newDocumentToPersit.setCourrier(courrierTampon);
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
            System.out.println("selectedDossier.getGed().getRefGed(): ");
            System.out.println("name: " + name);
            System.out.println("part.getInputStream(): ");
            System.out.println("document.getMimeType(): " + newDocumentToPersit.getMimeType());

            System.out.println("----------------------------------");
            InputStream inputStreamFichier = new FileInputStream(fichier);
            documentCmis = gedFacade.creerDocument(courrierTampon.getGed().getRefGed(), fichier.getName(), inputStreamFichier,
                    newDocumentToPersit.getMimeType(), "nouveau fichier");
            ged = new Ged(documentCmis.getId(), name, documentCmis.getId());
            gedFacade2.create(ged);

            Document docToPersit = new Document();
//            String localName = newDocumentToPersit.getFileName() + "." + JsfUtil.convertDate(new Date(), "EEEEE dd MMM yyyy 'A' HH:mm:ss");
            System.out.println("le fichier " + fichier.getName());
            docToPersit.setFileName(fichier.getName());
            docToPersit.setGed(ged);
            docToPersit.setMimeType(newDocumentToPersit.getMimeType());
            docToPersit.setSens(true);
            docToPersit.setSubmittedFileName(name);
            docToPersit.setTaille(newDocumentToPersit.getTaille());
            docToPersit.setTypeDocument(newDocumentToPersit.getTypeDocument());
            docToPersit.setCourrier(courrierTampon);

            docToPersit.setDescription("courrier");
            documentFacade.create(docToPersit);

            fichier.delete();

            newCourrier = new Courrier();
            this.listeCourriers = this.courrierFacade.findAll();
            msg = bundle.getString("CourrierCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
//            } else {
//                msg = bundle.getString("CourrierCreateNotAutoErrorMsg");
//                JsfUtil.addWarningMessage(msg);
//            }
        } catch (Exception e) {
            System.out.println("e-->" + e);
            msg = bundle.getString("CourrierCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doInsertion(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            OutputStream outputStream = new FileOutputStream(filePath + "\\CourrierSigne.pdf");
            PDFMerger.mergePdfFiles(listFilAdd, outputStream);
            File fichier = new File(filePath + "\\CourrierSigne.pdf");
            CourrierSignataire cour_Sig = courrierSignataireFacade.findWithTwoObjectId(connexionBean.getUserDG(), selectedProgressionCourrier.getCourrier());
            cour_Sig.setSigner(true);
            selectedTypeDocument = typeDocumentFacade.find("210120179");
//             creation du cdocument dans la ged
            gedFacade = connexionFacade.initGed(connexionBean.getCurrentUser());
            System.out.println("lid du dossier --" + selectedProgressionCourrier.getCourrier().getCode());
            // documentFacade.create(listDocument, listFile, dossierFacade.find(iddossier), selectedParties, audienceFacade.find(idaudience), "DOS-003", "PIECE");
            controlSizeFileWithFile(fichier);

//            documentFacade.createDocument(listDocumentToPersit, listFile, selectedFolderByGet, selectedParties, "DIC-022", "PIECE");
            Document newDocumentToPersit = new Document();
            newDocumentToPersit.setFileName(fichier.getName());
            newDocumentToPersit.setMimeType(listPartSaved.get(0).getContentType());
            newDocumentToPersit.setTaille("" + fichier.length());
            newDocumentToPersit.setSens(true);
            newDocumentToPersit.setTypeDocument(selectedTypeDocument);
            newDocumentToPersit.setCourrier(selectedProgressionCourrier.getCourrier());
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
            System.out.println("selectedDossier.getGed().getRefGed(): " + selectedProgressionCourrier.getCourrier().getGed().getRefGed());
            System.out.println("name: " + name);
            System.out.println("part.getInputStream(): ");
            System.out.println("document.getMimeType(): " + newDocumentToPersit.getMimeType());

            System.out.println("----------------------------------");
            InputStream inputStreamFichier = new FileInputStream(fichier);
            documentCmis = gedFacade.creerDocument(selectedProgressionCourrier.getCourrier().getGed().getRefGed(), fichier.getName(), inputStreamFichier,
                    newDocumentToPersit.getMimeType(), "nouveau fichier");
            ged = new Ged(documentCmis.getId(), name, documentCmis.getId());
            gedFacade2.create(ged);

            Document docToPersit = new Document();
//            String localName = newDocumentToPersit.getFileName() + "." + JsfUtil.convertDate(new Date(), "EEEEE dd MMM yyyy 'A' HH:mm:ss");
            System.out.println("le fichier " + fichier.getName());
            docToPersit.setFileName(fichier.getName());
            docToPersit.setGed(ged);
            docToPersit.setMimeType(newDocumentToPersit.getMimeType());
            docToPersit.setSens(true);
            docToPersit.setSubmittedFileName(name);
            docToPersit.setTaille(newDocumentToPersit.getTaille());
            docToPersit.setTypeDocument(newDocumentToPersit.getTypeDocument());
            docToPersit.setCourrier(selectedProgressionCourrier.getCourrier());

            docToPersit.setDescription("courrier");
            documentFacade.create(docToPersit);

            fichier.delete();

            msg = bundle.getString("InsertionCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            System.out.println("e-->" + e);
            msg = bundle.getString("CourrierCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public Ged creerDossier(Courrier courrier) {
        gedFacade = connexionFacade.initGed(connexionBean.getCurrentUser());
        Ged ged;

//        String name = entity.getNumeroRP().replaceAll("/", "-");
        String name = courrier.getCode();
        System.out.println("nom dossier issu du split pour etre name ds ged " + name);
        DossierCmis dossierCmis;

        System.out.println("description du dossier ");
        CmisObject obj;

//        CmisObject obj;
//
        System.out.println("contextBean.getALFRESCO_REPOSITORY() " + contextFacade.getALFRESCO_REPOSITORY_IGECOUR());
//        System.out.println("annee.getValeur() " + annee.getValeur());
        System.out.println("name" + name);

        obj = gedFacade.getCmisObjectByPath(cheminSuivi + "/" + courrier.getTypeCourrier().getCategorieCourrier().getLabel() + "/" + courrier.getTypeCourrier().getLibelle() + "/" + name);
        System.out.println(" le parent est alors  " + cheminSuivi + "/" + categorieCourrierSelected.getLabel() + "/" + name);
        if (obj == null) {
            dossierCmis = gedFacade.creerDossier(cheminSuivi + "/" + categorieCourrierSelected.getLabel() + "/" + courrier.getTypeCourrier().getLibelle(), name, "nouveau dossier" + name + " créé en ce jour le " + new Date());
        } else {
            dossierCmis = new DossierCmis((Folder) obj);
        }

        ged = new Ged(dossierCmis.getId(), name, dossierCmis.getId());
        gedFacade2.create(ged);

        return ged;
    }

    public void doEdit(ActionEvent event, ProgressionCourrier pgcour) {

        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;

        try {
            courrierFacade.editMany(pgcour.getCourrier());
            passCourrierView(pgcour);
            Evenement even = evenementFacade.find("Cou0013");
            ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(pgcour.getCourrier());
            lastProgresssioncourrier.setEvenement(even);
            lastProgresssioncourrier.setDateCreation(new Date());
            lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
            lastProgresssioncourrier.setUserReceive(lastProgresssioncourrier.getUserReceive());
            progressionCourrierFacade.create(lastProgresssioncourrier);
            msg = bundle.getString("CourrierEditSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("CourrierEditErrorMsg");
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
            courrierFacade.remove(selectedCourrier);
            msg = bundle.getString("CourrierDelSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeCourriers.remove(this.selectedCourrier);
            this.listeCourriers = this.courrierFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("CourrierDelErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void saveBool() {
        System.out.println("le bool est prit " + newCourrier.isWithTitre());
    }

    public void passFonction() {
        listSignataire.add(fonctionSelect);
        System.out.println("la liste est " + listSignataire);
    }

    public void delItem(Fonction signatSelect) {
        listSignataire.remove(signatSelect);
    }

    public Utilisateur findUserByonction(Fonction fonction) {
        return utilisateurFacade.getUserByFonction(fonction);
    }

    public void findListType() {
        listTypeCourrier = typeCourrierFacade.findListTypeCourrierByCate(categorieCourrierSelected);
        System.out.println("la liste de courrier est " + listTypeCourrier);
    }

    public String findInitiauxRecepEnvoi() {

        String nom = selectedProgressionCourrier.getCourrier().getInitiateur().getNom();
        String prenom = selectedProgressionCourrier.getCourrier().getInitiateur().getPrenom();
        System.out.println("les initiaux " + nom.charAt(0) + prenom.charAt(0) + "/");
        String initiaux = " " + nom.charAt(0) + prenom.charAt(0) + "/";
        return initiaux;

    }

    public String findInitiauxApercu() {
        String nom = connexionBean.getCurrentUser().getNom();
        String prenom = connexionBean.getCurrentUser().getPrenom();
        System.out.println("les initiaux " + nom.charAt(0) + prenom.charAt(0) + "/");
        String initiaux = " " + nom.charAt(0) + prenom.charAt(0) + "/";
        return initiaux;
    }

    public String findReference() {
        if (!connexionBean.getCurrentUser().isDirecteur() && connexionBean.getCurrentUser().isDg()) {
            String user = connexionBean.getCurrentUser().getFonction().getCode();
            String chef = connexionBean.getUserChefService().getFonction().getCode();
            return "N°______/ANAC/MIT/DAF/" + chef + "/" + user + "/SA";
        }
        return null;
    }

    public void findAllCourUserFromDataBase() {

        List<Courrier_Utilisateur_visa> lisTotal = courrier_utilisateurFacade.getAllCourrier_Utilisateur_visaByCourrier(selectedProgressionCourrier.getCourrier());

        for (Courrier_Utilisateur_visa courUser : lisTotal) {
            if (courUser.getUserVisa().getFonction().isChefDivision()) {
                listCourrier_ChefDiv.add(courUser);
            }
            if (courUser.getUserVisa().getFonction().isChefService()) {
                listCourrier_ChefSer.add(courUser);
            }
            if (courUser.getUserVisa().getFonction().isDirecteur()) {
                listCourrier_Dir.add(courUser);
            }
        }
        System.out.println("la liste est " + listCourrier_ChefDiv + " - " + listCourrier_ChefSer + " - " + listCourrier_Dir);
    }

    public List<Courrier_Utilisateur_visa> getListCourrier_ChefDiv() {
        return listCourrier_ChefDiv;
    }

    public void setListCourrier_ChefDiv(List<Courrier_Utilisateur_visa> listCourrier_ChefDiv) {
        this.listCourrier_ChefDiv = listCourrier_ChefDiv;
    }

    public List<Courrier_Utilisateur_visa> getListCourrier_ChefSer() {
        return listCourrier_ChefSer;
    }

    public void setListCourrier_ChefSer(List<Courrier_Utilisateur_visa> listCourrier_ChefSer) {
        this.listCourrier_ChefSer = listCourrier_ChefSer;
    }

    public List<Courrier_Utilisateur_visa> getListCourrier_Dir() {
        return listCourrier_Dir;
    }

    public void setListCourrier_Dir(List<Courrier_Utilisateur_visa> listCourrier_Dir) {
        this.listCourrier_Dir = listCourrier_Dir;
    }

    public void passCourrierView(ProgressionCourrier PgCourrier) {
        selectedProgressionCourrier = PgCourrier;
        listPageEnvoiActive = false;
        listDocActive = false;
        listCourrier_ChefDiv = new ArrayList<>();
        listCourrier_ChefSer = new ArrayList<>();
        listCourrier_Dir = new ArrayList<>();
        if (PgCourrier.getCourrier().getTypeCourrier().getCategorieCourrier() != CategorieCourrier.CourrierArrive) {
            findAllCourUserFromDataBase();
        }
        System.out.println("venu pour le view");
    }

    public String passCourrierViewFromDB(ProgressionCourrier PgCourrier) {
        selectedProgressionCourrier = PgCourrier;
        listPageEnvoiActive = false;
        listCourrier_ChefDiv = new ArrayList<>();
        listCourrier_ChefSer = new ArrayList<>();
        listCourrier_Dir = new ArrayList<>();
        findAllCourUserFromDataBase();
        if (idDBListSelect == "1") {
            return "/GestionCourrier/boiteEnvoi/index.xhtml?faces-redirect=true&PgCr=" + selectedProgressionCourrier.getCode();

        } else {
            return "/GestionCourrier/BoiteReception/index.xhtml?faces-redirect=true&PgCr=" + selectedProgressionCourrier.getCode();
        }
    }

    public void passCourrierViewDoc(ProgressionCourrier PgCourrier) {
        selectedProgressionCourrier = PgCourrier;
//        listPageEnvoiActive = false;
        listDocActive = true;
        System.out.println("venu pour le view");
    }

    public void passItemNotif(Notification item) {
        System.out.println("la demande est " + item);
        this.selectedCourrier = item.getCourrier();
        item.setStatut("See");
        notificationFacade.edit(item);
        if (item.getNote() == null) {
            withNote = false;
        } else {
            withNote = true;
        }
        notifiationSelected = item;
        listCourrier_ChefDiv = new ArrayList<>();
        listCourrier_ChefSer = new ArrayList<>();
        listCourrier_Dir = new ArrayList<>();
        List<Courrier_Utilisateur_visa> lisTotal = courrier_utilisateurFacade.getAllCourrier_Utilisateur_visaByCourrier(selectedCourrier);

        for (Courrier_Utilisateur_visa courUser : lisTotal) {
            if (courUser.getUserVisa().getFonction().isChefDivision()) {
                listCourrier_ChefDiv.add(courUser);
            }
            if (courUser.getUserVisa().getFonction().isChefService()) {
                listCourrier_ChefSer.add(courUser);
            }
            if (courUser.getUserVisa().getFonction().isDirecteur()) {
                listCourrier_Dir.add(courUser);
            }
        }

    }

    public void retourListCour() {
        System.out.println("venu pour le retour");
        listPageEnvoiActive = true;
        allcourrierInit = progressionCourrierFacade.getListAllProgressionFromBoitEnvoi(connexionBean.getCurrentUser());
    }

    public ProgressionCourrier findReferenceFromPgCour(Courrier courrier) {
        if (courrier != null) {
            System.out.println("le courrier non null" + courrier);
            lastPgCourrierForCour = progressionCourrierFacade.getLastProgressionCourrier(courrier);
        }
        return lastPgCourrierForCour;
    }

    public List<ProgressionCourrier> findListEvenCourrier(Courrier courrier) {
        return progressionCourrierFacade.getListAllProgressionFromCourrier(courrier);
    }

    public void doEnvoiChefService(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg = null;
        try {
            Evenement even = evenementFacade.find("Cou002");
            ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
            lastProgresssioncourrier.setEvenement(even);
            lastProgresssioncourrier.setDateCreation(today);
            lastProgresssioncourrier.setStatut("En cours de traitement");
            lastProgresssioncourrier.setUserReceive(connexionBean.getUserChefService());
            lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
            progressionCourrierFacade.create(lastProgresssioncourrier);
            notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), connexionBean.getUserChefService(), "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, null, connexionBean.getCurrentAppli());
            NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), connexionBean.getUserChefService(), "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today));
            msg = bundle.getString("CourrierSendChefServiceSuccesMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("CourrierSendChefServiceErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEnvoiDirecteur(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            Evenement even;
            if (connexionBean.getCurrentUser().isChefService()) {
                even = evenementFacade.find("Cou007");
                ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
                
                lastProgresssioncourrier.setEvenement(even);
                lastProgresssioncourrier.setDateCreation(today);
                lastProgresssioncourrier.setStatut("En cours de traitement");
                lastProgresssioncourrier.setUserReceive(connexionBean.getUserDirecteur());
                lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
                progressionCourrierFacade.create(lastProgresssioncourrier);
                if(lastProgresssioncourrier.getCourrier().getCourrierInstiguateur()!=null){
                doTerminerCourrierArrive(event,lastProgresssioncourrier.getCourrier().getCourrierInstiguateur());
                }
                notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), connexionBean.getUserDirecteur(), "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, null, connexionBean.getCurrentAppli());
                NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), connexionBean.getUserDirecteur(), "Un courrier vient de vous être envoyer par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser() + ")");
                msg = bundle.getString("CourrierSendDirecteurSuccesMsg");
                JsfUtil.addSuccessMessage(msg);
            }

            if (connexionBean.getCurrentUser().isDirecteur()) {
                even = evenementFacade.find("Cou008");
                ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
                lastProgresssioncourrier.setEvenement(even);
                lastProgresssioncourrier.setDateCreation(today);
                lastProgresssioncourrier.setStatut("En cours de traitement");
                if (signataireSelected.getFonction().getCode().equals("DG")) {
                    signataireSelected = utilisateurFacade.getUserByFonction(fonctionFacade.find("SP"));
                }
                lastProgresssioncourrier.setUserReceive(signataireSelected);
                lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
                progressionCourrierFacade.create(lastProgresssioncourrier);
                notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), signataireSelected, "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, null, connexionBean.getCurrentAppli());
                NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), signataireSelected, "Un courrier vient de vous être envoyer par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser() + ")");
            }
        } catch (Exception e) {
            msg = bundle.getString("CourrierSendDirecteurErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doEnvoiSecretaire(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            Evenement even;

            even = evenementFacade.find("Cou009");
            ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
            lastProgresssioncourrier.setEvenement(even);
            lastProgresssioncourrier.setDateCreation(today);
            lastProgresssioncourrier.setStatut("En cours de traitement");
            lastProgresssioncourrier.setUserReceive(signataireSelected);
            lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
            progressionCourrierFacade.create(lastProgresssioncourrier);
            notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), signataireSelected, "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, null, connexionBean.getCurrentAppli());
            NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), signataireSelected, "Un courrier  vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today));
            msg = bundle.getString("CourrierSendSAErrorMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            System.out.println("Ereur " + e);
            msg = bundle.getString("CourrierSendDirecteurErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doMiseEnCopie(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            Evenement even;

            if (typeCopieInfo) {
                even = evenementFacade.find("Cou004");
                ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
                lastProgresssioncourrier.setEvenement(even);
                lastProgresssioncourrier.setDateCreation(today);
                lastProgresssioncourrier.setStatut("En cours de traitement");
                lastProgresssioncourrier.setUserReceive(connexionBean.getCurrentUser());
                lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
                lastProgresssioncourrier.setObservation(noteForCopie);
                progressionCourrierFacade.create(lastProgresssioncourrier);
                Courrier_Utilisateur_visa courrier_utilisateur = new Courrier_Utilisateur_visa();
                courrier_utilisateur.setCourrier(courrier);
                courrier_utilisateur.setUserVisa(userSelectForCopie);
                courrier_utilisateur.setCourrierCode(courrier.getCode());
                courrier_utilisateur.setUtilisateurLogin(userSelectForCopie.getLogin());
                courrier_utilisateur.setDateCreation(today);
                courrier_utilisateur.setTypeCopieInfo(typeCopieInfo);
                courrier_utilisateurFacade.create(courrier_utilisateur);
            }
            if (typeCopieTraitement) {
                even = evenementFacade.find("Cou003");
                ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
                lastProgresssioncourrier.setEvenement(even);
                lastProgresssioncourrier.setDateCreation(today);
                lastProgresssioncourrier.setStatut("En cours de traitement");
                lastProgresssioncourrier.setUserReceive(userSelectForCopie);
                lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
                progressionCourrierFacade.create(lastProgresssioncourrier);
                Courrier_Utilisateur_visa courrier_utilisateur = new Courrier_Utilisateur_visa();
                courrier_utilisateur.setCourrier(courrier);
                courrier_utilisateur.setUserVisa(userSelectForCopie);
                courrier_utilisateur.setCourrierCode(courrier.getCode());
                courrier_utilisateur.setUtilisateurLogin(userSelectForCopie.getLogin());
                courrier_utilisateur.setDateCreation(today);
                courrier_utilisateur.setViser(false);
                courrier_utilisateurFacade.create(courrier_utilisateur);
                notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), userSelectForCopie, "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, noteForCopie, connexionBean.getCurrentAppli());
                NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), userSelectForCopie, "Un courrier  vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today));
            }

            msg = bundle.getString("CourrierSendCopieSuccesMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("CourrierSendCopieErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDemandeCorrectionCourrier(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            Evenement even;

            even = evenementFacade.find("Cou0012");
            ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
            lastProgresssioncourrier.setEvenement(even);
            lastProgresssioncourrier.setDateCreation(today);
            lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
            progressionCourrierFacade.create(lastProgresssioncourrier);

            notificationFacade.createCorrectionCourrier(connexionBean.getCurrentUser(), courrier.getInitiateur(), "Une demande de correction vous a été envoyée par le " + connexionBean.getCurrentUser().getFonction().getCode() + " par rapport à votre " + courrier.getTypeCourrier().getLibelle() + " du " + connexionBean.convertDateHeure(courrier.getDateCreation()), courrier, noteForCopie, connexionBean.getCurrentAppli());
            NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), courrier.getInitiateur(), "Une demande de correction vous a été envoyée par le " + connexionBean.getCurrentUser().getFonction().getCode() + " par rapport à votre " + courrier.getTypeCourrier().getLibelle() + " du " + connexionBean.convertDateHeure(courrier.getDateCreation()));

            msg = bundle.getString("CourrierSendDemCorrectionSuccesMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("CourrierSendCopieErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doDemCorrection(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            Evenement even;

            even = evenementFacade.find("Cou0012");
            ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
            lastProgresssioncourrier.setEvenement(even);
            lastProgresssioncourrier.setDateCreation(today);
            lastProgresssioncourrier.setStatut("En cours de traitement");
            lastProgresssioncourrier.setUserReceive(courrier.getInitiateur());
            lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
            progressionCourrierFacade.create(lastProgresssioncourrier);
            Courrier_Utilisateur_visa courrier_utilisateur = new Courrier_Utilisateur_visa();
            courrier_utilisateur.setCourrier(courrier);
            courrier_utilisateur.setUserVisa(userSelectForCopie);
            courrier_utilisateur.setCourrierCode(courrier.getCode());
            courrier_utilisateur.setUtilisateurLogin(userSelectForCopie.getLogin());
            courrier_utilisateur.setDateCreation(today);
            courrier_utilisateur.setViser(false);
            courrier_utilisateurFacade.create(courrier_utilisateur);
            notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), userSelectForCopie, "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, noteForCopie, connexionBean.getCurrentAppli());
            NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), userSelectForCopie, "Un courrier  vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today));

            msg = bundle.getString("CourrierSendCopieSuccesMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("CourrierSendCopieErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public List<ProgressionCourrier> findReception(Utilisateur user) {
        if (user.getFonction().getCode().equals("SA")) {
            return findListAttenteArchive();
        } else {
            return progressionCourrierFacade.getListAllProgressionFromReception(user);
        }
    }

    public void ajoutDesti() {
        if (destinataire != null) {
            listDestinataire.add(destinataire);
        }
        destinataire = null;
    }

    public void addFile() {
        try {
            System.out.println("Alleluia");
            listPartSaved.add(fileAdd);
            listFilAdd.add(fileAdd.getInputStream());
            System.out.println("la liste est " + listPartSaved);
        } catch (IOException ex) {
            System.out.println("Erreur " + ex);
            Logger.getLogger(CourrierBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeFile(Part file) {
        listPartSaved.remove(file);
        try {
            listFilAdd.remove(file.getInputStream());
            if (listPartSaved.size() > 1) {
                showRelierTrb = true;
            } else {
                showRelierTrb = false;
            }
        } catch (IOException ex) {
            Logger.getLogger(CourrierBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void removeDesti(String desti) {
        listDestinataire.remove(desti);
    }

    public void visaUser() {
        System.out.println("entré");
        try {
            Courrier_Utilisateur_visa cour_user = courrier_utilisateurFacade.findWithTwoObjectId(connexionBean.getCurrentUser(), selectedProgressionCourrier.getCourrier());
            cour_user.setViser(true);
            courrier_utilisateurFacade.edit(cour_user);
            listPageEnvoiActive = false;
            listCourrier_ChefDiv = new ArrayList<>();
            listCourrier_ChefSer = new ArrayList<>();
            listCourrier_Dir = new ArrayList<>();
            findAllCourUserFromDataBase();
            System.out.println("venu " + cour_user.isViser());
        } catch (Exception e) {
            System.out.println(" Erreur est " + e);
        }
    }

    public void signaUser() {
        System.out.println("entré");
        try {
            CourrierSignataire cour_Sig = courrierSignataireFacade.findWithTwoObjectId(connexionBean.getCurrentUser(), selectedProgressionCourrier.getCourrier());
            cour_Sig.setSigner(true);
            courrierSignataireFacade.edit(cour_Sig);
            listPageEnvoiActive = false;
            listCourrier_ChefDiv = new ArrayList<>();
            listCourrier_ChefSer = new ArrayList<>();
            listCourrier_Dir = new ArrayList<>();
            findAllCourUserFromDataBase();

        } catch (Exception e) {
            System.out.println(" Erreur est " + e);
        }
    }

    public void delsignaUser() {
        System.out.println("entré");
        try {
            CourrierSignataire cour_Sig = courrierSignataireFacade.findWithTwoObjectId(connexionBean.getCurrentUser(), selectedProgressionCourrier.getCourrier());
            cour_Sig.setSigner(false);
            courrierSignataireFacade.edit(cour_Sig);
            listPageEnvoiActive = false;
            listCourrier_ChefDiv = new ArrayList<>();
            listCourrier_ChefSer = new ArrayList<>();
            listCourrier_Dir = new ArrayList<>();
            findAllCourUserFromDataBase();

        } catch (Exception e) {
            System.out.println(" Erreur est " + e);
        }
    }

    public Courrier_Utilisateur_visa findIfAlredyVisa() {
        if (!connexionBean.getCurrentUser().isDg()) {
            return courrier_utilisateurFacade.findWithTwoObjectId(connexionBean.getCurrentUser(), selectedProgressionCourrier.getCourrier());
        } else {
            return null;
        }
    }

    public CourrierSignataire findIfAlredySign() {
        if (connexionBean.getCurrentUser().isDg() || connexionBean.getCurrentUser().isDirecteur()) {
            return courrierSignataireFacade.findWithTwoObjectId(connexionBean.getCurrentUser(), selectedProgressionCourrier.getCourrier());
        } else {
            return null;
        }
    }

    public void delVisaUser() {
        System.out.println("entré");
        try {
            Courrier_Utilisateur_visa cour_user = courrier_utilisateurFacade.findWithTwoObjectId(connexionBean.getCurrentUser(), selectedProgressionCourrier.getCourrier());
            cour_user.setViser(false);
            courrier_utilisateurFacade.edit(cour_user);
            listPageEnvoiActive = false;
            listCourrier_ChefDiv = new ArrayList<>();
            listCourrier_ChefSer = new ArrayList<>();
            listCourrier_Dir = new ArrayList<>();
            findAllCourUserFromDataBase();
            System.out.println("venu " + cour_user.isViser());
        } catch (Exception e) {
            System.out.println(" Erreur est " + e);
        }
    }

    public void saveNum() {
        System.out.println("entré");
        try {
            String num = findNumCourr(selectedProgressionCourrier);
            selectedProgressionCourrier.getCourrier().setNumCour(num);
            courrierFacade.edit(selectedProgressionCourrier.getCourrier());
            selectedProgressionCourrier.getCourrier().getInitiateur().getFonction().getDepartementDirec().setActualLastNumCourr(num);
            departementFacade.edit(selectedProgressionCourrier.getCourrier().getInitiateur().getFonction().getDepartementDirec());
            System.out.println("le selectedProgressionCourrier est " + selectedProgressionCourrier);
            if (selectedProgressionCourrier.getCourrier().getTypeSignature().equals("Numerique")) {
                affectationQRcode(selectedProgressionCourrier);
            }
            System.out.println("venu ");
        } catch (Exception e) {
            System.out.println(" Erreur est " + e);
        }
    }

    public void affectationQRcode(ProgressionCourrier pg) throws WriterException, IOException,
            NotFoundException {

        System.out.println("ProgressionCourrier est 2 " + pg.getCourrier());
        String qrCodeData = "https://192.168.15.1:8181/AnacAppStore/GestionCourrier/AuthentificationQRcode/page.xhtml?code=" + pg.getCourrier().getCode();
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/AllQRcode");
        filePath = filePath + "/" + pg.getCourrier().getCode() + ".png";
        System.out.println("1");
        String charset = "UTF-8"; // or "ISO-8859-1"
        Map hintMap = new HashMap();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        System.out.println("2");
        Qrcode.createQRCode(qrCodeData, filePath, charset, hintMap, 90, 90);
        System.out.println("QR Code image created successfully!");

        System.out.println("Data read from QR Code: "
                + Qrcode.readQRCode(filePath, charset, hintMap));

    }

    public List<ProgressionCourrier> findListSendForVisa() {
        return progressionCourrierFacade.getListAllProgressionFromSendForVisa(connexionBean.getCurrentUser());
    }

    public List<ProgressionCourrier> findListAlredySign() {
        return progressionCourrierFacade.getListAllProgressionAlredyVisa(connexionBean.getCurrentUser());
    }

    public List<ProgressionCourrier> findListSendForSign() {
        return progressionCourrierFacade.getListAllProgressionFromSendForSign(connexionBean.getCurrentUser());
    }

    public List<ProgressionCourrier> findListGiveDGForSign() {
        return progressionCourrierFacade.getListAllProgressionFromGiveDGForSign(connexionBean.getCurrentUser());
    }

    public List<Utilisateur> findListotherChefSService() {
        return utilisateurFacade.getAllUserChefServiceUntilMe(connexionBean.getCurrentUser(), connexionBean.getCurrentUser().getFonction().getDepartementDirec());
    }

    public List<Utilisateur> findListDirecteur() {
        return utilisateurFacade.getAllUserDirecteurUntilMe(connexionBean.getCurrentUser());
    }

    public List<CourrierSignataire> findSignataires() {
        System.out.println("le pg est " + selectedProgressionCourrier.getCode());
        return courrierSignataireFacade.getAllCourrierSignataireByCourrier(selectedProgressionCourrier.getCourrier());
    }

    public List<CourrierSignataire> findSignatairesEvoyé(Courrier courrier) {
        return courrierSignataireFacade.getAllCourrierSignataireByCourrierNotSign(courrier);
    }

    public boolean findIfSignataire(Utilisateur user, Courrier courrier) {
        return courrierSignataireFacade.findWithTwoObjectIdBool(user, courrier);
    }

    public boolean findIfVisa(Utilisateur user, Courrier courrier) {
        System.out.println("le boole est" + courrier_utilisateurFacade.findWithTwoObjectIdBool(user, courrier) + "dd");
        return courrier_utilisateurFacade.findWithTwoObjectIdBool(user, courrier);
    }

    public List<ProgressionCourrier> findListAttenteArchive() {
        return progressionCourrierFacade.getListPgAttenteArchive(connexionBean.getCurrentUser());
    }

    public List<ProgressionCourrier> findListNewCourrArrive() {
        return progressionCourrierFacade.getListPgCourrierArriveSave();
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
            System.out.println("lid du dossier --" + selectedProgressionCourrier.getCourrier().getCode());
            // documentFacade.create(listDocument, listFile, dossierFacade.find(iddossier), selectedParties, audienceFacade.find(idaudience), "DOS-003", "PIECE");
            controlSizeFile(fileAdd);

//            documentFacade.createDocument(listDocumentToPersit, listFile, selectedFolderByGet, selectedParties, "DIC-022", "PIECE");
            Document newDocumentToPersit = new Document();
            newDocumentToPersit.setFileName(fileAdd.getSubmittedFileName());
            newDocumentToPersit.setMimeType(fileAdd.getContentType());
            newDocumentToPersit.setTaille("" + fileAdd.getSize());
            newDocumentToPersit.setSens(true);
            newDocumentToPersit.setTypeDocument(selectedTypeDocument);
            newDocumentToPersit.setCourrier(selectedProgressionCourrier.getCourrier());
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
            System.out.println("selectedDossier.getGed().getRefGed(): " + selectedProgressionCourrier.getCourrier().getGed().getRefGed());
            System.out.println("name: " + name);
            System.out.println("part.getInputStream(): " + fileAdd.getInputStream());
            System.out.println("document.getMimeType(): " + newDocumentToPersit.getMimeType());

            System.out.println("----------------------------------");

            documentCmis = gedFacade.creerDocument(selectedProgressionCourrier.getCourrier().getGed().getRefGed(), fileAdd.getSubmittedFileName(), fileAdd.getInputStream(),
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
            docToPersit.setCourrier(selectedProgressionCourrier.getCourrier());

            docToPersit.setDescription(description);
            documentFacade.create(docToPersit);
            selectedProgressionCourrier.getCourrier().setPieceJointe(true);
            courrierFacade.editMany(selectedProgressionCourrier.getCourrier());

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

    public void controlSizeFileWithFile(File myFile) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        // System.out.println("fichier est: "+file1);

        try {
            Long tailleFichierEnMo = (myFile.length() / (1024 * 1024));
            System.out.println("la taille du fichier convertie en Mo est: " + tailleFichierEnMo);

            String tailleFichier = JsfUtil.convertFileSize(myFile.length());
            System.out.println("la taille du fichier formatée est: " + tailleFichier);

            if (tailleFichierEnMo > 50) {
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

    public List<Document> findListDocx() {
        return documentFacade.getListeDocumentsByCourrier(selectedProgressionCourrier.getCourrier());
    }

    public void findListAfterSelcted(String caracteristique) {
        switch (caracteristique) {
            case "Initié":
                listCourrierSelected = progressionCourrierFacade.getListAllProgressionFromBoitEnvoi(connexionBean.getCurrentUser());
                idDBListSelect = "1AllUser";
                break;
            case "Visa":
                listCourrierSelected = findListSendForVisa();
                idDBListSelect = "2AllUser";
                break;
            case "Signature":
                if (!connexionBean.getCurrentUser().getFonction().getCode().equals("DG")) {
                    listCourrierSelected = findListSendForSign();
                } else {
                    listCourrierSelected = findListGiveDGForSign();
                }
                idDBListSelect = "3AllUser";
                break;
            case "Archivage":
                listCourrierSelected = findListAttenteArchive();
                idDBListSelect = "1SA";
                break;

        }

    }

    public void receiveParam() {
        PgCr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("PgCr");
        if (PgCr != null) {
            passCourrierView(progressionCourrierFacade.find(PgCr));

        }
    }

    public void receiveCodeCourrierInstigateur() {
        String code = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cour");
        if (code != null) {
            courrierInstigateur = courrierFacade.find(code);

        }
    }

    public void receiveCodeQr() {
        String code = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("code");
        if (code != null) {
            selectedCourrier = courrierFacade.find(code);
            SelectedDocument = documentFacade.getDocumentsCourrierByCourrier(selectedCourrier);
            System.out.println("le code est " + code);
        }
    }

    public void controlPassword() throws IOException {
        boolean respons = utilisateurFacade.getIfPassExist(EncrypteDecrypte.sha256(passwordQRcode));
        System.out.println("la reponse est " + respons);
        if (respons) {
//         gedBean.ouvrirUnDocument(SelectedDocument);
            RequestContext.getCurrentInstance().execute("$('#detailsCourrier').modal('show')");
            gedBean.telechargerDocumentReyno(SelectedDocument);
            selectedProgressionCourrier = findReferenceFromPgCour(selectedCourrier);

        }
    }

    public String findNumCourr(ProgressionCourrier pc) {
        if (pc != null && pc.getCourrier().getTypeCourrier().getCategorieCourrier() != CategorieCourrier.CourrierArrive) {
            String lastNum = pc.getCourrier().getInitiateur().getFonction().getDepartementDirec().getActualLastNumCourr();
            String code = "";
            if (!lastNum.equals("0000")) {

                int position = Integer.valueOf(lastNum);

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
        } else {
            return "";
        }
    }

    public void doArchive(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg = null;
        try {
            Evenement even = evenementFacade.find("Cou0010");
            ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
            lastProgresssioncourrier.setEvenement(even);
            lastProgresssioncourrier.setDateCreation(today);
            lastProgresssioncourrier.setStatut("Archivé");
            lastProgresssioncourrier.setUserReceive(connexionBean.getCurrentUser());
            lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
            progressionCourrierFacade.create(lastProgresssioncourrier);
            msg = bundle.getString("CourrierArchiveSuccesMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("CourrierArchiveErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doGiveDG(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg = null;
        try {
            Evenement even = evenementFacade.find("Cou0011");
            ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
            lastProgresssioncourrier.setEvenement(even);
            lastProgresssioncourrier.setDateCreation(today);
            lastProgresssioncourrier.setStatut("En cours de traitement");
            lastProgresssioncourrier.setUserReceive(connexionBean.getUserDG());
            lastProgresssioncourrier.setUserSend(connexionBean.getCurrentUser());
            progressionCourrierFacade.create(lastProgresssioncourrier);
            courrier.setTypeSignature("Numerique");
            courrierFacade.edit(courrier);
            notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), connexionBean.getUserDG(), "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, null, connexionBean.getCurrentAppli());
            NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), connexionBean.getUserDG(), "Un courrier  vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today));
            msg = bundle.getString("CourrierGiveDGSuccesMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("CourrierGiveDGErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public void doSignPhysique(ActionEvent event, ProgressionCourrier pc) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg = null;
        try {

            pc.getCourrier().setTypeSignature("Physique");
            courrierFacade.edit(pc.getCourrier());
            affectationQRcode(pc);
            msg = bundle.getString("CourrierSignPhysiqueSuccesMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("CourrierGiveDGErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

    public List<ProgressionCourrier> findListPgAttenteReInsertion() {
        return progressionCourrierFacade.getListPgAttenteReInsertion(connexionBean.getCurrentUser());
    }

    public boolean findIfCourrierAlredySign(Courrier cour) {
        Document doc = documentFacade.getDocumentsCourrierByCourrier(cour);
        return doc != null;
    }

    public Document findDocumentCourrierArrive(Courrier courrier) {
        return documentFacade.getDocumentsCourrierByCourrier(courrier);
    }

    public void rechercheCriter() {
        selectedCourrier = null;
        listCourrierFindForRecherhe = courrierFacade.findListCourrierRecherche(selectedDirection, categorieCourrierSelected, selectedTypeCourrier);
        System.out.println("la lust find est " + listCourrierFindForRecherhe);
    }

    public void rechercheByDate() {
        selectedCourrier = null;
        if (selectedTypeCourrier.getCategorieCourrier() == CategorieCourrier.CourrierArrive) {
            if (connexionBean.getCurrentUser().getFonction().getCode().equals("SD")) {
                listCourrierFindForRecherhe = courrierFacade.findListCourrierArriRechercheByDateArchiveInDirection(selectedTypeCourrier, date1, date2, selectedDirection);
            } else {
                listCourrierFindForRecherhe = courrierFacade.findListCourrierArriRechercheByDateArchive(selectedTypeCourrier, date1, date2);
            }
        } else {
            listCourrierFindForRecherhe = courrierFacade.findListCourrierRechercheByDateArchive(selectedTypeCourrier, date1, date2);
        }
        System.out.println("la lust find est " + listCourrierFindForRecherhe);
    }

    public void getListFileInFolderByCode() {
        selectedCourrier = courrierFacade.find(codeCourriSelect);
        listDocInCour = documentFacade.getListeDocumentsByCourrier(selectedCourrier);
    }

    public void findDetailInArchive(Courrier courrier) {

        passItem(findReferenceFromPgCour(courrier));
//         rechercheByDate();
    }

    public void reset() {
        newCourrier.reset();
    }

    public void showListFileScan(ProgressionCourrier pc) {
        selectedProgressionCourrier = pc;
        showFileScan = true;
    }

    public void shekIfShowTrb() {
        try {

            try (InputStream input = fileAdd.getInputStream()) {
                System.out.println("okk begin   " + fileAdd.getSubmittedFileName() + "et " + filePath + " et le chemin est " + FacesContext.getCurrentInstance().getExternalContext());

                Files.copy(input, new File(filePath, fileAdd.getSubmittedFileName()).toPath());
            } catch (FileAlreadyExistsException e1) {
                System.out.println("Existe DDeja:" + e1);
                InputStream inputExist = fileAdd.getInputStream();
                Files.copy(inputExist, new File(filePath, fileAdd.getSubmittedFileName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
        }

    }

    public void converteInFile() throws Exception {
        System.out.println("entre pour converteInFile");
        try {
            OutputStream outputStream = new FileOutputStream(filePath + "\\CourrierScane.pdf");
            PDFMerger.mergePdfFiles(listFilAdd, outputStream);

        } catch (IOException ex) {

            Logger.getLogger(CourrierBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void previewCourrierrArrive(ProgressionCourrier pc) throws IOException {
        passCourrierView(pc);
        if (progressionCourrierFacade.findIfCourrierrIsAlredyAR(connexionBean.getCurrentUser(), pc.getCourrier()) == false) {
            System.out.println("entre dans if");
            try {
                today = new Date();
                Evenement even = evenementFacade.find("Cou0019");
                ProgressionCourrier newPg = new ProgressionCourrier();
                ProgressionCourrier lastPg = progressionCourrierFacade.getLastProgressionCourrierSendToUser(pc.getCourrier(), connexionBean.getCurrentUser());
                newPg.setCourrier(lastPg.getCourrier());
                newPg.setEvenement(even);
                newPg.setDateCreation(today);
                newPg.setStatut(lastPg.getStatut());
                newPg.setUserReceive(connexionBean.getCurrentUser());
                newPg.setUserSend(connexionBean.getCurrentUser());
                newPg.setServiceConcerne(lastPg.getServiceConcerne());
                newPg.setDivisionConcernee(lastPg.getDivisionConcernee());
                newPg.setInstructions(lastPg.getInstructions());
                newPg.setObservation(lastPg.getObservation());
                newPg.setDirectionConcernee(connexionBean.getCurrentUser().getFonction().getDepartementDirec());
                if(lastPg.getDateLimite() != null){
                  newPg.setDateLimite(lastPg.getDateLimite());
                }
                progressionCourrierFacade.create(newPg);
            } catch (Exception e) {
                System.out.println("Erreur " + e);
            }
        }

        Document doc = findDocumentCourrierArrive(pc.getCourrier());
        if (doc != null) {
            gedBean.ouvrirUnDocument(doc);
        }

    }

    public ProgressionCourrier userInstructe(Courrier courrier) {
        System.out.println("You are great...");
        ProgressionCourrier pc;
        return progressionCourrierFacade.findUserInstrute(connexionBean.getCurrentUser(), courrier);
    }

    public boolean findIfAlredySend(ProgressionCourrier pc) {
        return progressionCourrierFacade.getIfCourrierArriveIsSend(pc.getCourrier());
    }

    public boolean findIfCourIsWithDelai(Courrier courrier) {
        return progressionCourrierFacade.getIfCourrierSendWithDelai(courrier);
    }

    public void doEnvoiDGCourrierArrive(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            Evenement even;
            ProgressionCourrier newProgression;
            even = evenementFacade.find("Cou0015");

            ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
            newProgression = new ProgressionCourrier();
            newProgression.setCourrier(lastProgresssioncourrier.getCourrier());
            newProgression.setDateCreation(new Date());
            newProgression.setEvenement(even);
            newProgression.setStatut("En cours de traitement");
            newProgression.setUserReceive(connexionBean.getUserDG());
            newProgression.setUserSend(connexionBean.getCurrentUser());

            progressionCourrierFacade.create(newProgression);

//                notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), connexionBean.getUserDirecteur(), "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, null, connexionBean.getCurrentAppli());
//                NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), connexionBean.getUserDirecteur(), "Un courrier vient de vous être envoyer par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser() + ")");
            msg = bundle.getString("CourrierSendDirecteurSuccesMsg");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception ex) {
            msg = bundle.getString("CourrierSendDirecteurErrorMsg");
            JsfUtil.addErrorMessage(msg);
            System.out.println("Erreur ex " + ex);
        }
    }

    public void doTerminerCourrierArrive(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            Evenement even;
            ProgressionCourrier newProgression;
            even = evenementFacade.find("Cou0021");

            ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
            newProgression = new ProgressionCourrier();
            newProgression.setCourrier(lastProgresssioncourrier.getCourrier());
            newProgression.setDateCreation(new Date());
            newProgression.setEvenement(even);
            newProgression.setStatut("En cours de traitement");
            newProgression.setUserReceive(connexionBean.getCurrentUser().getFonction().getDepartementDirec().getSecretaireDeDirection());
            newProgression.setUserSend(connexionBean.getCurrentUser());
            newProgression.setDateLimite(lastProgresssioncourrier.getDateLimite());
            newProgression.setDirectionConcernee(lastProgresssioncourrier.getDirectionConcernee());
            newProgression.setServiceConcerne(lastProgresssioncourrier.getServiceConcerne());
            newProgression.setDivisionConcernee(lastProgresssioncourrier.getDivisionConcernee());
            progressionCourrierFacade.create(newProgression);
            listPageEnvoiActive = true;

//                notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), connexionBean.getUserDirecteur(), "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, null, connexionBean.getCurrentAppli());
//                NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), connexionBean.getUserDirecteur(), "Un courrier vient de vous être envoyer par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser() + ")");
            msg = bundle.getString("CourrierTerminerSuccesMsg");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception ex) {
            msg = bundle.getString("EchecMessage");
            JsfUtil.addErrorMessage(msg);
            System.out.println("Erreur ex " + ex);
        }
    }

    public void doArchiverCourrierArrive(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

            Evenement even;
            ProgressionCourrier newProgression;
            even = evenementFacade.find("Cou0022");

            ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);
            newProgression = new ProgressionCourrier();
            newProgression.setCourrier(lastProgresssioncourrier.getCourrier());
            newProgression.setDateCreation(new Date());
            newProgression.setEvenement(even);
            newProgression.setStatut("Archiver");
            newProgression.setUserReceive(connexionBean.getCurrentUser());
            newProgression.setUserSend(connexionBean.getCurrentUser());
            newProgression.setDateLimite(lastProgresssioncourrier.getDateLimite());
            newProgression.setDirectionConcernee(lastProgresssioncourrier.getDirectionConcernee());
            newProgression.setServiceConcerne(lastProgresssioncourrier.getServiceConcerne());
            newProgression.setDivisionConcernee(lastProgresssioncourrier.getDivisionConcernee());
            progressionCourrierFacade.create(newProgression);

//                notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), connexionBean.getUserDirecteur(), "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, null, connexionBean.getCurrentAppli());
//                NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), connexionBean.getUserDirecteur(), "Un courrier vient de vous être envoyer par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser() + ")");
            msg = bundle.getString("CourrierArchiverSuccesMsg");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception ex) {
            msg = bundle.getString("EchecMessage");
            JsfUtil.addErrorMessage(msg);
            System.out.println("Erreur ex " + ex);
        }
    }

    public List<ProgressionCourrier> findCourrierArrSendDG() {
        return progressionCourrierFacade.findListCourrierArrReceiveByDG(connexionBean.getCurrentUser());
    }

    public int findNbrCourrierArrSendDG() {
        return progressionCourrierFacade.findNbrCourrierArrReceiveFromSAOrSC(connexionBean.getCurrentUser(), evenementFacade.find("Cou0015"));
    }

    public int findNbrCourrierArrReceiveFromSC() {
        return progressionCourrierFacade.findNbrCourrierArrReceiveFromSCOrCreateByMe(connexionBean.getCurrentUser(), evenementFacade.find("Cou0020"), evenementFacade.find("Cou0014"));
    }

    public int findNbrCourrierArrSendDirecteurs() {
        return progressionCourrierFacade.findNbrCourrierArrReceiveFromDG(connexionBean.getCurrentUser(), evenementFacade.find("Cou0016"));
    }

    public int findNbrCourrierArrSendDChefServices() {
        return progressionCourrierFacade.findNbrCourrierArrReceiveFromDirecteur(connexionBean.getCurrentUser(), evenementFacade.find("Cou0017"));
    }

    public int findNbrCourrierArrSendDChefDivision() {
        return progressionCourrierFacade.findNbrCourrierArrReceiveFromChefService(connexionBean.getCurrentUser(), evenementFacade.find("Cou0018"));
    }

    public List<ProgressionCourrier> findListCourrierArrReceive() {
        Evenement even = null, evenAR = null;
        evenAR = evenementFacade.find("Cou0019");
        List<ProgressionCourrier> pc = new ArrayList<>();
        if (connexionBean.getCurrentUser().isDirecteur()) {
            even = evenementFacade.find("Cou0016");
            pc = progressionCourrierFacade.findListCourrierArrReceiveFromDG(connexionBean.getCurrentUser(), even, evenAR);
        }
        if (connexionBean.getCurrentUser().isChefService()) {
            even = evenementFacade.find("Cou0017");
            pc = progressionCourrierFacade.findListCourrierArrReceiveFromDirecteur(connexionBean.getCurrentUser(), even, evenAR);
        }
        if (connexionBean.getCurrentUser().getFonction().isChefDivision()) {
            even = evenementFacade.find("Cou0018");
            pc = progressionCourrierFacade.findListCourrierArrReceiveFromChefService(connexionBean.getCurrentUser(), even, evenAR);
        }
        return pc;

    }

    public void updateInstructions(String instruction) {
        if (isInstructionChecked()) {
            if (instructions == null) {
                instructions = instruction;
            } else {
                instructions += "  " + instruction;
            }
        } else {

            removeInstruction(instruction);
        }
        System.out.println("instruction " + instructions);
    }

    public void removeInstruction(String instruction) {

        instructions = instructions.replaceFirst(instruction, "");

    }

    public void addOrRemoveDirection(Departement departement) {
        if (isInstructionChecked()) {
            departementConcernes.add(departement);
        } else {
            departementConcernes.remove(departement);
        }
        System.out.println("departementConcerne " + departementConcernes.size());
    }

    public void addOrRemoveService(Service service) {
        if (isInstructionChecked()) {
            servicesConcernes.add(service);
        } else {
            servicesConcernes.remove(service);
        }
        System.out.println("departementConcerne " + servicesConcernes.size());
    }

    public void addOrRemoveDivision(Division div) {
        if (isInstructionChecked()) {
            divisionsConcernes.add(div);
        } else {
            divisionsConcernes.remove(div);
        }
        System.out.println("departementConcerne " + divisionsConcernes.size());
    }

    public void annotationDGorDirecOrChefSer(ActionEvent event, Courrier courrier) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            today = new Date();
            int sec = today.getSeconds();

            Evenement even;
            if (connexionBean.getCurrentUser().isDg()) {
                even = evenementFacade.find("Cou0016");
                for (Departement dep : departementConcernes) {
                    today.setSeconds(++sec);
                    ProgressionCourrier newPg = new ProgressionCourrier();
//                    ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrier(courrier);

                    newPg.setCourrier(courrier);
                    newPg.setEvenement(even);
                    newPg.setDateCreation(today);
                    newPg.setStatut("En cours de traitement");
                    newPg.setUserReceive(utilisateurFacade.getUserDirecteurFromDirection(dep));
                    newPg.setUserSend(connexionBean.getCurrentUser());
                    newPg.setServiceConcerne(null);
                    newPg.setDivisionConcernee(null);
                    newPg.setInstructions(instructions);
                    newPg.setObservation(observation);
                    newPg.setDirectionConcernee(dep);
                    progressionCourrierFacade.create(newPg);

                }
            }

            if (connexionBean.getCurrentUser().isDirecteur()) {
                even = evenementFacade.find("Cou0017");
                for (Service ser : servicesConcernes) {
                    today.setSeconds(++sec);
                    ProgressionCourrier newPg = new ProgressionCourrier();
                    ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrierSendToUser(courrier, connexionBean.getCurrentUser());
                    newPg.setCourrier(courrier);
                    newPg.setEvenement(even);
                    newPg.setDateCreation(today);
                    newPg.setStatut(lastProgresssioncourrier.getStatut());
                    newPg.setUserReceive(utilisateurFacade.getUserChefServiceByService(ser));
                    newPg.setUserSend(connexionBean.getCurrentUser());
                    newPg.setServiceConcerne(ser);
                    newPg.setDivisionConcernee(null);
                    newPg.setInstructions(instructions);
                    newPg.setObservation(observation);
                    newPg.setDirectionConcernee(ser.getDepartement());
                    if (sendWithDelai) {
                        newPg.setDateLimite(dateLimite);
                    }
                    progressionCourrierFacade.create(newPg);

                }
            }

            if (connexionBean.getCurrentUser().isChefService()) {
                even = evenementFacade.find("Cou0018");
                for (Division div : divisionsConcernes) {
                    today.setSeconds(++sec);

                    ProgressionCourrier newPg = new ProgressionCourrier();
                    ProgressionCourrier lastProgresssioncourrier = progressionCourrierFacade.getLastProgressionCourrierSendToUser(courrier, connexionBean.getCurrentUser());
                    newPg.setCourrier(courrier);
                    newPg.setEvenement(even);
                    newPg.setDateCreation(today);
                    newPg.setStatut(lastProgresssioncourrier.getStatut());
                    newPg.setUserReceive(utilisateurFacade.getUserChefDivisionByDivision(div));
                    newPg.setUserSend(connexionBean.getCurrentUser());
                    newPg.setServiceConcerne(div.getService());
                    newPg.setDivisionConcernee(div);
                    newPg.setInstructions(instructions);
                    newPg.setObservation(observation);
                    newPg.setDirectionConcernee(div.getService().getDepartement());
                    newPg.setDateLimite(lastProgresssioncourrier.getDateLimite());
                    progressionCourrierFacade.create(newPg);

                }
            }
            listPageEnvoiActive = true;

//                notificationFacade.createNotificationCourrier(connexionBean.getCurrentUser(), connexionBean.getUserDirecteur(), "Un courrier vous a été envoyé par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser().getName() + ") ce " + connexionBean.convertDate(today), courrier, null, connexionBean.getCurrentAppli());
//                NotificationCourrierEndPoint.sendNotif(connexionBean.getCurrentUser(), connexionBean.getUserDirecteur(), "Un courrier vient de vous être envoyer par (" + connexionBean.getCurrentUser().getFonction().getCode() + ") Mr/Mm (" + connexionBean.getCurrentUser() + ")");
            msg = bundle.getString("CourrierDispatchingSuccesMsg");
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception ex) {
            msg = bundle.getString("CourrierDispatchingErrorMsg");
            JsfUtil.addErrorMessage(msg);
            System.out.println("Erreur ex " + ex);
        }
    }

    public List<ProgressionCourrier> getPgsForTypeUser(Courrier courrier, String type) {
        List<ProgressionCourrier> listeProgression = new ArrayList<ProgressionCourrier>();
        try {
            if (courrier != null) {
                listeProgression = progressionCourrierFacade.findPGForTypeUser(courrier, type);
            }
            System.out.println("la liiste de " + type + "--> " + listeProgression);
        } catch (Exception e) {
            System.out.println("Erreur detailAR " + e);
        }
        return listeProgression;

    }

    public List<ProgressionCourrier> getPgsForUserSend(Courrier courrier, Utilisateur user) {
        List<ProgressionCourrier> listeProgression = new ArrayList<ProgressionCourrier>();
        try {
            if (courrier != null) {
                listeProgression = progressionCourrierFacade.findPGForUserSend(courrier, user);
            }
            System.out.println("la liiste de " + user.getFonction().getCode() + "--> " + listeProgression);
        } catch (Exception e) {
            System.out.println("Erreur detailAR " + e);
        }
        return listeProgression;

    }

    public List<ProgressionCourrier> findListPCFromLastReceive(Courrier courrier) {
        return progressionCourrierFacade.getListAllLastProgressionCourrierInDireection(courrier);
    }

    public boolean findIfCourrierAlredyTritByDG(Courrier courrier) {
        return progressionCourrierFacade.getIfAlredyTraitByDG(courrier);
    }

    public int findNbrTraiteInDirection(Departement direction) {
        return progressionCourrierFacade.findCourrierAttenteTraitement(direction).size();
    }

    public List<ProgressionCourrier> findListCourrierNotArchive(Departement direction) {
        return progressionCourrierFacade.findCourrierInDirectionNotArchive(direction);
    }

    public void passDep(Departement dep) {
        selectedDirection = dep;
    }

    public String getPgFromDelai(Courrier courrier) {
        ProgressionCourrier pc = progressionCourrierFacade.getPgFromDelai(courrier);
        Long nb = JsfUtil.getDureeEnJours(new Date(), pc.getDateLimite());
        if (nb > 0) {
            delaiPasse = false;
            return "Dans " + nb + " jour(s)";
        } else {
            delaiPasse = true;
            return "Il y a " + (-nb) + " jour(s)";
        }

    }

    public boolean findIfAlredyTermine(Courrier courrier) {
        return progressionCourrierFacade.getIfCourrierAlredyTermine(courrier);
    }

}

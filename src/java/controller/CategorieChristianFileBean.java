/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import ejb.CategorieChristianFileFacade;
import ejb.ChristianFileFacade;
import ejb.ConnexionFacade;
import ejb.ContextFacade;
import ejb.GedFacade2;
import ejb.ServiceFacade;
import ged.DocumentCmis;
import ged.DossierCmis;
import ged.ejb.GedFacade;
import jpa.CategorieChristianFile;
import util.JsfUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import jpa.ChristianFile;
import jpa.Demande;
import jpa.Departement;
import jpa.Document;
import jpa.Ged;
import jpa.Service;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;

/**
 *
 * @author MJLDH
 */
@Named(value = "categorieChristianFileBean")
@ViewScoped
public class CategorieChristianFileBean implements Serializable {
//FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
    @Inject
    private CategorieChristianFileFacade categorieChristianFileFacade;
      @Inject
    private ServiceFacade serviceFacade;
    private CategorieChristianFile selectedCategorieChristianFile;
    private CategorieChristianFile newCategorieChristianFile;
    private List<CategorieChristianFile> listeCategorieChristianFiles;
    private List<Service> listeService;
    private Departement selectedDep;
    private boolean isDirecteur;
    private Part fileAdd;
     private GedFacade gedFacade;
      @Inject
    private ConnexionBean connexionBean;
      @Inject
    ConnexionFacade connexionFacade;
      @Inject
    private GedFacade2 gedFacade2;
           @Inject
    private ChristianFileFacade christianFileFacade;
           @Inject
    ContextFacade contextFacade;
           private String cheminSuivi;
           private List<ChristianFile> allFileSelectedCat;

    /**
     * Creates a new instance of CategorieChristianFileBean
     */
    public CategorieChristianFileBean() {
        
    }

    @PostConstruct
    public void init() {
        cheminSuivi=contextFacade.getALFRESCO_REPOSITORY_CHRISTIAN_FILES();
        listeCategorieChristianFiles = categorieChristianFileFacade.findAll();
        listeService=serviceFacade.findAll();
        if(listeCategorieChristianFiles.size()!=0)
        {
        System.out.println("---->>>>"+listeCategorieChristianFiles.get(0).getCode());
        }
        allFileSelectedCat=christianFileFacade.findAll();
    }

    public CategorieChristianFileFacade getCategorieChristianFileFacade() {
        return categorieChristianFileFacade;
    }

    public void setCategorieChristianFileFacade(CategorieChristianFileFacade categorieChristianFileFacade) {
        this.categorieChristianFileFacade = categorieChristianFileFacade;
    }

    public CategorieChristianFile getSelectedCategorieChristianFile() {
        return selectedCategorieChristianFile;
    }

    public void setSelectedCategorieChristianFile(CategorieChristianFile selectedCategorieChristianFile) {
        this.selectedCategorieChristianFile = selectedCategorieChristianFile;
    }

    public List<CategorieChristianFile> getListeCategorieChristianFiles() {
        return listeCategorieChristianFiles;
    }

    public void setListeCategorieChristianFiles(List<CategorieChristianFile> listeCategorieChristianFiles) {
        this.listeCategorieChristianFiles = listeCategorieChristianFiles;
    }

    public CategorieChristianFile getNewCategorieChristianFile() {
        if (newCategorieChristianFile == null) {
            newCategorieChristianFile = new CategorieChristianFile();
        }
        return newCategorieChristianFile;
    }

    public void setNewCategorieChristianFile(CategorieChristianFile newCategorieChristianFile) {
        this.newCategorieChristianFile = newCategorieChristianFile;
    }

    public void passItem(CategorieChristianFile item) {
        this.selectedCategorieChristianFile = item;
    }

    public List<Service> getListeService() {
        return listeService;
    }

    public void setListeService(List<Service> listeService) {
        this.listeService = listeService;
    }

    public Departement getSelectedDep() {
        if(selectedDep==null) selectedDep=new Departement();
        return selectedDep;
    }

    public void setSelectedDep(Departement selectedDep) {
        this.selectedDep = selectedDep;
    }

    public boolean isIsDirecteur() {
        return isDirecteur;
    }

    public void setIsDirecteur(boolean isDirecteur) {
        this.isDirecteur = isDirecteur;
    }

    public Part getFileAdd() {
        return fileAdd;
    }

    public void setFileAdd(Part fileAdd) {
        this.fileAdd = fileAdd;
    }

    
    
    public void doCreate(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                "util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {

//            this.categorieChristianFileFacade.create(newCategorieChristianFile);
            String code = this.categorieChristianFileFacade.createAndGidCode(newCategorieChristianFile);
            newCategorieChristianFile.setGed(creerDossier(categorieChristianFileFacade.find(code)));
            categorieChristianFileFacade.edit(newCategorieChristianFile);
            msg = bundle.getString("CategorieChristianFileCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            newCategorieChristianFile = new CategorieChristianFile();
            this.listeCategorieChristianFiles = this.categorieChristianFileFacade.findAll();
        } catch (Exception e) {
            System.out.println("Erreur :"+ e);
            msg = bundle.getString("CategorieChristianFileCreateErrorMsg");
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
            categorieChristianFileFacade.edit(selectedCategorieChristianFile);
            this.listeCategorieChristianFiles = this.categorieChristianFileFacade.findAll();
               msg = bundle.getString("CategorieChristianFileCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
        } catch (Exception e) {
            msg = bundle.getString("CategorieChristianFileCreateErrorMsg");
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
            categorieChristianFileFacade.remove(selectedCategorieChristianFile);
           msg = bundle.getString("CategorieChristianFileCreateSuccessMsg");
            JsfUtil.addSuccessMessage(msg);
            this.listeCategorieChristianFiles.remove(this.selectedCategorieChristianFile);
            this.listeCategorieChristianFiles = this.categorieChristianFileFacade.findAll();
        } catch (Exception e) {
            msg = bundle.getString("CategorieChristianFileCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
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
    
    
    public void findListServiceInDep(){
     listeService=serviceFacade.getServiceByDep(selectedDep);
    }

    public void reset() {
        newCategorieChristianFile.reset();
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
           
            // documentFacade.create(listDocument, listFile, dossierFacade.find(iddossier), selectedParties, audienceFacade.find(idaudience), "DOS-003", "PIECE");
            controlSizeFile(fileAdd);

//            documentFacade.createDocument(listDocumentToPersit, listFile, selectedFolderByGet, selectedParties, "DIC-022", "PIECE");
            ChristianFile  newDocumentToPersit = new ChristianFile();
            newDocumentToPersit.setFileName(fileAdd.getSubmittedFileName());
            newDocumentToPersit.setMimeType(fileAdd.getContentType());
            newDocumentToPersit.setTaille("" + fileAdd.getSize());
            newDocumentToPersit.setSens(true);
            newDocumentToPersit.setCategorie(selectedCategorieChristianFile);
           
//             documentFacade.create(newDocumentToPersit);

            System.out.println("----------------------------------");
            System.out.println("doc name: " + newDocumentToPersit.getFileName());
            System.out.println("doc ged: " + newDocumentToPersit.getGed());
            System.out.println("doc mimeType: " + newDocumentToPersit.getMimeType());
            System.out.println("doc getTaille: " + newDocumentToPersit.getTaille());
            System.out.println("doc Type: " + newDocumentToPersit.getCategorie().getLibelle());
            System.out.println("----------------------------------");

            Ged ged;
            String name = newDocumentToPersit.getFileName();
            String[] ext = name.split("\\.");
            if (ext.length > 0) {
                name = ext[ext.length - 1];
            } else {
                name = "";
            }

            name = JsfUtil.getFileName(name, newDocumentToPersit.getCategorie().getLibelle());
            name=fileAdd.getSubmittedFileName();
            DocumentCmis documentCmis;

            //dossierCmis = gedFacade.creerDossier(authenticateBean.getConfig().getREPOSITORY().getValeur(), name, entity.getLibelleCourt());
            System.out.println("----------------------------------");
            System.out.println("selectedDossier.getGed().getRefGed(): " + selectedCategorieChristianFile.getGed().getRefGed());
            System.out.println("name: " + name);
            System.out.println("part.getInputStream(): " + fileAdd.getInputStream());
            System.out.println("document.getMimeType(): " + newDocumentToPersit.getMimeType());

            System.out.println("----------------------------------");

            documentCmis = gedFacade.creerDocument(selectedCategorieChristianFile.getGed().getRefGed(), fileAdd.getSubmittedFileName(), fileAdd.getInputStream(),
                    newDocumentToPersit.getMimeType(), "nouveau fichier");
            ged = new Ged(documentCmis.getId(), name, documentCmis.getId());
            gedFacade2.create(ged);

            ChristianFile docToPersit = new ChristianFile();
//            String localName = newDocumentToPersit.getFileName() + "." + JsfUtil.convertDate(new Date(), "EEEEE dd MMM yyyy 'A' HH:mm:ss");
            System.out.println("le fichier " + fileAdd.getSubmittedFileName());
            docToPersit.setFileName(fileAdd.getSubmittedFileName());
            docToPersit.setGed(ged);
            docToPersit.setMimeType(newDocumentToPersit.getMimeType());
            docToPersit.setSens(true);
            docToPersit.setSubmittedFileName(name);
            docToPersit.setTaille(newDocumentToPersit.getTaille());
            docToPersit.setCategorie(newDocumentToPersit.getCategorie());
           

            docToPersit.setDescription("fichier " + selectedCategorieChristianFile.getLibelle() + " enregistré ce " + JsfUtil.convertDate(new Date(), "EEEEE dd MMM yyyy 'A' HH:mm:ss"));
            christianFileFacade.create(docToPersit);

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

            if (tailleFichierEnMo > 1000) {
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
    
    
    public Ged creerDossier(CategorieChristianFile categorie) {
        gedFacade = connexionFacade.initGed(connexionBean.getCurrentUser());
        Ged ged;

//        String name = entity.getNumeroRP().replaceAll("/", "-");
        String name = categorie.getCode();
        System.out.println("nom dossier issu du split pour etre name ds ged " + name);
        DossierCmis dossierCmis;

        System.out.println("description du dossier ");
        CmisObject obj;

//        CmisObject obj;
//
        System.out.println("contextBean.getALFRESCO_REPOSITORY() " + contextFacade.getALFRESCO_REPOSITORY_CHRISTIAN_FILES());
//        System.out.println("annee.getValeur() " + annee.getValeur());
        System.out.println("name" + name);

        obj = gedFacade.getCmisObjectByPath(cheminSuivi + "/" + name);
        System.out.println(" le parent est alors  " + cheminSuivi);
        if (obj == null) {

            dossierCmis = gedFacade.creerDossier(cheminSuivi,categorie.getCode(), "nouveau dossier" + name + " créé en ce jour le " + connexionBean.convertDate(new Date()));
        } else {
            dossierCmis = new DossierCmis((Folder) obj);
        }

        ged = new Ged(dossierCmis.getId(), name, dossierCmis.getId());
        gedFacade2.create(ged);

        return ged;
    }

    public List<ChristianFile> getAllFileSelectedCat() {
        return allFileSelectedCat;
    }

    public void setAllFileSelectedCat(List<ChristianFile> allFileSelectedCat) {
        this.allFileSelectedCat = allFileSelectedCat;
    }
    
    
    public void selectedCat(CategorieChristianFile cat){
     allFileSelectedCat=christianFileFacade.getListeChhristianFilInCategorie(cat);
        System.out.println("la liste est "+allFileSelectedCat);
    }
}

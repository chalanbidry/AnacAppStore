/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import ejb.ConnexionFacade;
import ejb.DocumentFacade;
import ejb.ParametreFacade;

import ged.CmisOperationException;
import ged.DocumentCmis;
import ged.ejb.GedFacade;
import jpa.Document;
import jpa.Demande;
import jpa.Ged;
import jpa.Utilisateur;
import java.io.Serializable;
import javax.inject.Named;
import util.JsfUtil;
//import  util.pojo.AnneePojo;
//import  util.pojo.DocumentPojo;
//import  util.pojo.DossierPojo;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.primefaces.context.RequestContext;

/**
 *
 * @author MJLDH
 */
@Named(value = "gedBean")
@ViewScoped
public class GedBean implements Serializable {

    @Inject
    private ParametreFacade parametreFacade;
//    @Inject
//    private DossierFacade dossierFacade;

    @Inject
    private DocumentFacade documentFacade;

    private GedFacade gedFacade;
//    private Dossier dossierCourant;
    private Document documentCourant;
    private String documentsParAnneeJson;
    private String dossierCourantId;
    private String documentCourantId;
    private String fileName;
    private String filePath;
    private String mineType;
    private String content;
    //private Document selectedDocument;
    private Part file1;
    private Part fileToUpdate;
    static final Logger logger = Logger.getLogger("controller.GedBean");
//    private List<Dossier> listeDossiersExplorateur;
    private List<Document> listeDocuments;
    @Inject
    private ConnexionFacade connexionFacade;
    @Inject
    private ConnexionBean connexionBean;

    public GedBean() {
    }

    @PostConstruct
    public void init() {

        this.gedFacade = connexionFacade.initGed(connexionBean.getCurrentUser());
        System.out.println("entrer dans GedBean init");
//        this.gedFacade = this.contextBean.getGedFacade();

//        this.dossierCourant = new Dossier();
        this.documentCourant = new Document();
        // this.chargerTousDocumentsParAnnee();
//        listeDossiersExplorateur = new ArrayList<>();
        //listeDocuments = new ArrayList<>();
//        resultItem();
    }

   

    public List<Document> getListeDocuments() {
        return listeDocuments;
    }

    public void setListeDocuments(List<Document> listeDocuments) {
        this.listeDocuments = listeDocuments;
    }



    public String getDocumentsParAnneeJson() {
        return documentsParAnneeJson;
    }

    public void setDocumentsParAnneeJson(String documentsParAnneeJson) {
        this.documentsParAnneeJson = documentsParAnneeJson;
    }

   
    public Part getFile1() {
        return file1;
    }

    public void setFile1(Part file1) {
        this.file1 = file1;
    }

  

    public Document getDocumentCourant() {
        return documentCourant;
    }

    public void setDocumentCourant(Document documentCourant) {
        this.documentCourant = documentCourant;
    }

    public String getDossierCourantId() {
        return dossierCourantId;
    }

    public void setDossierCourantId(String dossierCourantId) {
        this.dossierCourantId = dossierCourantId;
    }

    public String getDocumentCourantId() {
        return documentCourantId;
    }

    public void setDocumentCourantId(String documentCourantId) {
        this.documentCourantId = documentCourantId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public Document getSelectedDocument() {
//        return selectedDocument;
//    }
//
//    public void setSelectedDocument(Document selectedDocument) {
//        this.selectedDocument = selectedDocument;
//    }
    
//    A revoir 
//    public void selectionnerUnDocument() {
//        dossierCourant = dossierCourantId != null ? dossierFacade.find(dossierCourantId) : new Dossier();
//        documentCourant = documentCourantId != null ? documentFacade.find(documentCourantId) : new Document();
//
//        System.out.println("Dossier : " + dossierCourant.getNomDossier());
//    }

    public void ouvrirUnDocument(Document document) throws IOException {
        try {
            documentCourant=document;
            System.out.println("ouvrirUnDocument - begin : " + documentCourant);
            String ref = documentCourant.getGed().getRefGed();
            System.out.println("Document ref : " + ref);
            DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
            System.out.println("Document : " + doc);

            mineType = documentCourant.getMimeType();
            System.out.println("mineType : " + mineType);
            content = doc.contenuBase64();
            System.out.println("content : " + content);
             RequestContext.getCurrentInstance().execute(" $('#page-preview').removeClass('hide');");
        } catch (Exception e) {
            System.out.println("Erreur est "+e);
        }

        /*
         fileName = doc.getNom();
         //filePath = parametreFacade.getAppDir().getPath() + File.separator + fileName;
         fileName = fileName.replace(" ", "_");
         fileName = fileName.replace("'", "_");
         String rootPath = "C:/Users/Ordinateur/Documents/NetBeansProjects/forsetiV2/web";
         filePath = rootPath + File.separator + "temp" + File.separator + fileName;
    

         //String tempFileName = String.format("%s_%d_%s", ref, ThreadLocalRandom.current().nextLong(), fileName);
         //String filePath = parametreFacade.getTempDirPath() + tempFileName;
         //Files.write(Paths.get(filePath), doc.contenuOctets(), CREATE, TRUNCATE_EXISTING, WRITE, DELETE_ON_CLOSE);
        
         FileOutputStream f = new FileOutputStream(filePath, false);
         f.write(doc.contenuOctets());

         System.out.println("fileName = " + fileName + " - filePath : " + filePath + " - mineType : " + mineType);

         String fileInName = filePath.toLowerCase();
         if (fileInName.endsWith("doc") || fileInName.endsWith("docx")) {
         String ext = FilenameUtils.getExtension(fileInName);
         String fileOutName = FilenameUtils.getBaseName(fileInName) + ".pdf";
         filePath = fileOutName;
         fileName = FilenameUtils.getName(filePath);
         mineType = "application/pdf";
            
         System.out.println("fileName = " + fileName + " - filePath : " + filePath + " - mineType : " + mineType);

         // Load DOCX
         System.out.println("Load DOCX... : " + fileInName);
         File fileIn = new File(fileInName);
         InputStream in = new FileInputStream(fileIn);
         XWPFDocument docx = new XWPFDocument(in);
         System.out.println("Load DOCX - end");

         // prepare options
         PdfOptions options = PdfOptions.create();

         // Conert to PDF
         System.out.println("Conert to PDF... : " + fileOutName);
         File fileOut = new File(fileOutName);
         OutputStream out = new FileOutputStream(fileOut);
         PdfConverter.getInstance().convert(docx, out, options);
         System.out.println("Conert to PDF - end");


         f = new FileOutputStream(filePath, false);
         f.write(doc.contenuOctets());

         }
         */
        System.out.println("ouvrirUnDocument - end");

    }

    public void passDocumentForArchive(Document item) throws IOException {
        documentCourant = item;
        System.out.println("Sélectionne un document=" + documentCourant);
        //this.telechargerDocument();
    }

    public void telechargerDocument(Document document) throws IOException {
        System.out.println("renter ds method");
        System.out.println("document courant telecharger " + document);
        try {
            if ((document != null) && (document.getGed() != null)) {
                System.out.println("downloadDocument : " + document.getFileName());
                String ref = document.getGed().getRefGed();
                System.out.println("Document ref : " + ref);
                DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
                System.out.println("Document : " + doc);

                FacesContext facesContext = FacesContext.getCurrentInstance();
                //  System.out.println("1**********");
                HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
                // System.out.println("2**********");
                response.reset();
                // System.out.println("1**********");
                response.setContentType(doc.getDocument().getContentStreamMimeType());
                //System.out.println("3**********"+doc.getDocument().getContentStreamMimeType());
                response.setHeader("Content-Disposition", "attachment;filename=" + doc.getDocument().getName());
                // System.out.println("4**********"+doc.getDocument().getName());
                response.setContentLength((int) doc.getDocument().getContentStream().getLength());
                //System.out.println("5**********"+(int) doc.getDocument().getContentStream().getLength());
                BufferedInputStream buf = new BufferedInputStream(doc.getDocument().getContentStream().getStream());
                //System.out.println("6**********"+buf);
                //System.out.println("6**********"+doc.contenuOctets());

//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            output.write(doc.contenuOctets());
//
//            String fileName = parametreFacade.getAppDir().getPath() + File.separator + documentCourant.getFileName();
//            System.out.println("fileName = " + fileName);
//            FileOutputStream f = new FileOutputStream(fileName);
//            f.write(doc.contenuOctets());
//            f.close();
//
//            int readBytes = 0;
//            while ((readBytes = buf.read()) != 1) {
//                response.getOutputStream().write(readBytes);
//            }
                response.getOutputStream().write(doc.contenuOctets());
                // System.out.println("7**********");
                response.getOutputStream().flush();
                // System.out.println("8**********");
                response.getOutputStream().close();
                // System.out.println("9**********");
                facesContext.responseComplete();
                //System.out.println("10**********");
                facesContext.renderResponse();
                //System.out.println("11**********");
                buf.close();
                System.out.println("12**********");
            }

        } catch (IOException ex) {
            System.out.println("erreur telecharger document ");
        } finally {
        }
//        String fileName = doc.getNom();
//
//        String tempFileName = String.format("%s_%d_%s", ref, ThreadLocalRandom.current().nextLong(), fileName);
//        String filePath = parametreFacade.getTempDirPath() + tempFileName;
//        Files.write(Paths.get(filePath), doc.contenuOctets(), CREATE, TRUNCATE_EXISTING, WRITE, DELETE_ON_CLOSE);
//        fileLink = filePath;

        /*
         ByteArrayOutputStream output = new ByteArrayOutputStream();
         output.write(doc.contenuOctets());
        
         String fileName = parametreFacade.getAppDir().getPath() + File.separator + documentCourant.getFileName();
         System.out.println("fileName = " + fileName);
         FileOutputStream f =new FileOutputStream(fileName);
         f.write(doc.contenuOctets());
         f.close();               

         fileLink = fileName;
         System.out.println("fileLink = " + fileLink);
         //        System.out.println("OutputStream ( " + output.size() + ") : " + output);
         //        try {
         //            JsfUtil.sendFile(output, documentCourant.getFileName());
         //        } catch (Exception ex) {
         //            Logger.getLogger(GedBean.class.getName()).log(Level.SEVERE, null, ex);
         //        }
         */
    }

//    public void verrouillerDocument() throws IOException {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ResourceBundle bundle;
//        bundle = ResourceBundle.getBundle(
//                " util.Bundle", context.getViewRoot().getLocale());
//        String msg;
//        try {
//            System.out.println("downloadDocument : " + documentCourant);
////            String ref = documentCourant.getGed().getRefGed();
////            System.out.println("Document ref : " + ref);
//            //DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
////            DocumentCmis doc = (DocumentCmis) gedFacade.verrouillerPourModification(ref);
////            System.out.println("Document : " + doc);
////
////            Ged ged = new Ged(doc.getId(), doc.getNom(), doc.getId());
////            documentCourant.setGed(ged);
////            documentFacade.edit(documentCourant);
//
//            DocumentCmis doc = verrouillerMonDocument(documentCourant);
//            msg = bundle.getString("lockedDocumentsuccessmsg");
//            JsfUtil.addErrorMessage(msg);
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
//            response.reset();
//
//            response.setContentType(doc.getDocument().getContentStreamMimeType());
//            response.setHeader("Content-Disposition", "attachment;filename=" + doc.getDocument().getName());
//            response.setContentLength((int) doc.getDocument().getContentStream().getLength());
//            BufferedInputStream buf = new BufferedInputStream(doc.getDocument().getContentStream().getStream());
//
//            response.getOutputStream().write(doc.contenuOctets());
//            response.getOutputStream().flush();
//            response.getOutputStream().close();
//            facesContext.responseComplete();
//            facesContext.renderResponse();
//            buf.close();
//        } catch (IOException ex) {
//            msg = bundle.getString("lockedDocumenterrormsg " + ex);
//            JsfUtil.addErrorMessage(msg);
//        } finally {
//        }
//    }
    public void verrouillerDocument() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                " util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            System.out.println("downloadDocument : " + documentCourant);
            DocumentCmis doc = verrouillerMonDocument(documentCourant);
            // msg = bundle.getString("lockedDocumentsuccessmsg");
            msg = "Document verrouillé avec succès ";
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            // msg = bundle.getString("lockedDocumenterrormsg");
            msg = "Le document n'a pas pu être verrouillé ";
            JsfUtil.addErrorMessage(msg);
            System.out.println("erreur verrou doc ");
        }
    }

    public void mettreAjourDocument() throws IOException {
        System.out.println("downloadDocument : " + documentCourantId);
        String ref = documentCourant.getGed().getRefGed();
        System.out.println("Document ref : " + ref);
        DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
        System.out.println("Document : " + doc);
        DocumentCmis documentCmis;
        gedFacade.enregistrerCopieModifiee(content, fileName, null, mineType, content);
    }

    public void enregistrerUnDocument() {
        System.out.println("enregistrerUnDocument : " + documentCourantId);
    }

    public void verreouillerUnDocument() {
        System.out.println("verreouillerUnDocument : " + documentCourantId);
    }

    public String upload(Document document) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) context
                .getExternalContext().getContext();
        String path = servletContext.getRealPath("");
        try (
                InputStream inputStream = file1.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(path + File.separator + "resources" + File.separator + "events" + File.separator + getFilename(file1))) {
            document.setSubmittedFileName(getFilename(file1));
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while (true) {
                bytesRead = inputStream.read(buffer);
                if (bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                } else {
                    break;
                }
            }
        }

        return null;
    }

    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.  
            }
        }
        return "";
    }

    public Part getFileToUpdate() {
        return fileToUpdate;
    }

    public void setFileToUpdate(Part fileToUpdate) {
        this.fileToUpdate = fileToUpdate;
    }

    public void doModifierDocument(ActionEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                " util.Bundle", context.getViewRoot().getLocale());
        String msg;

        try {
            if (fileToUpdate == null) {
                msg = bundle.getString("IncorrectDocument");
                JsfUtil.addErrorMessage(msg);
            } else {
                System.out.println("nom du fichier pris: " + fileToUpdate.getName());
                System.out.println("document selectionne: " + documentCourant.getId() + " " + documentCourant.getFileName());

                // InputStream contenu = this.getContenuDocumentInputStream(documentCourant);
                String commentaire = "Modification effectué par " + connexionBean.getCurrentUser().getName()+ " ce " + JsfUtil.convertDate(new Date(), "EEEEE dd MMM yyyy 'A' HH:mm:ss") + " - " + this.getDocumentCmisByDocument(documentCourant).getDescription();
                System.out.println("comentaire-- " + commentaire);
                System.out.println("documentCourant.getId()-- " + documentCourant.getId());
                System.out.println("fileToUpdate.getContentType()-- " + fileToUpdate.getContentType());
                System.out.println("-----------------contenu---------------------");
                System.out.println("fileToUpdate.getInputStream()-- " + fileToUpdate.getInputStream());
                System.out.println("-----------------contenu---------------------");

                String ref = documentCourant.getGed().getRefGed();
                System.out.println("Document ref : " + ref);
                DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
                System.out.println("Document : " + doc);
                DocumentCmis documentCmis;

                //documentCmis = getDocumentCmisByDocument(documentCourant);
                documentCmis = gedFacade.enregistrerCopieModifiee(doc.getId(), null, fileToUpdate.getInputStream(), fileToUpdate.getContentType(), commentaire);
                System.out.println("documentCmis : " + documentCmis);
                Ged ged = new Ged(documentCmis.getId(), documentCmis.getNom(), documentCmis.getId());
                System.out.println("Document mise à jour : " + documentCourant);
                System.out.println("ged : " + ged);
                documentCourant.setGed(ged);
                documentFacade.edit(documentCourant);

                // fileToUpdate= null;
                msg = bundle.getString("MAJDocumentCreateSuccessMsg");
                JsfUtil.addSuccessMessage(msg);
            }
        } catch (Exception e) {
            System.out.println("erreur--" + e);
            msg = bundle.getString("MAJDocumentCreateErrorMsg");
            JsfUtil.addErrorMessage(msg);
        }
    }

//    public void doCancelLockedDocument() throws IOException, CmisOperationException {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ResourceBundle bundle;
//        bundle = ResourceBundle.getBundle(
//                " util.Bundle", context.getViewRoot().getLocale());
//        String msg;
//        try {
//            System.out.println("downloadDocument : " + documentCourant);
//            String ref = documentCourant.getGed().getRefGed();
//            System.out.println("Document ref : " + ref);
//            gedFacade.annulerVerrou(ref);
//           DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
//        
//            msg = bundle.getString("unlockedDocumentsuccessmsg");
//            JsfUtil.addErrorMessage(msg);
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
//            response.reset();
//
//            response.setContentType(doc.getDocument().getContentStreamMimeType());
//            response.setHeader("Content-Disposition", "attachment;filename=" + doc.getDocument().getName());
//            response.setContentLength((int) doc.getDocument().getContentStream().getLength());
//            BufferedInputStream buf = new BufferedInputStream(doc.getDocument().getContentStream().getStream());
//
//            response.getOutputStream().write(doc.contenuOctets());
//            response.getOutputStream().flush();
//            response.getOutputStream().close();
//            facesContext.responseComplete();
//            facesContext.renderResponse();
//            buf.close();
//        } catch (IOException ex) {
//            msg = bundle.getString("unlockedDocumenterrormsg " + ex);
//            JsfUtil.addErrorMessage(msg);
//        } finally {
//        }
//    }
    public void doCancelLockedDocument() throws IOException, CmisOperationException {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle;
        bundle = ResourceBundle.getBundle(
                " util.Bundle", context.getViewRoot().getLocale());
        String msg;
        try {
            System.out.println("downloadDocument : " + documentCourant);
            String ref = documentCourant.getGed().getRefGed();
            System.out.println("Document ref : " + ref);
            gedFacade.annulerVerrou(ref);
            //msg = bundle.getString("unlockedDocumentsuccessmsg");
            msg = "Le document a été correctement déverrouillé ";
            JsfUtil.addSuccessMessage(msg);

        } catch (Exception e) {
            // msg = bundle.getString("unlockedDocumenterrormsg");
            msg = "Le document n'a pas pu être déverrouillé ";
            JsfUtil.addErrorMessage(msg);
        }
    }

    public InputStream getContenuDocumentInputStream(Document document) {
        try {
            if (document == null) {
                return null;
            }
            System.out.println("getContenuDocument : " + document);
            String ref = document.getGed().getRefGed();
            System.out.println("Document ref : " + ref);
            DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
            System.out.println("Document : " + doc);

//            return doc.contenuOctets();
            return doc.contenu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public DocumentCmis getDocumentCmisByDocument(Document document) {
        try {
            if (document == null) {
                return null;
            }
            System.out.println("getContenuDocument : " + document);
            String ref = document.getGed().getRefGed();
            System.out.println("Document ref : " + ref);
            DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
            System.out.println("Document : " + doc);

//            return doc.contenuOctets();
            return doc;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /*public void passDocumentForArchive(Document item) {
     selectedDocument = item;
     System.out.println("Sélectionne un document=" + selectedDocument);
     }

     public void doArchiveDocument(ActionEvent event) {
     FacesContext context = FacesContext.getCurrentInstance();
     ResourceBundle bundle;
     bundle = ResourceBundle.getBundle(
     " util.Bundle", context.getViewRoot().getLocale());
     String msg;

     try {
     System.out.println("le document pris est: "+selectedDocument.getId());
     DocumentCmis doc=verrouillerMonDocument(selectedDocument);
     documentFacade.archiveDocument(selectedDocument);
     // fileToUpdate= null;
     msg = bundle.getString("DocumentArchiveSuccessMsg");
     JsfUtil.addSuccessMessage(msg);

     } catch (Exception e) {
     System.out.println("erreur--" + e);
     msg = bundle.getString("DocumentArchiveErrorMsg");
     JsfUtil.addErrorMessage(msg);
     }
     }*/
    public DocumentCmis verrouillerMonDocument(Document monDoc) {
        System.out.println("downloadDocument : " + monDoc.getId());
        String ref = monDoc.getGed().getRefGed();
        System.out.println("Document ref : " + ref);
        DocumentCmis doc = (DocumentCmis) gedFacade.verrouillerPourModification(ref);
        System.out.println("Document : " + doc);

        Ged ged = new Ged(doc.getId(), doc.getNom(), doc.getId());
        monDoc.setGed(ged);
        documentFacade.edit(monDoc);
        return doc;
    }

    //Reynauld 
    public void telechargerDocumentReyno(Document item) throws IOException {
        documentCourant = item;
        System.out.println("renter ds method" + documentCourant);
        try {
            if ((documentCourant != null) && (documentCourant.getGed() != null)) {
                System.out.println("downloadDocument : " + documentCourant.getFileName());
                String ref = documentCourant.getGed().getRefGed();
                System.out.println("Document ref : " + ref);
                DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
                System.out.println("Document : " + doc);

                FacesContext facesContext = FacesContext.getCurrentInstance();
                //  System.out.println("1**********");
                HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
                // System.out.println("2**********");
                response.reset();
                // System.out.println("1**********");
                response.setContentType(doc.getDocument().getContentStreamMimeType());
                //System.out.println("3**********"+doc.getDocument().getContentStreamMimeType());
                response.setHeader("Content-Disposition", "attachment;filename=" + doc.getDocument().getName());
                // System.out.println("4**********"+doc.getDocument().getName());
                response.setContentLength((int) doc.getDocument().getContentStream().getLength());
                //System.out.println("5**********"+(int) doc.getDocument().getContentStream().getLength());
                BufferedInputStream buf = new BufferedInputStream(doc.getDocument().getContentStream().getStream());
                //System.out.println("6**********"+buf);
                //System.out.println("6**********"+doc.contenuOctets());

//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            output.write(doc.contenuOctets());
//
//            String fileName = parametreFacade.getAppDir().getPath() + File.separator + documentCourant.getFileName();
//            System.out.println("fileName = " + fileName);
//            FileOutputStream f = new FileOutputStream(fileName);
//            f.write(doc.contenuOctets());
//            f.close();
//
//            int readBytes = 0;
//            while ((readBytes = buf.read()) != 1) {
//                response.getOutputStream().write(readBytes);
//            }
                response.getOutputStream().write(doc.contenuOctets());
                // System.out.println("7**********");
                response.getOutputStream().flush();
                // System.out.println("8**********");
                response.getOutputStream().close();
                // System.out.println("9**********");
                facesContext.responseComplete();
                //System.out.println("10**********");
                facesContext.renderResponse();
                //System.out.println("11**********");
                buf.close();
                System.out.println("12**********");
            }

        } catch (IOException ex) {
            System.out.println("EErreur est dowload "+ex);
        } finally {
        }
//        String fileName = doc.getNom();
//
//        String tempFileName = String.format("%s_%d_%s", ref, ThreadLocalRandom.current().nextLong(), fileName);
//        String filePath = parametreFacade.getTempDirPath() + tempFileName;
//        Files.write(Paths.get(filePath), doc.contenuOctets(), CREATE, TRUNCATE_EXISTING, WRITE, DELETE_ON_CLOSE);
//        fileLink = filePath;

        /*
         ByteArrayOutputStream output = new ByteArrayOutputStream();
         output.write(doc.contenuOctets());
        
         String fileName = parametreFacade.getAppDir().getPath() + File.separator + documentCourant.getFileName();
         System.out.println("fileName = " + fileName);
         FileOutputStream f =new FileOutputStream(fileName);
         f.write(doc.contenuOctets());
         f.close();               

         fileLink = fileName;
         System.out.println("fileLink = " + fileLink);
         //        System.out.println("OutputStream ( " + output.size() + ") : " + output);
         //        try {
         //            JsfUtil.sendFile(output, documentCourant.getFileName());
         //        } catch (Exception ex) {
         //            Logger.getLogger(GedBean.class.getName()).log(Level.SEVERE, null, ex);
         //        }
         */
    }

//    public List<Annee> getListeAnneesPourDossier() {
//        List<Annee> list = dossierFacade.findAllAnneesForDossier();
//        List<String> list=dossierFacade.findAllAnneesForDossierUpdate();
//        for (Annee a : list) {
//            System.out.println(" annnee desc " + a.getValeur());
//            listeDossiersExplorateur = progressionDossierFacade.getListeDossiersAnnee(a.getValeur(), contextBean.getCurrentExerciceFonction().getService());
//            System.out.println(" liste dossiers Explorateur " + listeDossiersExplorateur);
////            for (Dossier d : listeDossiersExplorateur) {
////                listeDocuments = dossierFacade.findDocumentsByDossier(d);
////                System.out.println(" taille document Explorateur bis " + listeDocuments.size());
////                System.out.println(" liste document Explorateur bis " + listeDocuments);
////            }
//
//        }
//
//        return list;
//    }
  

   

    public void resultItem() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();

            //String idDossier = facesContext.getExternalContext().getRequestParameterMap().get("idDossier");
            String idDocument = facesContext.getExternalContext().getRequestParameterMap().get("idDocument");
            System.out.println(" totototo rentre tototooto ");
            System.out.println(" idDocument " + idDocument);
            //System.out.println(" idDossier "+idDossier);
            //dossierCourant = dossierFacade.find(idDossier);
            // System.out.println(" dossiers courant details " + dossierCourant);
            documentCourant = documentFacade.find(idDocument);
            System.out.println(" document courant details " + documentCourant);
        } catch (Exception e) {
            System.err.println("erreur ");
        }

    }

    public void telechargerDocumentPV(Document document) throws IOException {
        System.out.println("renter ds method");
        System.out.println("le document de tableau de bord " + document);
        System.out.println("le document 2 de tableau de bord " + document.getGed());
        try {
            if ((document != null) && (document.getGed() != null)) {
                System.out.println("downloadDocument : " + document.getFileName());
                String ref = document.getGed().getRefGed();
                System.out.println("Document ref : " + ref);
                DocumentCmis doc = (DocumentCmis) gedFacade.trouverObjet(ref);
                System.out.println("Document : " + doc);

                FacesContext facesContext = FacesContext.getCurrentInstance();
                //  System.out.println("1**********");
                HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
                // System.out.println("2**********");
                response.reset();
                // System.out.println("1**********");
                response.setContentType(doc.getDocument().getContentStreamMimeType());
                //System.out.println("3**********"+doc.getDocument().getContentStreamMimeType());
                response.setHeader("Content-Disposition", "attachment;filename=" + doc.getDocument().getName());
                // System.out.println("4**********"+doc.getDocument().getName());
                response.setContentLength((int) doc.getDocument().getContentStream().getLength());
                //System.out.println("5**********"+(int) doc.getDocument().getContentStream().getLength());
                BufferedInputStream buf = new BufferedInputStream(doc.getDocument().getContentStream().getStream());
                //System.out.println("6**********"+buf);
                //System.out.println("6**********"+doc.contenuOctets());

//            ByteArrayOutputStream output = new ByteArrayOutputStream();
//            output.write(doc.contenuOctets());
//
//            String fileName = parametreFacade.getAppDir().getPath() + File.separator + documentCourant.getFileName();
//            System.out.println("fileName = " + fileName);
//            FileOutputStream f = new FileOutputStream(fileName);
//            f.write(doc.contenuOctets());
//            f.close();
//
//            int readBytes = 0;
//            while ((readBytes = buf.read()) != 1) {
//                response.getOutputStream().write(readBytes);
//            }
                response.getOutputStream().write(doc.contenuOctets());
                // System.out.println("7**********");
                response.getOutputStream().flush();
                // System.out.println("8**********");
                response.getOutputStream().close();
                // System.out.println("9**********");
                facesContext.responseComplete();
                //System.out.println("10**********");
                facesContext.renderResponse();
                //System.out.println("11**********");
                buf.close();
                System.out.println("12**********");
            }

        } catch (IOException ex) {
            System.out.println("erreur telecharger document ");
        } finally {
        }
    }

    public String redirection() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        String iddoc = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        System.out.println(" Id ---" + iddoc);
        documentCourant = documentFacade.find(iddoc);
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.sendRedirect("/forsetiPenal/GED/explorateur_1/index.xhtml");
        return iddoc;
    }
    
    
    
        
public void test(Document doc){
    System.out.println(" le document est "+doc.getFileName());
}
 
}

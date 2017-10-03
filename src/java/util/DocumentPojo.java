/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import jpa.Document;

/**
 *
 * @author Ordinateur
 */
public class DocumentPojo {

    private String id;
    private String submittedFileName;
    private String fileName;
    private String mimeType;
    private String taille;
    private String refDocument;
    private String description;

    public DocumentPojo() {
    }

    public DocumentPojo(Document d) {
        this.id = d.getId();
        this.fileName = d.getFileName();
        this.taille = d.toString();
        this.description = d.getDescription();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubmittedFileName() {
        return submittedFileName;
    }

    public void setSubmittedFileName(String submittedFileName) {
        this.submittedFileName = submittedFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getTaille() {
        return taille;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getRefDocument() {
        return refDocument;
    }

    public void setRefDocument(String refDocument) {
        this.refDocument = refDocument;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

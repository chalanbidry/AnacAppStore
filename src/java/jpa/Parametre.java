package jpa;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Parametre implements Serializable {

    private String code;
    private String valeur;
    private String type;
    private String description;

    public Parametre() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getValueAsObject() {        
        switch (this.type) {
            case "java.lang.Boolean":
                return Boolean.parseBoolean(this.valeur);
            case "java.lang.Integer":
                return Integer.parseInt(this.valeur);
            case "java.lang.Double":
                return Double.parseDouble(this.valeur);            
        }        
        return this.valeur;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
            append("nom", code).
            append("valeur", valeur).
            build();
    }

    public void reset() {
        this.code = "";
        this.valeur = "";
        this.type = null;
        this.description = null;
    }
}

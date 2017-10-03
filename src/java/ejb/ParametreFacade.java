/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import jpa.Parametres;
import jpa.Parametre;
import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Gildasdarex
 */
@Singleton
public class ParametreFacade {
    
//    @Resource(lookup = "Ged/appdir") 
    //@Resource(lookup = "forsetiV2/appdir")
    String appDir;
    private String filePath;
    /**
     * nom du fichier de parametre.
     */
    static final String fileName = "config.xml";
    
    /**
     * Liste en mémoire des paramètres de l'application. Est initialisé au
     * "démarrage" de l'application.
     */
    List<Parametre> params;

    @PostConstruct
    public void init() {        
        filePath = appDir + "/" + fileName;
        params = findAll();
    }

    public File getAppDir() {
        return new File(appDir);
    }
    
    public List<Parametre> findAll() {
        return params = deserializeXml(filePath);
    }

    private List<Parametre> deserializeXml(String filePhath) {

        File f = new File(filePhath);
        List<Parametre> liste;
        if (f.exists() && !f.isDirectory()) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Parametres.class);
                Unmarshaller jaxbUnmarchaller = jaxbContext.createUnmarshaller();
                Parametres parametres = (Parametres) jaxbUnmarchaller.unmarshal(f);

                liste = parametres.getListe();
            } catch (JAXBException ex) {
                Logger.getLogger(ParametreFacade.class.getName()).log(Level.SEVERE, null, ex);
                liste = new LinkedList<>();
            }

        } else {
            liste = new LinkedList<>();
        }
        return liste;
    }

    private void serializeXml(List<Parametre> listeParametres, String filePath) {
        try {
            Parametres parametres = new Parametres();
            parametres.setListe(listeParametres);

            JAXBContext jaxbContext = JAXBContext.newInstance(Parametres.class);
            Marshaller jaxMarshaller = jaxbContext.createMarshaller();
            jaxMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxMarshaller.marshal(parametres, new File(filePath));
        } catch (JAXBException ex) {
            Logger.getLogger(ParametreFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void create(Parametre newParametre) {
        //List<Parametre> liste = findAll();
        if (params == null) {
            params = new LinkedList<>();
        }
        params.add(newParametre);

        serializeXml(params, filePath);
    }

    public void edit(Parametre selectedParametre) {
        if (params != null) {
            for (Parametre p : params) {
                if (p.getCode().equals(selectedParametre.getCode())) {
                    params.remove(p);
                    params.add(selectedParametre);
                    serializeXml(params, filePath);
                    break;
                }
            }
        }
    }

    public void remove(Parametre selectedParametre) {
        if (params != null) {
            for (Parametre p : params) {
                if (p.getCode().equals(selectedParametre.getCode())) {
                    params.remove(p);
                    serializeXml(params, filePath);
                    break;
                }
            }
        }
    }

    public Parametre getParameter(String code) {
        //List<Parametre> liste = deserializeXml(filePath);
        for (Parametre p : params) {
            if (p.getCode().equals(code)) {
                return p;
            }
        }
        return null;
    }

    public Object getParameterValue(String code) {
        Parametre p = getParameter(code);
        if(p != null)
            return p.getValueAsObject();
        else{
            System.out.println("Le paramère '" + code + "' n'est pas défini.");
            return null;
        }
    }
    
    public boolean isSet(String code) {
        return getParameter(code) != null;
    }
    
    public int getIntValue(String code) {
        return (Integer) getParameterValue(code);
    }
    
    public boolean getBooleanValue(String code) {
        return (Boolean) getParameterValue(code);        
    }
    
    public String getStringValue(String code) {
        return (String) getParameterValue(code);
    }
    
    public Double getDoubleValue(String code) {
        return (Double) getParameterValue(code);
    }
    
    public URL getUrlValue(String  code) {
        return (URL) getParameterValue(code);
    }

    public List<Parametre> getParameters() {
        return params;
    }
    
    //----------- PARAMETRES D'APPLICATION ---------------------// 
    
    public String getCodeChambreDistribution(){
        return this.getParameter("dis.ch.code").getValeur();
    }

}

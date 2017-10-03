/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;


import ged.ejb.GedFacade;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import jpa.Utilisateur;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author Ordinateur
 */
@Stateless
public class ConnexionFacade {

    Logger logger = Logger.getLogger(ConnexionFacade.class);
    /**
     * nom du paramètre de l'URL du service d'entrepôt CMIS dans la base de
     * données.
     */
//    static final String cmisSvcUrlParam = "cmis.service.url";
    /**
     * L'URL du service CMIS
     */
//    String cmisURL;

    /**
     * nom d'utilisateur de connexion.
     */
//    String cmisUsername;
    /**
     * mot de passe de connexion.
     */
//    String cmisPassword;
    @Inject
    private UtilisateurFacade utilisateurFacade;
 @Inject 
 private ContextFacade contextFacade;

    public ConnexionFacade() {
    }
  
     public Utilisateur initCurrentUser(String login) {
        Utilisateur currentUser = utilisateurFacade.find(login);
        if (currentUser == null) {
//            try {
//                JsfUtil.logInfo(logger, "######### initCurrentUser : login = " + login);
////                UtilisateurLdap ldapUser = (UtilisateurLdap) ldapFacade.trouver(login);
////                currentUser = ldapFacade.convertLdapUserToSimpleUser(ldapUser);
//            } catch (LdapGenericException e) {
//                JsfUtil.logError(logger, e);
//            }
        }
        JsfUtil.logInfo(logger, "Login : " + currentUser.getLogin() + " mdp : " + currentUser.getPassword());
        return currentUser;
    }
      /**
     * méthode qui retourne la liste des exerccefonctions
     *
     * @param currentUser
     * @return
     */
//   
      /**
     * méthode qui retourne un current exericce fonction
     *
     * @param listCurrentExerciceFonction
     * @return
     */

    /**
     * initialise les paramètres ci-dessus et assigne 'true' au champs
     * <code>ready</code> lorsque l'initilisation se fait avec succès.
     * @param currentUser
     * @return 
     */
    public GedFacade initGed(Utilisateur currentUser) {
        JsfUtil.logInfo(logger, "*** initGed - BEGIN ***");
        GedFacade gedFacade = null;
//        loadConfig();
        System.out.println("contextFacade ==> " + contextFacade);
        System.out.println("contextBean.getCmisURL() ==> " );
//        contextFacade.initParametres();
        String cmisURL = contextFacade.getCmisURL();
//        String cmisURL = contextBean.getCmisURL();
        System.out.println("cmisURL ==> " + cmisURL);
         System.out.println("user  " +currentUser.getLogin());
        String cmisUsername =currentUser.getLogin();
//        String cmisPassword = currentUser.getMdp();
         String cmisPassword = "password";
        //System.out.printf("***PARAMETRES CMIS INITIALISES*** %s, %s, %s", cmisUsername, cmisPassword, cmisURL);
        JsfUtil.logInfo(logger, String.format("***PARAMETRES CMIS INITIALISES*** %s, %s, %s", cmisUsername, cmisPassword, cmisURL));
        SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
        Map<String, String> parameters = new HashMap<>();
        //User credentials.
        parameters.put(SessionParameter.USER, cmisUsername);
        parameters.put(SessionParameter.PASSWORD, cmisPassword);

        // Connection settings.
        parameters.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        System.out.println("initGed cmisURL ==> " + cmisURL);
        parameters.put(SessionParameter.ATOMPUB_URL, cmisURL);  //URL to your CMIS server.
        parameters.put(SessionParameter.AUTH_HTTP_BASIC, "true");
        parameters.put(SessionParameter.COOKIES, "true");
        System.out.println("initGed parameters ==> " + parameters);
        //Create session.
        //Alfresco only provides one repository.
        Repository repository = sessionFactory.getRepositories(parameters).get(0);
        Session sessionCmis = repository.createSession();
        gedFacade = new GedFacade(sessionCmis);
        JsfUtil.logInfo(logger, "*** initGed - END ***");
        return gedFacade;
    } 
}

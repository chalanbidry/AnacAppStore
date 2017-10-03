/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.Utilisateur;
import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author MJLDH
 */
@FacesConverter("utilisateurConverter")
public class UtilisateurConverter implements Converter {

    private static Map<String, Utilisateur> cache = new HashMap<String, Utilisateur>();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return cache.get(value.trim());
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        String login = null;
        if (value instanceof Utilisateur) {
            Utilisateur model = (Utilisateur) value;
            login = String.valueOf(model.getLogin());
            if (login != null) {
                cache.put(login, model);
            }
        }
        return login;
    }
}

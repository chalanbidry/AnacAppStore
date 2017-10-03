/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.GroupeUtilisateur;
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
@FacesConverter("groupeUtilisateurConverter")
public class GroupeUtilisateurConverter implements Converter {

    private static Map<String, GroupeUtilisateur> cache = new HashMap<String, GroupeUtilisateur>();

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
        if (value instanceof GroupeUtilisateur) {
            GroupeUtilisateur model = (GroupeUtilisateur) value;
            login = String.valueOf(model.getId());
            if (login != null) {
                cache.put(login, model);
            }
        }
        return login;
    }
}

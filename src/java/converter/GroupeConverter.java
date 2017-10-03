/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.Groupe;
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
@FacesConverter("groupeConverter")
public class GroupeConverter implements Converter {

    private static Map<String, Groupe> cache = new HashMap<String, Groupe>();

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
        if (value instanceof Groupe) {
            Groupe model = (Groupe) value;
            login = String.valueOf(model.getCn());
            if (login != null) {
                cache.put(login, model);
            }
        }
        return login;
    }
}

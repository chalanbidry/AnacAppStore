/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.Notification;
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
@FacesConverter("notificationConverter")
public class NotificationConverter implements Converter {

    private static Map<String, Notification> cache = new HashMap<String, Notification>();

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
        String Libelle = null;
        if (value instanceof Notification) {
            Notification model = (Notification) value;
            Libelle = String.valueOf(model.getLibelle());
            if (Libelle != null) {
                cache.put(Libelle, model);
            }
        }
        return Libelle;
    }
}

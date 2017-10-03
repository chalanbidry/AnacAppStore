/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.Prestataire;
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
@FacesConverter("prestataireConverter")
public class PrestataireConverter implements Converter {

    private static Map<String, Prestataire> cache = new HashMap<String, Prestataire>();

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
        String Code = null;
        if (value instanceof Prestataire) {
            Prestataire model = (Prestataire) value;
            Code = String.valueOf(model.getIFU());
            if (Code != null) {
                cache.put(Code, model);
            }
        }
        return Code;
    }
}

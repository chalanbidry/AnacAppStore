/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.Demande;
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
@FacesConverter("demandeConverter")
public class DemandeConverter implements Converter {

    private static Map<String, Demande> cache = new HashMap<String, Demande>();

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
        if (value instanceof Demande) {
            Demande model = (Demande) value;
            Code = String.valueOf(model.getCode());
            if (Code != null) {
                cache.put(Code, model);
            }
        }
        return Code;
    }
}

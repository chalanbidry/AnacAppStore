/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.Departement;
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
@FacesConverter("departementConverter")
public class DepartementConverter implements Converter {

    private static Map<String, Departement> cache = new HashMap<String, Departement>();

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
        if (value instanceof Departement) {
            Departement model = (Departement) value;
            Code = String.valueOf(model.getCode());
            if (Code != null) {
                cache.put(Code, model);
            }
        }
        return Code;
    }
}

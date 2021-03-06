/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.TypeIntervention;
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
@FacesConverter("typeInterventionConverter")
public class TypeInterventionConverter implements Converter {

    private static Map<String, TypeIntervention> cache = new HashMap<String, TypeIntervention>();

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
        String Id = null;
        if (value instanceof TypeIntervention) {
            TypeIntervention model = (TypeIntervention) value;
            Id = String.valueOf(model.getId());
            if (Id != null) {
                cache.put(Id, model);
            }
        }
        return Id;
    }
}

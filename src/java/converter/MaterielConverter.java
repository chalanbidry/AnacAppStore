/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;


import java.util.HashMap;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import jpa.Materiel;

/**
 *
 * @author MJLDH
 */
@FacesConverter("materielConverter")
public class MaterielConverter implements Converter {

    private static Map<String, Materiel> cache = new HashMap<String, Materiel>();

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
        if (value instanceof Materiel) {
            Materiel model = (Materiel) value;
            Code = String.valueOf(model.getNumero());
            if (Code != null) {
                cache.put(Code, model);
            }
        }
        return Code;
    }
}

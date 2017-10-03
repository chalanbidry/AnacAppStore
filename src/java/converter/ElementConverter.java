/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.Element;
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
@FacesConverter("elementConverter")
public class ElementConverter implements Converter {

    private static Map<String, Element> cache = new HashMap<String, Element>();

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
        if (value instanceof Element) {
            Element model = (Element) value;
            Libelle = String.valueOf(model.getLibelle());
            if (Libelle != null) {
                cache.put(Libelle, model);
            }
        }
        return Libelle;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.CategorieCourrier;
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
@FacesConverter("categorieCourrierConverter")
public class CategorieCourrierConverter implements Converter {

    private static Map<String, CategorieCourrier> cache = new HashMap<String, CategorieCourrier>();

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
        String code = null;
        if (value instanceof CategorieCourrier) {
            CategorieCourrier model = (CategorieCourrier) value;
            code = String.valueOf(model.getLabel());
            if (code != null) {
                cache.put(code, model);
            }
        }
        return code;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.Application;
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
@FacesConverter("applicationConverter")
public class ApplicationConverter implements Converter {

    private static Map<String, Application> cache = new HashMap<String, Application>();

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
        if (value instanceof Application) {
            Application model = (Application) value;
            code = String.valueOf(model.getCode());
            if (code != null) {
                cache.put(code, model);
            }
        }
        return code;
    }
}

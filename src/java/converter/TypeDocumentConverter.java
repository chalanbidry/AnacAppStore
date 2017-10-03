/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.TypeDocument;
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
@FacesConverter("typeDocumentConverter")
public class TypeDocumentConverter implements Converter {

    private static Map<String, TypeDocument> cache = new HashMap<String, TypeDocument>();

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
        if (value instanceof TypeDocument) {
            TypeDocument model = (TypeDocument) value;
            Code = String.valueOf(model.getId());
            if (Code != null) {
                cache.put(Code, model);
            }
        }
        return Code;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import jpa.LigneBudgetaire;
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
@FacesConverter("ligneBudgetaireConverter")
public class LigneBudgetaireConverter implements Converter {

    private static Map<String, LigneBudgetaire> cache = new HashMap<String, LigneBudgetaire>();

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
        if (value instanceof LigneBudgetaire) {
            LigneBudgetaire model = (LigneBudgetaire) value;
            Libelle = String.valueOf(model.getId());
            if (Libelle != null) {
                cache.put(Libelle, model);
            }
        }
        return Libelle;
    }
}

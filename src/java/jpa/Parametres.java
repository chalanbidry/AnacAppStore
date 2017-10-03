/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.util.List;
import java.util.Optional;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author HP
 */
@XmlRootElement//(name = "parametres")
public class Parametres {

    public Parametres() {
    }

    private List<Parametre> liste;

    @XmlElement(name = "parametre")
    public List<Parametre> getListe() {
        return liste;
    }

    public void setListe(List<Parametre> liste) {
        this.liste = liste;
    }
    
    public void add(Parametre p) {
        liste.add(p);
    }
    
    public Parametre get(String code) {
        Optional<Parametre> opt = liste.stream().filter(e -> e.getCode().equals(code)).findAny();
        return opt.orElse(null);
    }
    
    public void set(Parametre p) {
        Optional<Parametre> opt = liste.stream().filter(e -> e.getCode().equals(p.getCode())).findAny();
        opt.ifPresent((Parametre e) -> { e.setType(p.getType()); 
                                        e.setDescription(p.getDescription());
                                        e.setValeur(p.getValeur());
                                    });
    }
    
    public void remove(String code) {
        for(Parametre p : liste) {
            if(p.getCode().equals(code)) {
                liste.remove(p);
                break;
            }
        }
    }
}

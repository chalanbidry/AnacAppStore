/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import controller.ConnexionBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import jpa.Demande;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Ged;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class GedFacade2 extends AbstractFacade<Ged> {

    Logger logger = Logger.getLogger(Ged.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
   
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GedFacade2() {
        super(Ged.class);
    }


}

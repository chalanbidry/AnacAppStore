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
import jpa.TypeDocument;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.Application;
import jpa.Ged;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class TypeDocumentFacade extends AbstractFacade<TypeDocument> {

    Logger logger = Logger.getLogger(TypeDocumentFacade.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
   
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TypeDocumentFacade() {
        super(TypeDocument.class);
    }

//    public String giveId(TypeDocument dossierParent){
//        if(dossierParent==null){
//            dossierParent=getMainFolder();
//        }
//        int size=getAllFolderInFolder(dossierParent).size();
//        String id="",inc="";
//        if(size!=0 && size!=9999){
//         int taille=String.valueOf(size+1).length();
//         for(int i=1;i<=(4-taille);i++){
//         inc=inc+"0";
//         }
//         inc=inc+(String.valueOf(size+1));
//  
//        id=dossierParent.getId()+"-2101"+JsfUtil.getCurrentYear()+inc;
//        }else if(size!=9999){
//        inc="1";
//        id=dossierParent.getId()+"-2101"+JsfUtil.getCurrentYear()+"000"+inc;
//        }
//       return id;
//    }
   

//    @Override
    public void createTypeDoc(TypeDocument entity) {
//        String id=giveId(dossierParent);
//        entity.setId(id);
      
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @param application
     * @return liste des utilisateurs
     */
 public List<TypeDocument> getAllTypeByApp(Application application) {
        Query q = getEntityManager().createQuery("select d from TypeDocument d where d.application=:application");
        q.setParameter("application", application);
        List<TypeDocument> dossiersFind = q.getResultList();

        // avoid returing null to managed beans
        if (dossiersFind == null) {
            dossiersFind = new ArrayList<>();
        }

        // return the suggestions
        return dossiersFind;

    }
 

}

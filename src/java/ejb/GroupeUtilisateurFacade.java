/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.ejb.EJB;
import javax.inject.Inject;
import jpa.GroupeUtilisateur;
import org.apache.log4j.Logger;
import util.JsfUtil;

/**
 *
 * @author utilisateur
 */
@Stateless
public class GroupeUtilisateurFacade extends AbstractFacade<GroupeUtilisateur> {

    Logger logger = Logger.getLogger(GroupeUtilisateur.class);

    @PersistenceContext(unitName = "PrestAnacPU")
    private EntityManager em;
  
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupeUtilisateurFacade() {
        super(GroupeUtilisateur.class);
    }

        public String giveId(){
     
        
        GroupeUtilisateur lastGroupUser=getLastGoupUser();
        String idLast=lastGroupUser.getId();
        String lastPart=idLast.substring(8);
        int position=Integer.valueOf(lastPart);
        String id="",inc="";
        if(lastGroupUser != null && position!=999999){
         int taille=String.valueOf(position+1).length();
         for(int i=1;i<=(6-taille);i++){
         inc=inc+"0";
         }
         inc=inc+(String.valueOf(position+1));
  
        id="1101"+JsfUtil.getCurrentYear()+inc;
        }else if(position!=999999){
        inc="1";
        id="1101"+JsfUtil.getCurrentYear()+"00000"+inc;
        }
       return id;
    }

    @Override
    public void create(GroupeUtilisateur entity) {
        System.out.println("le id est "+giveId());
        entity.setId(giveId());
        getEntityManager().persist(entity);
    }

    /**
     * trouve les exercices fonctions de l'utilisateur
     *
     *
     * @return liste des utilisateurs
     */
    public GroupeUtilisateur getLastGoupUser() {
        Query q = getEntityManager().createQuery("select GU from GroupeUtilisateur GU where GU.dateCreation >= ALL (SELECT GU1.dateCreation FROM GroupeUtilisateur GU1)");
        // set parameters
        List<GroupeUtilisateur> suggestions = q.getResultList();

        // avoid returing null to managed beans
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }

        // return the suggestions
        return suggestions.get(0);

    }

}

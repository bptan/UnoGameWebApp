/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sa42.uno.web.business;


import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sa42.uno.web.model.Users;

/**
 *
 * @author BP
 */
@Stateless
public class UserManager {

    //public static final String GROUP = "NormalUser";
    private static final String FIND_USER
            = "select u from users u where u.userid = :username";

    private static final String REGISTER_USER = "insert u from users u";

    @PersistenceContext
    private EntityManager em;
    
   
    private int checkUsernameInUse(String username) {

        return 0;
    }

    public Boolean registerUser(String username, String password) {
        Users u = new Users();
        u.setUsername(username);
        u.setPassword(password);
        try{
            em.persist(u);
            //groupMgr.addToGroup(GROUP, username);
            em.getEntityManagerFactory().getCache()
				.evict(Users.class, u.getUsername());
            return true;
        }catch(EntityExistsException ex){
            return false;
        }        
        
    }

}

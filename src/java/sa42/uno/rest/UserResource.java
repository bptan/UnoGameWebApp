/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sa42.uno.rest;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sa42.uno.web.business.UserManager;

/**
 *
 * @author BP
 */
@RequestScoped
@Path("/user")
public class UserResource {
    @EJB UserManager mgr;
    
    
    
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces("text/plain")
    public Response register(
            @FormParam("username")String username,
            @FormParam("password")String password){
        Boolean result = mgr.registerUser(username, password);
        
        if(result){
            return (Response.ok(username).build());
        }else{
            return (Response.noContent().build());
        }       
    }
    
}

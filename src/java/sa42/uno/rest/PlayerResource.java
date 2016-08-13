/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sa42.uno.rest;

import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sa42.uno.model.Game;
import sa42.uno.model.Player;
import sa42.uno.web.business.GameManager;

/**
 *
 * @author BP
 */
@RequestScoped
@Path("/games")
public class PlayerResource {
    
    @Inject private GameManager mgr;
    
    @GET
    @Path("/{gid}")
    public Response joinGame(@PathParam("gid") String gameId,
            @QueryParam("username") String username) {

        Optional<Game> opt = mgr.getOneGame(gameId);
        if (!opt.isPresent()) {
            return (Response.status(Response.Status.NOT_FOUND)
                    .build());
        }
 
        Game game = opt.get();
        if(game.getStatus()!=Game.Status.Waiting){
            return(Response.serverError().build());
        }
        Player p = new Player(username);
        game.addPlayer(p);
        
        return (Response.ok().build());
    }
    
    @GET
    @Path("{gid}/players/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewHand(@PathParam("gid")String gameId,@PathParam("name")String username){
        
        Optional<Game> opt = mgr.getOneGame(gameId);
        if (!opt.isPresent()){
            return (Response.status(Response.Status.BAD_REQUEST).build());
        }
        Game game = opt.get(); 
 
        Optional<Player> optPlayer = game.getPlayer(username);
        if (!optPlayer.isPresent()){
            return (Response.status(Response.Status.NOT_FOUND).build());
        }
        Player player = optPlayer.get();
        
        return (Response.ok(player.toJsonHandOnly()).build());
      
    }
    
}

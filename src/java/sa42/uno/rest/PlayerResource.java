/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sa42.uno.rest;

import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
//import javax.ws.rs.
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;

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
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    @Inject
    private GameManager mgr;
    
    

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/{gid}/players")
    public Response joinGame(@PathParam("gid") String gameId,
            @FormParam("username") String username) {

        Optional<Game> opt = mgr.getOneGame(gameId);
        if (!opt.isPresent()) {
            return (Response.status(Response.Status.NOT_FOUND)
                    .build());
        }

        Game game = opt.get();
        Map<String, Player> players = game.getPlayersMAP();

        //cannot join but may navigate to game if already joined
        if (game.getStatus() != Game.Status.Waiting) {
            //check if player is in game
            if (players.get(username) != null) {
                return (Response.ok().build());
            } else {
                return (Response.status(Response.Status.UNAUTHORIZED)
                        .build());
            }
        } else {
            //still can join
            System.out.println(players.toString());
            if (players.get(username) == null) {
                Player p = new Player(username);
                game.addPlayer(p);                
            }
            return (Response.ok().build());
        }
    }

    @GET
    @Path("{gid}/players/{name}")
    public Response viewHand(@PathParam("gid") String gameId, @PathParam("name") String username) {

        Optional<Game> opt = mgr.getOneGame(gameId);
        if (!opt.isPresent()) {
            return (Response.status(Response.Status.BAD_REQUEST).build());
        }
        Game game = opt.get();
        System.out.println(game.getStatus().toString());
        Optional<Player> optPlayer = game.getPlayer(username);
        if (!optPlayer.isPresent()) {
            return (Response.status(Response.Status.NOT_FOUND).build());
        }
        Player player = optPlayer.get();

        return (Response.ok(player.toJsonHandOnly()).build());

    }

}

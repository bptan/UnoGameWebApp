/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sa42.uno.rest;

import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import sa42.uno.model.Game;
import sa42.uno.web.business.GameManager;

/**
 *
 * @author BP
 */
@RequestScoped
@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {
    
    @Inject
    private GameManager mgr;
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response create(@FormParam("title")String title) {        
        mgr.create(title);
        return Response.ok().build();             
    }
    
    @GET
    @Path("/all")
    public Response getAll() {
        System.out.println("getallgames");
        Map<String, Game> games = mgr.getGames();        
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        games.values().stream()
                .map(g -> {
                    return (g.toJson());
                }).forEach(j -> {
            arrBuilder.add(j);
        });
        
        return (Response.ok(arrBuilder.build()).build());
    }
    
    @GET
    public Response browsemany(@QueryParam("username") String name) {
        Map<String, Game> games = mgr.browseAvailableGames(name);        
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        games.values().stream()
                .map(g -> {
                    return (g.toJson());
                }).forEach(j -> {
            arrBuilder.add(j);
        });

        return (Response.ok(arrBuilder.build()).build());
    }
    
    @PUT
    @Path("/{id}")
    public JsonObject start(@PathParam("id")String id) {
        Game game = mgr.start(id);
     
        return game.toGameTableJson();
    }
    /*
    @GET
    @Path("/get/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getGame(@PathParam("id")String id){
        Optional<Game> opt = mgr.getOneGame(id);
        if (!opt.isPresent()){
            return (Response.status(Response.Status.BAD_REQUEST).header(
                "Access-Control-Allow-Origin",PLAYERVIEW_URL).build());
        }
        Game game = opt.get();
        
        return (Response.ok(game.toJson()).header(
                "Access-Control-Allow-Origin",PLAYERVIEW_URL).build());
        
    }
    */
}

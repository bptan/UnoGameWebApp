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
public class GameResource {
    
    @Inject
    private GameManager mgr;
    
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response create(@FormParam("title")String title) {
        
        String id = mgr.create(title);
        return Response.ok("<a href=\"http://localhost:8080/uno/viewallgames.html\">View all games :)</a>").build();
                
               
    }
    
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
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
        
        return (Response.ok(arrBuilder.build()).header(
                "Access-Control-Allow-Origin", "http://localhost:63342").build());
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response browsemany(@QueryParam("username") String name) {
        Map<String, Game> games = mgr.browseAvailableGames(name);        
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        games.values().stream()
                .map(g -> {
                    return (g.toJson());
                }).forEach(j -> {
            arrBuilder.add(j);
        });

        return (Response.ok(arrBuilder.build()).header(
                "Access-Control-Allow-Origin", "http://localhost:63342").build());
    }
    
    @GET
    @Path("start/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public JsonObject start(@PathParam("id")String id) {
        Game game = mgr.start(id);
        
      
        return game.toJson();
    }
    
    
}

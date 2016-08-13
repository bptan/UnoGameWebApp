/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sa42.uno.web.business;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import sa42.uno.model.Game;


/**
 *
 * @author BP
 */
@ApplicationScoped
public class GameManager {
    
    private Map<String,Game> games = new HashMap<>();
    
    public String create(String title) {
        
        String id = UUID.randomUUID().toString().substring(0, 8);
        Game myGame = new Game(id,title);
       
        games.put(id, myGame);
        System.out.println("game created:" + games.get(id).toString());
        return id;
    }
    
    public Map<String,Game> browseAvailableGames(String username){
        
        //TODO some filtering by player
        
       return games;
    }
    
    public Optional<Game> getOneGame(String gameId){
        
        return Optional.ofNullable(games.get(gameId));
    }
    
    public Map<String,Game> getGames(){
        

       return games;
    }
    
    public Game start(String id) {
       
        Game myGame = games.get(id);
        System.out.println(myGame.getStatus());
        if(myGame.getStatus()==Game.Status.Waiting){
            myGame.setStatus(Game.Status.Started);
            myGame.distributeCards();
            myGame.addToDiscardPile(myGame.takeCardFromDeck());
        }
     
        return myGame;
    }
}

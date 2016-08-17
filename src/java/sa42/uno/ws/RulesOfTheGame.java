/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sa42.uno.ws;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Stack;
import java.util.logging.Level;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import sa42.uno.model.Card;
import sa42.uno.model.Game;
import sa42.uno.model.Player;
import static sa42.uno.ws.Constants.*;

/**
 *
 * @author BP
 */
public class RulesOfTheGame {

    public static void startGame(Game game) {
        updateGameTable(game);
              
        for (Player p : game.getPlayers()) {           
            updatePlayerHand(game,p);           
        }

    }

    public static void playCard(Game game, String playedBy, String card) {
/*
        JsonObject msg = Json.createObjectBuilder()
                .add(ATTRIBUTE_COMMAND, COMMAND_PLAYCARD)
                .add(ATTRIBUTE_GID, game.getId())
                .add(ATTRIBUTE_PLAYERNAME, playedBy)
                .add(ATTRIBUTE_CARD, card)
                .build();
        System.out.println(msg.toString());
        */
        Optional<Player> opt = game.getPlayer(playedBy);
        
        if (!opt.isPresent()) {
            return;
        }       
        Player player = opt.get();
        System.out.println(card); 
        Card removed = player.removeCard(card);
        System.out.println(removed.toString());
        Stack<Card> table = game.addToTable(removed);
        System.out.println(table.toString());// table is updated
        
        updateGameTable(game);
        updatePlayerHand(game,player);
    }

    public static void drawCard(Game game, String pname, Card card) {

        Optional<Player> opt = game.getPlayer(pname);
        if (!opt.isPresent()) {
            return;
        }       
        Player player = opt.get();
        
        player.addCard(card);
        updatePlayerHand(game,player);
        updateGameTable(game);
    }
    private static void updateGameTable(Game game){
        JsonObject msg = Json.createObjectBuilder()
                .add(ATTRIBUTE_COMMAND, COMMAND_YOURCARDS)
                .add(ATTRIBUTE_GID, game.getId())
                .add(ATTRIBUTE_PLAYERNAME, game.playerNameAsJsonArray())
                .add(ATTRIBUTE_HAND, game.getTable().get(game.getTable().size()-1).toJson())
                .add(ATTRIBUTE_DECKSIZE, game.getDeck().getNumberOfCards())
                .build();
               
        System.out.println(msg.toString());
        try {
            game.send(msg);
        } catch (IOException ex) {
            ex.printStackTrace();
        }       
    }
    private static void updatePlayerHand(Game game,Player player){
        JsonObject msg2 = Json.createObjectBuilder()
                    .add(ATTRIBUTE_COMMAND, COMMAND_YOURCARDS)
                    .add(ATTRIBUTE_GID, game.getId())
                    .add(ATTRIBUTE_PLAYERNAME, player.getName())
                    .add(ATTRIBUTE_HAND, player.toJsonHandOnly())
                    .build();
            try {
                player.send(msg2);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    };

}

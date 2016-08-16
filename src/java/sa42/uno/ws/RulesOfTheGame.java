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
        
        for (Player p : game.getPlayers()) {
            JsonObject msg2 = Json.createObjectBuilder()
                    .add(ATTRIBUTE_COMMAND, COMMAND_YOURCARDS)
                    .add(ATTRIBUTE_GID, game.getId())
                    .add(ATTRIBUTE_PLAYERNAME, p.getName())
                    .add(ATTRIBUTE_HAND, p.toJsonHandOnly())
                    .build();
            try {
                p.send(msg2);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public static void playCard(Game game, String playedBy, Card card) {

        JsonObject msg = Json.createObjectBuilder()
                .add(ATTRIBUTE_COMMAND, COMMAND_PLAYCARD)
                .add(ATTRIBUTE_GID, game.getId())
                .add(ATTRIBUTE_PLAYERNAME, playedBy)
                .add(ATTRIBUTE_CARD, card.toJson())
                .build();

        try {
            game.send(msg);
        } catch (IOException ex) {
            //session fail
        }
    }

    public static void drawCard(Game game, String pname, Card card) {

        Optional<Player> opt = game.getPlayer(pname);
        if (!opt.isPresent()) {
            return;
        }

        JsonObject msg = Json.createObjectBuilder()
                .add(ATTRIBUTE_COMMAND, COMMAND_DRAWCARD)
                .add(ATTRIBUTE_GID, game.getId())
                .add(ATTRIBUTE_PLAYERNAME, pname)
                .add(ATTRIBUTE_CARD, card.toJson())
                .build();

        Player player = opt.get();
        try {
            player.send(msg);
        } catch (IOException ex) {
            //session fail
        }

    }

}

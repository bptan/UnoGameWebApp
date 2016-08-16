/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sa42.uno.ws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import sa42.uno.model.Game;
import sa42.uno.model.Player;
import sa42.uno.web.business.GameManager;

import static sa42.uno.ws.Constants.*;

/**
 *
 * @author BP
 */
@RequestScoped
@ServerEndpoint("/games/{gid}/{pname}")
public class GameEndpoint {

    @Inject
    private GameManager mgr;

    @OnOpen
    public void onOpen(Session session, @PathParam("gid") String gameid,
            @PathParam("pname") String pname) {
        System.out.println(">>new connection " + session.getId());
        Optional<Game> opt = mgr.getOneGame(gameid);
        
        if (!opt.isPresent()) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, gameid));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Map<String, Object> sessionObj = session.getUserProperties();
        sessionObj.put(ATTRIBUTE_GID, gameid);
 
	Game game = opt.get();
        
        if(pname.equals("verysecrettable")){
            System.out.println("tablesession:"+session.getId());
            game.setGameSession(session);
        }else{        
            Optional<Player> opt2 = game.getPlayer(pname);
            Player player = opt2.get();
            player.setSession(session);
        }
        if(game.getPlayers().size()==3){
                    System.out.println("Inside if condition");
                    System.out.println(game.getId());
                    mgr.start(game.getId());
                    System.out.println("GameStarted");
                }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Close connection:" + session.getId());
        System.out.println("\treason for close:" + reason.getReasonPhrase());

    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        System.out.println(">>connection: " + session.getOpenSessions().size());
        InputStream bis = new ByteArrayInputStream(msg.getBytes());
        JsonReader reader = Json.createReader(bis);
        JsonObject jsObj = reader.readObject();

        String command = jsObj.getString(ATTRIBUTE_COMMAND);

        switch (command) {
            case COMMAND_STARTGAME:

                break;
            case COMMAND_DRAWCARD:

                break;
            case COMMAND_PLAYCARD:
                break;

            default:
                System.out.println("Unknown Command");

        }
    }
}

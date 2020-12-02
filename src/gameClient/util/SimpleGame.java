package gameClient.util;

import Server.Game_Server_Ex2;
import api.game_service;

public class SimpleGame {
    private static int gameNum;



    public static void play(){
        game_service game = Game_Server_Ex2.getServer(gameNum);
        String map = game.getGraph();

    }




    public static void main(String[] args) {

    }
}

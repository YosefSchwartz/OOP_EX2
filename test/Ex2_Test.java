import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.game_service;
import gameClient.Agent;
import gameClient.Ex2;
import gameClient.Pokemon;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static gameClient.Ex2.*;

/**
 * This class will test some function we use in our Pokemon game.
 * we will check the 11 scenario, with 6 pokemons each time and 3 agents.
 *
 * Note: we will check the pre-start game function. (by Yael guide)
 */
public class Ex2_Test {
    static game_service game = Game_Server_Ex2.getServer(11);
    List<Agent> agentList = new LinkedList<>();



    /**
     * check the data we get from server before game start,
     * sum agents and pokemon, and if the path is valid.
     * @throws JSONException
     */
    @Test
    public void getDataFromServer() throws JSONException {
        getGameData(game);
        Assertions.assertEquals(3,getSumAgents());
        Assertions.assertEquals(6,getSumPokemons());
        boolean ans = false;
        File f = new File(getPath());
        if(f.exists() && !f.isDirectory())
            ans = true;
        Assertions.assertTrue(ans);
    }

    /**
     * check if createAgentsList create list correctly, and if can add more agents
     * than the server allow us.
     * @throws JSONException
     */
    @Test
    public void agents() throws JSONException {
        for(int i=0;i<3;i++)
            game.addAgent(0);
        agentList = createAgentsList(game, new LinkedList<>());
        Assertions.assertEquals(3,agentList.size());

        //try ro add one more agents than server accept me
        game.addAgent(0);
        Assertions.assertNotEquals(4,agentList.size());
    }
}

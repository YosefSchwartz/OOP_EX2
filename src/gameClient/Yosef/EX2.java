package gameClient.Yosef;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class EX2 {
    private static final double EPS = 0.0001;
    static int sumPokemons;
    static boolean logged_in;
    static int sumAgents;
    static dw_graph_algorithms graphAL = new DWGraph_Algo();
    static directed_weighted_graph graphDS;
    static List<Pokemon> pokemonList = new LinkedList<>();
    static List<Agent> agentList = new LinkedList<>();
    static String path;

    public static void main(String[] args) throws JSONException {
        int level_number = 0;
        game_service game = Game_Server_Ex2.getServer(level_number);
        getGameData(game);
        graphAL.load(path);
        graphDS = graphAL.getGraph();
        updatePokemonList(game);
//        System.out.println(game);
//        System.out.println(game.getGraph());
//        System.out.println(game.getPokemons());

        //Locate the agents near to the pokemons
        for (int i = 0; i < sumAgents; i++) {
            if (i < pokemonList.size())
                game.addAgent(pokemonList.get(i).getSrc());
            else
                game.addAgent(graphDS.getV().stream().findFirst().get().getKey());
        }
        System.out.println(game.getAgents());

        createAgentsList(game);
        for (Agent a : agentList) {
            a.findClosestPokemon(graphAL, pokemonList);
            game.chooseNextEdge(a.getId(), a.getNextDest());
        }

        game.startGame();
        int i = 1;


        while (game.isRunning()) {

            for (Pokemon p : pokemonList) {
                if (p.getAgent().getPos().distance(p.getPos()) < EPS) {
                    System.out.println("I EAT POKEMON");
                    game.move();
                }
            }
            //TODO insert ratio principle

            for (int j = 0; j < agentList.size(); j++) {
                Agent a = agentList.get(j);
                if (a.getPos().distance(graphDS.getNode(a.getDest()).getLocation()) < (EPS)) {
                    System.out.println("i came to node: "+ a.getDest());
                    int dest = a.getNextDest();
                    System.out.println("THE NEXT DESTINATION IS: " +dest);
                    if (dest != -1) {
                        game.chooseNextEdge(a.getId(), dest);
                    } else {
                        insertNewPokemons(game.getPokemons());
                        a.findClosestPokemon(graphAL, pokemonList);
                        game.chooseNextEdge(a.getId(),a.getDest());
                        game.move();
                    }
                    System.out.println(i + ") " + "Agent " + a.getId() + ") " + "move to node: " + a.getDest());
                    i++;
                }
            }
        }
    }

    private static void insertNewPokemons(String pokemons) throws JSONException {
        JSONObject newPokemonsObj = new JSONObject(pokemons);
        JSONArray pokemonsArr = newPokemonsObj.getJSONArray("Pokemons");
        for (int i = 0; i < pokemonsArr.length(); i++) {
            Pokemon newPok = new Pokemon(pokemonsArr.getJSONObject(i).getJSONObject("Pokemon"));
            boolean ans = false;
            for (Pokemon alreadyPok : pokemonList) {
                if (alreadyPok.getPos().distance(newPok.getPos()) < EPS)
                    ans = true;
            }
            if (!ans) {
                pokemonList.add(newPok);
                return;
            }
        }
    }

    private static void createAgentsList(game_service game) throws JSONException {
        JSONObject agents = new JSONObject(game.getAgents());
        JSONArray agentsArr = agents.getJSONArray("Agents");
        for (int i = 0; i < agentsArr.length(); i++) {
            Agent ag = new Agent(agentsArr.getJSONObject(i).getJSONObject("Agent"));
            agentList.add(ag);
        }
    }

    private static void getGameData(game_service game) throws JSONException {
        JSONObject gameServer = new JSONObject(game.toString());
        JSONObject gameData = gameServer.getJSONObject("GameServer");
        sumAgents = gameData.getInt("agents");
        sumPokemons = gameData.getInt("pokemons");
        logged_in = gameData.getBoolean("is_logged_in");
        path = gameData.getString("graph");
    }

    private static void updatePokemonList(game_service game) throws JSONException {
        List<Pokemon> newP = new LinkedList<>();
        JSONObject pokemons = new JSONObject(game.getPokemons());
        JSONArray pokemonArr = pokemons.getJSONArray("Pokemons");
        for (int i = 0; i < pokemonArr.length(); i++) {
            Pokemon pok = new Pokemon(pokemonArr.getJSONObject(i).getJSONObject("Pokemon"));
            getSrcAndDest(pok);
            newP.add(pok);
        }
        pokemonList = newP;
    }

    private static void getSrcAndDest(Pokemon p) {
        for (node_data n : graphDS.getV()) {
            for (edge_data e : graphDS.getE(n.getKey())) {
                if (isOnEdge(p, e)) {
                    p.setSrc(e.getSrc());
                    p.setDest(e.getDest());
                    return;
                }
            }
        }
    }
    private static boolean isOnEdge(Pokemon p, edge_data e) {
        int srcID = e.getSrc();
        int destID = e.getDest();
        if(p.getType()<0 && destID>srcID) { return false;}
        if(p.getType()>0 && srcID>destID) { return false;}

        geo_location srcPos = graphDS.getNode(srcID).getLocation();
        geo_location destPos = graphDS.getNode(destID).getLocation();
        geo_location pokemonPos = p.getPos();

        return isOnEdge(pokemonPos, srcPos, destPos);
    }

    private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {
        double dist = src.distance(dest);
        double d1 = src.distance(p) + p.distance(dest);
        if (dist > d1 - EPS) {
            return true;
        }
        return false;
    }
//    private static boolean isOnEdge(geo_location p, int s, int d, directed_weighted_graph g) {
//        geo_location src = g.getNode(s).getLocation();
//        geo_location dest = g.getNode(d).getLocation();
//        return isOnEdge(p,src,dest);
//    }
//    private static boolean isOnEdge(geo_location p, edge_data e, int type, directed_weighted_graph g) {
//        int src = g.getNode(e.getSrc()).getKey();
//        int dest = g.getNode(e.getDest()).getKey();
//        if(type<0 && dest>src) {return false;}
//        if(type>0 && src>dest) {return false;}
//        return isOnEdge(p,src, dest, g);
//    }


}

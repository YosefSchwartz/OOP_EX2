package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.*;

/**
 * This class is the main class of our Pokemon game.
 * It create amazing GUI to imagine the algorithms behind, and have a fun experience from our project.
 * The GUI part use at JFrame, and logic part use the api package and pokemon and agent classes.
 * In this main class we express the OOP principle, additional to frame abilities.
 */
public class Ex2 implements Runnable {


    /**
     * This main support two options, execute from command line or use the GUI.
     * @param args - args array may contain data of id the user and scenario num.
     *
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        if(args.length==2) {
            Ex2 ex2 = new Ex2();
            int gameNum = Integer.MIN_VALUE, ID = Integer.MIN_VALUE;
            boolean ans = false;
            try {
                gameNum = Integer.parseInt(args[1]);
                ID = Integer.parseInt(args[0]);
                ans = true;
            } catch (NumberFormatException e) {
                System.out.println("ERROR, INVALID INPUT\nPlease try again");
            }
            if (ans) {
                ex2.setGameNumber(gameNum);
                ex2.setID(ID);
                Thread game_thread = new Thread(ex2);
                game_thread.start();
            }
        }
        else {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        }
    }
    private static final double EPS = 0.000001;
    static int sumPokemons;
    static boolean logged_in;
    static int sumAgents;
    static dw_graph_algorithms graphAL;
    static directed_weighted_graph graphDS;
    static List<Pokemon> pokemonList;
    static List<Agent> agentList;
    static List<String> GameInfo;
    static String path;
    private game_service game;
    private long min;
    private static GameFrame _win;
    private static GameData _ar;
    private int GameNumber;
    private int ID;
    public static Ex2 ex2;
    private static PriorityQueue<Pokemon> highValuePokemon = new PriorityQueue<>(2, new Comparator<Pokemon>() {
        @Override
        /**
         * Comparator by high value
         */
        public int compare(Pokemon o1, Pokemon o2) {
            if(o1.getValue()<o2.getValue())
                return 1;
            else if(o1.getValue()>o2.getValue())
                return  -1;
            else
                return 0;
        }
    });

    @Override
    public void run() {
        game = Game_Server_Ex2.getServer(GameNumber);
        initTheGame(game);
        try {
            RunningTheGame(game);
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }
        _win.close();
    }

    /**
     * all orders before the game will start, initial all game and frame wee need.
     * Finally start the game and set to each agents the first pokemon.
     * @param game - game
     */
    private void initTheGame(game_service game) {
        min = Long.MAX_VALUE;
        graphAL = new DWGraph_Algo();
        graphDS = new DWGraph_DS();
        pokemonList = new LinkedList<>();
        agentList = new LinkedList<>();
        GameInfo = new LinkedList<>();
        UpdateGameInfo(GameNumber, 0, 0);
        try {
            getGameData(game);
            graphAL.load(path);
            graphDS = graphAL.getGraph();
            CreateFirstPokemonList(game);
            _ar = new GameData(graphDS, agentList, pokemonList, GameInfo);
            setPlaceOfAgentsHighValue(game);
            _ar = new GameData(graphDS, agentList, pokemonList,GameInfo);
            _win = new GameFrame("test Ex2");
            _win.setSize(1000, 700);
            _win.update(_ar);
            _win.setVisible(true);
            game.login(ID);
            game.startGame();
            _win.setTitle("Ex2 - OOP: " + GameNumber);
            setPokToEachAgent(game);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The main loop of game, each iteration do a lot of algorithms to find the closest pokemon
     * and set the path to him.
     * @param game - game
     * @throws JSONException
     * @throws InterruptedException
     */
    private void RunningTheGame(game_service game) throws JSONException, InterruptedException {
        double TotalValue = 0;
        while (game.isRunning()) {
            min = Long.MAX_VALUE;
            String s = game.move();
            updatePoks(game.getPokemons());
            refreshLocation(s);
            UpdateGameInfo(GameNumber, game.timeToEnd(), TotalValue);
            _ar.setAgents(agentList);
            _ar.setPokemons(pokemonsInTheGame(game.getPokemons()));
            _ar.set_info(GameInfo);
            _win.repaint();
            TotalValue = 0;
            for (Agent a : agentList) {
                int tmp = a.getDest();
                if (a.getDest() == -1 ) {
                    String poks = game.getPokemons();
                    if (!a.pokemon.isInTheGame(poks, a.getPokemon()))
                        a.findClosestPokemon(graphAL, pokemonList);
                    game.chooseNextEdge(a.getId(), a.getNextDest());
                }
                long time = a.time(graphDS, tmp, game.getPokemons(), pokemonList);
                min = (time < min && time != 0) ? time : min;
                TotalValue += a.getValue();
            }
            if (min == Long.MAX_VALUE)
                min = 1;

            Thread.sleep(min);
        }
        _win.dispose();
        System.out.println(game.toString());
    }
///////////////////     PRIVATE FUNCTION

//////////      GETTER AND SETTER FUNCTION

    /**
     * set the game number
     * @param n - game number
     */
    public void setGameNumber(int n) {
        GameNumber = n;
    }

    /**
     * set the ID number of user
     * @param id - id number
     */
    public void setID(int id) {
        ID = id;
    }

    /**
     * used by tests
     * @return sum of pokemons each time in the game
     */
    public static int getSumPokemons() { return sumPokemons; }

    /**
     * used by tests
     * @return sum of agents each time in the game
     */
    public static int getSumAgents() { return sumAgents; }

    /**
     * used by tests
     * @return path to JSON file that represent the graph
     */
    public static String getPath() { return path; }

    //////////  LOGICAL FUNCTION

    /**
     * update the time to end and total value.
     * @param level - level num
     * @param time - time to end
     * @param value - total value that your agents collect.
     */
    public static void UpdateGameInfo(int level, long time, double value) {
        List<String> newList = new LinkedList<>();
        newList.add(String.valueOf(level));
        newList.add(String.valueOf(time / 1000));
        newList.add(String.valueOf(value));
        GameInfo = newList;
    }

    /**
     * update the agents location.
     * look for new location and update the old list.
     * @param s - current information of agents location
     * @throws JSONException
     */
    private static void refreshLocation(String s) throws JSONException {
        JSONObject obj = new JSONObject(s);
        JSONArray moveData = obj.getJSONArray("Agents");
        for (int i = 0; i < moveData.length(); i++) {
            JSONObject agent = moveData.getJSONObject(i).getJSONObject("Agent");
            Agent a = new Agent(agent);
            for (Agent ag : agentList) {
                if (ag.getId() == a.getId()) {
                    ag.setPos(a.getPos());
                    ag.setDest(a.getDest());
                    ag.setSrc(a.getSrc());
                    ag.setSpeed(a.getSpeed());
                    ag.setValue(a.getValue());
                }
            }
        }
    }

    /**
     * update all agent's pokemons list.
     * @param pokemons - available pokemons from server at JSON format
     * @throws JSONException
     */
    public static void updatePoks(String pokemons) throws JSONException {
        List<Pokemon> newPokemonList = new LinkedList<>();
        JSONObject newPokemonsObj = new JSONObject(pokemons);
        JSONArray newPokemonsArr = newPokemonsObj.getJSONArray("Pokemons");
        for (int i = 0; i < newPokemonsArr.length(); i++) {
            Pokemon newPokemon = new Pokemon(newPokemonsArr.getJSONObject(i).getJSONObject("Pokemon"));
            boolean connectToAgent = false;
            //For each agent we search pokemons that don't associated to agent
            //and remove them from new pokemons list, finally pokemons list get the new list.
            for (int j = 0; j < agentList.size(); j++) {
                List<Pokemon> pokemonsOfAgent = agentList.get(j).getMyPoks();
                List<Pokemon> pokemonsToRemove = new LinkedList<>();
                for (Pokemon p : pokemonsOfAgent) {
                    if (!p.isInTheGame(pokemons, p))
                        pokemonsToRemove.add(p);
                }
                pokemonsOfAgent.removeAll(pokemonsToRemove);
                for (Pokemon p : pokemonsOfAgent) {
                    if (p != null) {
                        geo_location pokL = newPokemon.getPos();
                        geo_location PL = p.getPos();
                        if (pokL.x() == PL.x() && pokL.y() == PL.y() && pokL.z() == PL.z()) {
                            connectToAgent = true;
                            newPokemonList.add(p);
                            break;
                        }
                    }
                }

            }
            if (!connectToAgent) {
                setSrcAndDest(newPokemon);
                edgeData e = (edgeData) (graphDS.getEdge(newPokemon.src, newPokemon.dest));
                edgeData.edgeLocation edgeLocation = new edgeData.edgeLocation(newPokemon.getPos(), e);
                newPokemon.setEL(edgeLocation);
                newPokemonList.add(newPokemon);
            }
        }
        pokemonList = newPokemonList;
    }

    /**
     * set the src and dest in Pokemon object
     * @param p - pokemon
     */
    private static void setSrcAndDest(Pokemon p) {
        for (node_data n : graphDS.getV())
            for (edge_data e : graphDS.getE(n.getKey()))
                if (isOnEdge(p, e)) {
                    p.setSrc(e.getSrc());
                    p.setDest(e.getDest());
                    return;
                }
    }

    /**
     * Check if pokemon is on edge
     * @param p - pokemon
     * @param e - edge
     * @return true if pokemon p is on edge e
     */
    private static boolean isOnEdge(Pokemon p, edge_data e) {
        int srcID = e.getSrc();
        int destID = e.getDest();
        //if the type doesn't match to this edge can return false
        if (p.getType() < 0 && destID > srcID) { return false; }
        if (p.getType() > 0 && srcID > destID) { return false; }

        geo_location srcPos = graphDS.getNode(srcID).getLocation();
        geo_location destPos = graphDS.getNode(destID).getLocation();
        geo_location pokemonPos = p.getPos();

        return isOnEdge(pokemonPos, srcPos, destPos);
    }

    /**
     * Check if some 3D point is between two 3D positions, up to epsilon.
     * @param p - 3D point
     * @param src - first 3D point
     * @param dest - last 3D point
     * @return - true if is between two points
     */
    private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {
        double dist = src.distance(dest);
        double d1 = src.distance(p) + p.distance(dest);
        if (dist > d1 - EPS)
            return true;
        return false;
    }


    //////////      BEFORE GAME FUNCTION
    /**
     * Get important data from server to init the game.
     * @param game - game
     * @throws JSONException
     */
    public static void getGameData(game_service game) throws JSONException {
        JSONObject gameServer = new JSONObject(game.toString());
        JSONObject gameData = gameServer.getJSONObject("GameServer");
        sumAgents = gameData.getInt("agents");
        sumPokemons = gameData.getInt("pokemons");
        logged_in = gameData.getBoolean("is_logged_in");
        path = gameData.getString("graph");
    }

    /**
     * Create agents list before game is run
     * @param game - game
     * @param agentList - the empty agents list
     * @return - the update agents list
     * @throws JSONException
     */
    public static List<Agent> createAgentsList(game_service game, List<Agent> agentList) throws JSONException {
        JSONObject agents = new JSONObject(game.getAgents());
        JSONArray agentsArr = agents.getJSONArray("Agents");
        for (int i = 0; i < agentsArr.length(); i++) {
            Agent ag = new Agent(agentsArr.getJSONObject(i).getJSONObject("Agent"));
            agentList.add(ag);
        }
        return agentList;
    }

    /**
     * Create the pokemon list from the server.
     * @param game - game
     * @return - the pokemon list
     * @throws JSONException
     */
    public static void CreateFirstPokemonList(game_service game) throws JSONException {
        List<Pokemon> firstPokemonList = pokemonsInTheGame(game.getPokemons());
        for (Pokemon p: firstPokemonList) {
            setSrcAndDest(p);
            edgeData e = (edgeData) (graphDS.getEdge(p.src, p.dest));
            edgeData.edgeLocation edgelocation = new edgeData.edgeLocation(p.getPos(), e);
            p.setEL(edgelocation);
        }
        pokemonList=  firstPokemonList;
    }
    /**
     * set to each agents the near pokemon, and set they first destination.
     * NOTE: must be after init the pokemons list
     * @param game - game
     * @throws JSONException
     */
    public void setPokToEachAgent (game_service game) throws JSONException {
        agentList = createAgentsList(game, agentList); // init the agents list
        for (Agent a : agentList)
            for (Pokemon p : pokemonList)
                if ((p.getSrc() == a.getSrc()) && (p.getAgent() == null)) {
                    a.setPokemon(p);
                    a.getMyPoks().add(p);
                    p.setAgent(a);
                    a.setDest(a.getPokemon().getDest());
                    game.chooseNextEdge(a.getId(), a.getPokemon().getDest());
                    break;
                }
    }

    /**
     * place the agents near to pokemons by high value order
     * @param game - game
     */
    private static void setPlaceOfAgentsHighValue (game_service game)
    {
        for(Pokemon p:pokemonList)
            highValuePokemon.add(p);
       int n = highValuePokemon.size();
        for (int i = 0; i < sumAgents; i++) {
            if (i < n)
                game.addAgent(highValuePokemon.poll().src);
             else
                game.addAgent(graphDS.getV().stream().findFirst().get().getKey());            }
    }

    //////////      FRAME FUNCTION
    /**
     * Create list of pokemons from string at JSON format.
     * @param pokemons - string
     * @return - List of all pokemons in the game
     * @throws JSONException
     */
    public static List<Pokemon> pokemonsInTheGame(String pokemons) throws JSONException {
        JSONObject newPokemonsObj = new JSONObject(pokemons);
        JSONArray pokemonsArr = newPokemonsObj.getJSONArray("Pokemons");
        List<Pokemon> p = new LinkedList<>();
        for (int i = 0; i < pokemonsArr.length(); i++) {
            Pokemon newPok = new Pokemon(pokemonsArr.getJSONObject(i).getJSONObject("Pokemon"));
            p.add(newPok);
        }
        return p;
    }
}
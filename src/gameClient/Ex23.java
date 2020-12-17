//package gameClient;
//
//import Server.Game_Server_Ex2;
//import api.*;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.UnsupportedAudioFileException;
//import javax.swing.*;
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.List;
//
//public class Ex23 implements Runnable {
//
//    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
////        LoginFrame lf=new LoginFrame();
////        lf.setVisible(true);
//        //  Ex2 ex2=new Ex2();
//        // ex2.setID(ID);
//
//            game_service game = Game_Server_Ex2.getServer(0);
//            Ex23 ex2=new Ex23(game);
//            Frame frame=new Frame(0,game, _ar, _win, agentList, pokemonList);
//            Thread Game = new Thread(ex2);
//            Game.start();
//            Thread fra=new Thread(frame);
//            fra.start();
//
//            //Game.join();
//
//    }
//    private static final double EPS = 0.000001;
//    static int sumPokemons;
//    static boolean logged_in;
//    static int sumAgents;
//    static dw_graph_algorithms graphAL;
//    static directed_weighted_graph graphDS;
//    static List<Pokemon> pokemonList;
//    static List<Agent> agentList;
//    static List<String> GameInfo;
//    static String path;
//    private game_service game;
//    private long min;
//    private static GameFrame _win;
//    private static GameData _ar;
//    private int GameNumber;
//    private int ID;
//    public static Ex23 ex2;
//
//    public Ex23(game_service game){
//        this.game=game;
//        initTheGame(game);
//    }
//    @Override
//    public void run() {
//        int level_number = 0;
//        //game = Game_Server_Ex2.getServer(GameNumber);
////        initTheGame(game);
//        try {
//            RunningTheGame(game);
//        } catch (JSONException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void initTheGame(game_service game) {
//        min = Long.MAX_VALUE;
//        graphAL = new DWGraph_Algo();
//        graphDS = new DWGraph_DS();
//        pokemonList = new LinkedList<>();
//        agentList = new LinkedList<>();
//        GameInfo=new LinkedList<>();
//        UpdateGameInfo(GameNumber, 0, 0);
//        try {
//            getGameData(game);
//            graphAL.load(path);
//            graphDS = graphAL.getGraph();
//            updatePokemonList(game);
//            setPlaceOfAgents(game);
//            _ar = new GameData(graphDS, agentList, pokemonList,GameInfo);
//            _win = new GameFrame("test Ex2");
//            _win.setSize(1000, 700);
//            _win.update(_ar);
//            _win.setVisible(true);
//            _win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//             //game.login(20);
//            game.startGame();
//            _win.setTitle("Ex2 - OOP: " +GameNumber);
//            setPokToEachAgent(game);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void UpdateGameInfo(int level, long time, double value)
//    {
//        List<String> newList=new LinkedList<>();
//        newList.add(String.valueOf(level));
//        newList.add(String.valueOf(time/1000));
//        newList.add(String.valueOf(value));
//        GameInfo=newList;
//    }
//    private static void regfreshLocation(String s) throws JSONException {
//        JSONObject obj = new JSONObject(s);
//        JSONArray moveData = obj.getJSONArray("Agents");
//        for (int i = 0; i < moveData.length(); i++) {
//            JSONObject agent = moveData.getJSONObject(i).getJSONObject("Agent");
//            Agent a = new Agent(agent);
//            for (Agent ag : agentList) {
//                if (ag.getId() == a.getId()) {
//                    ag.setPos(a.getPos());
//                    ag.setDest(a.getDest());
//                    ag.setSrc(a.getSrc());
//                    ag.setSpeed(a.getSpeed());
//                    ag.setValue(a.getValue());
//                }
//            }
//        }
//    }
//    public static void updatePoks(String pokemons) throws JSONException {
//        List<Pokemon> newP=new LinkedList<>();
//        JSONObject newPokemonsObj = new JSONObject(pokemons);
//        JSONArray pokemonsArr = newPokemonsObj.getJSONArray("Pokemons");
//        for (int i = 0; i < pokemonsArr.length(); i++) {
//            Pokemon pok = new Pokemon(pokemonsArr.getJSONObject(i).getJSONObject("Pokemon"));
//            boolean connectToAgent=false;
//            for (Agent a: agentList)
//            {
//                Pokemon p=a.getPokemon();
//                if(p!=null) {
//                    geo_location pokL=pok.getPos();
//                    geo_location PL=p.getPos();
//                    if(pokL.x()==PL.x() && pokL.y()==PL.y() && pokL.z()==PL.z()) {
//                        connectToAgent = true;
//                        break;
//                    }
//                }
//            }
//            if (connectToAgent==false) {
//                setSrcAndDest(pok);
//                edgeData e = (edgeData) (graphDS.getEdge(pok.src, pok.dest));
//                edgeData.edgeLocation edgelocation = new edgeData.edgeLocation(pok.getPos(), e);
//                pok.setEL(edgelocation);
//                newP.add(pok);
//            }
//        }
//        pokemonList = newP;
//    }
//
//    private void RunningTheGame(game_service game) throws JSONException, InterruptedException {
//
//        String s;
//        while (game.isRunning()) {
//            min = Long.MAX_VALUE;
//            s = game.move();
//            regfreshLocation(s);
//            for (Agent a : agentList) {
//                int tmp = a.getDest();
//                if (a.getDest() == -1) {
//                    if (!(a.getPokemon().is_in_the_game(game.getPokemons(), a.getPokemon()))) { //a took the pokemon
//                            updatePoks(game.getPokemons());
//                            a.findClosestPokemon(graphAL, pokemonList);
//                    }
//                    game.chooseNextEdge(a.getId(), a.getNextDest());
//                }
//                long time = a.time(graphDS,tmp,game.getPokemons());
//                min = (time<min)?time:min;
//            }
//            Thread.sleep(min);
//        }
//        _win.dispose();
//        System.out.println(game.toString());
//    }
//
//
//    private static void updatePokemonList (game_service game) throws JSONException {
//        List<Pokemon> newP = new LinkedList<>();
//        JSONObject pokemons = new JSONObject(game.getPokemons());
//        JSONArray pokemonArr = pokemons.getJSONArray("Pokemons");
//        for (int i = 0; i < pokemonArr.length(); i++) {
//            Pokemon pok = new Pokemon(pokemonArr.getJSONObject(i).getJSONObject("Pokemon"));
//            setSrcAndDest(pok);
//            edgeData e = (edgeData) (graphDS.getEdge(pok.src, pok.dest));
//            edgeData.edgeLocation edgelocation = new edgeData.edgeLocation(pok.getPos(), e);
//            pok.setEL(edgelocation);
//            newP.add(pok);
//        }
//        pokemonList = newP;
//    }
//
//    private static void setPlaceOfAgents (game_service game)
//    {
//        for (int i = 0; i < sumAgents; i++) {
//            if (i < pokemonList.size()) {
//                game.addAgent(pokemonList.get(i).getSrc());
//            } else
//                game.addAgent(graphDS.getV().stream().findFirst().get().getKey());
//        }
//    }
//    public void setPokToEachAgent (game_service game) throws JSONException {
//        createAgentsList(game);
//        for (Agent a : agentList) {
//            a.findClosestPokemon(graphAL, pokemonList);
//            game.chooseNextEdge(a.getId(), a.getNextDest());
//        }
//    }
//
//    private static void createAgentsList (game_service game) throws JSONException {
//        JSONObject agents = new JSONObject(game.getAgents());
//        JSONArray agentsArr = agents.getJSONArray("Agents");
//        for (int i = 0; i < agentsArr.length(); i++) {
//            Agent ag = new Agent(agentsArr.getJSONObject(i).getJSONObject("Agent"));
//            agentList.add(ag);
//        }
//    }
//
//    private static void getGameData (game_service game) throws JSONException {
//        JSONObject gameServer = new JSONObject(game.toString());
//        JSONObject gameData = gameServer.getJSONObject("GameServer");
//        sumAgents = gameData.getInt("agents");
//        sumPokemons = gameData.getInt("pokemons");
//        logged_in = gameData.getBoolean("is_logged_in");
//        path = gameData.getString("graph");
//    }
//    private static void setSrcAndDest(Pokemon p) {
//        for (node_data n : graphDS.getV()) {
//            for (edge_data e : graphDS.getE(n.getKey())) {
//                if (isOnEdge(p, e)) {
//                    p.setSrc(e.getSrc());
//                    p.setDest(e.getDest());
//                    return;
//                }
//            }
//        }
//    }
//
//    private static boolean isOnEdge(Pokemon p, edge_data e) {
//        int srcID = e.getSrc();
//        int destID = e.getDest();
//        if (p.getType() < 0 && destID > srcID) {
//            return false;
//        }
//        if (p.getType() > 0 && srcID > destID) {
//            return false;
//        }
//
//        geo_location srcPos = graphDS.getNode(srcID).getLocation();
//        geo_location destPos = graphDS.getNode(destID).getLocation();
//        geo_location pokemonPos = p.getPos();
//
//        return isOnEdge(pokemonPos, srcPos, destPos);
//    }
//
//    private static boolean isOnEdge(geo_location p, geo_location src, geo_location dest) {
//        double dist = src.distance(dest);
//        double d1 = src.distance(p) + p.distance(dest);
//        if (dist > d1 - EPS) {
//            return true;
//        }
//        return false;
//    }
//    public void setGameNumber(int n){
//        GameNumber=n;
//    }
//    public void setID(int id){
//        ID=id;
//    }
//
//}
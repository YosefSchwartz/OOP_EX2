package gameClient;

import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Policy;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This class represent an "agent" on our Pokemon game.
 * Agent is object that can move on directed weighted graph, agent search for close pokemons and eat them.
 * each agent have source node, destination node, current value, current speed, realtime position that represent by 3D points,
 * Pokemon- the next pokemon that this agents will eat.
 * list of pokemon - be use when there is 2 pokemon near to one agent.
 * list of the path that represent each time the next node that this agent will go to. (implemented by queue).
 */
public class Agent {

    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private geo_location pos;
    Pokemon pokemon;
    List<Pokemon> MyPoks;
    Queue<Integer> myPath = new LinkedList<>();

    /**
     * @return the pokemon that this agent go to.
     */
    public Pokemon getPokemon() { return pokemon; }

    /**
     * set pokemon for this agent.
     * @param pokemon - pokemon
     */
    public void setPokemon(Pokemon pokemon) { this.pokemon = pokemon; }

    /**
     * constructor function - build new agent from string in JSON format
     *
     * @param agentObj - string at JSON format
     * @throws JSONException
     */
    public Agent(JSONObject agentObj)throws JSONException {
        this.id = agentObj.getInt("id");
        this.value=agentObj.getDouble("value");
        this.src=agentObj.getInt("src");
        this.dest=agentObj.getInt("dest");
        this.speed = agentObj.getDouble("speed");
        String pos = agentObj.getString("pos");
        String[] locST = pos.split(",", 3);
        Double[] geoL = new Double[3];
        for (int j = 0; j < locST.length; j++)
            geoL[j] = Double.parseDouble(locST[j]);
        this.pos= new NodeData.geoLocation(geoL[0], geoL[1], geoL[2]);
        MyPoks=null;
        MyPoks=new LinkedList<>();
    }


    /**
     * search for closest pokemon for this agents,
     * and remove this pokemon from pokemon list.
     *
     * @param ga - graph
     * @param poks - list of pokemon in the game
     */
    public void findClosestPokemon (dw_graph_algorithms ga, List<Pokemon> poks){
        Pokemon closestPok = null;
        double dist = Double.MAX_VALUE;
        for(Pokemon p:poks) {
            if(p.agent == null) {
                double shortestPath=ga.shortestPathDist(src, p.getSrc());
                shortestPath+=p.getEL().getRatio()*p.getEL().getEdge().getWeight();
                if (shortestPath< dist) {
                    closestPok = p;
                    dist = shortestPath;
                }
            }
        }
        if(closestPok!=null) {
            //Set the closest pokemon for this agent, add him to his pokemon list and set this agent on this pokemon
            closestPok.setAgent(this);
            MyPoks.add(closestPok);
            this.setPokemon(closestPok);
        //set the path to this pokemon
        setPath(ga);
        }else{
            directed_weighted_graph g = ga.getGraph();
            int n = g.getE(src).stream().findAny().get().getDest();
            if(myPath.isEmpty())
                myPath.add(n);
        }
    }

    /**
     * set path to its pokemon by shortestPath function
     * @param ga - dw_graph_algorithms
     */
    public void setPath(dw_graph_algorithms ga){
        List<node_data> shortestPath = ga.shortestPath(src,pokemon.getSrc());
        //Empty the path the last exist.
        while (!myPath.isEmpty()) {
            myPath.poll();
        }
        //Add the new path until the src of pokemon.
        for(int i = 1;i<shortestPath.size();i++) {
            myPath.add(shortestPath.get(i).getKey());
        }
        //Complete the path by add the dest of pokemon
        myPath.add(pokemon.getDest());
    }

    /**
     * @return the id of this agent
     */
    public int getId() {
        return id;
    }

    /**
     * @return the value of this agent
     */
    public double getValue() {
        return value;
    }

    /**
     * set the value of this agent
     * @param value - new value
     */
    public void setValue(double value) { this.value = value;}

    /**
     * @return the id of src node
     */
    public int getSrc() { return src; }

    /**
     * set the id of src node
     * @param src - src id
     */
    public void setSrc(int src) { this.src = src; }

    /**
     * @return the id of dest node
     */
    public int getDest() { return dest; }

    /**
     * set the id of dest node
     * @param dest - dest id
     */
    public void setDest(int dest) { this.dest = dest; }

    /**
     * @return the speed of this agent
     */
    public double getSpeed() { return speed; }

    /**
     * set the speed of this agent
     * @param speed - speed
     */
    public void setSpeed(double speed) { this.speed = speed; }

    /**
     * @return the current position of this agent
     */
    public geo_location getPos() { return pos; }

    /**
     * set the position of this agent
     * @param pos - position that present like 3D point
     */
    public void setPos(geo_location pos) { this.pos = pos; }

    /**
     * @return the list of pokemons that associated to this agent
     */
    public List<Pokemon> getMyPoks() { return MyPoks; }

    /**
     * toString function -
     * @return
     */
    public String toString() {
        return "id: "+id+", value: "+value+", src: "+src+", dest: "+dest+", speed: "+
                speed+", pos: "+pos+", pokemon: "+pokemon+"\n--------\n";
    }

    /**
     * @return the id of next node at the path
     */
    public int getNextDest() {
        setDest(myPath.poll());
        return dest;
    }

    /**
     * complex function that calculate the time until the agent will be enough close to next node or pokemon.
     * it calculate the time by multiple of his speed, weight of relevant edge, and ratio (if it may eat pokemon).
     *
     * and maybe this move catch this an agent on middle of edge because other agent came to node or eat their pokemon.
     *
     * @param g - graph of game
     * @param tmp - indicator if this agents in node or edge
     * @param activePokemons - string at JSON format that bring information about pokemon in the game
     * @param poks - list of pokemon that on the graph
     * @return minimum time that this agent need to wait until he be near to next node or pokemon.
     * @throws JSONException -
     */
    public long time(directed_weighted_graph g, int tmp, String activePokemons, List<Pokemon> poks) throws JSONException {
        double w;
        long time=Long.MAX_VALUE;
        for (Pokemon p: poks) {
            if (tmp == -1) {
                //Agent in node - at next .move() can eat pokemon or go to next node
                w = g.getEdge(src, dest).getWeight();
                //Go to eat pokemon
                if (src == p.src) {
                    double ratio = p.getEL().getRatio();
                    long t = (long) (ratio * w * 1000 / speed);
                    if (t < time && t != 0) time = t;
                } else { //Go to next node
                    long t = (long) (w * 1000 / speed);
                    if (t < time && t != 0) time = t;
                }
            } else { //Agent in edge - maybe eat his pokemon, may eat his pokemon,
                     // and maybe both (when there are two pokemons on one edge.
                    //  And maybe this agents just catch on middle of edge and just need to continue to next node
                double edgeDist = g.getNode(src).getLocation().distance(g.getNode(dest).getLocation());
                if (src != p.getSrc()) { // just in middle of edge, need to continue to his dest.
                    double partlyDist = pos.distance(g.getNode(dest).getLocation());
                    double ratio = partlyDist / edgeDist;
                    w = g.getEdge(src, dest).getWeight();
                    long t = (long) (w * ratio * 1000 / speed);
                    if (t < time && t != 0) time = t;
                } else { // there is pokemon on this edge
                    w = p.getEL().getEdge().getWeight();
                    if (p.isInTheGame(activePokemons, p)) { // If p i already available
                        double partlyDist = pos.distance(p.getPos());
                        double ratio = partlyDist / edgeDist;
                        long t = (long) (w * 1000 * ratio / speed);
                        if (t < time && t != 0) time = t;
                    } else { // This agent just eat his pokemon in this move
                        double partlyDist = pos.distance(g.getNode(dest).getLocation());
                        double ratio = partlyDist / edgeDist;
                        long t = (long) (ratio * w * 1000 / speed);
                        if (t < time && t != 0) time = t;
                    }
                }
            }
        }
        return time;
    }
}
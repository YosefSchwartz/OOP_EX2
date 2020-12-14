package gameClient;

import api.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Agent {

    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private geo_location pos;
    Pokemon pokemon;
    private int previesSRC;

    Queue<Integer> myPath = new LinkedList<>();



    public Agent(int id,double value, int src, int dest, double speed, geo_location pos){
        this.id = id;
        this.value=value;
        this.src=src;
        this.dest=dest;
        this.speed=speed;
        this.pos=pos;
        previesSRC=-1;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Agent(JSONObject agObj)throws JSONException {
        this.id = agObj.getInt("id");
        this.value=agObj.getDouble("value");
        this.src=agObj.getInt("src");
        this.dest=agObj.getInt("dest");
        this.speed = agObj.getDouble("speed");
        String pos = agObj.getString("pos");
        String[] locST = pos.split(",", 3);
        Double[] geoL = new Double[3];
        for (int j = 0; j < locST.length; j++)
            geoL[j] = Double.parseDouble(locST[j]);
        geo_location location = new NodeData.geoLocation(geoL[0], geoL[1], geoL[2]);
        this.pos=location;
        pokemon = null;
    }

    public void updateDest(int num){
        if(myPath.peek()!=null)
            this.dest = myPath.poll();
        else
            this.dest = -1;
    }

    /**
     * serach for closest pokemon for this agents,
     * and remove this pokemon from pokemon list
     *
     * @param ga
     * @param poks
     */
    public void findClosestPokemon (dw_graph_algorithms ga, List<Pokemon> poks){
        Pokemon closestPok = null;
        double dist = Double.MAX_VALUE;
        //  System.out.println("pokemon list: "+poks);
        for(Pokemon p:poks) {
            if(p.agent == null) {
                double shortestPath=ga.shortestPathDist(src, p.getSrc());
                if (shortestPath< dist) {
                    closestPok = p;
                    dist = shortestPath;
                }
            }
        }
        closestPok.setAgent(this);
        this.setPokemon(closestPok);

        // System.out.println("agent "+getId()+" go to pok "+closestPok.src+"->"+closestPok.getDest());
    }

    public void setPath(dw_graph_algorithms ga){
        List<node_data> shortestPath = ga.shortestPath(src,pokemon.getSrc());
        while (!myPath.isEmpty()) {
            myPath.poll();
        }
        for(int i = 0;i<shortestPath.size();i++) {
            myPath.add(shortestPath.get(i).getKey());
        }
        myPath.add(pokemon.getDest());
    }

    public long timeNodeToNode(directed_weighted_graph g){
        node_data src = g.getNode(this.src);
        node_data dest = g.getNode(this.dest);
        double w = g.getEdge(this.src,this.dest).getWeight();
        double agentSpeed = speed;
        long TotalSpeed = (long) ((w*1000) / agentSpeed);
        return TotalSpeed;
    }

    public long TimetoPok(Pokemon p, directed_weighted_graph gg) {
        double ratio = p.getEL().getRatio();
        double w = p.getEL().getEdge().getWeight();
        double agentSpeed = speed;
        double TotalSpeed = (w*1000) / agentSpeed;
        long timetopok = (long) (ratio*TotalSpeed);
        return timetopok;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
        //  System.out.println("*src: "+src);
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
        //System.out.println("*dest: "+dest);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public geo_location getPos() {
        return pos;
    }

    public void setPos(geo_location pos) {
        this.pos = pos;
    }

    public Queue<Integer> getMyPath() {
        return myPath;
    }

    public void setMyPath(Queue<Integer> myPath) {
        this.myPath = myPath;
    }

    public boolean hasNext() {
        return (this.myPath.peek() != null);
    }
    public String toString()
    {
        return "id: "+id+", value: "+value+", src: "+src+", dest: "+dest+", speed: "+
                speed+", pos: "+pos+", pokemon: "+pokemon+"\n--------\n";
    }

    public int getNextDest() {
        setDest(myPath.poll());
        return dest;
    }
}
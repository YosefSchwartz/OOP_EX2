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
    List<Pokemon> MyPoks;
    Queue<Integer> myPath = new LinkedList<>();



    public Agent(int id,double value, int src, int dest, double speed, geo_location pos){
        this.id = id;
        this.value=value;
        this.src=src;
        this.dest=dest;
        this.speed=speed;
        this.pos=pos;
        MyPoks=new LinkedList<>();

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
        MyPoks=null;
        MyPoks=new LinkedList<>();
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
        double val = Double.MIN_VALUE;
        //  System.out.println("pokemon list: "+poks);
        for(Pokemon p:poks) {
            if(p.agent == null) {
                double shortestPath=ga.shortestPathDist(src, p.getSrc());
                shortestPath+=p.getEL().getRatio()*p.getEL().getEdge().getWeight();
//                double value = p.getValue()/shortestPath;
                if (shortestPath< dist) {
//                if(value>val){
                    closestPok = p;
//                    val=value;
                    dist = shortestPath;
                }
            }
        }
        closestPok.setAgent(this);
        MyPoks.add(closestPok);
        this.setPokemon(closestPok);
        setPath(ga);

         //System.out.println("agent "+getId()+" go to pok "+closestPok.src+"->"+closestPok.getDest());
    }

    public void setPath(dw_graph_algorithms ga){
        List<node_data> shortestPath = ga.shortestPath(src,pokemon.getSrc());
        while (!myPath.isEmpty()) {
            myPath.poll();
        }
        for(int i = 1;i<shortestPath.size();i++) {
            myPath.add(shortestPath.get(i).getKey());
        }
        myPath.add(pokemon.getDest());
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
    }

    public int getDest() {
        return dest;
    }

    public void setDest(int dest) {
        this.dest = dest;
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
    public List<Pokemon> getMyPoks() {
        return MyPoks;
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

    public long time(directed_weighted_graph g, int tmp, String game, List<Pokemon> poks) throws JSONException {
        double w;
        int mark=-1;
        Long time=Long.MAX_VALUE;
       // System.out.println("pokkkkss: "+poks);
        for (Pokemon p: poks) {
            if (tmp == -1) {
                if (src == p.src) {
                    double ratio = p.getEL().getRatio();
                    w = p.getEL().getEdge().getWeight();
                    // System.out.println("time1: "+(long) (w * ratio * 1000 / speed));
                    long t = (long) (ratio * w * 1000 / speed);
                    if (t < time && t != 0) time = t;
                    if(t>1000000) mark=1;
                } else {
                    w = g.getEdge(src, dest).getWeight();
                    //System.out.println("time2: "+(long) (w*1000 / speed));
                    long t = (long) (w * 1000 / speed);
                    if (t < time && t != 0) time = t;
                    if(t>1000000) mark=2;

                }
            } else {
                double edgeDist = g.getNode(src).getLocation().distance(g.getNode(dest).getLocation());
                if (src != p.getSrc()) {
                    double partlyDist = pos.distance(g.getNode(dest).getLocation());
                    double ratio = partlyDist / edgeDist;
                    w = g.getEdge(src, dest).getWeight();
                    //System.out.println("time3: "+(long) (w * ratio * 1000 / speed));
                    long t = (long) (w * ratio * 1000 / speed);
                    if (t < time && t != 0) time = t;
                    if(t>1000000) mark=3;

                } else {
                    w = p.getEL().getEdge().getWeight();
                    if (p.is_in_the_game(game.toString(), p)) {
                        double partlyDist = pos.distance(p.getPos());
                        double ratio = partlyDist / edgeDist;
                        //System.out.println("4time: "+(long) (w * ratio * 1000 / speed));
                        long t = (long) (w * 1000 * ratio / speed);
                        if (t < time && t != 0) time = t;
                        if(t>1000000) mark=4;
                    } else {
                        double partlyDist = pos.distance(g.getNode(dest).getLocation());
                        double ratio = partlyDist / edgeDist;
                        // System.out.println("5time: "+(long) (w * ratio * 1000 / speed));
                        long t = (long) (ratio * w * 1000 / speed);
                        if (t < time && t != 0) time = t;
                        if(t>1000000) mark=6;
                    }
                }
            }
//        if(mark!=-1 && time>1000000)
//        System.out.println(mark);
        }
        //System.out.println(time);
        return time;
    }

    public long timeNodeToNode(directed_weighted_graph g){
        double w = g.getEdge(this.src,this.dest).getWeight();
        return (long)(w*1000.0/speed);
    }

    public double timetoPok(Pokemon p, directed_weighted_graph gg) {
        double ratio = p.getEL().getRatio();
        double w = p.getEL().getEdge().getWeight();
        //w*ratio
        double TotalSpeed = w*ratio*1000 / speed;
        node_data edgeSrc = gg.getNode(p.getEL().getEdge().getSrc());
        node_data edgeDest = gg.getNode(p.getEL().getEdge().getDest());
        double edgeLength = edgeSrc.getLocation().distance(edgeDest.getLocation());
        double destToPok = edgeLength * ratio;
        double timetopok = destToPok / TotalSpeed;
        return timetopok;
    }
}

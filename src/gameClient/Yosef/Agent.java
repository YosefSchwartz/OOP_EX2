package gameClient.Yosef;

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

    Queue<Integer> myPath = new LinkedList<>();



    public Agent(int id,double value, int src, int dest, double speed, geo_location pos){
        this.id = id;
        this.value=value;
        this.src=src;
        this.dest=dest;
        this.speed=speed;
        this.pos=pos;
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

        for(Pokemon p:poks) {
            if(p.agent == null) {
                if (ga.shortestPathDist(src, p.getSrc()) < dist) {
                    closestPok = p;
                    dist = ga.shortestPathDist(src, p.getSrc());
                }
            }
            //TODO need check if null pointer exist
//            else
//                return;
        }
        closestPok.setAgent(this);

        List<node_data> shortestPath = ga.shortestPath(src,closestPok.getSrc());
        for(int i = 0;i<shortestPath.size();i++) {
            myPath.add(shortestPath.get(i).getKey());
        }
        myPath.add(closestPok.getDest());
//        dest= myPath.peek();
    }
    private double TimetoPok(Pokemon p, directed_weighted_graph gg) {
        double ratio = p.getEL().getRatio();
        double w = p.getEL().getEdge().getWeight();
        double agentSpeed = speed;
        double TotalSpeed = w / agentSpeed;
        node_data edgeSrc = gg.getNode(p.getEL().getEdge().getSrc());
        node_data edgeDest = gg.getNode(p.getEL().getEdge().getDest());
        double edgeLength = edgeSrc.getLocation().distance(edgeDest.getLocation());
        double destToPok = edgeLength * ratio;
        double timetopok = destToPok / TotalSpeed;
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

    public void setMyPath(Queue<Integer> myPath) {
        this.myPath = myPath;
    }

    public boolean hasNext() {
        return (this.myPath.peek() != null);
    }

    public int getNextDest() {
        this.src = dest;
        if(myPath.peek()!=null)
            dest = myPath.poll();
        else
            dest = -1;

        return dest;
    }
}

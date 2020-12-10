package gameClient.Yosef;

import api.NodeData;
import api.dw_graph_algorithms;
import api.geo_location;
import api.node_data;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Queue;

public class Agent {
    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private geo_location pos;

    Queue<node_data> myPath = null;





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
            this.dest = myPath.poll().getKey();
        else
            this.dest = -1;
    }


    public void findClosestPokemon (dw_graph_algorithms ga, Pokemon pok){
        int destPlus = pok.getDest();
        int dest = pok.getSrc();




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

    public Queue<node_data> getMyPath() {
        return myPath;
    }

    public void setMyPath(Queue<node_data> myPath) {
        this.myPath = myPath;
    }
}

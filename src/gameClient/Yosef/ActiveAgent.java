package gameClient.Yosef;

import api.NodeData;
import api.geo_location;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.event.ActionListener;

public class ActiveAgent {
    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;

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

    private geo_location pos;

    public ActiveAgent(JSONObject obj) throws JSONException {
        this.id = obj.getInt("id");
        this.value = obj.getDouble("value");
        this.src = obj.getInt("src");
        this.dest = obj.getInt("dest");
        this.speed = obj.getDouble("speed");
        String pos = obj.getString("pos");
        String[] locST = pos.split(",", 3);
        Double[] geoL = new Double[3];
        for (int j = 0; j < locST.length; j++)
            geoL[j] = Double.parseDouble(locST[j]);
        geo_location location = new NodeData.geoLocation(geoL[0], geoL[1], geoL[2]);
        this.pos=location;
    }
}

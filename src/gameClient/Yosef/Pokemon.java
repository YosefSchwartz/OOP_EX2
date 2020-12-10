package gameClient.Yosef;

import api.NodeData;
import api.geo_location;
import api.node_data;
import org.json.JSONException;
import org.json.JSONObject;

public class Pokemon {
    private double value;
    private int type;
    private geo_location pos;
    int src;
    int dest;

    public Pokemon(double value, int type, geo_location pos)
    {
        this.value=value;
        this.type=type;
        this.pos=pos;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public geo_location getPos() {
        return pos;
    }

    public void setPos(geo_location pos) {
        this.pos = pos;
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

    //Create pokemon from JSON Object
    public Pokemon(JSONObject pokObj) throws JSONException {
        this.value=pokObj.getDouble("value");
        this.type= pokObj.getInt("type");
        String pos = pokObj.getString("pos");
        String[] locST = pos.split(",", 3);
        Double[] geoL = new Double[3];
        for (int j = 0; j < locST.length; j++)
            geoL[j] = Double.parseDouble(locST[j]);
        geo_location location = new NodeData.geoLocation(geoL[0], geoL[1], geoL[2]);
        this.pos=location;
    }


}


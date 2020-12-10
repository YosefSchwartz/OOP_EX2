package gameClient.Yosef;

import api.NodeData;
import api.geo_location;
import org.json.JSONException;
import org.json.JSONObject;

public class Agent {
    private int id;
    private double value;
    private int src;
    private int dest;
    private double speed;
    private geo_location pos;




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
}

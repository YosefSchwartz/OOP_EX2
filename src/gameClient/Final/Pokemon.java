package gameClient.Final;

import api.NodeData;
import api.edgeData;
import api.geo_location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Pokemon {
    private double value;
    private int type;
    private geo_location pos;
    int src;
    int dest;
    private edgeData.edgeLocation EL;
    Agent agent;
    boolean isLogin;

    public Pokemon(double value, int type, geo_location pos)
    {
        this.value=value;
        this.type=type;
        this.pos=pos;
        this.EL=null;
        agent=null;
        isLogin=false;
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
    public void setEL(edgeData.edgeLocation el)
    {
        EL=el;
    }
    public edgeData.edgeLocation getEL()
    {
        return EL;
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
        agent=null;
    }

    public boolean has_been_eaten(String poksInTheGame, Pokemon pok) throws JSONException {
        JSONObject newPokemonsObj = new JSONObject(poksInTheGame);
        JSONArray pokemonsArr = newPokemonsObj.getJSONArray("Pokemons");
        boolean ans = false;
        for (int i = 0; i < pokemonsArr.length(); i++) {
            Pokemon newPok = new Pokemon(pokemonsArr.getJSONObject(i).getJSONObject("Pokemon"));
            if (pok.getPos().distance(newPok.getPos()) < 0.000001) {
                ans = true;
            }
        }
        return ans;
    }

    public void setLogin(boolean b)
    {
        this.isLogin=b;
    }
    public boolean getLogin()
    {
        return isLogin;
    }
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Agent getAgent(){
        return agent;
    }
    public String toString()
    {
      String s;
            s="<value: "+value+", type: "+type+", src: "+src+", dest: "+dest+" agent: ";
      if (agent!=null)
          return s+agent.getId()+" src: ("+agent.getSrc()+", "+agent.getDest()+") >\n";
      else
          return s+"non>\n";
    }
}


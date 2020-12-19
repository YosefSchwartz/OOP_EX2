package gameClient;

import api.NodeData;
import api.edgeData;
import api.geo_location;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * This class represent an "pokemon" on our Pokemon game.
 * pokemon is object that static on directed weighted graph, agent search for close pokemons and eat them.
 * each pokemon have position that represent by 3D points, value and type that implements the directed principle.
 * Agent- the agent that associated to this pokemon.
 * src and dest that represent the tops of edge that pokemon on, EL - edge location of this edge.
 */
public class Pokemon {
    private double value;
    private int type;
    private geo_location pos;
    int src;
    int dest;
    private edgeData.edgeLocation EL;
    Agent agent;

    /**
     * @return the value of this pokemon
     */
    public double getValue() { return value; }

    /**
     * if type is positive - the pokemon on the edge that his (src id < dest id),
     * else is on (src id>dest id) edge.
     * @return the type of this pokemon
     */
    public int getType() { return type; }

    /**
     * @return the position of this pokemon, like 3D point.
     */
    public geo_location getPos() { return pos; }

    /**
     * @return id of src node
     */
    public int getSrc() { return src; }

    /**
     * set the src variable of this pokemon
     * @param src - nod id of src
     */
    public void setSrc(int src) {this.src = src; }

    /**
     * @return id of dest node
     */
    public int getDest() { return dest; }

    /**
     * set the dest variable of this pokemon
     * @param dest - nod id of dest
     */
    public void setDest(int dest) { this.dest = dest; }

    /**
     * set edge location data of the edge that this pokemon on.
     * @param el - edge_location
     */
    public void setEL(edgeData.edgeLocation el) { EL=el; }

    /**
     * @return edge location of the edge that this pokemon on.
     */
    public edgeData.edgeLocation getEL() { return EL; }


    /**
     * constructor function - create new Pokemon object from JSON string.
     * @param pokObj - string at JSON format
     * @throws JSONException -
     */
    public Pokemon(JSONObject pokObj) throws JSONException {
        this.value=pokObj.getDouble("value");
        this.type= pokObj.getInt("type");
        String pos = pokObj.getString("pos");
        //split the string that represent position to 3 double value.
        String[] locST = pos.split(",", 3);
        Double[] geoL = new Double[3];
        for (int j = 0; j < locST.length; j++)
            geoL[j] = Double.parseDouble(locST[j]);
        this.pos= new NodeData.geoLocation(geoL[0], geoL[1], geoL[2]);
        //at first, for each pokemon agent is null.
        agent=null;
    }

    /**
     * check if pokemon is still alive, get the update data from JSON format.
     * @param poksInTheGame - JSON string that represent the active pokemon.
     * @param pok - pokemon that we want to know about.
     * @return - true if this pokemon still alive.
     * @throws JSONException
     */
    public boolean isInTheGame(String poksInTheGame, Pokemon pok) throws JSONException {
        JSONObject newPokemonsObj = new JSONObject(poksInTheGame);
        JSONArray pokemonsArr = newPokemonsObj.getJSONArray("Pokemons");
        for (int i = 0; i < pokemonsArr.length(); i++) {
            Pokemon newPok = new Pokemon(pokemonsArr.getJSONObject(i).getJSONObject("Pokemon"));
            //if this pokemon near up to epsilon to alive pokemon
            if (pok.getPos().distance(newPok.getPos()) < 0.000001)
                return true;
        }
        return false;
    }

    /**
     * set the agent to this pokemon
     * @param agent - agent
     */
    public void setAgent(Agent agent) { this.agent = agent; }

    /**
     * @return the agent that associated to this pokemon
     */
    public Agent getAgent(){ return agent; }

    /**
     * toString function
     * @return
     */
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
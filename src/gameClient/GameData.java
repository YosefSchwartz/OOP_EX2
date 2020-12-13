package gameClient;

import api.directed_weighted_graph;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;
import gameClient.util.Range2Range;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a multi Agents Arena which move on a graph - grabs Pokemons and avoid the Zombies.
 * @author boaz.benmoshe
 *
 */
public class GameData {
	private directed_weighted_graph g;
	private List<Agent> agents;
	private List<Pokemon> pokemons;
	private List<String> info;
	private double TimeToEnd;
	private static Point3D MIN = new Point3D(0, 100,0);
	private static Point3D MAX = new Point3D(0, 100,0);

	public GameData() {
		info = new ArrayList<String>();
	}
	public GameData(directed_weighted_graph g, List<Agent> ag, List<Pokemon> p) {
		TimeToEnd=0;
		this.g = g;
		this.setAgents(ag);
		this.setPokemons(p);
	}
	public void setTimeToEnd(double t)
	{
		TimeToEnd=t;
	}
	public double getTimeToEnd()
	{
		return TimeToEnd;
	}
	public void setPokemons(List<Pokemon> poks) {
		this.pokemons = poks;
	}
	public void setAgents(List<Agent> ag) {
		this.agents = ag;
	}
	public void setGraph(directed_weighted_graph g) {this.g =g;}

	private void init( ) { //fined the most edges nodes
		MIN=null; MAX=null;
		double x0=0,x1=0,y0=0,y1=0;
		Iterator<node_data> iter = this.g.getV().iterator();
		while(iter.hasNext()) {
			geo_location c = iter.next().getLocation();
			if(MIN==null) {
			x0 = c.x();
			y0=c.y();
			x1=x0;
			y1=y0;
			MIN = new Point3D(x0,y0);
			}
			if(c.x() < x0) {x0=c.x();}
			if(c.y() < y0) {y0=c.y();}
			if(c.x() > x1) {x1=c.x();}
			if(c.y() > y1) {y1=c.y();}
		}
		double dx = x1-x0, dy = y1-y0;
		MIN = new Point3D(x0-dx/10,y0-dy/10);
		MAX = new Point3D(x1+dx/10,y1+dy/10);
		
	}
	public List<Agent> getAgents() {return agents;}
	public List<Pokemon> getPokemons() {return pokemons;}

	
	public directed_weighted_graph getGraph() {
		return this.g;
	}
	public List<String> get_info() {
		return info;
	}
	public void set_info(List<String> _info) {
		this.info = _info;
	}

	private static Range2D GraphRange(directed_weighted_graph g) {
		Iterator<node_data> itr = g.getV().iterator();
		double x0=0,x1=0,y0=0,y1=0;
		boolean first = true;
		while(itr.hasNext()) {
			geo_location p = itr.next().getLocation();
			if(first) {
				x0=p.x(); x1=x0;
				y0=p.y(); y1=y0;
				first = false;
			}
			else {
				if(p.x()<x0) {x0=p.x();}
				if(p.x()>x1) {x1=p.x();}
				if(p.y()<y0) {y0=p.y();}
				if(p.y()>y1) {y1=p.y();}
			}
		}
		Range xr = new Range(x0,x1);
		Range yr = new Range(y0,y1);
		return new Range2D(xr,yr);
	}
	public static Range2Range w2f(directed_weighted_graph g, Range2D frame) {
		Range2D world = GraphRange(g);
		Range2Range ans = new Range2Range(world, frame);
		return ans;
	}

}

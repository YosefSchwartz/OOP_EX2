package api;

import java.util.Collection;
import java.util.HashMap;
/**
 * This class represents a directional weighted graph.
 * It contain one map of nodes, and one map with inner map (at value field) of edges.
 * support some function to get information about the graph
 */
public class DWGraph_DS implements directed_weighted_graph {
    HashMap<Integer, node_data> graph;
    HashMap<Integer, HashMap<Integer, edge_data>> edges;
    int MC, edgeNum;

    /**
     * constructor function
     */
    public DWGraph_DS() {
        graph = new HashMap<>();
        edges = new HashMap<>();
        MC = 0;
        edgeNum = 0;
    }
    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (!(graph.containsKey(key)))
            return null;
        return graph.get(key);
    }
    /**
     * Returns the data of the edge (src,dest), null if none.
     * @param src - id of source node
     * @param dest - id of destination node
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        //Check if both nodes are in the graph, and if this edge are exist
        if (!(graph.containsKey(src)) || !(graph.containsKey(dest)) ||
                !(edges.get(src).containsKey(dest)))
            return null;
        return edges.get(src).get(dest);
    }
    /**
     * Adds a new node to the graph with the given node_data.
     * @param n - new node_data to add
     */
    @Override
    public void addNode(node_data n) {
        graph.put(n.getKey(), n);
        edges.put(n.getKey(), new HashMap<>());
        MC++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        if (!(graph.containsKey(src)) || !(graph.containsKey(dest)) || getEdge(src, dest) != null)
            return;
        edge_data e = new edgeData(getNode(src), getNode(dest), w);
        edges.get(src).put(dest, e);
        edgeNum++;
        MC++;
    }

    @Override
    public Collection<node_data> getV() {
        return graph.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        if (!(graph.containsKey(node_id)))
            return null;
        return edges.get(node_id).values();
    }

    @Override
    public node_data removeNode(int key) {
        if (!(graph.containsKey(key)))
            return null;
        node_data tmp = graph.get(key);

        for (node_data n : getV()) {
            if (edges.get(n.getKey()).containsKey(key)) {
                edges.get(n.getKey()).remove(key);
                edgeNum--;
            }
        }

        MC++;
        edgeNum -= getE(key).size();
        edges.remove(key);
        graph.remove(key);

        return tmp;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        edge_data e = getEdge(src, dest);
        if (e == null)
            return null;
        edges.get(src).remove(dest);
        return e;
    }

    @Override
    public int nodeSize() {
        return graph.size();
    }

    @Override
    public int edgeSize() {
        return edgeNum;
    }

    @Override
    public int getMC() {
        return MC;
    }

    public String toString() {
        String s = "";
        for (node_data n : getV()) {
            s += "KEY: " + n.getKey() + "\n";
            s += "EDGES: ";
            for (edge_data e : getE(n.getKey())) {
                s += "(" + e.getSrc() + "->" + e.getDest() + " W:" + e.getWeight() + ")";
            }
            s += "\n";
        }
        return s;
    }

}


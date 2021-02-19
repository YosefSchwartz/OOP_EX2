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
     *
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
     *
     * @param src  - id of source node
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
     *
     * @param n - new node_data to add
     */
    @Override
    public void addNode(node_data n) {
        if(n == null) return;
        graph.put(n.getKey(), n);
        edges.put(n.getKey(), new HashMap<>());
        MC++;
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (!(graph.containsKey(src)) || !(graph.containsKey(dest)) || getEdge(src, dest) != null)
            return;
        edge_data e = new edgeData(getNode(src), getNode(dest), w);
        edges.get(src).put(dest, e);
        edgeNum++;
        MC++;
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return graph.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     *
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (!(graph.containsKey(node_id)))
            return null;
        return edges.get(node_id).values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     */
    @Override
    public node_data removeNode(int key) {
        if (!(graph.containsKey(key)))
            return null;
        node_data tmp = graph.get(key);

        for (node_data n : getV()) {
            if (edges.get(n.getKey()).containsKey(key)) {
                edges.get(n.getKey()).remove(key);
                edgeNum--;
                MC++;
            }
        }

        MC++;
        edgeNum -= getE(key).size();
        edges.remove(key);
        graph.remove(key);

        return tmp;
    }

    /**
     * Deletes the edge from the graph,
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        edge_data e = getEdge(src, dest);
        if (e == null)
            return null;
        edges.get(src).remove(dest);
        MC++;
        edgeNum--;
        return e;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     *
     * @return - size of nodes in graph
     */
    @Override
    public int nodeSize() {
        return graph.size();
    }

    /**
     * Returns the number of edges (assume directional graph).
     *
     * @return - size of edges in graph
     */
    @Override
    public int edgeSize() {
        return edgeNum;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return - size of act that performed on this graph.
     */
    @Override
    public int getMC() {
        return MC;
    }

    /** return true if the graph is equal to the
     * graph in the class.
     * @param other
     * @return
     */
    public boolean equals(Object other)
    {
        if(!(other instanceof directed_weighted_graph)) {
            return false;
        }
        directed_weighted_graph g= (directed_weighted_graph)(other);
        if(g.nodeSize()!=nodeSize() || g.edgeSize()!=edgeSize()) {
            return false;
        }
        for (node_data n: getV()) {
            node_data gNode = g.getNode(n.getKey());
            if (gNode == null) {
                return false;
            }
            if(g.getE(n.getKey()).size()!=getE(n.getKey()).size()) {
                return false;
            }
            for (edge_data e: getE(n.getKey())) {
                edge_data e1=g.getEdge(e.getSrc(), e.getDest());
               if(e1==null | e1.getWeight()!=e.getWeight()) {
                  return false;
               }
            }
        }
        return true;
    }

    /**
     * toString function
     * @return  - main data of this graph
     */
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


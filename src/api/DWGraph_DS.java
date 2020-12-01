package api;

import java.util.Collection;
import java.util.HashMap;

public class DWGraph_DS implements directed_weighted_graph {
    //eden
    HashMap<Integer, node_data> graph;
    HashMap<Integer, HashMap<Integer, edge_data>> edges;
    int MC, edgeNum;

    public DWGraph_DS() {
        graph = new HashMap<>();
        edges = new HashMap<>();
        MC = 0;
        edgeNum = 0;
    }

    @Override
    public node_data getNode(int key) {
        if (!(graph.containsKey(key)))
            return null;
        return graph.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if (!(graph.containsKey(src)) || !(graph.containsKey(dest)) ||
                !(edges.get(src).containsKey(dest)))
            return null;
        return edges.get(src).get(dest);
    }

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
        edge_data e = new edgeData(src, dest, w);
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
//    public String toString(){
//        String g="";
//        for ( node_data n: getV())
//            g+=n+ ", edges: "+ getE(n.getKey())+"\n";
//        return g;
//    }

    private static class edgeData implements edge_data {
        int src;
        int dest;
        double weight;
        String info;
        int tag;

        public edgeData(int src, int dest, double weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
            info = null;
            tag = 0;
        }

        @Override
        public int getSrc() {
            return src;
        }

        @Override
        public int getDest() {
            return dest;
        }

        @Override
        public double getWeight() {
            return weight;
        }

        @Override
        public String getInfo() {
            return info;
        }

        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        @Override
        public int getTag() {
            return tag;
        }

        @Override
        public void setTag(int t) {
            this.tag = t;
        }
    }

    private static class edgeLocation implements edge_location {
        edgeData edge;
        double ratio;

        public edgeLocation() {
            edge = null;
            ratio = 0;
        }

        @Override
        public edge_data getEdge() {
            return edge;
        }

        @Override
        public double getRatio() {
            return ratio;
        }
    }

}


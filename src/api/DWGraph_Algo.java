package api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {

    directed_weighted_graph ga = new DWGraph_DS();
    HashMap<Integer, node_data> re;

    @Override
    public void init(directed_weighted_graph g) {
        this.ga = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return ga;
    }

    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph gaCopy = new DWGraph_DS();
        //Add all nodes
        for (node_data tmp : ga.getV()) {
            node_data n = new NodeData(tmp.getKey(), tmp.getLocation());
            gaCopy.addNode(n);
        }
        //Add all edges
        for (node_data tmpNode : ga.getV()) {
            for (edge_data tmpEdge : ga.getE(tmpNode.getKey())) {
                gaCopy.connect(tmpEdge.getSrc(), tmpEdge.getDest(), tmpEdge.getWeight());
            }
        }
        return gaCopy;
    }

    public directed_weighted_graph oppositeGraph(directed_weighted_graph g) {
        directed_weighted_graph OppGraph = new DWGraph_DS();
        for (node_data n : g.getV()) { //copy all nodes
            node_data n1 = new NodeData(n.getKey(), n.getLocation());
            OppGraph.addNode(n1);
        }
        for (node_data n : g.getV()) {//copy all edges - opposite direction
            for (edge_data e : g.getE(n.getKey()))
                OppGraph.connect(e.getDest(), e.getSrc(), e.getWeight());
        }

        return OppGraph;
    }

    //TODO
    /*
    rename BFS to correct name
     */
    private boolean BFS(directed_weighted_graph g, node_data node) {
        resetTagTo0(); //O(v)
        final int mark = 1;

        Stack<node_data> t = new Stack<>();
        t.push(node);
        node.setTag(mark);

        while (!t.isEmpty()) { //O(v)
            node_data tmpNode = t.pop();
            for (edge_data tmp : g.getE(tmpNode.getKey())) { //O(v-1) --> O(v^2)
                node_data currNode = g.getNode(tmp.getDest());
                if (currNode.getTag() != mark) {
                    t.push(currNode);
                    currNode.setTag(mark);
                }
            }
        }
        for (node_data n : g.getV()) //O(v)
            if (n.getTag() != mark)
                return false;
        return true;
    }

    private void resetTagTo0() { //O(v)
        for (node_data tmp : ga.getV())
            tmp.setTag(0);
    }

    @Override
    public boolean isConnected() {
        boolean ans;
        if (ga == null) return false;

        if (ga.nodeSize() <= 1) return true;

        node_data tmp = ga.getV().stream().findFirst().get();
        ans = BFS(ga, tmp);
        if (ans) {
            directed_weighted_graph gaOpp = oppositeGraph(ga);
            ans = BFS(gaOpp, tmp);
        }
        return ans;
    }

    private void Dijkstra(int src) {
        final int mark = 1;
        PriorityQueue<node_data> dist = new PriorityQueue<>();
        re = new HashMap<Integer, node_data>(); //save the node that we came for to each key {sun, father}
        resetTagTo0();
        for (node_data n : ga.getV()) // go over all the graph's nodes
        {
            n.setWeight(Double.MAX_VALUE); //reset the weight to infinity
            re.put(n.getKey(), null); //reset all nodes to null (the "father")
        }

        node_data tmp = ga.getNode(src);
        tmp.setWeight(0.0); //set the tag-the distance of the ex1.ex1.src from the ex1.ex1.src to 0
        dist.add(tmp); //add him to the the queue of the distances
        while (!dist.isEmpty()) {
            tmp = dist.poll(); //poll and save the node with the lowest distance from the ex1.ex1.src in the queue
            for (edge_data e : ga.getE(tmp.getKey())) //go over temp's neighbor
            {
                int destKey = e.getDest();
                if (ga.getNode(destKey).getTag() != mark) //if the neighbor hasn't been visited
                {
                    double NewDist = e.getWeight() + tmp.getWeight(); //temp's distance from the ex1.ex1.src+ the edge between the tmp and the neighbor
                    if (NewDist < ga.getNode(destKey).getWeight()) { //if the NewDist < from the present distance of the neighbor
                        ga.getNode(destKey).setWeight(NewDist);
                        re.replace(destKey, tmp); //save tmp ad the node that we came for to this neighbor
                        dist.add(ga.getNode(destKey));
                    }
                }
            }
            tmp.setTag(mark);
        }
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if (ga == null)
            if (ga.getNode(src) == null || ga.getNode(dest) == null) //if the ex1.ex1.src or the dest isn't at the graph return -1
                return -1;

        Dijkstra(src);
        if (ga.getNode(dest).getWeight() == Double.MAX_VALUE) //if the dest's tag remain infinity- he has no path from the ex1.ex1.src
            return -1;
        return ga.getNode(dest).getWeight();//return the dest's tag(his distance from the ex1.ex1.src)
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        List<node_data> path = new ArrayList<>();
        if (src == dest) {
            path.add(ga.getNode(src));
            return path;
        }
        if (shortestPathDist(src, dest) == -1) //if there is no nodes with ex1.ex1.src or dest key return null
            return null;
        path.add(ga.getNode(dest));
        for (node_data n = re.get(dest); n != null; n = re.get(n.getKey())) //reconstruct the path from dest to ex1.ex1.src
            path.add(n);
        Collections.reverse(path); //reverse the path so ex1.ex1.src to dest
        return path;
    }

    @Override
    public boolean save(String file) {
        /*Pattern of our save
            JSONObj graph-
            {JSONArr edges,JSONArr nodes}

            Arr edges-
            [{JSONObj edge},...,{JSONObj edges}]

            Each JSONObj edge-
            {(int) src,(double) w,(int) dest}

            JSONArr nodes-
            [{JSONObj node},...,{JSONObj node}]

            Each JSONObj node-
            {(String) pos, int id}

            total-
            {"Edges":[{src,w,dest},...],"Nodes":[{pos,id}...]}
         */
        //Create new Json object - graph
        JSONObject graph = new JSONObject();
        //Declare two Json arrays
        JSONArray edges = new JSONArray();
        JSONArray nodes = new JSONArray();
        try {
            //For each node
            for (node_data n : ga.getV()) {
                //Scan all his edges
                for (edge_data e : ga.getE(n.getKey())) {
                    //Declare Json object - edge
                    JSONObject edge = new JSONObject();
                    //Insert the data to this object
                    edge.put("src", e.getSrc());
                    edge.put("w", e.getWeight());
                    edge.put("dest", e.getDest());
                    //Insert this object to edges array
                    edges.put(edge);
                }
                //Declare Json object - node
                JSONObject node = new JSONObject();
                //Insert the data to this object
                node.put("pos", n.getLocation().x() + "," + n.getLocation().y() + "," + n.getLocation().z());
                node.put("id", n.getKey());
                //Insert this object to nodes array
                nodes.put(node);
            }
            //Insert this both arrays to the graph object
            graph.put("Edges", edges);
            graph.put("Nodes", nodes);

            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(graph.toString());
            pw.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) {
        try {
            //JSONObject that represent the graph from JSON file
            JSONObject graph = new JSONObject(new String(Files.readAllBytes(Paths.get(file))));

            //Two JSONArray that represents the Edges and Nodes
            JSONArray edges = graph.getJSONArray("Edges");
            JSONArray nodes = graph.getJSONArray("Nodes");

            //Declare of the new graph
            directed_weighted_graph loadedGraph = new DWGraph_DS();
            //For each Node, get the data ,make new node and add him to the graph
            for (int i = 0; i < nodes.length(); i++) {
                JSONObject nJSON = nodes.getJSONObject(i);

                //geoLocation (call "pos")
                String pos = nodes.getJSONObject(i).getString("pos");
                //Should be extract from string that add 3 elements (x,y,z) and split by ","
                String[] locST = pos.split(",", 3);
                //Parse the string to double
                Double[] geoL = new Double[3];
                for (int j = 0; j < locST.length; j++)
                    geoL[j] = Double.parseDouble(locST[j]);

                //Add this location to new geo_location object
                geo_location location = new NodeData.geoLocation(geoL[0], geoL[1], geoL[2]);
                //Build node that contain the id an pos
                node_data n = new NodeData(nJSON.getInt("id"),location);

                //Add this node to the graph
                loadedGraph.addNode(n);
            }
            //For each edge, get the data and connect two vertex by the data
            for (int i = 0; i < edges.length(); i++) {
                JSONObject edge = edges.getJSONObject(i);
                int src = edge.getInt("src");
                double w = edge.getDouble("w");
                int dest = edge.getInt("dest");

                loadedGraph.connect(src, dest, w);
            }
            //init the new graph to this class graph
            this.ga = loadedGraph;
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public String toString()
    {
        return ga.toString();
    }
}

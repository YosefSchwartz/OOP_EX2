package api;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/**
 * This Class represents a Directed (positive) Weighted Graph Theory Algorithms,
 * and two function to save graph to JSON file and read graph from JSON file.
 * connectivity and shortest path base on Dijkstra's algorithm.
 *
 * you can read more here:
 * https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 */
public class DWGraph_Algo implements dw_graph_algorithms {
    directed_weighted_graph ga = new DWGraph_DS();
    //This hashmap used by Dijkstra's algorithms,
    HashMap<Integer, node_data> re;

    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g - the graph to perform some algorithm on
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.ga = g;
    }
    /**
     * Return the underlying graph of which this class works.
     * @return - pointer to the graph
     */
    @Override
    public directed_weighted_graph getGraph() {
        return ga;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return - copy of this graph
     */
    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph gaCopy = new DWGraph_DS();
        //Add all nodes
        for (node_data tmp : ga.getV()) {
            node_data n = new NodeData(tmp.getKey(), tmp.getLocation());
            gaCopy.addNode(n);
        }
        //Add all edges
        for (node_data tmpNode : ga.getV())
            for (edge_data tmpEdge : ga.getE(tmpNode.getKey()))
                gaCopy.connect(tmpEdge.getSrc(), tmpEdge.getDest(), tmpEdge.getWeight());

        return gaCopy;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     * @return - True iff this graph is one SCC (=Strongly Connectivity Component).
     */
    @Override
    public boolean isConnected() {
        boolean ans;
        // Null graph
        if (ga == null) return false;
        //1 node is always SCC
        if (ga.nodeSize() <= 1) return true;

        node_data tmp = ga.getV().stream().findFirst().get();

        //We need to check if random node s had path to all other nodes,
        //and if in this graph transpose s is still connect to other nodes
        //if both are true, the graph is one SCC.
        ans = BFS(ga, tmp);
        if (ans) {
            directed_weighted_graph gaTranspose = graphTranspose(ga);
            ans = BFS(gaTranspose, tmp);
        }
        return ans;
    }

    private void Dijkstra(int src) {
        final int mark = 1;
        PriorityQueue<node_data> dist = new PriorityQueue<>();
        re = new HashMap<>(); //save the node that we came for to each key {sun, father}
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
    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return - total weight of this shortest path
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (ga.getNode(src) == null || ga.getNode(dest) == null) //if the src or the dest isn't at the graph return -1
            return -1;

        Dijkstra(src);
        //if the dest tag remain infinity- he has no path from src to dest
        if (ga.getNode(dest).getWeight() == Double.MAX_VALUE)
            return -1;
        //return the dest tag(his distance from the src)
        return ga.getNode(dest).getWeight();
    }
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return - List<node_data> that contain the path from src to dest
     */
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        List<node_data> path = new ArrayList<>();
        //If src=dest, the path is only this node
        if (src == dest) {
            path.add(ga.getNode(src));
            return path;
        }
        //If shortest path is (-1), it mean that no path available
        if (shortestPathDist(src, dest) == -1) //if there is no nodes with ex1.ex1.src or dest key return null
            return null;
        path.add(ga.getNode(dest));
        //AWARE! - special rule to this for loop!
        for (node_data n = re.get(dest); n != null; n = re.get(n.getKey())) //reconstruct the path from dest to ex1.ex1.src
            path.add(n);

        //need to reverse the path, because it was from dest to src
        Collections.reverse(path);
        return path;
    }
    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
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
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
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

/**
 * toString function
 */
    public String toString() { return ga.toString(); }

//////////////////////////////////  Private functions //////////////////
    /**
     * get graph and return new graph with same nodes but opposite edges.
     * will not affect on original graph.
     * @param g - original graph
     * @return - new graph transpose
     */
    private directed_weighted_graph graphTranspose(directed_weighted_graph g) {
        directed_weighted_graph graphT = new DWGraph_DS();

        for (node_data n : g.getV()) { //copy all nodes
            node_data newNode = new NodeData(n.getKey(), n.getLocation());
            graphT.addNode(newNode);
        }
        for (node_data n : g.getV()) //copy all edges - opposite direction
            for (edge_data e : g.getE(n.getKey()))
                graphT.connect(e.getDest(), e.getSrc(), e.getWeight());

        return graphT;
    }

    /**
     * based on BFS algorithm
     * to more information:
     * https://en.wikipedia.org/wiki/Breadth-first_search
     *
     * Note: name like grayNode and Black came from principle that unvisited node is white,
     * in process is gray, and when we finish with the node is black
     *
     * @param g - graph
     * @param startNode - start node
     * @return - True iff exist path from start node to all other nodes in graph
     */
    private boolean BFS(directed_weighted_graph g, node_data startNode) {
        resetTagTo0(); //O(v)
        final int Black = 1;

        Stack<node_data> grayNodes = new Stack<>();
        grayNodes.push(startNode);
        startNode.setTag(Black);

        while (!grayNodes.isEmpty()) { //O(v) 
            node_data tmpNode = grayNodes.pop();
            for (edge_data tmp : g.getE(tmpNode.getKey())) {    //O(v-1)
                node_data currNode = g.getNode(tmp.getDest());
                if (currNode.getTag() != Black) {
                    grayNodes.push(currNode);
                    currNode.setTag(Black);                     //Total O(v^2)
                }
            }
        }
        //If there is node in graph that his tag is not black,
        //mean that we start from startNode and don't visit in all nodes.
        for (node_data n : g.getV())    //O(v)
            if (n.getTag() != Black)
                return false;

        return true;
    }

    /**
     * reset tag field in all nodes to 0
     */
    private void resetTagTo0() {    //O(v)
        for (node_data n : ga.getV())
            n.setTag(0);
    }
    private void resetInfo() {    //O(v)
        for (node_data n : ga.getV())
            n.setInfo(null);
    }
    public List<Integer> connectedComponent (int id){
        List<Integer> scc = new LinkedList<>();
        if(this.ga == null)
            return scc;
        if(!ga.getV().contains(ga.getNode(id)))
            return scc;
        final String beHere = "We be here!";
        for (node_data n : ga.getV()){
            if(shortestPathDist(n.getKey(),id)!= -1 && shortestPathDist(id,n.getKey()) != -1){
                scc.add(n.getKey());
                n.setInfo(beHere);
            }
        }
        return scc;
    }

    public List<List<Integer>> connectedComponents(){
        List<List<Integer>> sccs = new LinkedList<>();
        if(ga == null) return sccs;
        resetInfo();
        for(node_data n :ga.getV()){
            if (n.getInfo() == null){
                sccs.add(connectedComponent(n.getKey()));
            }
        }
        return sccs;
    }
}

//package api;
//
//import org.junit.jupiter.api.*;
//
//import java.lang.reflect.Array;
//import java.util.Arrays;
//import java.util.Random;
//
//public class DWGraph_DS_Test {
//    static directed_weighted_graph graph;
//    static Random rnd = new Random(330);
//
//        while(graph.edgeSize()<20){
//            int a = nextRnd(0,graph.nodeSize());
//            int b = nextRnd(0,graph.nodeSize());
//            Random r = new Random();
//            double w = r.nextDouble()*20;
//            graph.connect(nodes[a],nodes[b],w);
//        }
//        return graph;
//    }
//    private void AddNodesToGraph(int sumNodes) {
//        for(int i=0;i<sumNodes;i++){
//            node_data n = new NodeData();
//            graph.addNode(n);
//        }
//
//    }
//
//    @BeforeAll
//    public static void initGraphToNewGraph() {
//        graph = new DWGraph_DS();
//    }
//
//    /**
//     * 1. check getNode-
//     * a. that doesn't exist in graph
//     * b. that exist in graph
//     * c. the node that insert equal to node we get (by key)
//     */
//    @Test
//    public void getNode() {
//        node_data node = new NodeData();
//        graph.addNode(node);
//        node_data tmp = graph.getNode(2);
//        Assertions.assertNull(tmp, "1a. FAIL!\n\t should be null");
//        tmp = graph.getNode(node.getKey());
//        Assertions.assertNotNull(tmp, "1b. FAIL!\n\t should be not null");
//        Assertions.assertEquals(node.getKey(), tmp.getKey(), "1c. FAIL!\n\t should be 1");
//    }
//
//    /**
//     * 2. check to getEdge-
//     * a. between two nodes that one of them doesn't exist
//     * b. between two nodes isn't edge
//     * c. between two nodes connected
//     */
//    @Test
//    public void getEdge() {
//        //Add 3 nodes
//        AddNodesToGraph(3);
//        graph.connect(0, 2, 2.4);
//        Assertions.assertEquals(-1, graph.getEdge(1, 2), "2.a FAIL!\n\t should be -1");
//        Assertions.assertEquals(-1, graph.getEdge(1, 3), "2.b FAIL!\n\t should be -1");
//        graph.connect(1, 2, 5.6);
//        Assertions.assertEquals(5.6, graph.getEdge(1, 2), "2.c FAIL!\n\t should be 5.6");
//    }
//
//    /**
//     * 3. check nodeSize (include addNode and removeNode functions)-
//     * a. after adding some nodes.
//     * b. after adding some nodes and remove part of them.
//     */
//    @Test
//    public void NodeSize() {
//        AddNodesToGraph(4);
//        Assertions.assertEquals(4, graph.nodeSize(), "3.a FAIL!\n\t should be 4");
//        graph.removeNode(3);
//        Assertions.assertEquals(3, graph.nodeSize(), "3.b FAIL!\n\t should be 3");
//    }
//
//    /**
//     * 4. check edgeSize (include connect, removeNode and removeEdge functions) -
//     * a. build graph with 10 node and 20 edges.
//     * b. remove some edges.
//     * c. remove node with some edges.
//     * d. try to connect nodes that doesn't exist.
//     * e. try to connect edges that already exist.
//     */
//    @Test
//    public void edgeSize() {
//        graph = getGraph10V20E();
//
//        Assertions.assertEquals(20, g.edgeSize(), "5.a FAIL!\n\t should be 20");
//        graph.removeEdge(0, 1);
//        graph.removeEdge(1, 6);
//        graph.removeEdge(8, 6);
//        Assertions.assertEquals(17, graph.edgeSize(), "5.b FAIL!\n\t should be 17");
//        graph.removeNode(1);
//        Assertions.assertEquals(13, graph.edgeSize(), "5.c FAIL!\n\t should be 13");
//        graph.connect(1, 6, 2);
//        Assertions.assertEquals(13, graph.edgeSize(), "5.d FAIL!\n\t should be 13");
//        graph.connect(8, 9, 2);
//        Assertions.assertEquals(13, graph.edgeSize(), "5.e FAIL!\n\t should be 13");
//
//    }
//    private int[] nodesToArray(directed_weighted_graph g){
//        int [] nodes = new int[graph.nodeSize()];
//        int i =0;
//        for(node_data n: graph.getV()){
//            nodes[i++]=n.getKey();
//        }
//        Arrays.sort(nodes);
//        return nodes;
//    }
//    private directed_weighted_graph getGraph10V20E() {
//        for(int i=0;i<10;i++){
//            node_data n = new NodeData();
//            graph.addNode(n);
//        }
//        int[] nodes = nodesToArray(graph);
//
//        while(graph.edgeSize()<20){
//            int a = nextRnd(0,graph.nodeSize());
//            int b = nextRnd(0,graph.nodeSize());
//            Random r = new Random();
//            double w = r.nextDouble()*20;
//            graph.connect(nodes[a],nodes[b],w);
//        }
//        return graph;
//    }
//    private int[] nodesToArray(directed_weighted_graph g){
//        int [] nodes = new int[graph.nodeSize()];
//        int i =0;
//        for(node_data n: graph.getV()){
//            nodes[i++]=n.getKey();
//        }
//        Arrays.sort(nodes);
//        return nodes;
//    }
//    private directed_weighted_graph getGraph10V20E() {
//        for(int i=0;i<10;i++){
//            node_data n = new NodeData();
//            graph.addNode(n);
//        }
//        int[] nodes = nodesToArray(graph);
//
//    private static int nextRnd(int min, int max) {
//        double v = nextRnd(0.0+min, (double)max);
//        int ans = (int)v;
//        return ans;
//    }
//    private static double nextRnd(double min, double max) {
//        double d = rnd.nextDouble();
//        double dx = max-min;
//        double ans = d*dx+min;
//        return ans;
//    }
////    /**
////     * 6. check getV() (include getV(node_id)) -
////     * a. get V from graph with 10 nodes
////     * b. get V(i) from i node
////     */
////    @Test
////    public void getV() {
////        graph = getGraph10V20E();
////        Collection<node_info> x = graph.getV();
////        Assertions.assertEquals(10, x.size(), "6.a FAIL!\n\t should be 10");
////        x = graph.getV(1);
////        Assertions.assertEquals(6, x.size(), "6.b FAIL!\n\t should be 6");
////    }
////
////    /**
////     * 7. check MC-
////     * a. after build graph with 10 nodes and 20 edges
////     * b. after remove 1 node with 5 edges
////     * c. after remove 1 edge
////     */
////    @Test
////    public void MC() {
////        graph = getGraph10V20E();
////        Assertions.assertEquals(30, graph.getMC(), "7.a FAIL!\n\t should be 30");
////        graph.removeNode(0);
////        Assertions.assertEquals(36, graph.getMC(), "7.b FAIL!\n\t should be 36");
////        graph.removeEdge(2, 6);
////        Assertions.assertEquals(37, graph.getMC(), "7.c FAIL!\n\t should be 37");
////    }
////
////
////
////}

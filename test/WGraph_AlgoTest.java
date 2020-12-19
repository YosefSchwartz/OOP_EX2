import api.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class WGraph_AlgoTest {

    private static int[] nodes(directed_weighted_graph g) { //create an array with all the graph's keys
        int size = g.nodeSize();
        Collection<node_data> V = g.getV();
        node_data[] nodesArray = new node_data[size];
        V.toArray(nodesArray); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodesArray[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
    @BeforeAll
    public static void begin()
    {
        System.out.println("Running tests for Ex1, DWGraph_Algo class");
    }

    @Test
    public void init_copy_Test()
    {
        Random rnd = new Random(1);
        directed_weighted_graph g1 = DWGraph_DSTest.graphCreator(20);
        int[] nodes = nodes(g1);
        dw_graph_algorithms ga= new DWGraph_Algo();
        ga.init(g1);
        assertEquals(g1, ga.getGraph());
        for(int i=0; i<=(nodes.length)/2;i++) //remove half of the nodes of g1
            g1.removeNode(i);
        assertEquals(g1, ga.getGraph()); //check if the nodes removed from ga too and the graphs is steel equals
        directed_weighted_graph copyOFga= ga.copy(); //create a copy of ga
        assertEquals(copyOFga, ga.getGraph());
        ga.getGraph().removeNode(nodes.length-2); //remove a node from ga
        boolean equals4=copyOFga.equals(ga.getGraph());
        assertEquals(false, equals4); //check that the remove doesn't happened on the copy graph too
        ga.getGraph().addNode(new NodeData(nodes.length-2, new NodeData.geoLocation(0,0,0))); //adding the node that has been remove back
        copyOFga.removeNode(nodes.length-3); //remove a node from the copy graph
        boolean equals5=copyOFga.equals(ga.getGraph());
        assertEquals(false, equals5); //check that the remove doesn't happened on ga too
    }
    @Test
    public void save_load_Test() throws IOException {
        int v=3, e=3;
        Random rnd = new Random(1);
        directed_weighted_graph graph= DWGraph_DSTest.graphCreator(v);
        int n1=0,n2=0;
        for (int i=0;i<e;i++){
            double w = Math.random()*20;
            n1 = rnd.nextInt((v-1));
            n2 = rnd.nextInt((v-1));
            graph.connect(n1,n2, w);
        }
        dw_graph_algorithms ga1 = new DWGraph_Algo();
        dw_graph_algorithms ga2 = new DWGraph_Algo();
        ga1.init(graph);
        ga1.save("ga1save.json"); //save g1
        ga2.load("ga1save.json");//load the g1 text file to g2
        assertEquals(graph, ga2.getGraph()); //check if g2=graph(that g1 init to) like supposed to
        assertEquals(ga1.getGraph(), ga2.getGraph());//check if g2=g1 like supposed to

    }
    @Test //create a graph with million vertices and edges and check how much time it takes to save and load it
    public void save_load_TimeTest() throws IOException {
        assertTimeout(Duration.ofSeconds(10), () -> {
            int v=1000000, e=100000;
            Random rnd = new Random(1);
            directed_weighted_graph graph= DWGraph_DSTest.graphCreator(v);
            int n1=0,n2=0;
            for (int i=0;i<e;i++){
                double w = Math.random()*20;
                n1 = rnd.nextInt((v-1));
                n2 = rnd.nextInt((v-1));
                graph.connect(n1,n2, w);
            }
            dw_graph_algorithms ga1 = new DWGraph_Algo();
            dw_graph_algorithms ga2 = new DWGraph_Algo();
            ga1.init(graph);
            ga1.save("saveOfga1.json");
            ga2.load("saveOfga1.json");
        });
    }
    @Test //create a graph with million vertices and edges and check how much time it takes to copy it
    public void copy_TimeTest()  {
        assertTimeout(Duration.ofSeconds(10), () -> {
            int v=1000000, e=100000;
            Random rnd = new Random(1);
            directed_weighted_graph graph= DWGraph_DSTest.graphCreator(v);
            int n1=0,n2=0;
            for (int i=0;i<e;i++){
                double w = Math.random()*20;
                n1 = rnd.nextInt((v-1));
                n2 = rnd.nextInt((v-1));
                graph.connect(n1,n2, w);
            }
            dw_graph_algorithms ga1 = new DWGraph_Algo();
            dw_graph_algorithms ga2 = new DWGraph_Algo();
            ga1.init(graph);
            ga1.save("saveOfga1");
            ga2.load("saveOfga1");
        });
    }
    @Test
    public void Path_dist_connected()
    {
        directed_weighted_graph g= DWGraph_DSTest.graphCreator(6);
        g.connect(0,1,0.5);
        g.connect(0,2,16.6);
        g.connect(1,3,1.2);
        g.connect(1,2,1.0);
        g.connect(2,1,5.5);
        g.connect(2,3,8.3);
        g.connect(4,2,7.0);
        g.connect(5,4,2.0);
        dw_graph_algorithms ga=new DWGraph_Algo();
        ga.init(g);
        boolean isConnected=ga.isConnected();
        assertEquals(false, isConnected);
        int[] path1= {0,1,2};
        int[] path2= {5,4,2,1,3};
        int[] path3= {2,1,3};
        List<node_data> Path1=ga.shortestPath(0,2);
        double dist1= ga.shortestPathDist(0,2);
        List<node_data> Path2=ga.shortestPath(5,3);
        double dist2=ga.shortestPathDist(5,3);
        List<node_data> Path3=ga.shortestPath(2,3);
        double dist3= ga.shortestPathDist(2,3);
        List<node_data> Path4=ga.shortestPath(4,0);
        double dist4=ga.shortestPathDist(4,0);
        assertEquals(1.5,dist1);
        assertEquals(15.7, dist2);
        assertEquals(6.7, dist3);
        assertEquals(-1, dist4);
        boolean p1=(Path1.size()==path1.length), p2=(Path2.size()==path2.length),
                p3=(Path3.size()==path3.length);

        for (int i=0; i<path1.length; i++){
            if(path1[i]!=Path1.get(i).getKey())
                p1=false;
        }

        for (int i=0; i<path2.length; i++){
            if(path2[i]!=Path2.get(i).getKey())
                p2=false;
        }

        for (int i=0; i<path3.length; i++){
            if(path3[i]!=Path3.get(i).getKey())
                p3=false;
        }
    assertEquals(true, p1);
    assertEquals(true, p2);
    assertEquals(true, p3);
    assertEquals(null, Path4);
    g.connect(3,2,9.6);
    g.connect(2,5,25.0);
    g.connect(1,0,0.3);

    isConnected=ga.isConnected();
    assertEquals(true, isConnected);

    }
    @Test
    public void Path_dist_connectedTime() //check the time to shortestPath, shortestPathDist, isConnected
    {
        assertTimeout(Duration.ofSeconds(10), () -> {
            int v = 100000, e = 100000;
            Random rnd = new Random(1);
            directed_weighted_graph graph = DWGraph_DSTest.graphCreator(v);
            int n1 = 0, n2 = 0;
            for (int i = 0; i < e; i++) {
                double w = Math.random() * 30;
                n1 = rnd.nextInt((v - 1));
                n2 = rnd.nextInt((v - 1));
                graph.connect(n1, n2, w);
            }
            dw_graph_algorithms ga1 = new DWGraph_Algo();
            ga1.init(graph);
            ga1.isConnected();
            n1 = 1 + rnd.nextInt((v - 1));
            ga1.shortestPathDist(n1, n1 - 1);
            ga1.shortestPath(n1, n1 - 1);
        });
    }
}

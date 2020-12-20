import api.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DWGraph_DSTest {
    private static long startTest;
    public static directed_weighted_graph graphCreator(int v_size) { //create graph with v nodes
        directed_weighted_graph g = new DWGraph_DS();
        for (int i = 0; i < v_size; i++) {
            node_data n=new NodeData(i, new NodeData.geoLocation(0,0,0));
            g.addNode(n);
        }
        return g;
    }
    private static int[] nodes(directed_weighted_graph g) { //create an array with all the graph's keys
        int size = g.nodeSize();
        Collection<node_data> V = g.getV();
        node_data[] nodesArray = new node_data[size];
        V.toArray(nodesArray);
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodesArray[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
    @BeforeAll
    public static void begin()
    {
        System.out.println("Running tests for Ex1, DWGraph_DS class");
        startTest = System.currentTimeMillis();
    }
    @AfterAll
    public static void end()
    {
        long endTest = System.currentTimeMillis();
        double timeout= (endTest-startTest)/1000.0;
        System.out.println("time for all test: "+ timeout);
    }

    @Test
    public void add_nodes_and_edges()
    {
        Random rnd = new Random(1);
        directed_weighted_graph g1 = graphCreator (6);
        int inRange= rnd.nextInt(6),outRange=7+rnd.nextInt(12);
        node_data b1 = g1.getNode(outRange);// get node that isn't in the graph
        assertEquals(b1, null); //check if the node related to the key "outRange" is null (the node isn't in the graph)
        int[] nodes = nodes(g1);
        double r1, r2;
        for(int i=2;i<6;i++) {
            r1= Math.random()*20;
            r2=Math.random()*20;
            int n0 = nodes[i-2];
            int n1 = nodes[i-1];
            int n2 = nodes[i];
            //connect every node to the node before him and the node 2 before him
            g1.connect(n0, n1, r1);
            g1.connect(n0, n2, r2);
        }
        int node=(int)(Math.random()*4);
        try{g1.connect(node, outRange, 7.6);} //make a connection with node that isn't in the graph
        catch (Exception e)
        {
            System.out.println("cannot connect "+node+" and "+outRange+", "
                    +outRange+" does not exist at the graph");
        }
        boolean edge1 = (g1.getEdge(node, node+2)!=null);
        boolean edge2 = (g1.getEdge(node, node+1)!=null);
        boolean edge3 = (g1.getEdge(nodes[0], nodes[nodes.length-1])!=null);
        boolean edge4 = (g1.getEdge(inRange, outRange)!=null); // checking an edge with node that isn't in the graph
        try{ g1.removeNode(node);}
        catch (Exception e)
        {
            System.out.println("problem with removing node number: "+node);
            System.out.println(e.getCause());
        }
        try{ node_data afterRemove= g1.removeNode(node);//remove node that isn't in the graph
            assertEquals( null, afterRemove);
        }
        catch (Exception e)
        {
            System.out.println("problem with removing node number: "+node);
            System.out.println(e.getCause());
        }

        boolean edge5 = (g1.getEdge(node , node+1)!=null); // checking an edge with node that has been remove
        assertEquals(true, edge1);
        assertEquals(true, edge2);
        assertEquals(false, edge3);
        assertEquals(false, edge4);
        assertEquals(false, edge5);

    }
    @Test
    public void edgesTest() {
        Random rnd = new Random(2);
        directed_weighted_graph g2 = graphCreator(6);
        int[] nodes = nodes(g2);
        for (int i = 1; i <=5; i++) { //connecting 0 with all the other nodes except 6
            int n1 = nodes[i];
            g2.connect(0, n1, i + 0.5);
        }
        int edgesize= g2.edgeSize();
        assertEquals(5, edgesize); // 5 edges between 0 and 1-5
        int node1= 1+rnd.nextInt(5); //random key from 1 to 5
        int node2=1+ rnd.nextInt(5);
        double edge1 = g2.getEdge(0, node1).getWeight();
        double edge2 = g2.getEdge(0, node2).getWeight();
        edge_data edge3 = g2.getEdge(0, 6); //get edge of edge that doesn't exist
        boolean b1= (g2.getEdge(0, node1)!=null);
        g2.removeEdge(0,node1);
        g2.removeEdge(0,node1);
        boolean b2= (g2.getEdge(0, node1)!=null);
        int MC=g2.getMC();
        assertEquals(12, MC); //adding 6 vertices, 5 edges, removing 1 edge =12 actions
        g2.connect(0, node2, g2.getEdge(0, node2).getWeight()); //supposed to do nothing -  there is already edge with the same weight
        int MCnoCange=g2.getMC();
        assertEquals(MC,MCnoCange);
        try{ g2.removeEdge(6,node2);//try to remove an edge that doesn't exist
            g2.removeEdge(12,node2); //try to remove an edge with node that doesn't exist in the graph
        }
        catch (Exception e)
        {
            System.out.println("edge not found");
        }
        assertEquals(node1+0.5, edge1);
        assertEquals(node2+0.5, edge2);
        assertEquals(null, edge3);
        assertEquals(true, b1);
        assertEquals(false, b2);

    }
    @Test
    public void getCollections()
    {
        Random rnd = new Random(1);
        directed_weighted_graph g3 = graphCreator(0);
        int[] nodes3 = nodes(g3); //function that use at getV
        int size3=g3.nodeSize();
        assertEquals(size3, nodes3.length);
        directed_weighted_graph g4 = graphCreator(8);
        int[] nodes4 = nodes(g4);
        int size4=g4.nodeSize();
        assertEquals(size4, nodes4.length);
        int n1= rnd.nextInt(6);
        //add to n1 2 neighbors
        g4.connect(n1, n1+1, 1.0);
        g4.connect(n1, n1+2, 2.0);
        int neiSize=g4.getE(n1).size();
        assertEquals(2,neiSize);
        boolean isNei1= g4.getEdge(n1,n1+1)!=null;
        boolean isNei2= g4.getEdge(n1,n1+2)!=null;
        boolean isNei3= g4.getEdge(n1+1,n1)!=null;
        assertEquals(true, isNei1);
        assertEquals(true, isNei2);
        assertEquals(false, isNei3);
        g4.removeEdge(n1, n1+2);
        int neiSize2=g4.getE(n1).size();
        assertEquals(1,neiSize2);
        isNei3=g4.getEdge(n1, n1+2)!=null;
        assertEquals(false, isNei3);
    }

    @Test
    public void TimeTest() //check the time to create a graph with million vertices and edges
    {
        Random rnd = new Random(1);
        int v=1000000, e=v;
        double start = System.currentTimeMillis();
        directed_weighted_graph graph= new DWGraph_DS();
        for(int i=0;i<v;i++)
        {
            node_data n=new NodeData(i, new NodeData.geoLocation(0,0,0));
            graph.addNode(n);
        }
        int n1=0,n2=0;
        for (int i=0;i<e;i++){
            double w = Math.random()*20;
            n1 = rnd.nextInt((v-1));
            n2 = rnd.nextInt((v-1));
            graph.connect(n1,n2, w);
        }
        double end = System.currentTimeMillis();
        double timeout= (end-start)/1000.0;
        if (timeout>10)
            fail("Exception in times");
        else{
            System.out.println("time to build a graph with 1 Million vertices and 1 Million edges: "+ timeout);
        }

    }

}
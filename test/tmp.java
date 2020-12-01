

import api.*;

public class tmp {
    public static void main(String[] args) {

//        directed_weighted_graph g = new DWGraph_DS();
//
//        for(int i=0;i<6;i++) {
//            node_data n = new NodeData();
//            g.addNode(n);
//        }
//
//        g.connect(2,0,2.3);
//        g.connect(2,1,63.5);
//        g.connect(3,0,0);
//        g.connect(3,4,5.2);
//        g.connect(4,1,5.10);
//        g.connect(4,5,5.1);
//
//        dw_graph_algorithms ga = new DWGraph_Algo();
//        ga.init(g);
//
//        System.out.println(ga.isConnected());
//        g.connect(0,1,1);
//        g.connect(1,3,5.1);
//        g.connect(3,2,4.8);
//        g.connect(3,5,4.5);
//        g.connect(5,4,1.2);
//        System.out.println(ga.isConnected());


        directed_weighted_graph g2 = new DWGraph_DS();

        for(int i=0;i<5;i++) {
            node_data n = new NodeData();
            g2.addNode(n);
        }
        g2.removeNode(0);
        System.out.println("DONE");

        g2.connect(1,2,10.1);
        g2.connect(1,3,4.5);
        g2.connect(3,4,0.3);
        g2.connect(2,4,6.1);
        g2.connect(4,2,2.1);
//        g.connect(4,5,5.1);

        dw_graph_algorithms ga = new DWGraph_Algo();
        ga.init(g2);

        //System.out.println(ga.isConnected());
//        System.out.println(ga.shortestPathDist(1,2));
//        System.out.println(ga.shortestPath(1,2));
//        System.out.println(ga.shortestPathDist(3,1));
//        System.out.println(ga.shortestPath(3,1));
        System.out.println(ga);
        ga.save("mygraph.json");
        directed_weighted_graph g3 = new DWGraph_DS();
        node_data t = new NodeData();
        g3.addNode(t);
        ga.init(g3);
        System.out.println(ga);
        System.out.println(ga.load("mygraph.json"));
        System.out.println(ga);
    }
}

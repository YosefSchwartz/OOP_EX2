package api;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class forEX3 {

    public static void check (String path){
        String [] name = path.split("/");
        String nameOfGraph = name[1];

        DWGraph_Algo ga = new DWGraph_Algo();
        ga.load(path);
        System.out.println(nameOfGraph);
        System.out.println(" ---");
        int v1 = 0, v2 = 0, n = 0;

        switch (name[1]){
            case "G_10_80_1.json" :
                v1 = 9;
                v2 = 4;
                n = 5;
                break;
            case "G_100_800_1.json" :
                v1 = 79;
                v2 = 32;
                n = 94;
                break;
            case "G_1000_8000_1.json" :
                v1 = 637;
                v2 = 261;
                n = 759;
                break;
            case "G_10000_80000_1.json" :
                v1 = 4185;
                v2 = 5874;
                n = 8684;
                break;
            case "G_20000_160000_1.json" :
                v1 = 8370;
                v2 = 11748;
                n = 17368;
                break;
            case "G_30000_240000_1.json" :
                v1 = 20411;
                v2 = 8370;
                n = 24306;
                break;
        }

        long startTimeShortestPath = System.currentTimeMillis();
        ga.shortestPath(v1,v2);
        long finishTimeShortestPath = System.currentTimeMillis();
        long shortestPathTime = finishTimeShortestPath-startTimeShortestPath;

        long startTimeSCC = System.currentTimeMillis();
        ga.connectedComponent(n);
        long finishTimeSCC = System.currentTimeMillis();
        long SCCTime = finishTimeSCC-startTimeSCC;

        System.out.println("time to check shortest_path in Java -> " + shortestPathTime/1000.0 + " sec");
        System.out.println("time to check connected_components in Java -> " + SCCTime/1000.0 + " sec");
    }

    public static void main(String[] args) {
        check("data/G_10_80_1.json");
        check("data/G_100_800_1.json");
        check("data/G_1000_8000_1.json");
        check("data/G_10000_80000_1.json");
        check("data/G_20000_160000_1.json");
        check("data/G_30000_240000_1.json");


    }
}

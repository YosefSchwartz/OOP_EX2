package api;

public class edgeData implements edge_data {
        double weight;
        String info;
        int tag;
        node_data src;
        node_data dest;


        public edgeData(node_data src, node_data dest, double weight) {
            this.src=src;
            this.dest=dest;
            this.weight = weight;
            info = null;
            tag = 0;
        }

        @Override
        public int getSrc() {
            return src.getKey();
        }

        @Override
        public int getDest() {
            return dest.getKey();
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

        public node_data getSRC()
        {
            return src;
        }
        public node_data getDEST(){
            return dest;
        }

    public class edgeLocation implements edge_location {
        geo_location point;
        edgeData edge;
        double ratio;

        public edgeLocation(geo_location p, edgeData e) {
            edge = e;
            point=p;
            double PartlyDist= p.distance(e.getSRC().getLocation());
            double FullDist= e.getSRC().getLocation().distance(e.getDEST().getLocation());
            ratio =PartlyDist/FullDist;
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

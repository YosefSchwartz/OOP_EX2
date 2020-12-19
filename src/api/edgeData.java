package api;

/**
 * This Class represents the set of operations applicable on a
 * directional edge(src,dest) in a (directional) weighted graph.
 */
public class edgeData implements edge_data {
    double weight;
    String info;
    int tag;
    node_data src;
    node_data dest;

    /**
     * constructor
     *
     * @param src    - src node
     * @param dest   - dest node
     * @param weight - the weight of (src,dest) edge
     */
    public edgeData(node_data src, node_data dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
        info = null;
        tag = 0;
    }

    /**
     * The id of the source node of this edge.
     *
     * @return src key
     */
    @Override
    public int getSrc() {
        return src.getKey();
    }

    /**
     * The id of the destination node of this edge
     *
     * @return dest key
     */
    @Override
    public int getDest() {
        return dest.getKey();
    }

    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * @return the remark (meta data) associated with this edge.
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s new info
     */
    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return tag
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    /**
     * @return pointer to src node
     */
    public node_data getSrcNode() {
        return src;
    }
    /**
     * @return pointer to dest node
     */
    public node_data getDestNode() {
        return dest;
    }

    public String toString(){
       return "(src: "+src+", dest: "+dest+", weight: "+weight+")";
    }

    /**
     * This class represents a position on the graph
     * each node contains edge, point on his edge and the ratio of this point
     */
    public static class edgeLocation implements edge_location {
        geo_location point;
        edgeData edge;
        double ratio;

        public edgeLocation(geo_location p, edgeData e) {
            edge = e;
            point = p;
            double PartlyDist = p.distance(e.getSrcNode().getLocation());
            double FullDist = e.getSrcNode().getLocation().distance(e.getDestNode().getLocation());
            ratio = PartlyDist / FullDist;
        }
        /**
         * @return the edge on which the location is.
         */
        @Override
        public edge_data getEdge() {
            return edge;
        }
        /**
         * @return the relative ration [0,1] of the location between src and dest.
         */
        @Override
        public double getRatio() {
            return ratio;
        }
    }
}

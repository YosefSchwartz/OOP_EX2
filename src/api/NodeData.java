package api;
/**
 * This Class represents the set of operations applicable on a
 * node (vertex) in a (directional) weighted graph.
 */
public class NodeData implements node_data, Comparable<Object> {
    int key;
    static int counter = 0;
    geoLocation gl;
    double weight;
    String info;
    int tag;
    int k;

    /**
     * constructor
     */
    public NodeData() {
        key = counter;
        gl = new geoLocation(0,0,0);
        weight = 0;
        tag = 0;
        info = null;
        counter++;
        k=0;
    }

    /**
     * constructor
     * @param key - id
     * @param p - position
     */
    public NodeData(int key, geo_location p) {
        this.key = key;
        gl = new geoLocation(p);
        weight = 0;
        tag = 0;
        info = null;
        k=0;
    }
    /**
     * @return the key (id) associated with this node.
     */
    @Override
    public int getKey() {
        return key;
    }
    /**
     * @return the location of this node, null if none.
     */
    @Override
    public geo_location getLocation() {
        return gl;
    }
    /** Allows changing this node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        gl = new geoLocation(p);
    }
    /**
     * Returns the weight associated with this node.
     * @return
     */
    @Override
    public double getWeight() {
        return weight;
    }
    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        weight = w;
    }
    /**
     * Returns the remark (meta data) associated with this node.
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }
    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s
     */
    @Override
    public void setInfo(String s) {
        info = s;
    }
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    @Override
    public int getTag() {
        return tag;
    }
    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = t;
    }

    public void setK(int k){ this.k = k; }

    public String toString()
    {
        return ""+key;
    }

    @Override
    public int compareTo(Object o) {
        node_data n =(node_data)(o);
        if(weight-n.getWeight()>0) return 1;
        return -1;
    }

    /**
     * This interface represents a geo location <x,y,z>, aka Point3D
     */
    public static class geoLocation implements geo_location {
        double x;
        double y;
        double z;

        /**
         * constructor from other position
         * @param other - other point
         */
        public geoLocation(geo_location other) {
            x=other.x();
            y=other.y();
            z=other.z();
        }

        /**
         * constructor from specific coordinates
         * @param x - x coordinate
         * @param y - y coordinate
         * @param z - z coordinate
         */
        public geoLocation(double x, double y, double z) {
            this.x=x;
            this.y=y;
            this.z=z;
        }

        /**
         * @return x coordinate of this pos
         */
        @Override
        public double x() {
            return x;
        }
        /**
         * @return y coordinate of this pos
         */
        @Override
        public double y() {
            return y;
        }
        /**
         * @return z coordinate of this pos
         */
        @Override
        public double z() {
            return z;
        }

        /**
         * compute the distance between this to other point
         * @param g - other point
         * @return - distance
         */
        @Override
        public double distance(geo_location g) {
            double dx = this.x() - g.x();
            double dy = this.y() - g.y();
            double dz = this.z() - g.z();
            double t = (dx*dx+dy*dy+dz*dz);
            return Math.sqrt(t);
        }

        /**
         * toString function
         *
         * @return
         */
        public String toString(){
            return x+","+y+","+z;
        }
    }
}

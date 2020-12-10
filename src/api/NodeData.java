package api;

public class NodeData implements node_data, Comparable<Object> {
    int key;
    static int counter = 0;
    geoLocation gl;
    double weight;
    String info;
    int tag;

    public NodeData() {
        key = counter;
        gl = new geoLocation(0,0,0);
        weight = 0;
        tag = 0;
        info = null;

        counter++;
    }

    public NodeData(int key, geo_location p) {
        this.key = key;
        gl = new geoLocation(p);
        weight = 0;
        tag = 0;
        info = null;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public geo_location getLocation() {
        return gl;
    }

    @Override
    public void setLocation(geo_location p) {
        gl = new geoLocation(p);
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        weight = w;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        tag = t;
    }

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
    public static class geoLocation implements geo_location {
        double x;
        double y;
        double z;

        public geoLocation(geo_location other)
        {
            x=other.x();
            y=other.y();
            z=other.z();
        }
        public geoLocation(double x, double y, double z)
        {
            this.x=x;
            this.y=y;
            this.z=z;
        }
        @Override
        public double x() {
            return x;
        }

        @Override
        public double y() {
            return y;
        }

        @Override
        public double z() {
            return z;
        }

        @Override
        public double distance(geo_location g) {
            double dx = this.x() - g.x();
            double dy = this.y() - g.y();
            double dz = this.z() - g.z();
            double t = (dx*dx+dy*dy+dz*dz);
            return Math.sqrt(t);
        }
        public String toString(){
            return x+","+y+","+z;
        }
    }
}

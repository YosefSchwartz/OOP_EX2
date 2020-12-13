package gameClient.Final;


import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class GameFrame extends JFrame {
    private int _ind;
    private GameData _ar;
    private ImageIcon pokemon;
    private ImageIcon agent;
    //private JLabel TimeToEnd;
    private gameClient.util.Range2Range _w2f;


    public GameFrame(String a) {
        super(a);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
//        TimeToEnd = new JLabel();
//        TimeToEnd.setVisible(true);
//        this.add(TimeToEnd);
//        TimeToEnd.setText("Time to end: ");

        int _ind = 0;
    }
    public void update(GameData ar) {
        this._ar = ar;
        updateFrame();
    }
    private void updateFrame() {
        Range rx = new Range(50,this.getWidth()-50);
        Range ry = new Range(this.getHeight()-50,100);
        Range2D frame = new Range2D(rx,ry);
//        TimeToEnd.setBounds(50, 100,50, 25);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = GameData.w2f(g,frame);
    }

        public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        setSize(w,h);
        updateFrame();
        Image buffer_image;
        Graphics buffer_graphics;
        buffer_image = createImage(w, h);
        buffer_graphics = buffer_image.getGraphics();
        buffer_graphics.clearRect(0, 0, w, h);
        drawGraph(buffer_graphics);
        drawPokemons(buffer_graphics);
        drawAgants(buffer_graphics);
        drawInfo(buffer_graphics);
        g.drawImage(buffer_image, 0, 0, this);
        }

    private void drawInfo(Graphics g) {
        java.util.List<String> str = _ar.get_info();
        String dt = "none";
        for(int i=0;i<str.size();i++) {
            g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
        }
    }
    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            node_data n = iter.next();
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.black);
                drawEdge(e, g);
            }
        }
        Iterator<node_data> iter1 = gg.getV().iterator();
        while(iter1.hasNext()) {
            node_data n = iter1.next();
            g.setColor(new Color(221,183, 63 ));
            drawNode(n,14,g);

        }
    }
    private void drawPokemons(Graphics g) {
        List<Pokemon> fs = _ar.getPokemons();
        if(fs!=null) {
            Iterator<Pokemon> itr = fs.iterator();

            while(itr.hasNext()) {

                Pokemon p = itr.next();
                double x=p.getPos().x(), y=p.getPos().y(), z=p.getPos().z();
                Point3D c = new Point3D(x,y,z);
                int r=10;
                g.setColor(Color.green);
                if(p.getType()<0) {g.setColor(Color.orange);}
                if(c!=null) {

                    geo_location fp = this._w2f.world2frame(c);
                    pokemon=new ImageIcon("src//gameClient//files//pokemonBall.png");
                    Image pokemon1 = pokemon.getImage();
                    Image pokemon2 = pokemon1.getScaledInstance(4*r, 4*r,Image.SCALE_DEFAULT);
                    pokemon=new ImageIcon(pokemon2);
                    pokemon.paintIcon(this, g, (int)fp.x()-2*r,(int)fp.y()-2*r);
                }
            }
        }
    }
    private void drawAgants(Graphics g) {
        List<Agent> rs = _ar.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        int i=0;
        while(rs!=null && i<rs.size()) {
            geo_location c = rs.get(i).getPos();
            int r=8;
            i++;
            if(c!=null) {

                geo_location fp = this._w2f.world2frame(c);

                agent=new ImageIcon("src//gameClient//files//pikachu.png");
                Image agent1 = agent.getImage();
                Image agent2 = agent1.getScaledInstance(10*r, 12*r,Image.SCALE_DEFAULT);
                agent=new ImageIcon(agent2);
                agent.paintIcon(this, g, (int)fp.x()-4*r,(int)fp.y()-6*r);
            }
        }
    }
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.setColor(Color.BLACK);
        g.drawString(""+n.getKey(), (int)fp.x()-3, (int)fp.y()+3);
    }
    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        Graphics2D g1= (Graphics2D)g;
        g1.setStroke(new BasicStroke(2));
        g1.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
    }

}

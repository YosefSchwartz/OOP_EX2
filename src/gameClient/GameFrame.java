package gameClient;


import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

public class GameFrame extends JFrame {
    private int _ind;
    private GameData _ar;
    private ImageIcon pokemon;
    private ImageIcon agent;
    private JPanel panel;
    private JLabel TimeToEnd;
    private JLabel TotalValue;
    private JLabel GameLevel;
    //private JLabel TimeToEnd;
    private gameClient.util.Range2Range _w2f;


    public GameFrame(String a) {
        super(a);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        panel =new JPanel();
        this.add(panel);
//        TimeToEnd=new JLabel();
//        TotalValue=new JLabel();
//        GameLevel=new JLabel();
        int _ind = 0;
    }
    public void update(GameData ar) {
        this._ar = ar;
        updateFrame();
    }
    private void updateFrame() {
        double h=this.getHeight(), w=this.getWidth();
        Range rx = new Range(0.06*w,w-0.06*w);
        Range ry = new Range(h-0.08*h,0.30*h);
        Range2D frame = new Range2D(rx,ry);
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
        List<String> str = _ar.get_info();
        double w=getWidth(), h=0.22*getHeight();
        int size=(int)(w*0.03);
        Font f=new Font("SansSerif", Font.BOLD, size);
        g.setFont(f);
        System.out.println("h: "+h);
        g.setColor(Color.BLACK);
        double[] s1=getSize("Level: "+str.get(0), g, f);
        double[] s2=getSize("Time left: ",g,f);
        g.fillRect(0,0,(int)(w), (int)(h));
        g.setColor(new Color(221,183, 63 ));
        g.fillRect(0,(int)(h)-2,(int)(w),4);
        g.drawString("Level: "+str.get(0), (int)((w/2)-(s1[0]/2)),70);
        g.drawString("Score: "+str.get(2), 296,137);
        int time=Integer.parseInt(str.get(1));
        if(time<6)
        {
            g.drawString("Time left: ",530 ,137);
            g.setColor(Color.red);
            g.drawString(str.get(1),670 ,137);
        }
        else g.drawString("Time left: "+str.get(1),530 ,137);

    }
    private double[] getSize(String s, Graphics g, Font f)
    {
        g.setColor(Color.BLACK);
        double[] size=new double[2];
        Rectangle2D p=g.getFontMetrics(f).getStringBounds(s,g);
        size[0]=p.getWidth();
        size[1]=p.getHeight();
        return size;
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
            drawNode(n,12,g);

        }
    }
    private void drawPokemons(Graphics g) {
        List<Pokemon> fs = _ar.getPokemons();
        for (int i=0; i<fs.size();i++) {
            Pokemon p = fs.get(i);
            double x=p.getPos().x(), y=p.getPos().y(), z=p.getPos().z();
            Point3D c = new Point3D(x,y,z);
            int r=14;
            g.setColor(Color.green);
            if(p.getType()<0) {g.setColor(Color.orange);}
            if(c!=null) {

                geo_location fp = this._w2f.world2frame(c);
                pokemon=new ImageIcon("src//gameClient//files//pokemonBall.png");
                Image pokemon1 = pokemon.getImage();
                Image pokemon2 = pokemon1.getScaledInstance(2*r+4, 2*r,Image.SCALE_DEFAULT);
                pokemon=new ImageIcon(pokemon2);
                pokemon.paintIcon(this, g, (int)fp.x()-r-2,(int)fp.y()-r);
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
            if(c!=null) {

                geo_location fp = this._w2f.world2frame(c);

                agent=new ImageIcon("src//gameClient//files//pikachu.png");
                Image agent1 = agent.getImage();
                Image agent2 = agent1.getScaledInstance(10*r, 12*r,Image.SCALE_DEFAULT);
                agent=new ImageIcon(agent2);
                int x=(int)fp.x()-4*r, y=(int)fp.y()-6*r;
                agent.paintIcon(this, g,x ,y);
                Font f=new Font("SansSerif", Font.BOLD, 12);
                g.setFont(f);
                g.setColor(Color.BLACK);
               // new Color(220, 36,36)
                double Value= rs.get(i).getValue();
                g.drawString("Value: "+Value, x-3, y-5);
              //  g.drawString("Speed: "+rs.get(i).getSpeed(), x-, y-20);
            }
            i++;
        }
    }
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.setColor(Color.BLACK);
        Font f=new Font("SansSerif", Font.CENTER_BASELINE, 12);
        g.setFont(f);
        g.drawString(""+n.getKey(), (int)fp.x()-5, (int)fp.y()+4);
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
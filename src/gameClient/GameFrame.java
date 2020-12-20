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
import java.io.File;
import java.util.Iterator;
import java.util.List;

public class GameFrame extends JFrame {
    public GameData _ar;
    private ImageIcon pokemon;
    private ImageIcon agent;

    private gameClient.util.Range2Range _w2f;


    public GameFrame(String a) {
        super(a);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    /**
     * updates the Game data to the given new Game data
     * @param ar
     */
    public void update(GameData ar) {
        this._ar = ar;
        updateFrame();
    }

    /**
     * update the frame size to the current size of the frame
     */
    private void updateFrame() {
        double h=this.getHeight(), w=this.getWidth();
        Range rx = new Range(0.06*w,w-0.06*w);
        Range ry = new Range(h-0.08*h,0.35*h);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = GameData.w2f(g,frame);
    }

    /**
     * paint all the details of the game:
     * graph's nodes and edges, pokemons, agents and the info of the game
     * @param g
     */
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

    /**
     * draw the info of the game (game number, time to end and the total score)
     * @param g
     */
    private void drawInfo(Graphics g) {
        List<String> str = _ar.get_info();
        double w=getWidth(), h=0.22*getHeight();
        int size=(int)((0.07)*((w+h)/2));
        if(w>700)
            size=30;
        Font f=new Font("SansSerif", Font.BOLD, size);
        g.setFont(f);
        g.setColor(Color.BLACK);
        double w1=getWidth("Level: "+str.get(0), g, f)[0];
        double w2=getWidth("Score: ",g,f)[0];
        double w3=getWidth("Time left: ",g,f)[0];
        double w4=getWidth(str.get(1),g,f)[0];
        double h1=getWidth("Level: "+str.get(0), g, f)[1];
        g.fillRect(0,0,(int)(w), (int)(h));
        g.setColor(new Color(221,183, 63 ));
        g.fillRect(0,(int)(h)-2,(int)(w),4);
        g.drawString("Level: "+str.get(0), (int)(0.5*w-0.5*w1), (int)(h*0.49));
        double x=(w-(2.5*w2+w3+w4))/2;
        g.drawString("Score: "+str.get(2), (int)(x) ,(int)(0.58*h+h1));
        int time=Integer.parseInt(str.get(1));
        x+=2.5*w2;
        g.drawString("Time left: ",(int)(x) ,(int)(0.58*h+h1));
        x+=w3;
        if(time<6)
        {
            g.setColor(Color.red);
            g.drawString(str.get(1),(int)(x) ,(int)(0.58*h+h1));
        }
        else g.drawString(str.get(1),(int)(x) ,(int)(0.58*h+h1));

    }

    /**
     * check the height and width for the given string.
     * @param s,g,f.
     * @return arr with height and width
     */
    private double[] getWidth(String s, Graphics g, Font f)
    {
        double[] arr=new double[2];//w,h
        g.setColor(Color.BLACK);
        Rectangle2D p=g.getFontMetrics(f).getStringBounds(s,g);
        arr[0]= p.getWidth();
        arr[1]=p.getHeight();
        return arr;
    }

    /**
     * draw the details of the graph in this game (nodes and edges)
     * @param g
     */
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

    /**
     * draw the present pokemons in the game
     * @param g
     */
    private void drawPokemons(Graphics g) {
        List<Pokemon> fs = _ar.getPokemons();
        for (int i=0; i<fs.size();i++) {
            Pokemon p = fs.get(i);
            double x=p.getPos().x(), y=p.getPos().y(), z=p.getPos().z();
            Point3D c = new Point3D(x,y,z);
            int r=14;
            g.setColor(Color.green);
            if(p.getType()<0) {g.setColor(Color.blue);}
            if(c!=null) {
                geo_location fp = this._w2f.world2frame(c);
                File file=new File("data//images//pokemonBall.png");
                if(file.exists()) {
                    pokemon = new ImageIcon("data//images//pokemonBall.png");
                    Image pokemon1 = pokemon.getImage();
                    Image pokemon2 = pokemon1.getScaledInstance(2 * r + 4, 2 * r, Image.SCALE_DEFAULT);
                    pokemon = new ImageIcon(pokemon2);
                    pokemon.paintIcon(this, g, (int) fp.x() - r - 2, (int) fp.y() - r);
                }
                else{
                    r=7;
                    g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);}
            }
        }
    }

    /**
     * draw the agents in the game at the current positions
     * @param g
     */
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
                Font f = new Font("SansSerif", Font.BOLD, 12);
                g.setFont(f);
                g.setColor(Color.BLACK);
                double Value = rs.get(i).getValue();
                File file=new File("data//images//pikachu.png");
                if(file.exists()) {
                    agent = new ImageIcon("data//images//pikachu.png");
                    Image agent1 = agent.getImage();
                    Image agent2 = agent1.getScaledInstance(10 * r, 12 * r, Image.SCALE_DEFAULT);
                    agent = new ImageIcon(agent2);
                    int x = (int) fp.x() - 4 * r, y = (int) fp.y() - 6 * r;
                    agent.paintIcon(this, g, x, y);

                    g.drawString("Value: " + Value, x - 3, y - 5);
                }
                else {
                    g.setColor(Color.red);
                    g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                }
            }
            i++;
        }
    }
    /**
     * draw the nodes of the graph in this game
     * @param n,r,g
     */
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.setColor(Color.BLACK);
        Font f=new Font("SansSerif", Font.CENTER_BASELINE, 12);
        g.setFont(f);
        g.drawString(""+n.getKey(), (int)fp.x()-5, (int)fp.y()+4);
    }

    /**
     * draw the edges of the graph in this game
     * @param e,g
     */
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
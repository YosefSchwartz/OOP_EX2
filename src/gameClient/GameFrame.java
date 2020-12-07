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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

public class GameFrame extends JFrame implements ActionListener {
    private BufferedImage bi;
    private Insets insets;
    private int _ind;
    private Arena _ar;
    private ImageIcon pokemon;
    private ImageIcon agent;
    private gameClient.util.Range2Range _w2f;
    private static JTextField ID_Text;
    private static JLabel ID_Label;
    private static JLabel gameNumber_Label;
    private static JTextField gameNumber_Text;
    private static JPanel panel;
    private static JButton button;
    private static int ID;
    private static int gameNumber;
    private static int mark=0;

    GameFrame(String a) {
        super(a);
        int _ind = 0;
    }
    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }

    public int[] login() {
        panel = new JPanel();
        panel.setLayout(null);
        this.add(panel);
        ID_Label = new JLabel("ID");
        ID_Label.setBounds(10, 20, 80, 25);
        ID_Text = new JTextField(20);
        ID_Text.setBounds(150, 20, 165, 25);
        gameNumber_Label = new JLabel("Game number");
        gameNumber_Label.setBounds(10, 50, 80, 25);
        gameNumber_Text = new JTextField(2);
        gameNumber_Text.setBounds(150, 50, 165, 25);
        panel.add(gameNumber_Label);
        panel.add(ID_Label);
        panel.add(ID_Text);
        panel.add(gameNumber_Text);
        button = new JButton("login");
        button.setBounds(50, 110, 80, 25);
        panel.add(button);
        button.addActionListener(this);
        int[] loginArr={ID, gameNumber, mark};
        //int[] loginArr={333, 11};
        return loginArr;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    if(e.getSource()==button) {
        try {
            ID = Integer.parseInt(ID_Text.getText());
            gameNumber = Integer.parseInt(gameNumber_Text.getText());
            System.out.println("ID: " + ID);
            System.out.println("game number: " + gameNumber);
            mark=1;
            //this.remove(panel);
        } catch (Exception P) {
            System.out.println("ERROR in the details");
        }
    }
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
            drawNode(n,5,g);
        }
    }
    private void drawPokemons(Graphics g) {
        java.util.List<CL_Pokemon> fs = _ar.getPokemons();
        if(fs!=null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while(itr.hasNext()) {

                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r=10;
                g.setColor(Color.green);
                if(f.getType()<0) {g.setColor(Color.orange);}
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
        List<CL_Agent> rs = _ar.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        int i=0;
        while(rs!=null && i<rs.size()) {
            geo_location c = rs.get(i).getLocation();
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
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
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

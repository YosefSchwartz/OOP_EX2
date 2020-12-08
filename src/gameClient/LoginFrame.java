package gameClient;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class LoginFrame extends JFrame implements ActionListener {
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

    LoginFrame() {
        panel = new JPanel();
        panel.setLayout(null);
        this.add(panel);
    }
//    public void update(Arena ar) {
//        this._ar = ar;
//        updateFrame();
//    }

//    private void updateFrame() {
//
//    }

    public int[] login() {

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
        int[] loginArr={ID, gameNumber};
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
            paintComponents(getGraphics());
        } catch (Exception P) {
            System.out.println("ERROR in the details");
        }
    }
    }
        public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        setSize(w,h);
       // updateFrame();
//        Image buffer_image;
//        Graphics buffer_graphics;
//        buffer_image = createImage(w, h);
//        buffer_graphics = buffer_image.getGraphics();
        }

}

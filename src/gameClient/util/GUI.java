package gameClient.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//public class GUI extends JFrame implements ActionListener{
public class GUI{
//    private static JTextField UseNameText;
//    private static JLabel User_Name_Label;
//    private static JLabel scenario_Label;
//    private static JTextField scenarioText;
//    private static JFrame frame;
//    private static JPanel panel;
//    private static JButton button;
//    private static Image image;
//    private static Graphics g;
//    private static String UserName;
//    private static int scenario;

    public static void main(String[] args) {

    example e=new example();
    e.setVisible(true);
//        frame = new JFrame("MyTest");
//        frame.setVisible(true);
//        frame.setResizable(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        panel = new JPanel();
//        panel.setLayout(null);
//        frame.setSize(100, 100);
//        frame.add(panel);
//        User_Name_Label = new JLabel("ID");
//        User_Name_Label.setBounds(10, 20, 80, 25);
//        UseNameText = new JTextField(20);
//        UseNameText.setBounds(150, 20, 165, 25);
//        scenario_Label = new JLabel("Game number");
//        scenario_Label.setBounds(10, 50, 80, 25);
//        scenarioText = new JTextField(2);
//        scenarioText.setBounds(150, 50, 165, 25);
//        panel.add(scenario_Label);
//        panel.add(User_Name_Label);
//        panel.add(UseNameText);
//        panel.add(scenarioText);
//        button = new JButton("login");
//        button.setBounds(50, 110, 80, 25);
//        panel.add(button);
//        button.addActionListener(new GUI());

    }
//    public void paint(Graphics g)
//    {
//        image=frame.createImage(100,100);
//        g=image.getGraphics();
//        paintComponents(g);
//        g.drawImage(image,0,0,frame);
//    }
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        UserName = UseNameText.getText();
//        scenario = Integer.parseInt(scenarioText.getText());
//        System.out.println("user name: " + UserName);
//        System.out.println("scenario: " + scenario);
//
//    }
//
//    public void paintComponents(Graphics g) {
//        if (scenario >= 0 && scenario < 24) {
//            g.setColor(Color.BLUE);
//            Font font = new Font("BN Loco", Font.PLAIN, 30);
//            g.setFont(font);
//            g.drawString("Login success", 40, 140);
//        } else {
//            g.setColor(Color.red);
//            Font font = new Font("BN Loco", Font.BOLD, 30);
//            g.setFont(font);
//            g.drawString("Invalid game number", 40, 140);
//        }
//}
}
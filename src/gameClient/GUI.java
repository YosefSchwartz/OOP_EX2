package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener, Runnable{
    private static JTextField UseNameText;
    private static JLabel User_Name_Label;
    private static JLabel scenario_Label;
    private static JTextField scenarioText;
    private static JPanel panel;
    private static JButton button;
    private static int UserName;
    public static int scenario;

    public static JFrame frame;
    private static Image image;
    private static Graphics g;
    private int[] ans;

    public static void main(String[] args) {
       // GUI gg=new GUI();
    }
    public GUI() {
    }
    public void init() throws InterruptedException {
        ans=new int[2];
        frame = new JFrame("MyTest");
        frame.setVisible(true);
        frame.validate();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(null);
        frame.setSize(350, 350);
        frame.add(panel);
        User_Name_Label = new JLabel("ID");
        User_Name_Label.setBounds(10, 20, 80, 25);
        UseNameText = new JTextField(20);
        UseNameText.setBounds(150, 20, 165, 25);
        scenario_Label = new JLabel("Game number");
        scenario_Label.setBounds(10, 50, 80, 25);
        scenarioText = new JTextField(2);
        scenarioText.setBounds(150, 50, 165, 25);
        panel.add(scenario_Label);
        panel.add(User_Name_Label);
        panel.add(UseNameText);
        panel.add(scenarioText);
        button = new JButton("login");
        button.setBounds(50, 110, 80, 25);
        panel.add(button);
        button.addActionListener(this);
            this.wait();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       try {
            UserName = Integer.parseInt(UseNameText.getText());
            scenario = Integer.parseInt(scenarioText.getText());
            System.out.println("user name: " + UserName);
            System.out.println("scenario: " + scenario);
            if(scenario>=0 && scenario<24)
            {
//               GameFrame _win = new GameFrame("test Ex2");
//               _win.setSize(1000, 700);
                frame.setVisible(false);
            this.notify();
            }
        }
        catch (Exception p)
        {
            System.out.println("ERROR");
            System.out.println(p.getCause());
        }
    }

    @Override
    public void run() {
        try {
            init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//    public void playMusic() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
//        File musicPath =new File("C:/Users/עדן שקורי/IdeaProjects/OOP_EX2/src/gameClient/pokemon_song.wav");
//        AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
//        Clip clip=AudioSystem.getClip();
//        clip.open(audioInput);
//        clip.start();
//    }
}
package gameClient;


import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class LoginFrame extends JFrame implements ActionListener {
    private JTextField ID_Text;
    private JLabel ID_Label;
    private JLabel GameNumber_Label;

    private JPanel panel;
    private JComboBox GameOpt;
    private JButton LoginButton;
    private JButton Mute;
    private JButton unMute;
    private JLabel text;
    //JComboBox
    private JRadioButton SaveGrade;
    private int Mark;
    private int countMark;
    private int ID;
    private int GameNumber;
    private int h = 350;
    private int w = 550;
    private ImageIcon pokemon;
    private Image image;
    private Graphics gr;
    private JLabel back;
    private Clip clip;

    public LoginFrame() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        setSize(w, h);
        init();
        this.revalidate();
        panel.revalidate();
    }

    public void init() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        Mark=0;
        countMark=0;
        w = getWidth();
        h = getHeight();
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        this.revalidate();
        panel.revalidate();
        panel.setLayout(null);
        back = new JLabel();
        setID();
        setGameNumber();
        setButtons();
        setPanel();
        this.add(panel);
       // playMusic();
    }

    public void paint(Graphics g) {
        w = getWidth();
        h = getHeight();
        this.setSize(w, h);
        image = this.createImage(w, h);
        gr = image.getGraphics();
        paintComponents(gr);
        g.drawImage(image, 0, 0, this);
        back.setBounds(0, 0, getWidth(), getHeight());
    }

    private void setID() {
        ID_Label = new JLabel("ID number");
        Font f1 = new Font("SansSerif", Font.BOLD, 15);
        Font f2 = new Font("SansSerif", Font.PLAIN, 12);
        ID_Label.setFont(f1);
        ID_Label.setBounds(175, 40, 200, 20);
        ID_Text = new JTextField(20);
        ID_Text.setFont(f2);
        ID_Text.setBounds(175, 70, 200, 28);
    }

    private void setGameNumber() {
        Font f1 = new Font("SansSerif", Font.BOLD, 15);
        Font f2 = new Font("SansSerif", Font.PLAIN, 12);
        GameNumber_Label = new JLabel("Game number");
        GameNumber_Label.setFont(f1);
        GameNumber_Label.setBounds(175, 120, 200, 20);
        String[] s={"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
                "14", "15", "16", "17", "18", "19", "10", "21", "22", "23", "24"};
        GameOpt=new JComboBox(s);
        GameOpt.setBackground(Color.white);
        GameOpt.setFont(f2);
        GameOpt.setBounds(175, 150, 200, 28);
    }

    private void setButtons() {
        //login button
        LoginButton = new JButton("login");
        LoginButton.setVisible(true);
        Font f3 = new Font("Dialog", Font.BOLD, 15);
        LoginButton.setBackground(new Color(217, 89, 64));
        LoginButton.setFont(f3);
        LoginButton.setBounds(225, 250, 100, 25);
        LoginButton.addActionListener(this);

        //SaveGrade
        SaveGrade=new JRadioButton();
        SaveGrade.setBounds(175, 190, 160, 25);
        Font f4 = new Font("SansSerif", Font.PLAIN, 12);
        SaveGrade.setFont(f4);
        SaveGrade.setBackground(new Color(193, 216, 250));
        SaveGrade.setText("I want to save my score");
        SaveGrade.addActionListener(this);

//      SaveGrade.setBackground();

        //Mute
//        ImageIcon M = new ImageIcon("C://Users//עדן שקורי//IdeaProjects//OOP_EX2//src//gameClient//files//mute.jpg");
//        try {
//            Mute = new JButton(M);
//            Mute.setBounds(195, 220, 10, 10);
//        }
//        catch (Exception e){
//        System.out.println(e);}
//        Mute.addActionListener(this);

        //unMute
//        Image UM = new ImageIcon(("C://Users//עדן שקורי//IdeaProjects//OOP_EX2//src//gameClient//files//unmute.jpg")).getImage();
//        unMute = new JButton("");
//        unMute.setIcon(new ImageIcon(UM));
//        unMute.setVisible(true);
//        unMute.setBounds(0, 0, 400, 350);
        //unMute.addActionListener(this);
    }

    private void setPanel() {
        panel.add(GameNumber_Label);
        panel.add(ID_Label);
        panel.add(ID_Text);
        panel.add(LoginButton);
        panel.add(GameOpt);
        panel.add(SaveGrade);
       // panel.add(Mute);
        //panel.add(unMute);
        panel.add(back);
        panel.setSize(getWidth(), getHeight());
    }

    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        pokemon = new ImageIcon("C://Users//עדן שקורי//IdeaProjects//OOP_EX2//src//gameClient//files//ash.jpg");
        Image pokemon1 = pokemon.getImage();
        Image pokemon2 = pokemon1.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        pokemon = new ImageIcon(pokemon2);
        back.setIcon(pokemon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ((e.getSource())==LoginButton) {
            try {
                ID = Integer.parseInt(ID_Text.getText());
                GameNumber = GameOpt.getSelectedIndex();
                    // clip.stop();
                    Ex2 ex2=new Ex2();
                    JOptionPane.showMessageDialog(this, "Let's start the game");
                    System.out.println("Mark: "+ Mark);
                    this.dispose();
//                ex2.setID(ID);
//                ex2.setScenario_num(GameNumber);
//                ex2.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID\nPlease enter again");
                System.out.println("ERROR, enter again");
            }
        }
        if(e.getSource()==SaveGrade)
        {
            countMark++;
            if((countMark%2)!=0) Mark=1;
            else Mark=0;
        }
//        if(e.getSource()==unMute) {
//            unMute.setVisible(false);
//            Mute.setVisible(true);
//        }
//        if(e.getSource()==Mute) {
//            unMute.setVisible(true);
//            Mute.setVisible(false);
//        }
    }

    public void playMusic() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        File musicPath = new File("C:/Users/עדן שקורי/IdeaProjects/OOP_EX2/src/gameClient/files/pokemon_song.wav");
        AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
        clip = AudioSystem.getClip();
        clip.open(audioInput);
        clip.start();
    }

    public int getGameNumber() {
        return GameNumber;
    }

    public int getID() {
        return ID;
    }
}

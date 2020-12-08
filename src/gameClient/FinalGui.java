package gameClient;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FinalGui extends JFrame {
//    public static void main(String[] args) {
//        FinalGui e=new FinalGui();
//        e.setVisible(true);
//    }
    private int h=360;
    private int w=728;
    private ImageIcon pokemon;
    private JLabel label1;
    private JPanel panel;
    private Graphics gra;
    private Image im;
    private static JTextField UseNameText;
    private static JLabel User_Name_Label;

    FinalGui(){
        initGui();
    }

    private void initGui()
    {
        this.setSize(w,h);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        panel=new JPanel();
        label1=new JLabel();
        panel.setLayout(null);
        this.add(panel);
//        try {
//            playMusic();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (UnsupportedAudioFileException e) {
//            e.printStackTrace();
//        } catch (LineUnavailableException e) {
//            e.printStackTrace();
//        }
//        User_Name_Label = new JLabel("ID");
//        User_Name_Label.setBounds(10, 20, 80, 25);
//        UseNameText = new JTextField(20);
//        UseNameText.setBounds(150, 20, 165, 25);
//        panel.add(User_Name_Label);
//        panel.add(UseNameText);
//        validate();
    }
    public void paint(Graphics g)
    {
        im=this.createImage(w,h);
        gra=im.getGraphics();
        paintComponents(gra);
        g.drawImage(im,0,0,this);
    }
    public void paintComponents(Graphics g)
    {
        super.paintComponents(g);
        pokemon=new ImageIcon("C://Users//עדן שקורי//IdeaProjects//OOP_EX2//src//gameClient//files//ash.jpg");
        Image pokemon1 = pokemon.getImage();
        Image pokemon2 = pokemon1.getScaledInstance(w, h,Image.SCALE_DEFAULT);
        pokemon=new ImageIcon(pokemon2);
        pokemon.paintIcon(this, g, 0,0);

      //  panel2.setBackground(new Color(0,0,0,80));
//        User_Name_Label = new JLabel("ID");
//        User_Name_Label.setBounds(10, 20, 80, 25);
//        UseNameText = new JTextField(20);
//        UseNameText.setBounds(150, 20, 165, 25);
//        panel.add(User_Name_Label);
//        panel.add(UseNameText);

//        Font font = new Font("BN Loco", Font.PLAIN, 20);
//        g.setFont(font);
//        g.drawRect(10, 20, 200, 100);
//        g.drawString("Login success", 10, 20);
    }

    public void playMusic() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File musicPath =new File("C:/Users/עדן שקורי/IdeaProjects/OOP_EX2/src/gameClient/pokemon_song.wav");
        AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
        Clip clip=AudioSystem.getClip();
        clip.open(audioInput);
        clip.start();
    }
}

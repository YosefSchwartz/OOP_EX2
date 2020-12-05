package gameClient.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class example extends JFrame{
private int h=200;
private int w=400;
private Graphics gra;
private Image im;

    example(){
    initGuy();
}

private void initGuy()
{
    this.setSize(w, h);
    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    MenuBar menu =new MenuBar();
    this.setMenuBar(menu);
    Menu M =new Menu("MENU");
    MenuItem item1 =new MenuItem("close");
    MenuItem item2 =new MenuItem("clean");
    M.add(item1);
    M.add(item2);
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
    g.setColor(Color.DARK_GRAY);
    g.fillOval(180, 80,40 , 40 );
    g.setColor(Color.red);
    Font f=new Font("BN Loco", Font.PLAIN, 40);
    g.setFont(f);
    g.drawString("hello", 230, 130);
    //g.setFont();
    //g.drawLine(20,20, 80,80);
}
//public void playMusic() throws FileNotFoundException {
//        File Musicpath  =new File("C://Users//עדן שקורי//IdeaProjects//OOP_EX2//src//gameClient")
//        AudioInputStream audiostreem =new AudioSystem.getAudioInputStream(Musicpath);
//        Clip clip=new AudioSystem.getClip();
//        clip.open();
//        clip.start();
//    AudioStream audio=new AudioStream(music);
//}


}

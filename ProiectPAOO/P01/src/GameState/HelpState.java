package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HelpState extends GameState
{
    private Background bg;

    private int currentChoice = 0;

    private String[] options = {
            "PLAY",
            "BACK"
    };

    private Color titleColor;
    private Font font;

    private Font font1;

    public HelpState(GameStateManager gsm)
    {
        this.gsm = gsm;
        try
        {
            bg = new Background("/Backgrounds/help.png", 1);

            titleColor = new Color(7, 49, 227);
            font = new Font("Britannic Bold", Font.PLAIN, 15);
            font1 = new Font("Berlin Sans FB Bold", Font.PLAIN, 15);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void init() {}

    public void update() { bg.update(); }

    public void draw(Graphics2D g)
    {
        // draw bg
        bg.draw(g);

        // draw title
        g.setColor(titleColor);
        g.setFont(font);
        g.drawString("Game controls : ", 100, 18);
        g.setColor(Color.black);
        g.drawString("For move left or move right use : ", 15, 60);
        g.drawString("LEFT ARROW/RIGHT ARROW", 120, 80);
        g.drawString("For jumping use : SPACE", 15, 100);
        g.drawString("For gliding use : SHIFT", 15, 120);
        g.drawString("For attack enemies use : X", 15, 140);
        g.drawString("For collect key and open the door use : C", 15, 160);

        // draw menu options
        g.setFont(font1);
        for(int i = 0; i < options.length; i++)
        {
            if(i == currentChoice)
            {
                g.setColor(Color.GREEN);
            }
            else
            {
                g.setColor(Color.BLACK);
            }
            g.drawString(options[i], 270, 215 + i * 15);
        }
    }

    private void select()
    {
        if(currentChoice == 0)
        {
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if(currentChoice == 1)
        {
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    public void keyPressed(int k)
    {
        if(k == KeyEvent.VK_ENTER)
        {
            select();
        }
        if(k == KeyEvent.VK_UP)
        {
            currentChoice--;
            if(currentChoice == -1)
            {
                currentChoice = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN)
        {
            currentChoice++;
            if(currentChoice == options.length)
            {
                currentChoice = 0;
            }
        }
    }
    public void keyReleased(int k) {}
}
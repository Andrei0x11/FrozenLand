package GameState;

import Entity.Hero;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState
{
    private Background bg;

    private int currentChoice = 0;
    private String[] options = {
            "START",
            "HELP",
            "QUIT"
    };

    private Color titleColor;
    private Font titleFont;
    private Font menuFont;

    private Font font;

    public MenuState(GameStateManager gsm)
    {
        this.gsm = gsm;
        try
        {

            bg = new Background("/Backgrounds/fundal.png", 1);
            bg.setVector(0, 0);

            titleColor = new Color(7, 49, 227);
            titleFont = new Font("Calibri Bold", Font.PLAIN, 35);
            font = new Font("Arial Black", Font.PLAIN, 12);
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
        g.setFont(titleFont);
        g.drawString("Tărămul Gheții", 55, 60);

        // draw menu options
        g.setFont(font);
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
            g.drawString(options[i], 140, 165 + i * 20);
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
            gsm.setState(GameStateManager.HELPSTATE);
        }
        if(currentChoice == 2)
        {
            System.exit(0);
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
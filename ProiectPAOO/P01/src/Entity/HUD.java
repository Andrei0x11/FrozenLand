package Entity;

import GameState.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HUD
{

    private Hero hero;

    private BufferedImage image;
    private Font font;

    public HUD(Hero h)
    {
        hero = h;
        try
        {
            image = ImageIO.read(getClass().getResourceAsStream("/HUD/hud.png"));
            font = new Font("Arial", Font.PLAIN, 14);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g)
    {
        g.drawImage(image, 0, 3, null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(hero.getHealth() + " / " + hero.getMaxHealth(), 30, 18);
        //g.drawString(hero.getNrKey() + " / " + 3,30,38);
    }
}

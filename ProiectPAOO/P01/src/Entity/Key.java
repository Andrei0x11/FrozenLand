package Entity;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Key extends MapObject
{
    private BufferedImage[] sprites;

    public Key(TileMap tm)
    {
        super(tm);
        moveSpeed = 0;
        maxSpeed = 0;
        fallSpeed = 0;
        maxFallSpeed = 0;

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        try
        {

            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Key/goldenkey.png"));

            sprites = new BufferedImage[1];
            for(int i = 0; i < sprites.length; i++)
            {
                sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
            }

        } catch(Exception e)
        {
            e.printStackTrace();
        }

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);

        right = true;
        facingRight = true;
    }

    public void draw(Graphics2D g)
    {
        setMapPosition();
        super.draw(g);
    }
}

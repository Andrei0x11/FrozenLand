package Entity;

import GameState.GameState;
import TileMap.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Hero extends MapObject
{
    // player stuff
    private int health;
    private int maxHealth;
    private boolean dead = false;
    private boolean flinching;
    private long flinchTimer;

    // scratch
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;

    // gliding
    private boolean gliding;

    //number of key
    private int nrKey = 0;

    //colect and open gate
    private boolean action;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {2, 8, 1, 2, 1, 3};

    // animation actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int GLIDING = 4;
    private static final int SCRATCHING = 5;

    private int score1;
    private int score2;

    public Hero(TileMap tm)
    {
        super(tm);

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 3;

        scratchDamage = 8;
        scratchRange = 30;

        // load sprites
        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Hero/herosprites.png"));
            sprites = new ArrayList<BufferedImage[]>();
            for(int i = 0; i < 6; i++)
            {

                BufferedImage[] bi = new BufferedImage[numFrames[i]];

                for(int j = 0; j < numFrames[i]; j++)
                {
                   bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
                }
                sprites.add(bi);
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);
    }

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getNrKey() { return nrKey; }

    public void setScratching()
    {
        scratching = true;
    }
    public void setGliding(boolean b)
    {
        gliding = b;
    }
    public void setAction(boolean b) {action = b;}
    public int getScore1() { return score1; }
    public int getScore2() { return score2; }

    public void setScore1(int score) { score1 = score; }
    public void setScore2(int score) { score2 = score; }

    public void checkAttack(ArrayList<Enemy> enemies)
    {

        // loop through enemies
        for(int i = 0; i < enemies.size(); i++)
        {
            Enemy e = enemies.get(i);
            // scratch attack
            if(scratching)
            {
                if(facingRight)
                {
                    if(e.getx() > x && e.getx() < x + scratchRange && e.gety() > y - height / 2 && e.gety() < y + height / 2)
                    {
                        e.hit(scratchDamage);
                        score1+=150;
                        score2+=150;
                    }
                }
                else
                {
                    if(e.getx() < x && e.getx() > x - scratchRange && e.gety() > y - height / 2 && e.gety() < y + height / 2)
                    {
                        e.hit(scratchDamage);
                        score1+=150;
                        score2+=150;
                    }
                }
            }

            // check enemy collision
            if(intersects(e))
            {
                hit(e.getDamage());
                facingRight = true;
                setPosition(100,70);
                score1 -= 200;
                score2 -= 200;
            }
        }
    }

    public void collectKey(ArrayList<Key> keys)
    {
        for(int i = 0;i < keys.size();i++)
        {
            Key K = keys.get(i);
            if(intersects(K) && action == true)
            {
                nrKey++;
                keys.remove(i);
                i--;
                score1 += 100;
                score2 += 100;
            }
        }
    }

    public boolean openDoor1(Door d)
    {
        return (intersects(d) && action == true && nrKey == 3);
    }
    public boolean openDoor2(Door d)
    {
        return (intersects(d) && action == true && nrKey == 5);
    }

    public void hit(int damage)
    {
        if(flinching) return;
        health -= damage;
        if(health < 0) health = 0;
        if(health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public boolean isDead()
    {
        if(health == 0) dead = true;
        return dead;
    }

    private void getNextPosition() {

        // movement
        if(left)
        {
            dx -= moveSpeed;
            if(dx < -maxSpeed)
            {
                dx = -maxSpeed;
            }
        }
        else if(right)
        {
            dx += moveSpeed;
            if(dx > maxSpeed)
            {
                dx = maxSpeed;
            }
        }
        else
        {
            if(dx > 0)
            {
                dx -= stopSpeed;
                if(dx < 0)
                {
                    dx = 0;
                }
            }
            else if(dx < 0)
            {
                dx += stopSpeed;
                if(dx > 0)
                {
                    dx = 0;
                }
            }
        }

        // cannot move while attacking, except in air
        if((currentAction == SCRATCHING) && !(jumping || falling))
        {
            dx = 0;
        }

        // jumping
        if(jumping && !falling)
        {
            dy = jumpStart;
            falling = true;
        }

        // falling
        if(falling)
        {

            if(dy > 0 && gliding) dy += fallSpeed * 0.1;
            else dy += fallSpeed;

            if(dy > 0) jumping = false;
            if(dy < 0 && !jumping) dy += stopJumpSpeed;

            if(dy > maxFallSpeed) dy = maxFallSpeed;

        }

    }

    public void update()
    {

        // update position
        getNextPosition();
        checkTileMapCollision();
        if(y>=402)
        {
            health--;
            //JOptionPane.showMessageDialog(null,"You lost a life!Be more careful!");
            if(health < 0) health = 0;
            if(health == 0) {dead = true;}
            facingRight = true;
            setPosition(100,70);
            score1 -= 200;
            score2 -= 200;
            flinching = true;
            flinchTimer = System.nanoTime();
        }else
        {
            setPosition(xtemp, ytemp);
        }


        // check attack has stopped
        if(currentAction == SCRATCHING)
        {
            if(animation.hasPlayedOnce()) scratching = false;
        }

        // check done flinching
        if(flinching)
        {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 1000) { flinching = false; }
        }

        // set animation
        if(scratching)
        {
            if(currentAction != SCRATCHING)
            {
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(40);
            }
        }
        else if(dy > 0)
        {
            if(gliding)
            {
                if(currentAction != GLIDING)
                {
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 30;
                }
            }
            else if(currentAction != FALLING)
            {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 30;
            }
        }
        else if(dy < 0)
        {
            if(currentAction != JUMPING)
            {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 30;
            }
        }
        else if(left || right)
        {
            if(currentAction != WALKING)
            {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        }
        else
        {
            if(currentAction != IDLE)
            {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 30;
            }
        }

        animation.update();

        // set direction
        if(currentAction != SCRATCHING)
        {
            if(right) facingRight = true;
            if(left) facingRight = false;
        }

    }

    public void draw(Graphics2D g) {

        setMapPosition();
        // draw player
        if(flinching)
        {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed / 100 % 2 == 0) { return; }
        }
        super.draw(g);
    }
}
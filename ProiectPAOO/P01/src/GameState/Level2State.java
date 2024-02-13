package GameState;

import Entity.*;
import Entity.Enemies.Slugger;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class Level2State extends GameState
{
    private TileMap tileMap;
    private Background bg;

    private Hero hero;

    private ArrayList<Enemy> enemies;
    private ArrayList<Key> keys;
    private Door D;

    private HUD hud;

    public Level2State(GameStateManager gsm)
    {
        this.gsm = gsm;
        init();
    }

    public void init()
    {
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/platforms.png");
        tileMap.loadMap("/Maps/level2.map");
        tileMap.setPosition(0, 0);
        tileMap.setTween(1);

        bg = new Background("/Backgrounds/fundal-nivel2.png", 0.1);

        hero = new Hero(tileMap);
        hero.setPosition(100, 70);
        hero.setScore2(600);

        populateEnemies();
        populateKeys();

        D = new Door(tileMap);
        D.setPosition(2317,380);

        hud = new HUD(hero);
    }

    private void populateEnemies()
    {
        enemies = new ArrayList<Enemy>();

        Slugger s;
        Point[] points = new Point[] {
                new Point(207, 140),
                new Point(380,380),
                new Point(943, 290),
                new Point(1276, 380),
                new Point(1806, 170),
                new Point(2222, 80),
                new Point(1680, 380)
        };
        for(int i = 0; i < points.length; i++)
        {
            s = new Slugger(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
    }

    private void populateKeys()
    {
        keys = new ArrayList<Key>();
        Key k;
        Point[] points = new Point[]{
                new Point(88,203),
                new Point(687,113),
                new Point(1118,353),
                new Point(1556,323),
                new Point(2352,233)
        };
        for(int i = 0;i < points.length;i++)
        {
            k = new Key(tileMap);
            k.setPosition(points[i].x,points[i].y);
            keys.add(k);
        }
    }

    public void update()
    {
        // update hero
        hero.update();
        tileMap.setPosition(GamePanel.WIDTH / 2 - hero.getx(), GamePanel.HEIGHT / 2 - hero.gety());

        // attack enemies
        hero.checkAttack(enemies);

        // update all enemies
        for(int i = 0; i < enemies.size(); i++)
        {
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead())
            {
                enemies.remove(i);
                i--;
            }
        }

        //reset when healt is 0
        if(hero.isDead())
        {
            JOptionPane.showMessageDialog(null,"Ai pierdut!\nReîncearcă!");
            Connection c = null;
            Statement stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:DBScore.db");
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String sql = "UPDATE SCORES set SCORE = " + hero.getScore2() + " where LEVEL=2;";
                stmt.executeUpdate(sql);
                c.commit();
                stmt.close();
                c.close();
            } catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
                System.exit(0);
            }
            hero.setPosition(100,70);
            gsm.setState(GameStateManager.LEVEL2STATE);
        }

        //open door
        if(hero.openDoor2(D)){
            JOptionPane.showMessageDialog(null,"Ai terminat acest nivel cu scorul : "+ hero.getScore2() + "\n" +"Felicitari ai terminat jocul cu scorul : " + (hero.getScore1() + hero.getScore2()) +"\nAi câstigat jocul!");
            Connection c = null;
            Statement stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:DBScore.db");
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String sql = "UPDATE SCORES set SCORE = " + hero.getScore2() + " where LEVEL=2;";
                stmt.executeUpdate(sql);
                c.commit();
                stmt.close();
                c.close();
            } catch (Exception e1) {
                System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
                System.exit(0);
            }
            gsm.setState((GameStateManager.MENUSTATE));
        }

        hero.collectKey(keys);
    }

    public void draw(Graphics2D g)
    {
        // draw bg
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        //draw door
        D.draw(g);

        // draw hero
        hero.draw(g);

        // draw enemies
        for(int i = 0; i < enemies.size(); i++)
        {
            enemies.get(i).draw(g);
        }

        //draw keys
        for(int i = 0;i < keys.size();i++)
        {
            keys.get(i).draw(g);
        }

        // draw hud
        hud.draw(g);
        g.drawString(hero.getNrKey() + " / " + 5,30,38);
    }

    public void keyPressed(int k)
    {
        if(k == KeyEvent.VK_LEFT) hero.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) hero.setRight(true);
        if(k == KeyEvent.VK_UP) hero.setUp(true);
        if(k == KeyEvent.VK_DOWN) hero.setDown(true);
        if(k == KeyEvent.VK_SPACE) hero.setJumping(true);
        if(k == KeyEvent.VK_SHIFT) hero.setGliding(true);
        if(k == KeyEvent.VK_X) hero.setScratching();
        if(k == KeyEvent.VK_C) hero.setAction(true);
    }

    public void keyReleased(int k)
    {
        if(k == KeyEvent.VK_LEFT) hero.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) hero.setRight(false);
        if(k == KeyEvent.VK_UP) hero.setUp(false);
        if(k == KeyEvent.VK_DOWN) hero.setDown(false);
        if(k == KeyEvent.VK_SPACE) hero.setJumping(false);
        if(k == KeyEvent.VK_SHIFT) hero.setGliding(false);
        if(k == KeyEvent.VK_C) hero.setAction(false);
    }
}

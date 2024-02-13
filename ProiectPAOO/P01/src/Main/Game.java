package Main;

import javax.swing.JFrame;

public class Game
{
    public static void main(String[] args)
    {

        JFrame window = new JFrame("Tărâmul Gheții");
        GamePanel gp = GamePanel.get_instance();
        window.setContentPane(gp);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
}
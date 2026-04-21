import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInvaders67 extends JPanel implements ActionListener, KeyListener {

    private String input = "";

    private int playerX = 180;
    private Image playerImage;

    private Image png1 = new ImageIcon("C:\\Users\\tgardner2\\Documents\\67SPEEDJAVA\\Frames\\FRAME 0.png").getImage();
    private Image png2 = new ImageIcon("C:\\Users\\tgardner2\\Documents\\67SPEEDJAVA\\Frames\\FRAME 2.png").getImage();

    private ArrayList<Rectangle> enemies = new ArrayList<>();
    private ArrayList<Rectangle> bullets = new ArrayList<>();

    private Timer gameLoop;

    private int enemySpeed = 1;
    private int spawnDelay = 60; // lower = more enemies
    private int tick = 0;

    private int score = 0;

    private Random rand = new Random();

    public SpaceInvaders67() {
        setPreferredSize(new Dimension(400, 500));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        playerImage = png1;

        gameLoop = new Timer(30, this);
        gameLoop.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tick++;

        // Spawn enemies continuously
        if (tick % spawnDelay == 0) {
            int x = rand.nextInt(350);
            enemies.add(new Rectangle(x, 0, 30, 30));
        }

        // Move enemies
        for (Rectangle enemy : enemies) {
            enemy.y += enemySpeed;

            if (enemy.y > 450) {
                gameOver();
            }
        }

        // Move bullets
        ArrayList<Rectangle> removeList = new ArrayList<>();

        for (Rectangle bullet : bullets) {
            bullet.y -= 8;

            for (Rectangle enemy : enemies) {
                if (bullet.intersects(enemy)) {
                    removeList.add(enemy);
                    removeList.add(bullet);
                    score++;
                }
            }
        }

        enemies.removeAll(removeList);
        bullets.removeAll(removeList);

        // 🔥 Difficulty scaling over time
        if (tick % 300 == 0) { // every ~9 seconds
            enemySpeed++;
            if (spawnDelay > 15) spawnDelay -= 5;
        }

        repaint();
    }

    private void shoot() {
        bullets.add(new Rectangle(playerX + 55, 380, 5, 10));
    }

    private void gameOver() {
        gameLoop.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
        System.exit(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Player (big PNG)
        g.drawImage(playerImage, playerX, 380, 120, 120, null);

        // Enemies
        g.setColor(Color.RED);
        for (Rectangle enemy : enemies) {
            g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
        }

        // Bullets
        g.setColor(Color.YELLOW);
        for (Rectangle bullet : bullets) {
            g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
        }

        // HUD
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Speed: " + enemySpeed, 10, 40);
        g.drawString("Type 67 to shoot!", 10, 60);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();

        if (key == '6') {
            input += "6";
            playerImage = png1;
        } else if (key == '7') {
            input += "7";
            playerImage = png2;
        }

        if (!"67".startsWith(input)) {
            input = "";
        }

        if (input.equals("67")) {
            shoot();
            input = "";
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) playerX -= 15;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) playerX += 15;
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders 67 - Endless Mode");
        SpaceInvaders67 game = new SpaceInvaders67();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
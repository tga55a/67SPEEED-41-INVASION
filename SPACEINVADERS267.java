import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SPACEINVADERS267 extends JPanel implements ActionListener, KeyListener {

    // ---------------- PLAYER ----------------
    private int playerX = 200;
    private final int playerY = 380;

    private Image playerImage;

    private final int drawWidth = 250;
    private final int drawHeight = 200;

    private final int hitboxW = 100;
    private final int hitboxH = 70;

    private Image png1 = new ImageIcon("C:\\Users\\tgardner2\\Documents\\67SPEEDJAVA\\Frames\\FRAME 0.png")
            .getImage().getScaledInstance(drawWidth, drawHeight, Image.SCALE_SMOOTH);

    private Image png2 = new ImageIcon("C:\\Users\\tgardner2\\Documents\\67SPEEDJAVA\\Frames\\FRAME 2.png")
            .getImage().getScaledInstance(drawWidth, drawHeight, Image.SCALE_SMOOTH);

    // ---------------- ENTITIES ----------------
    private List<Rectangle> enemies = new ArrayList<>();
    private List<Shooter> shooters = new ArrayList<>();
    private List<Rectangle> bullets = new ArrayList<>();
    private List<Rectangle> enemyBullets = new ArrayList<>();

    private class Shooter {
        Rectangle rect;
        int cooldown;
        Color color;
        int hitFlash;

        Shooter(Rectangle r) {
            this.rect = r;
            this.cooldown = 60;
            this.color = brainrotColor();
            this.hitFlash = 0;
        }
    }

    private Random rand = new Random();

    // ---------------- LOOP ----------------
    private Timer gameLoop;

    // ---------------- GAME STATE ----------------
    private int tick = 0;
    private int enemySpeed = 1;
    private int spawnDelay = 60;
    private int score = 0;

    // ---------------- SHAKE ----------------
    private int shakeTimer = 0;
    private int shakeIntensity = 0;

    // ---------------- BOSS ----------------
    private boolean bossActive = false;
    private Rectangle boss;
    private int bossHealth = 20;

    // ---------------- INIT ----------------
    public SPACEINVADERS267() {
        setPreferredSize(new Dimension(400, 500));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        playerImage = png1;

        gameLoop = new Timer(30, this);
        gameLoop.start();
    }

    // ---------------- BRAINROT COLORS ----------------
    private Color brainrotColor() {
        Color[] colors = {
                Color.CYAN, Color.PINK, Color.MAGENTA,
                Color.GREEN, Color.YELLOW, Color.ORANGE,
                new Color(255, 0, 200),
                new Color(0, 255, 255),
                new Color(255, 255, 0)
        };
        return colors[rand.nextInt(colors.length)];
    }

    // ---------------- GAME LOOP ----------------
    @Override
    public void actionPerformed(ActionEvent e) {
        tick++;

        if (shakeTimer > 0) shakeTimer--;
        else shakeIntensity = 0;

        // NORMAL ENEMIES
        if (!bossActive && tick % spawnDelay == 0) {
            enemies.add(new Rectangle(rand.nextInt(340), 0, 45, 45));
        }

        // CYAN MIDTIER SHOOTERS
        if (!bossActive && tick % (spawnDelay * 3) == 0) {
            shooters.add(new Shooter(new Rectangle(rand.nextInt(330), 0, 40, 40)));
        }

        // MOVE ENEMIES
        for (Rectangle e1 : enemies) {
            e1.y += enemySpeed;
            if (e1.y > 450) gameOver();
        }

        // MOVE SHOOTERS + SHOOT LOGIC
        for (Shooter s : shooters) {
            s.rect.y += enemySpeed;
            s.rect.x += rand.nextInt(3) - 1;

            if (s.hitFlash > 0) s.hitFlash--;

            s.cooldown--;

            if (s.cooldown <= 0) {
                enemyBullets.add(new Rectangle(
                        s.rect.x + 18,
                        s.rect.y + 20,
                        6,
                        12
                ));
                s.cooldown = 90;
            }

            if (s.rect.y > 450) gameOver();
        }

        // BOSS
        if (bossActive && boss != null) {
            boss.x += (int)(Math.sin(tick * 0.05) * 3);

            if (rand.nextInt(20) == 0) {
                enemyBullets.add(new Rectangle(boss.x + 50, boss.y + 60, 8, 15));
            }
        }

        updateBullets();
        repaint();
    }

    // ---------------- BULLETS ----------------
    private void updateBullets() {

        List<Rectangle> remove = new ArrayList<>();

        // PLAYER BULLETS
        for (Rectangle b : bullets) {
            b.y -= 8;

            // RED ENEMIES HIT
            for (Rectangle e : enemies) {
                if (b.intersects(e)) {
                    remove.add(b);
                    remove.add(e);
                    score++;
                    shake(5);
                }
            }

            // CYAN MIDTIER HIT (NO DEATH, ONLY REACTION)
            for (Shooter s : shooters) {
                if (b.intersects(s.rect)) {

                    remove.add(b);
                    score += 2;
                    shake(8);

                    s.color = brainrotColor();
                    s.hitFlash = 10;

                    s.rect.y += 2; // slight reaction
                }
            }

            if (bossActive && boss != null && b.intersects(boss)) {
                remove.add(b);
                bossHealth--;
                shake(10);

                if (bossHealth <= 0) {
                    bossActive = false;
                    boss = null;
                    score += 10;
                }
            }
        }

        // PLAYER HITBOX
        Rectangle player = new Rectangle(
                playerX - hitboxW / 2,
                playerY,
                hitboxW,
                hitboxH
        );

        for (Rectangle eb : enemyBullets) {
            eb.y += 6;

            if (eb.intersects(player)) {
                shake(15);
                gameOver();
            }
        }

        bullets.removeAll(remove);
        enemies.removeAll(remove);
    }

    // ---------------- SHOOT ----------------
    private void shoot() {
        bullets.add(new Rectangle(playerX, playerY, 6, 12));
    }

    // ---------------- SHAKE ----------------
    private void shake(int intensity) {
        shakeTimer = 10;
        shakeIntensity = intensity;
    }

    // ---------------- GAME OVER ----------------
    private void gameOver() {
        gameLoop.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
        System.exit(0);
    }

    // ---------------- DRAW ----------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.setColor(Color.WHITE);

        int offsetX = 0;
        int offsetY = 0;

        if (shakeTimer > 0) {
            offsetX = rand.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
            offsetY = rand.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
        }

        g.translate(offsetX, offsetY);

        // PLAYER
        g.drawImage(playerImage, playerX - 125, playerY, null);

        // RED ENEMIES (UPDATED STYLE ONLY)
        g.setFont(new Font("Impact", Font.BOLD, 22));
        for (Rectangle e : enemies) {
            if (rand.nextInt(10) == 0) {
                g.setColor(new Color(255, 80, 80));
            } else {
                g.setColor(Color.RED);
            }
            g.drawString("41", e.x, e.y);

            g.setColor(Color.BLACK);
            g.drawString("41", e.x + 1, e.y + 1);
        }

        // CYAN MIDTIER SHOOTERS (RESTORED ORIGINAL CHAOS STYLE)
        for (Shooter s : shooters) {

            if (s.hitFlash > 0) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(s.color);
            }

            g.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
            g.drawString("41", s.rect.x, s.rect.y);
        }

        // BULLETS
        g.setColor(Color.YELLOW);
        for (Rectangle b : bullets) {
            g.fillRect(b.x, b.y, 6, 12);
        }

        // ENEMY BULLETS
        g.setColor(Color.ORANGE);
        for (Rectangle eb : enemyBullets) {
            g.fillRect(eb.x, eb.y, 6, 12);
        }

        // BOSS
        if (bossActive && boss != null) {
            g.setColor(Color.MAGENTA);
            g.fillRect(boss.x, boss.y, boss.width, boss.height);
            g.setColor(Color.WHITE);
            g.drawString("BOSS HP: " + bossHealth, boss.x, boss.y - 10);
        }

        // HUD
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Score: " + score, 10, 20);

        g.translate(-offsetX, -offsetY);
    }

    // ---------------- INPUT ----------------
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) playerX -= 14;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) playerX += 14;

        playerX = Math.max(20, Math.min(380, playerX));
    }

    @Override public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();

        if (key == '6') playerImage = png1;
        if (key == '7') {
            playerImage = png2;
            shoot();
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders 67 - Final Brainrot Edition");
        SPACEINVADERS267 game = new SPACEINVADERS267();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
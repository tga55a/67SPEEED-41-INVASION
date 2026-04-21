import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Type67Game extends JFrame implements KeyListener {

    private JLabel imageLabel;
    private JLabel infoLabel;

    private ImageIcon png1;
    private ImageIcon png2;

    private String input = "";
    private int round = 1;
    private int timeLeft = 5; // seconds
    private Timer timer;

    public Type67Game() {
        setTitle("Type 67 Game");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Load images
        png1 = new ImageIcon("C:\\Users\\tgardner2\\Documents\\67SPEEDJAVA\\Frames\\FRAME 0.png");
        png2 = new ImageIcon("C:\\Users\\tgardner2\\Documents\\67SPEEDJAVA\\Frames\\FRAME 2.png");

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        infoLabel = new JLabel("Round 1 - Type 67!", JLabel.CENTER);

        add(imageLabel, BorderLayout.CENTER);
        add(infoLabel, BorderLayout.SOUTH);

        addKeyListener(this);
        setFocusable(true);

        startRound();
    }

    private void startRound() {
        input = "";
        timeLeft = Math.max(1, 5 - round); // harder each round

        infoLabel.setText("Round " + round + " - Time: " + timeLeft + "s");

        if (timer != null) timer.stop();

        timer = new Timer(1000, e -> {
            timeLeft--;
            infoLabel.setText("Round " + round + " - Time: " + timeLeft + "s");

            if (timeLeft <= 0) {
                gameOver();
            }
        });

        timer.start();
    }

    private void gameOver() {
        timer.stop();
        //JOptionPane.showMessageDialog(this, "Game Over! You reached round " + round);
        //System.exit(0);
    }

    private void winRound() {
        timer.stop();
        round++;
        //JOptionPane.showMessageDialog(this, "You win this round!");
        startRound();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();

        if (key == '6') {
            input += "6";
            imageLabel.setIcon(png1);
        } else if (key == '7') {
            input += "7";
            imageLabel.setIcon(png2);
        } else {
            return;
        }

        // Check sequence
        if (!"67".startsWith(input)) {
            input = ""; // reset if wrong
        }

        if (input.equals("67")) {
            winRound();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Type67Game game = new Type67Game();
            game.setVisible(true);
        });
    }
}
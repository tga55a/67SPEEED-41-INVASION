import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game67 {
    private JFrame frame;
    private JLabel instructionLabel;
    private JLabel timerLabel;
    private JLabel roundLabel;
    private JLabel imageLabel;
    private JTextField inputField;

    private ImageIcon img1;
    private ImageIcon img2;

    private String target = "67";
    private int timeLeft;
    private int round = 1;
    private int baseTime = 5;

    private Timer countdownTimer;

    public Game67() {
        frame = new JFrame("67 Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 350);
        frame.setLayout(new GridLayout(5, 1));

        instructionLabel = new JLabel("Type 6 then 7!", SwingConstants.CENTER);
        timerLabel = new JLabel("", SwingConstants.CENTER);
        roundLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel = new JLabel("", SwingConstants.CENTER);
        inputField = new JTextField();

        // Load images
        img1 = new ImageIcon("C:\\Users\\tgardner2\\Documents\\67SPEEDJAVA\\Frames\\FRAME 0.png");
        img2 = new ImageIcon("C:\\Users\\tgardner2\\Documents\\67SPEEDJAVA\\Frames\\FRAME 2.png");

        frame.add(instructionLabel);
        frame.add(roundLabel);
        frame.add(timerLabel);
        frame.add(imageLabel);
        frame.add(inputField);

        frame.setVisible(true);

        startRound();

        inputField.addKeyListener(new KeyAdapter() {
            private String currentInput = "";

            @Override
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();

                if (key == '6' || key == '7') {
                    currentInput += key;

                    // Show correct image
                    if (key == '6') {
                        imageLabel.setIcon(img1);
                    } else {
                        imageLabel.setIcon(img2);
                    }

                    // Update text field manually (optional)
                    inputField.setText(currentInput);

                    // Game logic
                    if (!target.startsWith(currentInput)) {
                        lose();
                    } else if (currentInput.equals(target)) {
                        nextRound();
                    }
                    currentInput = "";
                }

                // Prevent weird extra input behavior
                e.consume();
            }
        });
    }

    private void startRound() {
        inputField.setText("");
        inputField.setEditable(true);

        // Difficulty scaling: reduce time each round (minimum 1 sec)
        timeLeft = Math.max(1, baseTime - (round - 1));

        roundLabel.setText("Round: " + round);
        timerLabel.setText("Time: " + timeLeft);

        // ✅ default image
        imageLabel.setIcon(img1);

        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);

            if (timeLeft <= 0) {
                lose();
            }
        });

        countdownTimer.start();
    }

    private void nextRound() {
        countdownTimer.stop();
        round++;
        startRound();
    }

    private void lose() {
        countdownTimer.stop();
        inputField.setEditable(false);
        instructionLabel.setText("You lost at round " + round);

        // Optional: restart after 2 seconds
        new Timer(2000, e -> {
            round = 1;
            instructionLabel.setText("Type 6 then 7!");
            startRound();
        }) {{ setRepeats(false); }}.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game67::new);
    }
}
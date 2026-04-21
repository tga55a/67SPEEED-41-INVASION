import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ImageCycler {
    private JLabel label;
    private ImageIcon img1;
    private ImageIcon img2;
    private boolean toggle = false;

    public ImageCycler() {
        JFrame frame = new JFrame("Image Cycler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Load images (make sure paths are correct)
        img1 = new ImageIcon("C:\\Users\\tgardner2\\Documents\\67SPEEDJAVA\\Frames\\FRAME 0.png");
        img2 = new ImageIcon("C:\\Users\\tgardner2\\Documents\\67SPEEDJAVA\\Frames\\FRAME 2.png");

        label = new JLabel(img1);
        label.setHorizontalAlignment(JLabel.CENTER);

        frame.add(label);
        frame.setVisible(true);

        // Timer fires every 500ms (0.5 seconds)
        Timer timer = new Timer(75, (ActionEvent e) -> {
            toggle = !toggle;
            label.setIcon(toggle ? img2 : img1);
        });

        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageCycler::new);
    }
}
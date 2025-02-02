import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageViewer extends JFrame {
    private BufferedImage image;
    
    public ImageViewer(int width, int height) {
        this.setTitle("Image Viewer");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        
        panel.setPreferredSize(new Dimension(width, height));
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }
    
    // Method to update a pixel
    public void setPixel(int x, int y, Color color) {
        image.setRGB(x, y, color.getRGB());
        this.repaint();
    }
    
    // Method to update entire image at once
    public void setImage(int[][] pixels) {
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                image.setRGB(x, y, pixels[x][y]);
            }
        }
        this.repaint();
    }
    
    public static void main(String[] args) {
        // Example usage
        SwingUtilities.invokeLater(() -> {
            ImageViewer viewer = new ImageViewer(500, 500);
            viewer.setVisible(true);
            
            // Example: Draw a red square
            for (int x = 100; x < 200; x++) {
                for (int y = 100; y < 200; y++) {
                    viewer.setPixel(x, y, Color.RED);
                }
            }
        });
    }
}
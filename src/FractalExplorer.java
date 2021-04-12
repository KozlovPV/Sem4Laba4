import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;

public class FractalExplorer {
    private int displaySize;
    private JImageDisplay imageDisplay;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double range;

    public FractalExplorer(int displaySize) {

        this.displaySize = displaySize;
        this.fractalGenerator = new Mandelbrot();
        this.range = new Rectangle2D.Double(0,0,0,0);
        fractalGenerator.getInitialRange(this.range);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal Explorer");
        imageDisplay = new JImageDisplay(displaySize, displaySize);
        JButton button = new JButton("Reset Display");
        button.setPreferredSize(new Dimension(200,100));



        ActionHandler aHandler = new ActionHandler();
        MouseHandler mHandler = new MouseHandler();
        button.addActionListener(aHandler);
        imageDisplay.addMouseListener(mHandler);

        frame.setLayout(new java.awt.BorderLayout());
        frame.add(imageDisplay, BorderLayout.CENTER);
        frame.add(button, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public void drawFractal() {
        for (int i = 0; i < displaySize; i++) {
            for (int j = 0; j < displaySize; j++) {
                double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, i);
                double yCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, j);
                double count = fractalGenerator.numIterations(xCoord,yCoord);

                if(count==-1){
                    imageDisplay.drawPixel(i,j,0);
                }
                else{
                    float hue = 0.3f + (float) count / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    imageDisplay.drawPixel(i,j,rgbColor);
                }
            }
        }
        imageDisplay.repaint();
    }

    public class ActionHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }

    public class MouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            double xCoord = FractalGenerator.getCoord(range.x,
                    range.x + range.width, displaySize, e.getX());
            double yCoord = FractalGenerator.getCoord(range.y,
                    range.y + range.width, displaySize, e.getY());
            fractalGenerator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }

    public static void main(String[] args) {
        FractalExplorer fracExp = new FractalExplorer(700);
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }
}
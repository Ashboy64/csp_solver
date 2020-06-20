package map_coloring;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Visualizer extends Frame {

    private final int SCALE = 500;
    private List<Double[][]> lines;

    public static void main(String args[]) {
        new Visualizer(new ArrayList<>());
    }

    public Visualizer(List<Double[][]> lines) {
        super("Map Visualizer");
        this.lines = lines;

        setSize(SCALE+100,SCALE+100);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.blue);
        g2.setStroke(new BasicStroke(3));

        Random r = new Random();

        for (Double[][] l : lines) {
            // g2.setColor(Color.getHSBColor(r.nextFloat(), 1f, 0.75f));
            Double[] p_i = new Double[]{SCALE*l[0][0]+50, SCALE*l[0][1]+50};
            Double[] p_f = new Double[]{SCALE*l[1][0]+50, SCALE*l[1][1]+50};
            g2.drawLine(p_i[0].intValue(), p_i[1].intValue(), p_f[0].intValue(), p_f[1].intValue());
        }
    }
}
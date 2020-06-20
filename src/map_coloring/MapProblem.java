package map_coloring;

import csp_solver.CSP;
import csp_solver.Relation;
import csp_solver.Variable;

import java.util.*;
import java.util.List;

public class MapProblem extends CSP {

    private List<Double[][]> lines;
    private List<Variable<Point>> variables;
    private List<Relation<Point>> relations;

    public MapProblem(int n) {
        variables = new ArrayList<>();
        relations = new ArrayList<>();

        initialize(n);
        plot();
    }

    private void initialize(int n) {
        initLines(n);
        System.out.println(variables.size());
        System.out.println(relations.size());
    }

    // TODO: Take any number of colors in rather than hardcoding three
    private void initLines(int n) {
        Random rand = new Random();

        Double[][] points = new Double[n][2];
        for (int i = 0; i < points.length; i++) {
            points[i][0] = rand.nextDouble();
            points[i][1] = rand.nextDouble();
        }

        lines = new ArrayList<>();

        List<Double[]> toProcess = new ArrayList<>();
        Collections.addAll(toProcess, points);

        for (Double[] p : points) {
            List<Point> domain = new ArrayList<>();
            domain.add(new Point(p[0], p[1], "red"));
            domain.add(new Point(p[0], p[1], "blue"));
            domain.add(new Point(p[0], p[1], "green"));

            this.variables.add(new Variable<>(domain));
        }

        while (true) {

            List<Double[]> candidates = new ArrayList<>();
            Collections.addAll(candidates, points);

            List<Variable<Point>> variables = new ArrayList<>(this.variables);

            int varIdx1 = rand.nextInt(toProcess.size());

            Double[] x1 = toProcess.remove(varIdx1);
            candidates.remove(x1);
            Variable<Point> var1 = variables.remove(varIdx1);

            if (toProcess.size() == 0) {
                break;
            }

            while (true) {
                if (candidates.size() == 0) {
                    break;
                }

                Double[] x2 = findClosest(x1, candidates);
                Variable<Point> var2 = variables.get(candidates.indexOf(x2));

                Double[][] newLine = new Double[2][2];
                newLine[0] = x1;
                newLine[1] = x2;

                if (!intersect(newLine, lines)) {
                    lines.add(newLine);
                    relations.add(new ColorRelation(var1, var2));
                }

                candidates.remove(x2);
                variables.remove(var2);
            }
        }
    }

    private boolean intersect(Double[][] l, List<Double[][]> lines) {
        for (Double[][] l2 : lines) {
            if (intersect(l, l2)) {
                return true;
            }
        }
        return false;
    }

    private boolean intersect(Double[][] l1, Double[][] l2) {
        if (l1[0][0].equals(l2[0][0]) && l1[0][1].equals(l2[0][1]) &&
                l1[1][0].equals(l2[1][0]) && l1[1][1].equals(l2[1][1])) {
            return true;
        }
        if (l1[1][0].equals(l2[0][0]) && l1[1][1].equals(l2[0][1]) &&
                l1[0][0].equals(l2[1][0]) && l1[0][1].equals(l2[1][1])) {
            return true;
        }
        Double[] p1 = l1[0];
        Double[] p2 = l1[1];
        Double[] p3 = l2[0];
        Double[] p4 = l2[1];

        double[] v = new double[]{p2[0] - p1[0], p2[1] - p1[1]};
        double[] v1 = new double[]{p4[0] - p1[0], p4[1] - p1[1]};
        double[] v2 = new double[]{p3[0] - p1[0], p3[1] - p1[1]};

        double s1 = cross(v, v2);
        double s2 = cross(v, v1);
        s1 /= Math.abs(s1);
        s2 /= Math.abs(s2);

        if (s1 != -s2) {
            return false;
        }

        double[] u = new double[]{p4[0] - p3[0], p4[1] - p3[1]};
        double[] u1 = new double[]{p1[0] - p3[0], p1[1] - p3[1]};
        double[] u2 = new double[]{p2[0] - p3[0], p2[1] - p3[1]};

        s1 = cross(u, u2);
        s2 = cross(u, u1);
        s1 /= Math.abs(s1);
        s2 /= Math.abs(s2);

        if (s1 != -s2) {
            return false;
        }

        return true;
    }

    private Double[] findClosest(Double[] p, List<Double[]> points) {
        Double[] min = points.get(0);
        double minDist = dist(p, min);

        for (Double[] point : points) {
            double d = dist(p, point);
            if (d < minDist && (!point[0].equals(p[0]) || !point[1].equals(p[1]))) {
                minDist = d;
                min = point;
            }
        }

        return min;
    }

    private double dist(Double[] p1, Double[] p2) {
        return Math.sqrt(Math.pow(p2[0] - p1[0], 2) +  Math.pow(p2[1] - p1[1], 2));
    }

    private double cross(double[] a, double[] b) {
        return a[0]*b[1] - a[1]*b[0];
    }

    public void plot() {
        Visualizer v = new Visualizer(lines);
    }

    @Override
    public List<Variable> getVariables() {
        return null;
    }

    @Override
    public List<Relation> getRelations() {
        return null;
    }
}

class Point {
    private String color = "unassigned";
    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y, String color) {
        this(x, y);
        setColor(color);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }
}

class ColorRelation extends Relation<Point> {

    public ColorRelation(Variable<Point> p1, Variable<Point> p2) {
        super();
        variables = new ArrayList<>();
        variables.add(p1);
        variables.add(p2);
    }

    @Override
    public boolean satisfies(Map<Variable<Point>, Point> assignments) {
        Point p1 = assignments.get(variables.get(0));
        Point p2 = assignments.get(variables.get(1));
        return !p1.getColor().equals(p2.getColor());
    }
}
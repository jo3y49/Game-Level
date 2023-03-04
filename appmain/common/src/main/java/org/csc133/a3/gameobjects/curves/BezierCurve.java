package org.csc133.a3.gameobjects.curves;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.util.MathUtil;
import org.csc133.a3.gameobjects.GameObject;

import java.util.ArrayList;

public class BezierCurve extends GameObject {
    private ArrayList<Point2D> controlPoints;

    public BezierCurve(ArrayList<Point2D> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public BezierCurve() {
        controlPoints = new ArrayList<>();
        controlPoints.add(new Point2D(0,0));
    }

    public Point2D getStartControlPoint() {
        return controlPoints.get(0);
    }

    public ArrayList<Point2D> getControlPoints(){
        return controlPoints;
    }

    public void setControlPoints(ArrayList<Point2D> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public Point2D evaluateCurve(double t) {
        Point2D p = new Point2D(0, 0);

        int d = controlPoints.size() - 1;

        for (int i = 0; i < controlPoints.size(); i++) {
            p.setX(p.getX() + bernsteinD(d, i, t) *
                controlPoints.get(i).getX());
            p.setY(p.getY() + bernsteinD(d, i, t) *
                controlPoints.get(i).getY());
        }
        return p;
    }

    private void drawBezierCurve(Graphics g,
                                 ArrayList<Point2D> controlPoints){
        final double smallFloatIncrement = 0.001;

        g.setColor(ColorUtil.GREEN);
        int pointSize = 10;
        for (Point2D p : controlPoints) {
            g.fillArc((int) p.getX() - pointSize / 2,
                (int) p.getY() - pointSize / 2,
                pointSize, pointSize, 0, 360);
        }

        Point2D currentPoint = controlPoints.get(0);
        Point2D nextPoint;

        double t = 0;
        while (t < 1) {
            nextPoint = evaluateCurve(t);

            g.drawLine((int) currentPoint.getX(), (int) currentPoint.getY(),
                (int) nextPoint.getX(), (int) nextPoint.getY());

            currentPoint = nextPoint;
            t += smallFloatIncrement;
        }

        nextPoint = controlPoints.get(controlPoints.size() - 1);
        g.drawLine((int) currentPoint.getX(), (int) currentPoint.getY(),
            (int) nextPoint.getX(), (int) nextPoint.getY());
    }

    private double choose(int n, int k) {
        if (k <= 0 || k >= n) {
            return 1;
        }

        return choose(n - 1, k - 1) + choose(n - 1, k);
    }

    private double bernsteinD(int d, int i, double t) {
        return choose(d, i) * MathUtil.pow(t, i) *
            MathUtil.pow(1 - t, d - i);
    }

    public void setTail(Point2D lastControlPoint) {
        controlPoints.set(controlPoints.size() - 1, lastControlPoint);
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        containerTranslate(g, parentOrigin);
        drawBezierCurve(g, controlPoints);
    }
}

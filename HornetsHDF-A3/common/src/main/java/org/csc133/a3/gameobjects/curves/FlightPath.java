package org.csc133.a3.gameobjects.curves;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.gameobjects.GameObject;

import java.util.ArrayList;

import static org.csc133.a3.GameWorld.getWorldSize;

public class FlightPath extends GameObject {
    private BezierCurve initCurve;
    private final Point2D upperLeft, upperRight,
        bottomLeft, bottomRight, riverPoint;

    public FlightPath(Point2D helipad, Point2D river){
        upperLeft = new Point2D(-getWorldSize().getWidth()/5f,
                                    river.getY());
        upperRight = new Point2D(6*getWorldSize().getWidth()/5f,
                                    river.getY());
        bottomLeft = new Point2D(upperLeft.getX(), 0);
        bottomRight = new Point2D(upperRight.getX(), 0);
        riverPoint = river;

        ArrayList<Point2D> initPoints = new ArrayList<>();
        initPoints.add(helipad);
        initPoints.add(new Point2D(getWorldSize().getWidth()/2f,
                                      getWorldSize().getHeight()/2f));
        initPoints.add(upperLeft);
        initPoints.add(river);
        initCurve = new BezierCurve(initPoints);

    }

    public BezierCurve getInitCurve(){
        return initCurve;
    }

    public Point2D getRiverControlPoint(){
        return riverPoint;
    }

    public Point2D getBottomLeft() {
        return bottomLeft;
    }

    public Point2D getBottomRight() {
        return bottomRight;
    }

    public Point2D getUpperLeft() {
        return upperLeft;
    }

    public Point2D getUpperRight() {
        return upperRight;
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin,
                             Point screenOrigin) {
        initCurve.draw(g, containerOrigin, screenOrigin);
    }
}

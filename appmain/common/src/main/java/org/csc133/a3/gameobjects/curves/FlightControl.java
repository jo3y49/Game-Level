package org.csc133.a3.gameobjects.curves;

import com.codename1.ui.geom.Point2D;
import org.csc133.a3.gameobjects.FireDispatch;
import org.csc133.a3.gameobjects.NonPlayerHelicopter;

import java.util.ArrayList;

public class FlightControl {
    private Traversal primary, correction;
    private NonPlayerHelicopter h;
    private FireDispatch fd;
    private boolean wantWater;

    public FlightControl(NonPlayerHelicopter h, FireDispatch fd) {
        this.h = h;
        this.fd = fd;
        primary = new Traversal(h);
        correction = new Traversal(h);

        primary.setControlPoints(h.getFlightPath().
            getInitCurve().getControlPoints());

        primary.activate();
        correction.deactivate();
        wantWater = true;
    }

    public void moveAlongAPath(Point2D c) {
        if (!primary.hasChanged() && !correction.isActive()) {
            primary.moveAlongAPath(c);
            if (primary.getT() >= 1) {
                if (h.checkMaxWater()) {
                    if (primary.isActive()) {
                        h.fireWater(fd.getSelectedFire());
                    } else {
                        primary.activate();
                        primary.setT(0);
                        Point2D p = fd.findFurthestFire(c);
                        ArrayList<Point2D> controlPoints = new ArrayList<>();
                        controlPoints.add(c);
                        if (p.getX() > c.getX()) {
                            controlPoints.add(
                                h.getFlightPath().getBottomRight());
                        } else {
                            controlPoints.add(
                                h.getFlightPath().getBottomLeft());
                        }
                        controlPoints.add(p);
                        primary.setControlPoints(controlPoints);
                        wantWater = false;
                    }
                }

                if (!h.checkMaxWater()) {
                    if (!primary.isActive()) {
                        h.drinkWater();
                    } else {
                        if (wantWater) {
                            primary.deactivate();
                        } else {
                            primary.setT(0);
                            ArrayList<Point2D> controlPoints =
                                new ArrayList<>();
                            controlPoints.add(c);
                            if (h.getFlightPath().getRiverControlPoint()
                                .getX() < c.getX()) {
                                controlPoints.add(
                                    h.getFlightPath().getUpperRight());
                            } else {
                                controlPoints.add(
                                    h.getFlightPath().getUpperLeft());
                            }
                            controlPoints.add(
                                h.getFlightPath().getRiverControlPoint());
                            primary.setControlPoints(controlPoints);
                            wantWater = true;
                        }
                    }
                }
            }
        }

        if (primary.hasChanged()){
            ArrayList<Point2D> cPoints = new ArrayList<>();
            cPoints.add(c);
            cPoints.add(primary.evaluateCurve(primary.getT()));
            correction.setControlPoints(cPoints);
            correction.setT(0);
            correction.activate();
            primary.setChanged(false);
        }
        if (correction.isActive()){
            correction.moveAlongAPath(c);
            if (correction.getT() >= 1){
                correction.deactivate();
            }
        }
    }

    public Traversal getPrimary() {
        return primary;
    }
    public Traversal getCorrection() {
        return correction;
    }
}

package org.csc133.a3.gameobjects.curves;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.util.MathUtil;
import org.csc133.a3.gameobjects.NonPlayerHelicopter;

public class Traversal extends BezierCurve {
    private double t;
    private boolean active, changed;
    private NonPlayerHelicopter h;

    public Traversal(NonPlayerHelicopter h) {
        this.h = h;
        active = changed = false;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void activate() {
        active = true;
    }
    public void deactivate() {
        active = false;
    }
    public boolean isActive() {
        return active;
    }
    public boolean hasChanged(){
        return changed;
    }
    public void setChanged(boolean changed){
        this.changed = changed;
    }

    @Override
    public void setTail(Point2D lastControlPoint) {
        super.setTail(lastControlPoint);
        changed = true;
    }

    public void moveAlongAPath(Point2D c) {
        Point2D p = evaluateCurve(t);

        double tx = p.getX() - c.getX();
        double ty = p.getY() - c.getY();

        h.translate(tx, ty);

        if (t <= 1) {
            t += h.getSpeed() * .01;
            int theta = 360 - (int) Math.toDegrees(MathUtil.atan2(ty, tx));
            h.rotate(h.getHeadingD() - theta);
            h.setHeadingD(theta);
        }
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        if (active) {
            super.localDraw(g, parentOrigin, screenOrigin);
        }
    }
}

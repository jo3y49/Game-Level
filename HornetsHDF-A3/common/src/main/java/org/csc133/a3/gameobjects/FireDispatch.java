package org.csc133.a3.gameobjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.gameobjects.collections.Fires;

public class FireDispatch extends GameObject{
    private Fires fires;

    public FireDispatch(){
        fires = new Fires();
    }

    public Fires getFires() {
        return fires;
    }

    public Point2D findFurthestFire(Point2D c){
        Point2D f = c;
        if (fires.size() > 0) {
            Fire fSelect = new Fire();
            for (Fire fire : fires) {
                if (distance(fire.getLocation(), c) > distance(f, c)) {
                    f = fire.getLocation();
                    fSelect = fire;
                }
            }
            fSelect.select(true);
        }
        return f;
    }
    public Fire getSelectedFire(){
        for (Fire fire : fires){
            if (fire.isSelected()){
                return fire;
            }
        }
        return new Fire();
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin){}

    @Override
    public void updateLocalTransforms(){
        for (Fire fire : fires){
            if (fire.isExtinguished()){
                fire.select(false);
                fires.remove(fire);
            }
        }
    }
}

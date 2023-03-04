package org.csc133.a3.gameobjects.collections;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.Building;
import org.csc133.a3.gameobjects.Fire;

public class Buildings extends GameObjectCollectionIterator<Building> {

    public Buildings(){
        super();
        setColor(ColorUtil.rgb(255,0,0));
    }

    public Fires getFires(){
        Fires fires = new Fires();
        for (Building building : getGameObjects()){
            for (Fire fire : building.getFires()){
                fires.add(fire);
            }
        }
        return fires;
    }

    public double getTotalFireSize(){
        double a = 0;
        for (Building building : getGameObjects()){
            a += building.getTotalFireSize();
        }
        return a;
    }
    public int getTotalFires(){
        int t = 0;
        for (Building building : getGameObjects()){
            t += building.getTotalFires();
        }
        return t;
    }
    public double getTotalDamage(){
        double d = 0;
        for (Building building : getGameObjects()){
            d += building.getDamage();
        }
        return d;
    }
    public double getTotalValue(){
        double v = 0;
        for (Building building : getGameObjects()){
            v += building.getValue();
        }
        return v;
    }
    public double getTotalLoss(){
        double l = 0;
        for (Building building : getGameObjects()){
            l += building.getLoss();
        }
        return l;
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        for (Building building : this){
            building.draw(g, parentOrigin, screenOrigin);
        }
    }
}

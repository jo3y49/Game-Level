package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Point2D;

public class PlayerHelicopter extends Helicopter {

    public PlayerHelicopter(Point2D helipad, int fuel) {
        super(fuel);

        setColor(ColorUtil.YELLOW);
        setHeliColor(getColor());
        setRotationalSpeed(0);

        for (GameObject go : getHeloParts()){
            go.setColor(getColor());
        }

        translate(helipad.getX(), helipad.getY());
    }
}
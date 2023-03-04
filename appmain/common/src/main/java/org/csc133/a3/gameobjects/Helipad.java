package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.parts.ArcFixed;
import org.csc133.a3.gameobjects.parts.RectangleFixed;

import java.util.ArrayList;

import static org.csc133.a3.GameWorld.getWorldSize;

public class Helipad extends Fixed{
    private final int radius;
    private final static int outerSize = getWorldSize().getWidth() -
        (getWorldSize().getWidth() / 2 - getWorldSize().getWidth() / 25)*2;
    private final static int circleSize = 4*outerSize/5;
    private final ArrayList<GameObject> padParts;

    private static class Outer extends RectangleFixed {
        public Outer(){
            super(ColorUtil.LTGRAY, outerSize, outerSize,
                0,0,1,1,0);
        }
    }
    private static class Inner extends ArcFixed {
        public Inner(){
            super(ColorUtil.LTGRAY, circleSize, circleSize,
                0,0,
                1,1,0,0,360);
        }
    }

    public Helipad() {
        super(getWorldSize().getWidth() - (getWorldSize().getWidth() /
              2 - getWorldSize().getWidth() / 25)*2);

        setColor(ColorUtil.GRAY);
        radius = circleSize/2;
        padParts = new ArrayList<>();
        padParts.add(new Outer()); padParts.add(new Inner());
        translate(getWorldSize().getWidth()/2f,
            getWorldSize().getHeight()/5f);
    }

    public int getRadius(){
        return radius;
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        for(GameObject go : padParts){
            go.draw(g, parentOrigin, screenOrigin);
        }
    }
}

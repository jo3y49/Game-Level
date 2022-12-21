package org.csc133.a3.gameobjects.parts;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.Movable;

public class RectangleMovable extends Movable {
    public RectangleMovable(int color, int w, int h, float tx, float ty,
                            float sx, float sy, float degreesRotation){
        setColor(color);
        setDimension(new Dimension(w,h));

        translate(tx,ty);
        scale(sx,sy);
        rotate(degreesRotation);
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin){
        g.setColor(getColor());
        containerTranslate(g,parentOrigin);
        cn1ForwardPrimitiveTranslate(g,getDimension());
        g.drawRect(0,0,getWidth(),getHeight());
    }
}

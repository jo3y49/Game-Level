package org.csc133.a3.gameobjects.parts;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.Fixed;

public class ArcFixed extends Fixed {
    private final int startAngle;
    private final int arcAngle;

    public ArcFixed(int color, int w, int h, float tx, float ty,
                    float sx, float sy, float degreesRotation,
                    int startAngle, int arcAngle) {
        super(new Dimension(w,h));

        setColor(color);
        this.startAngle = startAngle;
        this.arcAngle = arcAngle;

        translate(tx,ty);
        scale(sx,sy);
        rotate(degreesRotation);
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        g.setColor(getColor());
        containerTranslate(g,parentOrigin);
        cn1ForwardPrimitiveTranslate(g,getDimension());
        g.drawArc(0,0,getWidth(),getHeight(),startAngle,arcAngle);
    }
}

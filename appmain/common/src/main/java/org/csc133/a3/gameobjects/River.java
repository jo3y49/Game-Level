package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.parts.RectangleFixed;

import static org.csc133.a3.GameWorld.getWorldSize;

public class River extends Fixed {
    private static final int height = getWorldSize().getHeight() / 12;
    private final Shape shape;

    private static class Shape extends RectangleFixed {
        public Shape(){
            super(ColorUtil.BLUE, getWorldSize().getWidth(), height,
                0, 0,1,1,0);
        }
    }

    public River(){
        super(new Dimension(getWorldSize().getWidth(),
                        getWorldSize().getHeight() / 12));
        shape = new Shape();
        translate(getWorldSize().getWidth() / 2f,
                 (2f*getWorldSize().getHeight() / 3f)+height);
    }
    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        shape.draw(g,parentOrigin,screenOrigin);
    }
}

package org.csc133.a3.gameobjects.collections;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.Fire;

public class Fires extends GameObjectCollectionIterator<Fire> {

    public Fires(){
        super();
        setColor(ColorUtil.MAGENTA);
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin){
        for (Fire fire : this){
            fire.draw(g, parentOrigin, screenOrigin);
        }
    }
}

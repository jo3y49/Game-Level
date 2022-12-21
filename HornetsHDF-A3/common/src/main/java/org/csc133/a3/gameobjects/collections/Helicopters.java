package org.csc133.a3.gameobjects.collections;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.Helicopter;

public class Helicopters extends GameObjectCollectionIterator<Helicopter> {

    public Helicopters(){
        super();
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        for(Helicopter helicopter : this){
            helicopter.draw(g, parentOrigin, screenOrigin);
        }
    }
}

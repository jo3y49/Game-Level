package org.csc133.a3.gameobjects.collections;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.gameobjects.*;
import org.csc133.a3.gameobjects.curves.FlightPath;

public class GameObjectCollection
    extends GameObjectCollectionIterator<GameObject> {

    public GameObjectCollection(){
        super();
    }

    public Fires getFires(){
        Fires fires = new Fires();
        for (GameObject go : this){
            if (go instanceof Fire){
                fires.add((Fire) go);
            }
        }
        return fires;
    }
    public Buildings getBuildings(){
        Buildings buildings = new Buildings();
        for (GameObject go : this){
            if (go instanceof Building){
                buildings.add((Building) go);
            }
        }
        return buildings;
    }
    public River getRiver(){
        for(GameObject go : this){
            if (go instanceof River){
                return (River) go;
            }
        }
        return new River();
    }
    public Helipad getHelipad(){
        for(GameObject go : this){
            if (go instanceof Helipad){
                return (Helipad) go;
            }
        }
        return new Helipad();
    }
    public Helicopters getHelicopters(){
        Helicopters helicopters = new Helicopters();
        for (GameObject go : this){
            if (go instanceof Helicopter){
                helicopters.add((Helicopter) go);
            }
        }
        return helicopters;
    }
    public NonPlayerHelicopter getNonPlayerHelicopter(){
        for (GameObject go : this){
            if (go instanceof NonPlayerHelicopter){
                return (NonPlayerHelicopter) go;
            }
        }
        return new NonPlayerHelicopter(0, new FlightPath(
               new Point2D(0,0), new Point2D(0,0)),
            new FireDispatch());
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        for (GameObject go : this){
            go.draw(g, parentOrigin, screenOrigin);
        }
    }
}

package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.gameobjects.curves.FlightControl;
import org.csc133.a3.gameobjects.curves.FlightPath;

public class NonPlayerHelicopter extends Helicopter{
    private FlightControl fc;
    private FlightPath fp;
    private Avoid avoid;

    private class Avoid{

        public boolean checkForHelicopter(Point2D player) {
            if (distance(getLocation(), player) <= getHeight()*1.5){
                if (distance(getLocation(), player) <= getHeight()){
                    return true;
                }
                setSpeed(0);
            } else {
                setSpeed(2);
            }
            return false;
        }
    }

    public NonPlayerHelicopter(int fuel, FlightPath flightPath,
                               FireDispatch fd) {
        super(fuel);

        setColor(ColorUtil.GREEN);
        setHeliColor(getColor());
        setSpeed(2);
        rotate(-90);
        setRotationalSpeed(getMAX_ROTATIONAL_SPEED());

        for (GameObject go : getHeloParts()){
            go.setColor(getColor());
        }

        fp = flightPath;
        fc = new FlightControl(this, fd);
        avoid = new Avoid();


        translate(
            fc.getPrimary().getStartControlPoint().getX(),
            fc.getPrimary().getStartControlPoint().getY());

    }
    public void drinkWater(){
        setWater(getWater() + 100);
    }
    public void fireWater(Fire fire){
        fire.shrink(getWater());
        setWater(0);
    }

    public boolean checkMaxWater(){
        return getWater() == getMAX_WATER();
    }

    public boolean checkForHelicopter(Point2D player){
        return avoid.checkForHelicopter(player);
    }

    public FlightControl getFlightControl(){
        return fc;
    }

    public FlightPath getFlightPath(){
        return fp;
    }

    @Override
    public void updateLocalTransforms(){
        fc.moveAlongAPath(new Point2D(getMyTranslation().getTranslateX(),
            getMyTranslation().getTranslateY()));

        setFuel(getFuel() - 5 + (getSpeed() * getSpeed()));

        getHeloBlade().updateLocalTransforms(getRotationalSpeed());
        getHeloTailBlade().updateLocalTransforms(getRotationalSpeed());
        getFuelString().updateLocalTransforms(getFuel());
        getWaterString().updateLocalTransforms(getWater());
    }
}

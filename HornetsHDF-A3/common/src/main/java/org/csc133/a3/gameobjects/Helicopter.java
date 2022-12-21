package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.gameobjects.collections.Fires;
import org.csc133.a3.gameobjects.parts.*;
import org.csc133.a3.interfaces.Steerable;

import java.util.ArrayList;

public abstract class Helicopter extends Movable implements Steerable {
    final static int BUBBLE_RADIUS = 60;
    final static int ENGINE_BLOCK_WIDTH = (int)(BUBBLE_RADIUS*1.8);
    final static int ENGINE_BLOCK_HEIGHT = ENGINE_BLOCK_WIDTH/3;
    final static int BLADE_WIDTH = 15;
    final static int BLADE_LENGTH = BUBBLE_RADIUS * 5;
    final static int TAIL_LENGTH = BLADE_LENGTH/3;
    final static int TAIL_WIDTH = 2*BLADE_WIDTH/3;
    private static int heliColor;
    private final int MAX_WATER = 1000;
    private final int MAX_SPEED = 10;
    private final double MAX_ROTATIONAL_SPEED = -20d;
    private int fuel, water;
    private double radius, rotationalSpeed;
    private ArrayList<GameObject> heloParts;
    private HeloBlade heloBlade;
    private HeloTailBlade heloTailBlade;
    private FuelString fuelString;
    private WaterString waterString;
    private HeloState state;

    //--------------------------------------------------------------------------
    // Parts

    private static class HeloFootRight extends RectangleMovable{

        public HeloFootRight(){
            super(heliColor, 3*TAIL_WIDTH/2,
                 BUBBLE_RADIUS*3,BUBBLE_RADIUS*1.3f,
                ENGINE_BLOCK_HEIGHT, 1,1,0);
        }
    }
    private static class HeloFootLeft extends RectangleMovable{

        public HeloFootLeft(){
            super(heliColor, 3*TAIL_WIDTH/2,
                BUBBLE_RADIUS*3,-BUBBLE_RADIUS*1.3f,
                 ENGINE_BLOCK_HEIGHT, 1,1,0);
        }
    }
    private static class HeloFeetConnectors extends Movable{
        private final static int CONNECTOR_WIDTH = TAIL_WIDTH*2;
        private final static int CONNECTOR_HEIGHT = BLADE_WIDTH/2;
        private final static int footColor = ColorUtil.LTGRAY;
        private ArrayList<Movable> feet;

        public HeloFeetConnectors(){
            feet = new ArrayList<>();
            feet.add(new FrontRight());
            feet.add(new FrontLeft());
            feet.add(new BackRight());
            feet.add(new BackLeft());
        }

        private static class FrontRight extends FillRectangleMovable{
            public FrontRight(){
                super(footColor, CONNECTOR_WIDTH, CONNECTOR_HEIGHT,
                    BUBBLE_RADIUS, 3*BUBBLE_RADIUS/2f,
                    1,1,0);
            }
        }
        private static class FrontLeft extends FillRectangleMovable{
            public FrontLeft(){
                super(footColor, CONNECTOR_WIDTH, CONNECTOR_HEIGHT,
                    -BUBBLE_RADIUS, 3*BUBBLE_RADIUS/2f,
                    1,1,0);
            }
        }
        private static class BackRight extends FillRectangleMovable{
            public BackRight(){
                super(footColor, CONNECTOR_WIDTH, CONNECTOR_HEIGHT,
                    BUBBLE_RADIUS, 0,
                    1,1,0);
            }
        }
        private static class BackLeft extends FillRectangleMovable{
            public BackLeft(){
                super(footColor, CONNECTOR_WIDTH, CONNECTOR_HEIGHT,
                    -BUBBLE_RADIUS, 0,
                    1,1,0);
            }
        }

        @Override
        protected void localDraw(Graphics g, Point parentOrigin,
                                 Point screenOrigin) {
            for (Movable m : feet){
                m.draw(g, parentOrigin, screenOrigin);
            }
        }
    }

    private static class HeloBubble extends ArcMovable {

        public HeloBubble(){
            super(heliColor,2*BUBBLE_RADIUS,
                2*BUBBLE_RADIUS, 0, BUBBLE_RADIUS, 1, 1,
                0, 135, 270);
        }
    }
    private static class HeloEngineBlock extends RectangleMovable {

        public HeloEngineBlock(){
            super(heliColor, ENGINE_BLOCK_WIDTH,
                ENGINE_BLOCK_HEIGHT, 0, 0,1,1,0);
        }
    }

    static class HeloBlade extends RectangleMovable {
        public HeloBlade(){
            super(ColorUtil.LTGRAY, BLADE_LENGTH, BLADE_WIDTH,
                0, 0, 1,1,45);
        }
        public void updateLocalTransforms(double rotationSpeed){
            rotate(rotationSpeed);
        }
    }

    private static class HeloBladeShaft extends FillArcMovable {

        public HeloBladeShaft() {
            super(ColorUtil.LTGRAY, 2*BLADE_WIDTH/3, 2*BLADE_WIDTH/3,
                0, 0, 1, 1,
                0, 0, 360);
        }
    }
    private static class HeloTail extends FillRectangleMovable{

        public HeloTail(){
            super(ColorUtil.LTGRAY,TAIL_WIDTH,TAIL_LENGTH,0,
                    -ENGINE_BLOCK_HEIGHT/2f - TAIL_LENGTH/2f,
                    1,1,0);
        }
    }

    private static class HeloTailBlock extends FillRectangleMovable{

        public HeloTailBlock(){
            super(ColorUtil.LTGRAY, TAIL_WIDTH*3,ENGINE_BLOCK_HEIGHT/2,
                    0,-ENGINE_BLOCK_HEIGHT/2f - TAIL_LENGTH,
                    1,1,0);
        }
    }
    private static class HeloTailConnector extends FillRectangleMovable{

        public HeloTailConnector(){
            super(ColorUtil.GRAY, TAIL_WIDTH, ENGINE_BLOCK_HEIGHT/5,
                    2*TAIL_WIDTH,
                    -ENGINE_BLOCK_HEIGHT/2f - TAIL_LENGTH,
                    1,1,0);
        }
    }
    static class HeloTailBlade extends FillRectangleMovable{
        private boolean grow;
        private final int initHeight;

        public HeloTailBlade() {
            super(ColorUtil.LTGRAY, TAIL_WIDTH/2, ENGINE_BLOCK_HEIGHT*2,
                2*TAIL_WIDTH, -ENGINE_BLOCK_HEIGHT/2f - TAIL_LENGTH,
                1,1,0);
            grow = false;
            initHeight = getHeight();
        }

        public void updateLocalTransforms(double rotationSpeed){
            if (initHeight < getHeight()){
                grow = false;
            }
            if (grow){
                setDimension(new Dimension(getWidth(),
                    (int)(getHeight() + (-2 * rotationSpeed))));
            } else {
                if ((int)(getHeight() - (-.001 * rotationSpeed)) > 0){
                    setDimension(new Dimension(getWidth(),
                        (int) (getHeight() - (-2 * rotationSpeed))));
                } else {
                    grow = true;
                }
            }
        }
    }

    static class FuelString extends StringPart{

        public FuelString(int fuel){
            super(heliColor, 0,0, ENGINE_BLOCK_WIDTH,
                -BUBBLE_RADIUS, 1,1,0,"F: "+fuel);
        }
        protected void updateLocalTransforms(int fuel){
            setText("F: "+fuel);
        }
    }
    static class WaterString extends StringPart{

        public WaterString(int water){
            super(heliColor, 0,0, ENGINE_BLOCK_WIDTH,
                -BUBBLE_RADIUS - 50, 1,1,
                0,"W: "+water);
        }
        protected void updateLocalTransforms(int water){
            setText("W: "+water);
        }
    }

    //------------------------------------------------------------------------
    // States

    abstract static class HeloState{

        abstract void startOrStopEngine();

        void accelerate(){}
        void brake(){}
        void fireWater(Fires fires){}
        void drinkWater(double riverHeight, Point2D riverLocation){}

        void spinBlades(){}

        void drainFuel(){}

        public boolean checkLand(Point2D helipad, int padRadius){
            return false;
        }
        void move(){}
        void steerLeft(){}
        void steerRight(){}
        public abstract void updateLocalTransforms();
    }

    private class Off extends HeloState{
        @Override
        public void startOrStopEngine() {
            state = new Starting();
        }
        @Override
        public boolean checkLand(Point2D helipad, int padRadius){
            return getSpeed() == 0 && distance(helipad,
                   getLocation()) <= padRadius;
        }

        @Override
        public void updateLocalTransforms() {}
    }
    private class Starting extends HeloState{
        @Override
        public void startOrStopEngine() {
            state = new Stopping();
        }

        @Override
        void spinBlades(){
            if (rotationalSpeed > MAX_ROTATIONAL_SPEED){
                rotationalSpeed -= .25;
            } else {
                state = new Ready();
            }
        }
        @Override
        void drainFuel(){
            if (fuel > 0){
                fuel -= 5 + (getSpeed() * getSpeed());
            } else if (fuel < 0){
                fuel = 0;
            }
        }

        @Override
        public void updateLocalTransforms() {
            state.spinBlades();
            heloBlade.updateLocalTransforms(rotationalSpeed);
            heloTailBlade.updateLocalTransforms(rotationalSpeed);
            state.drainFuel();
            fuelString.updateLocalTransforms(fuel);
        }
    }
    private class Stopping extends HeloState{
        @Override
        public void startOrStopEngine() {
            state = new Starting();
        }

        @Override
        void spinBlades(){
            if (rotationalSpeed < 0){
                rotationalSpeed += .25;
            } else {
                state = new Off();
            }
        }

        @Override
        public void updateLocalTransforms() {
            state.spinBlades();
            heloBlade.updateLocalTransforms(rotationalSpeed);
            heloTailBlade.updateLocalTransforms(rotationalSpeed);
        }
    }
    private class Ready extends HeloState{
        @Override
        void startOrStopEngine(){
            if (getSpeed() <= 0){
                state = new Stopping();
            }
        }
        @Override
        void accelerate() {
            if (getSpeed() < MAX_SPEED) {
                setSpeed(getSpeed() + 1);
            }
        }
        @Override
        void brake(){
            if (getSpeed() > 0){
                setSpeed(getSpeed() - 1);
            }
        }
        private boolean canFire(Point2D fireLocation, double fireSize){
            return water > 0 && getSpeed() <= 2 && (distance(
                fireLocation, getLocation()) <= fireSize/2.0 || (fireSize <
                getWidth() && distance(fireLocation, getLocation())
                <= radius + fireSize));
        }
        private boolean canDrain(double riverHeight, Point2D riverLocation){
            return  riverLocation.getY() - riverHeight/2 < getY() &&
                riverLocation.getY() + riverHeight/2 > getY() &&
                getSpeed() <= 2 && water < MAX_WATER;
        }
        @Override
        void fireWater(Fires fires){
            for (Fire fire : fires) {
                if (canFire(fire.getLocation(), fire.getHeight())){
                    fire.shrink(water);
                    break;
                }
            }
            water = 0;
        }
        @Override
        void drinkWater(double riverHeight, Point2D riverLocation){
            if (canDrain(riverHeight, riverLocation)){
                water += 100;
            }
        }
        @Override
        void move(){
            Helicopter.this.move();
            drainFuel();
        }
        @Override
        void drainFuel(){
            if (fuel > 0){
                fuel -= 5 + (getSpeed() * getSpeed());
            } else if (fuel < 0){
                fuel = 0;
            }
        }

        @Override
        void steerLeft() {
            if (getHeadingD() == 0){
                setHeadingD(330);
            } else {
                setHeadingD(getHeadingD()-30);
            }

            setHeadingR(getHeadingR()-Math.PI / 6.0);

            rotate(30);
        }
        @Override
        void steerRight() {
            if (getHeadingD() == 330){
                setHeadingD(0);
            } else {
                setHeadingD(getHeadingD()+30);
            }

            setHeadingR(getHeadingR()+Math.PI / 6.0);

            rotate(-30);
        }

        @Override
        public void updateLocalTransforms() {
            heloBlade.updateLocalTransforms(rotationalSpeed);
            heloTailBlade.updateLocalTransforms(rotationalSpeed);
            state.move();
            fuelString.updateLocalTransforms(fuel);
            waterString.updateLocalTransforms(water);
        }
    }

    //-----------------------------------------------------------------------
    // Helicopter Object

    public Helicopter(int fuel){
        this.fuel = fuel;
        water = 0;
        setSpeed(0);
        setHeadingR(0.0);
        setHeadingD(0);

        state = new Off();

        heloParts = new ArrayList<>();
        heloParts.add(new HeloFootRight());
        heloParts.add(new HeloFootLeft());
        heloParts.add(new HeloFeetConnectors());
        heloParts.add(new HeloBubble());
        heloParts.add(new HeloEngineBlock());
        heloBlade = new HeloBlade();
        heloParts.add(heloBlade);
        heloParts.add(new HeloBladeShaft());
        heloParts.add(new HeloTail());
        heloParts.add(new HeloTailBlock());
        heloParts.add(new HeloTailConnector());
        heloTailBlade = new HeloTailBlade();
        heloParts.add(heloTailBlade);
        fuelString = new FuelString(fuel);
        heloParts.add(fuelString);
        waterString = new WaterString(water);
        heloParts.add(waterString);

        setDimension(2*BLADE_LENGTH/3);
        radius = getHeight()/2.0;
    }

    public int getFuel(){
        return fuel;
    }
    public int getWater(){
        return water;
    }
    public int getMAX_WATER(){
        return MAX_WATER;
    }
    public ArrayList<GameObject> getHeloParts(){
        return heloParts;
    }
    public HeloState getState(){
        return state;
    }
    public HeloBlade getHeloBlade(){
        return heloBlade;
    }
    public HeloTailBlade getHeloTailBlade(){
        return heloTailBlade;
    }
    public FuelString getFuelString(){
        return fuelString;
    }
    public WaterString getWaterString(){
        return waterString;
    }
    public double getRotationalSpeed() {
        return rotationalSpeed;
    }
    public double getMAX_ROTATIONAL_SPEED() {
        return MAX_ROTATIONAL_SPEED;
    }

    void setWater(int water){
        this.water = water;
    }
    void setFuel(int fuel){
        this.fuel = fuel;
    }
    void setHeliColor(int color){
        heliColor = color;
    }
    void setRotationalSpeed(double rotationalSpeed){
        this.rotationalSpeed = rotationalSpeed;
    }

    public boolean checkLand(Point2D helipad, int padRadius){
        return state.checkLand(helipad, padRadius);
    }

    public void fireWater(Fires fires){
        state.fireWater(fires);
    }
    public void drinkWater(double riverHeight, Point2D riverLocation){
        state.drinkWater(riverHeight, riverLocation);
    }
    public void accelerate() {
        state.accelerate();
    }
    public void brake(){
        state.brake();
    }
    public void startOrStopEngine(){
        state.startOrStopEngine();
    }

    @Override
    public void steerLeft() {
        state.steerLeft();
    }
    @Override
    public void steerRight() {
        state.steerRight();
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        for(GameObject go : heloParts){
            go.draw(g, parentOrigin, screenOrigin);
        }
    }
    public void updateLocalTransforms(){
        state.updateLocalTransforms();
    }
}

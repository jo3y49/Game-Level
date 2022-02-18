//Joseph May

package org.csc133.a1;

import static com.codename1.ui.CN.*;

import com.codename1.charts.util.ColorUtil;
import com.codename1.maps.BoundingBox;
import com.codename1.maps.Coord;
import com.codename1.system.Lifecycle;
import com.codename1.ui.*;
import com.codename1.ui.geom.Point;
import com.codename1.ui.layouts.*;
import com.codename1.io.*;
import com.codename1.ui.plaf.*;
import com.codename1.ui.util.Resources;
import com.codename1.ui.util.UITimer;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename
 * One</a> for the purpose of building native mobile applications using Java.
 */

public class AppMain extends Lifecycle {
    @Override
    public void runApp() {
        new Game();
    }
}
class Game extends Form implements Runnable{
    GameWorld gw;

    final static int DISP_H = Display.getInstance().getDisplayHeight();
    final static int DISP_W = Display.getInstance().getDisplayWidth();

    public static int getSmallDim() { return Math.min(DISP_H,DISP_W); }
    public static int getLargeDim() { return Math.max(DISP_H,DISP_W); }

    public Game(){
        gw = new GameWorld();

        addKeyListener(-93,(evt) -> gw.turnLeft()); //left
        addKeyListener(-94,(evt) -> gw.turnRight()); //right
        addKeyListener(-91,(evt) -> gw.speedUp()); //up
        addKeyListener(-92,(evt) -> gw.speedDown()); //down
        addKeyListener('f',(evt) -> gw.checkFire());
        addKeyListener('d',(evt) -> gw.checkWater());
        addKeyListener('Q',(evt) -> gw.quit());

        UITimer timer = new UITimer(this);
        timer.schedule(100,true,this);

        this.getAllStyles().setBgColor(ColorUtil.BLACK);
        this.show();
    }
    @Override
    public void run() {
        gw.tick();
        repaint();
    }
    public void paint (Graphics g){
        super.paint(g);
        gw.draw(g);
    }
}
class GameWorld {
    final int HELICOPTER_FUEL = 25000;

    private River river;
    private Helipad helipad;
    private ArrayList<Fire> fires;
    private ArrayList<Fire> deadFires;
    private Helicopter helicopter;

    public GameWorld() { init(); }

    private void init() {
        river = new River();
        helipad = new Helipad();
        fires = new ArrayList<>();
        deadFires = new ArrayList<>();

        int fireVariationX = Game.DISP_W / 15;
        int fireVariationY = Game.DISP_H / 15;

        Fire fire1 = new Fire(new Point(new Random().nextInt(fireVariationX)
                                          + fireVariationX,
                  new Random().nextInt(fireVariationY) + fireVariationY));

        Fire fire2 = new Fire(new Point(new Random().nextInt(fireVariationX) +
                     Game.DISP_W / 2 + Game.DISP_W / 10,
                  new Random().nextInt(fireVariationY) + fireVariationY));

        Fire fire3 = new Fire(new Point(new Random().nextInt(fireVariationX) +
                     Game.DISP_W / 3 + Game.DISP_W / 30,
                  new Random().nextInt(fireVariationY)+(Game.DISP_H * 2) / 5));

        fires.add(fire1); fires.add(fire2); fires.add(fire3);

        helicopter = new Helicopter(helipad.getCenter(), HELICOPTER_FUEL);
    }
    public void quit() { Display.getInstance().exitApplication(); }

    public void win(){
        if(Dialog.show("VICTORY","You Won! your final score is " +
                helicopter.getFuel(),"Quit","Play Again")){
            quit();
        } else {
            init();
        }
    }
    public void lose(){
        if (Dialog.show("FAILURE","You ran out of fuel","Quit","Play Again")){
            quit();
        } else {
            init();
        }
    }
    void draw(Graphics g) {
        g.setFont(Font.createSystemFont(FACE_MONOSPACE,
                  STYLE_BOLD, SIZE_LARGE));
        g.fillRect(0,0,Game.DISP_W,Game.DISP_H);

        river.draw(g);
        helipad.draw(g);
        for (Fire fire : fires) {
            fire.draw(g);
        }
        helicopter.draw(g);
    }
    public void tick() {
        helicopter.moveX();
        helicopter.moveY();

        for (Fire fire : fires) {
            if (fire.checkPutOut()) {
                deadFires.add(fire);
            }
            fire.grow();
        }
        fires.removeAll(deadFires);

        helicopter.drainFuel(5);

        if (helipad.checkLand(helicopter.getCenter()) &&
                helicopter.getSpeed() <= 0 && fires.size() <= 0){
            win();
        }
        if (helicopter.getFuel() <= 0) {
            lose();
        }
    }
    public void speedUp() { helicopter.speedUp(); }
    public void speedDown() { helicopter.speedDown(); }
    public void turnLeft() { helicopter.turnLeft(); }
    public void turnRight() { helicopter.turnRight(); }

    public void checkFire() {
        for (Fire fire : fires) {
            if (helicopter.getWater() > 0 && helicopter.getSpeed() <= 2 &&
                    helicopter.fightFire(fire.getCenter(),fire.getSize())) {
                fire.shrink(helicopter.getWater());
                break;
            }
        }
        helicopter.shootWater();
    }
    public void checkWater(){
        if (river.checkDrain(helicopter.getCenter().getY()) && helicopter.getSpeed() <= 2){
            helicopter.drainWater();
        }
    }
    public static double distance(Point a, Point b){
        return Math.sqrt((b.getX()-a.getX())*(b.getX()-a.getX())+
                (b.getY()-a.getY())*(b.getY()-a.getY()));
    }
}
class River{
    private Point location;
    private int height, width;

    public River(){
        height = Game.DISP_H/12;
        width = Game.DISP_W-1;
        location = new Point(0,Game.DISP_H/3-height/2);
    }
    public boolean checkDrain(int heli){
        return location.getY() < heli && location.getY()+height > heli;
    }
    void draw(Graphics g){
        g.setColor(ColorUtil.BLUE);
        g.drawRect(location.getX(),location.getY(),width,height);
    }
}
class Helipad{
    private Point locationS, locationC, center;
    private int square, circle;
    private double radius;

    public Helipad(){
        locationS = new Point(Game.DISP_W/2-
               Game.DISP_W/20,Game.DISP_H- Game.DISP_H/5);
        square = Game.DISP_W-locationS.getX()*2;
        circle = square-square/5;
        radius = (double) circle/2;
        locationC = new Point(locationS.getX()+square/10,
                              locationS.getY()+square/10);
        center = new Point(locationC.getX()+circle/2,
                           locationC.getY()+circle/2);
    }
    public Point getCenter(){ return center; }

    public boolean checkLand(Point heli){
        return GameWorld.distance(heli,center) < radius;
    }
    void draw(Graphics g) {
        g.setColor(ColorUtil.GRAY);
        g.drawRect(locationS.getX(), locationS.getY(), square, square, 5);
        g.drawArc(locationC.getX(), locationC.getY(), circle, circle, 0, 360);
    }
}
class Fire{
    private Point location, center;
    private int size, maxSize, growth;
    private double radius;

    public Fire(Point location){
        this.location = location;
        maxSize = (Game.DISP_H + Game.DISP_W)/9;
        size = new Random().nextInt(maxSize/5)+(maxSize*4)/5;
        radius = (double)size/2;
        center = new Point(this.location.getX()+size/2,
                           this.location.getY()+size/2);
        growth = maxSize/70;
    }
    public Point getCenter() { return center; }
    public int getSize() { return size; }

    public boolean checkPutOut(){ return size <= 0; }

    public void grow() {
        if (size > 0) {
            if (new Random().nextInt(40) == 0) {
                size += growth;
                location.setX(location.getX()-growth/2);//keeps it centered
                location.setY(location.getY()-growth/2);
            }
        }
    }
    public void shrink(int water){
        int shrinkFactor = (water*growth)/50;
        if (size > shrinkFactor) {
            size -= shrinkFactor;
            location.setX(location.getX()+shrinkFactor/2);//keeps it centered
            location.setY(location.getY()+shrinkFactor/2);
        } else {
            size = 0;
        }
    }
    void draw(Graphics g){
        if (size > 0) {
            g.setColor(ColorUtil.MAGENTA);
            g.fillArc(location.getX(), location.getY(), size, size, 0, 360);

            g.drawString(""+size,location.getX()+size,location.getY()+size);
        }
    }
}
class Helicopter{
    private Point location, center, lineTip;
    private int size, length, fuel, water, speed, maxSpeed, maxWater;
    private double heading;

    public Helicopter(Point helipad, int fuel){
        size = Game.DISP_W/40;
        length = size*2;
        heading = 0.0;
        location = new Point(helipad.getX()-size/2,helipad.getY()-size/3);
        center = new Point(location.getX()+size/2,location.getY()+size/2);
        lineTip = new Point((int) (length * Math.sin(heading)) + center.getX(),
                  (int) (length * (-Math.cos(heading))) + center.getY());
        speed = 0;
        maxSpeed = 10;
        water = 0;
        maxWater = 1000;
        this.fuel = fuel;
    }
    public Point getCenter() { return center; }
    public int getWater() { return water; }
    public int getSpeed() { return speed; }
    public int getFuel() { return fuel; }

    public void drainFuel(int drain){
        fuel -= drain + (speed * speed);
        if (fuel < 0){
            fuel = 0;
        }
    }
    public void drainWater(){
        if (water < maxWater) {
            water += 100;
        }
    }
    public void shootWater() { water = 0; }

    public void speedUp() { if (speed < maxSpeed) speed++; }
    public void speedDown(){ if (speed > 0) speed--; }

    public void turnLeft(){
        heading -= Math.PI / 6.0;
        lineTip = new Point((int) (length * Math.sin(heading)) + center.getX(),
                  (int) (length * -Math.cos(heading)) + center.getY());
    }
    public void turnRight(){
        heading += Math.PI / 6.0;
        lineTip = new Point((int) (length * Math.sin(heading)) + center.getX(),
                  (int) (length * -Math.cos(heading)) + center.getY());
    }
    public void moveX(){
        if (location.getX() < 0) {
            blockLeft();
        } else if (location.getX() + size > Game.DISP_W) {
            blockRight();
        } else {
            changeX();
        }
    }
    public void moveY(){
        if (location.getY() < 0){
            blockUp();
        } else if (location.getY() + size > Game.DISP_H){
            blockDown();
        } else {
            changeY();
        }
    }
    private void blockLeft(){
        location.setX(size / 2);
        center.setX(location.getX() + size / 2);
        lineTip.setX((int) (length * Math.sin(heading)) + center.getX());
    }
    private void blockRight(){
        location.setX(Game.DISP_W - size);
        center.setX(location.getX() + size / 2);
        lineTip.setX((int) (length * Math.sin(heading)) + center.getX());
    }
    private void changeX(){
        int movX = (((center.getX() - lineTip.getX()) / 30) * speed);
        location.setX(location.getX() - movX);
        center.setX(center.getX() - movX);
        lineTip.setX(lineTip.getX() - movX);
    }
    private void blockUp(){
        location.setY(size / 4);
        center.setY(location.getY()+size/2);
        lineTip.setY((int) (length * (-Math.cos(heading)))+center.getY());
    }
    private void blockDown(){
        location.setY(Game.DISP_H - size);
        center.setY(location.getY()+size/2);
        lineTip.setY((int) (length * (-Math.cos(heading)))+center.getY());
    }
    private void changeY(){
        int movY = (((center.getY() - lineTip.getY())/30)*speed);
        location.setY(location.getY() - movY);
        center.setY(center.getY() - movY);
        lineTip.setY(lineTip.getY() - movY);
    }
    public boolean fightFire(Point fire, int fireSize){
        return ((int)GameWorld.distance(fire, center) < fireSize/2) ||
                (size > fireSize && (location.getX() - fireSize < fire.getX() +
                fireSize && location.getX() + size > fire.getX() - fireSize &&
                location.getY() - fireSize < fire.getY() + fireSize &&
                location.getY() + size > fire.getY() - fireSize));
    }
    void draw(Graphics g){
        g.setColor(ColorUtil.YELLOW);
        g.fillArc(location.getX(),location.getY(),size,size,0,360);
        g.drawLine(center.getX(),center.getY(),lineTip.getX(),lineTip.getY());

        g.drawString("F  : "+fuel,location.getX()+size/2,
                                      location.getY()+size*2);
        g.drawString("W : "+water,location.getX()+size/2,
                                      location.getY()+size*3);
    }
}
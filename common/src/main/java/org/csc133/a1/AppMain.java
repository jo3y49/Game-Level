//Joseph May 3557

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

    public static int getSmallDim() {return Math.min(DISP_H,DISP_W);}
    public static int getLargeDim() {return Math.max(DISP_H,DISP_W);}

    public Game(){
        gw = new GameWorld();


        addKeyListener(-93,(evt) -> gw.move('l')); //left
        addKeyListener(-94,(evt) -> gw.move('r')); //right
        addKeyListener(-91,(evt) -> gw.move('u')); //up
        addKeyListener(-92,(evt) -> gw.move('d')); //down
        //addKeyListener('f',(evt) -> );
       // addKeyListener('d',(evt) -> );*/
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
class GameWorld{
    final int NUMBER_OF_FIRES = 3;
    private boolean drink;
    private River river;
    private Helipad helipad;
    private ArrayList<Fire> fires;
    private Helicopter helicopter;

    public GameWorld(){
        init();
    }
    private void init(){
        river = new River();
        helipad = new Helipad();
        fires = new ArrayList<>();
        Fire fire1 = new Fire();
        fire1.setLocation(new Point(new Random().nextInt(150)+15,
                                   new Random().nextInt(150)+100));

        Fire fire2 = new Fire();
        fire2.setLocation(new Point(new Random().nextInt(150)+
                            Display.getInstance().getDisplayWidth()/2,
                        new Random().nextInt(150)+100));
        Fire fire3 = new Fire();
        fire3.setLocation(new Point(new Random().nextInt(150)+
                        Display.getInstance().getDisplayWidth()/2-150,
                        new Random().nextInt(150)+
                        Display.getInstance().getDisplayHeight()/3));
        fires.add(fire1); fires.add(fire2); fires.add(fire3);
        helicopter = new Helicopter();
        helicopter.initLocation(helipad.getCenter());
        helicopter.changeFuel(3000);
        drink = false;
    }
    public void quit(){
        Display.getInstance().exitApplication();
    }

    void draw(Graphics g){
        river.draw(g);
        helipad.draw(g);
        for(Fire fire : fires){
            fire.draw(g);
        }
        helicopter.draw(g);
    }

    public void tick() {
        for(Fire fire : fires){
            fire.grow();
        }
        helicopter.changeFuel(-5);
        helicopter.move();
        helicopter.drainWater(river.getLocation());
        for(int n = 0; n < NUMBER_OF_FIRES; n++){
            helicopter.fireWater(fires.get(n).getLocation());
            fires.get(n).shrink(helicopter.getLocation());
        }
        //if river.intersect(helicopter.getLocation());
            //if fires.get(n).intersect(helicopter.getLocation());
    }
    public void move(char c){
        switch (c) {
            case 'u':
                helicopter.speedUp();
            break;
            case 'd':
                helicopter.speedDown();
            break;
            case 'l':
                helicopter.changeDirection(-Math.PI/6.0);
            break;
            case 'r':
                helicopter.changeDirection(Math.PI/6.0);
            break;
        }
    }
}
class River{
    private Point location;
    private int height, width;

    public River(){
        location = new Point(1,Display.getInstance().getDisplayHeight()/4);
        height = Display.getInstance().getDisplayHeight()/16;
        width = Display.getInstance().getDisplayWidth()-8;
    }

    public Point getLocation() {return location;}

    void draw(Graphics g){
        g.setColor(ColorUtil.BLUE);
        g.drawRect(location.getX(),location.getY(),width,height);
    }
}
class Helipad{
    //separated variables for the square and circle to make it easier
    private Point locationS, locationC;
    private int square, circle;

    public Helipad(){
        square = Display.getInstance().getDisplayWidth()/5;
        locationS = new Point(Display.getInstance().getDisplayWidth()/2-
                     square/2,Display.getInstance().getDisplayHeight()-
                            Display.getInstance().getDisplayHeight()/5);
        circle = square-square/5;
        locationC = new Point(locationS.getX()+square/10,
                              locationS.getY()+square/10);

    }

    public Point getCenter(){
        return new Point(locationC.getX(),locationC.getY()/2);
    }

    void draw(Graphics g){
        g.setColor(ColorUtil.GRAY);
        g.drawRect(locationS.getX(),locationS.getY(),square,square,5);
        g.drawArc(locationC.getX(),locationC.getY(),circle,circle,0,360);
    }
}
class Fire{
    private Point location;
    private int size, growth;

    public Fire(){
        location = new Point(100,100);
        size = new Random().nextInt(100)+350;
        growth = 6;
    }
    public void grow(){
        if (new Random().nextInt(30) == 0){
            this.size += growth;
            this.location.setX(location.getX()-growth/2); //keeps it centered
            this.location.setY(location.getY()-growth/2);
        }
    }

    public void shrink(Point heli){
        /*if (location.getX() < heli.getX() && heli.getX()+size > heli.getX()
        && location.getY() < heli.getY() && heli.getY()+size > heli.getY()){
            this.size -= growth;
            this.location.setX(location.getX()+growth/2); //keeps it centered
            this.location.setY(location.getY()+growth/2);
        }*/
    }

    public void setLocation(Point location){ this.location = location; }
    public Point getLocation() {return location;}

    void draw(Graphics g){
        g.setColor(ColorUtil.MAGENTA);
        g.fillArc(location.getX(),location.getY(),size,size,0,360);
        //TODO: font too small
        g.drawString(""+size,location.getX()+size,location.getY()+size);

    }
}
class Helicopter{
    private Point init, location, lineBase, lineEnd;
    private int size, length, fuel, water, speed, maxSpeed, maxWater;
    private double heading;

    public Helicopter(){
        water = 0;
        fuel = 0;
        speed = 0;
        maxSpeed = 10;
        maxWater = 1000;
        heading = 0.0;
        size = Display.getInstance().getDisplayWidth()/28;
        length = size*2+size/2;
        location = new Point(0,0);
        lineBase = new Point(0,0);
        lineEnd = new Point(0,0);
    }

    public Point getLocation(){return location;}
    public void initLocation(Point location){
        init = location;
        this.location = location;
        lineBase = new Point(location.getX()+size/2,location.getY()+size/2);
        lineEnd = new Point((int) (length * Math.sin(heading)) + lineBase.getX(),
                (int) (length * (-Math.cos(heading))) + lineBase.getY());
    }

    public void changeFuel(int fuel){
        this.fuel += fuel;
    }

    public void drainWater(Point river){
        if (water < maxWater && (location.getX() < river.getX() &&
             location.getX()+size > river.getX() && location.getY() < river.getY()
             && location.getY()+size > river.getY())){
            water += 100;
        }
    }
    public void fireWater(Point fire){
        if (water > 0 && (location.getX() < fire.getX() &&
              location.getX()+size > fire.getX() && location.getY() < fire.getY()
              && location.getY()+size > fire.getY())){
             water -= 100;
        }
    }

    public void speedUp(){
        if (speed < maxSpeed) speed++;
    }
    public void speedDown(){
        if (speed > 0) speed--;
    }
    public void changeDirection(double heading){
        this.heading += heading;
        lineEnd = new Point((int) (length * Math.sin(this.heading)) + lineBase.getX(),
                (int) (length * (-Math.cos(this.heading))) + lineBase.getY());
    }
    public void move(){
        int movX = (((lineBase.getX()-lineEnd.getX())/2)/20)*speed;
        location.setX(location.getX()-movX);
        lineBase.setX(lineBase.getX()-movX);
        lineEnd.setX(lineEnd.getX()-movX);

        int movY = (((lineBase.getY()-lineEnd.getY())/2)/20)*speed;
        location.setY(location.getY()-movY);
        lineBase.setY(lineBase.getY()-movY);
        lineEnd.setY(lineEnd.getY()-movY);
    }

    void draw(Graphics g){
        g.setColor(ColorUtil.YELLOW);
        g.fillArc(location.getX(),location.getY(),size,size,0,360);
        g.drawLine(lineBase.getX(),lineBase.getY(),lineEnd.getX(),lineEnd.getY());

        g.drawString("F  : "+fuel,location.getX()+size/2,location.getY()+size*3);
        g.drawString("W  : "+water,location.getX()+size/2,location.getY()+size*4);
    }
}
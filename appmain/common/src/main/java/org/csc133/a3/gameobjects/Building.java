package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.gameobjects.collections.Fires;
import org.csc133.a3.gameobjects.parts.StringPart;

import java.util.ArrayList;
import java.util.Random;

public class Building extends Fixed{
    private final int MAX_HEALTH, INITVALUE;
    private final double area;
    private double damage, value, loss;
    private Fires fires;
    private ArrayList<Double> deadFireDamage;
    private ValueString valueString;
    private DamageString damageString;

    public Building(Point2D location, Dimension dimension) {
        super(dimension);
        setColor(ColorUtil.rgb(255,0,0));
        MAX_HEALTH = 100;
        INITVALUE = (new Random().nextInt(10)+1)*100;
        value = INITVALUE;
        damage = 0;
        loss = 0;
        area = getHeight()*getWidth();
        fires = new Fires();
        deadFireDamage = new ArrayList<>();

        valueString = new ValueString(getColor(),
                      getWidth(), getHeight(), "V: "+INITVALUE);
        damageString = new DamageString(getColor(),
            getWidth(), getHeight(), "D: "+(int)damage+"%");

        translate(location.getX(), location.getY());
    }

    public double getDamage(){
        return damage;
    }
    public int getMAX_HEALTH() {
        return MAX_HEALTH;
    }
    public double getValue(){
        return value;
    }
    public double getLoss(){
        return loss;
    }
    public Fires getFires(){
        return fires;
    }
    public int getTotalFires() {
        return fires.size();
    }
    public double getTotalFireSize(){
        double s = 0;
        for (Fire fire : fires){
            s += fire.getArea();
        }
        return s;
    }

    public void burn(){
        if (damage < MAX_HEALTH) {
            double a = 0;
            for (Fire fire : fires) {
                a += fire.getMaxArea();
                fire.grow();
            }
            for (Double d : deadFireDamage) {
                a += d;
            }
            damage = (a / area) * 100.0;
            value = INITVALUE - damage * (double) (INITVALUE / MAX_HEALTH);
            loss = INITVALUE - value;
        } else if (damage > MAX_HEALTH){
            damage = MAX_HEALTH;
            value = 0;
            loss = INITVALUE;
        }
    }

    public void putOutFire(Fire fire){
        deadFireDamage.add(fire.getMaxArea());
        fires.remove(fire);
    }

    public void setFireInBuilding(Fire fire){
        fire.translate((new Random().nextInt(
                getWidth()) - getWidth()/2f) + getX(),
            (new Random().nextInt(getHeight()) - getHeight()/2f +
                        getY()));
        fires.add(fire);
        fire.start();
    }

    private static class ValueString extends StringPart {

        public ValueString(int color, int width, int height, String text) {
            super(color, width, height, width*1.53f, -height + 50,
                1, 1, 0, text);
        }
    }
    private static class DamageString extends StringPart {

        public DamageString(int color, int width, int height, String text) {
            super(color, width, height, width*1.53f, -height,
                1, 1, 0, text);
        }

        protected void updateLocalTransforms(double damage) {
            setText("D: "+(int)damage+"%");
        }
    }

    @Override
    public void updateLocalTransforms() {
        burn();
        damageString.updateLocalTransforms(damage);
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        g.setColor(getColor());
        containerTranslate(g,parentOrigin);
        cn1ForwardPrimitiveTranslate(g,getDimension());
        g.drawRect(0,0,getWidth(),getHeight());

        valueString.draw(g, parentOrigin, screenOrigin);
        damageString.draw(g, parentOrigin, screenOrigin);
    }


}
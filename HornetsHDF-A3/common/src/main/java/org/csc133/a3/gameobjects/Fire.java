package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.gameobjects.parts.StringPart;
import org.csc133.a3.interfaces.Selectable;

import java.util.Random;

public class Fire extends GameObject implements Selectable {
    private final int initArea;
    private double radius, area, maxArea;
    private FireState state;
    private Building building;
    private boolean selected;
    private FireDispatch fd;
    private SizeString sizeString;

    abstract static class FireState{
        public void grow(int initArea){}
        public void shrink(int water, int initArea){}
        public void localDraw(Graphics g, Point parentOrigin,
                              Point screenOrigin){}
    }

    static class UnStarted extends FireState{}
    class Burning extends FireState {
        @Override
        public void grow(int initArea) {
            if (new Random().nextInt(30) == 0) {
                changeFireSize(getWidth() + initArea / 40);
            }
        }
        @Override
        public void shrink(int water, int initArea) {
            int shrinkFactor = water * initArea / 3000;
            if (getWidth() > shrinkFactor) {
                changeFireSize(getWidth() - shrinkFactor);
            } else {
                state = new Extinguished();
            }
        }

        @Override
        public void localDraw(Graphics g, Point parentOrigin,
                              Point screenOrigin) {
            g.setColor(getColor());
            containerTranslate(g,parentOrigin);
            cn1ForwardPrimitiveTranslate(g,getDimension());
            g.fillArc(0,0,getWidth(),getHeight(),0,360);

            sizeString.draw(g, parentOrigin, screenOrigin);
        }
    }
    static class Extinguished extends FireState{}

    public Fire(int area, Building building, FireDispatch fd) {
        setColor(ColorUtil.MAGENTA);
        this.building = building;
        this.fd = fd;
        state = new UnStarted();
        setDimension(2 * (int)Math.sqrt(area / Math.PI));
        radius = getDimension().getHeight()/2.0;
        this.area = area;
        initArea = area;
        maxArea = area;
        selected = false;
        sizeString = new SizeString(getColor(), getWidth());
    }

    public Fire(){
        initArea = 0;
        state = new UnStarted();
    }

    public double getArea(){
        return area;
    }
    public double getMaxArea(){
        return maxArea;
    }

    public boolean isExtinguished(){
        return state.getClass() == Extinguished.class;
    }

    public void changeFireSize(int size){
        setDimension(size);
        radius = size/2.0;
        area = (Math.PI*radius)*(Math.PI*radius);
        if (area > maxArea){
            maxArea = area;
        }
    }

    void start() {
        state = new Burning();
    }
    void grow(){
        state.grow(initArea);
    }
    void shrink(int water) {
        state.shrink(water, initArea);
        select(false);
        if (state.getClass() == Extinguished.class) {
            building.putOutFire(this);
            fd.getFires().remove(this);
            setDimension(0);
        }
    }

    @Override
    public boolean contains(Point2D p) {
        return distance(getLocation(), p) <= radius;
    }

    @Override
    public void select(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    private static class SizeString extends StringPart{


        public SizeString(int color, int size) {
            super(color, size, size, size*2, -size*11,
                .8f, .8f, 0, String.valueOf(size));
        }
        protected void updateLocalTransforms(int size){
            setText(String.valueOf(size));
        }
    }
    @Override
    public void updateLocalTransforms(){
        sizeString.updateLocalTransforms(getWidth());
    }

    @Override
    protected void localDraw(Graphics g, Point parentOrigin,
                             Point screenOrigin) {
        state.localDraw(g, parentOrigin, screenOrigin);
    }


}

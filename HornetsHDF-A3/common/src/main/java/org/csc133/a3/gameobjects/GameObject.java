package org.csc133.a3.gameobjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;

public abstract class GameObject {
    private int color;
    private Dimension dimension;
    private Transform myTranslation = Transform.makeIdentity();
    private Transform myScale = Transform.makeIdentity();
    private Transform myRotation = Transform.makeIdentity();

    public int getColor(){
        return color;
    }
    public Point2D getLocation(){
        return new Point2D(getX(), getY());
    }
    public double getX(){
        return myTranslation.getTranslateX();
    }
    public double getY(){
        return myTranslation.getTranslateY();
    }
    public Dimension getDimension() {
        return dimension;
    }
    public int getWidth() {
        return dimension.getWidth();
    }
    public int getHeight(){
        return dimension.getHeight();
    }

    public Transform getMyTranslation() {
        return myTranslation;
    }

    protected void setColor(int color){
        this.color = color;
    }
    void setDimension(int dimension){
        this.dimension = new Dimension(dimension, dimension);
    }
    protected void setDimension(Dimension dimension){
        this.dimension = dimension;
    }

    public void translate(double x, double y) {
        myTranslation.translate((float) x, (float) y);
    }

    public void scale(double x, double y) {
        myScale.scale((float) x, (float) y);
    }

    public void rotate(double degrees){
        myRotation.rotate((float)Math.toRadians(degrees),0,0);
    }

    protected void containerTranslate(Graphics g, Point parentOrigin){
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(parentOrigin.getX(),parentOrigin.getY());
        g.setTransform(gxForm);
    }

    protected void cn1ForwardPrimitiveTranslate(Graphics g,
                                                Dimension dimension){
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(-dimension.getWidth()/2f,
            -dimension.getHeight()/2f);
        g.setTransform(gxForm);
    }

    public static int distance(Point2D a, Point2D b){
        return (int)Math.sqrt((b.getX()-a.getX())*(b.getX()-a.getX())+
                (b.getY()-a.getY())*(b.getY()-a.getY()));
    }

    public void draw(Graphics g, Point parentOrigin, Point screenOrigin){
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        Transform gOriginXForm = gxForm.copy();

        gxForm.translate(screenOrigin.getX(), screenOrigin.getY());

        gxForm.translate(myTranslation.getTranslateX(),
            myTranslation.getTranslateY());
        gxForm.concatenate(myRotation);
        gxForm.scale(myScale.getScaleX(),myScale.getScaleY());

        gxForm.translate(-screenOrigin.getX(),-screenOrigin.getY());
        g.setTransform(gxForm);

        localDraw(g,parentOrigin,screenOrigin);

        g.setTransform(gOriginXForm);
    }
    protected abstract void localDraw(Graphics g, Point parentOrigin,
                                      Point screenOrigin);
    public void updateLocalTransforms(){}
}

package org.csc133.a3.gameobjects.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.GameWorld;
import org.csc133.a3.gameobjects.Fire;
import org.csc133.a3.gameobjects.GameObject;

public class MapView extends Container {
    GameWorld gw;

    public MapView(GameWorld gw) {
        this.gw = gw;

        getStyle().setBgColor(ColorUtil.BLACK);
        getStyle().setBgTransparency(255);
    }

    @Override
    public void laidOut(){
        gw.setDimension(new Dimension(getWidth(), getHeight()));
        gw.init();
    }

    private Transform buildWorldToNDXform(float winWidth, float winHeight,
                                          float winLeft, float winBottom){
        Transform tmpXform = Transform.makeIdentity();
        tmpXform.scale(1/winWidth, (1/winHeight));
        tmpXform.translate(-winLeft, -winBottom);
        return tmpXform;
    }
    private Transform buildNDToDisplayXform(float displayWidth,
                                            float displayHeight){
        Transform tmpXform = Transform.makeIdentity();
        tmpXform.translate(0, displayHeight);
        tmpXform.scale(displayWidth, -displayHeight);
        return tmpXform;
    }
    private Transform getVTM(){
        Transform worldToND, ndToDisplay, theVTM;
        float winLeft, winRight, winTop, winBottom;

        winLeft = winBottom = 0;
        winRight = getWidth();
        winTop = getHeight();

        float winHeight = winTop - winBottom;
        float winWidth = winRight - winLeft;

        worldToND = buildWorldToNDXform(winWidth, winHeight,
            winLeft, winBottom);
        ndToDisplay = buildNDToDisplayXform(getWidth(), getHeight());
        theVTM = ndToDisplay.copy();
        theVTM.concatenate(worldToND);
        return theVTM;
    }
    private Transform getInverseVTM(){
        Transform inverseVTM = Transform.makeIdentity();
        try {
            getVTM().getInverse(inverseVTM);

        } catch (Transform.NotInvertibleException e){
            e.printStackTrace();
        }
        return inverseVTM;
    }
    private void setUpVTM(Graphics g){
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(getAbsoluteX(), getAbsoluteY());
        gxForm.concatenate(getVTM());
        gxForm.translate(-getAbsoluteX(), -getAbsoluteY());
        g.setTransform(gxForm);
    }
    private Point2D transformPoint2D(Transform t, Point2D p){
        float[] in = new float[2];
        float[] out = new float[2];
        in[0] = (float) p.getX();
        in[1] = (float) p.getY();
        t.transformPoint(in, out);
        return new Point2D(out[0], out[1]);
    }
    @Override
    public void pointerPressed(int x, int y){
        x -= getAbsoluteX();
        y -= getAbsoluteY();

        Point2D sp = transformPoint2D(getInverseVTM(), new Point2D(x,y));

        for(Fire fire : gw.getFireDispatch().getFires()){
            if (fire.contains(sp) && !fire.isSelected() &&
                gw.getGameObjectCollection().
                    getNonPlayerHelicopter().checkMaxWater()){
                gw.getFireDispatch().getSelectedFire().select(false);
                fire.select(true);
                gw.getGameObjectCollection().getNonPlayerHelicopter().
                getFlightControl().getPrimary().setTail(fire.getLocation());
            }
        }
    }

    public void zoom(){}

    @Override
    public void paint(Graphics g){
        super.paint(g);

        setUpVTM(g);

        Point parentOrigin = new Point(getX(),getY());
        Point screenOrigin = new Point(getAbsoluteX(),getAbsoluteY());

        for(GameObject go: gw.getGameObjectCollection()){
            go.draw(g, parentOrigin, screenOrigin);
        }
        g.resetAffine();
    }
    public void updateLocalTransform(){
        for(GameObject go: gw.getGameObjectCollection()){
            go.updateLocalTransforms();
        }
    }
}

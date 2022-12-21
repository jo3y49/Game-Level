package org.csc133.a3;

import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.UITimer;
import org.csc133.a3.commands.*;
import org.csc133.a3.gameobjects.views.ControlCluster;
import org.csc133.a3.gameobjects.views.MapView;
import org.csc133.a3.gameobjects.views.GlassCockpit;

public class Game extends Form implements Runnable{
    private GameWorld gw;
    private MapView mapView;
    private GlassCockpit glassCockpit;
    private ControlCluster controlCluster;

    public final static int DISP_H = Display.getInstance().getDisplayHeight();
    public final static int DISP_W = Display.getInstance().getDisplayWidth();

    public static int getSmallDim() { return Math.min(DISP_H,DISP_W); }
    public static int getLargeDim() { return Math.max(DISP_H,DISP_W); }

    public Game(){
        gw = new GameWorld();
        setTitle("HFD");

        mapView = new MapView(gw);
        glassCockpit = new GlassCockpit(gw);
        controlCluster = new ControlCluster(gw);

        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, glassCockpit);
        add(BorderLayout.CENTER, mapView);
        add(BorderLayout.SOUTH, controlCluster);

        addKeyListener(-93, new TurnLeftCommand(gw));
        addKeyListener(-94, new TurnRightCommand(gw));
        addKeyListener(-91, new AccelerateCommand(gw));
        addKeyListener(-92, new BrakeCommand(gw));
        addKeyListener('f', new FightCommand(gw));
        addKeyListener('d', new DrinkComand(gw));
        addKeyListener('s', new EngineCommand(gw));
        addKeyListener('Q', new CloseCommand(gw));
        addKeyListener('z', new ZoomCommand(mapView));

        UITimer timer = new UITimer(this);
        timer.schedule(100,true,this);

        show();
    }

    @Override
    public void run() {
        gw.tick();
        glassCockpit.update();
        mapView.updateLocalTransform();
        repaint();
    }

    public void paint (Graphics g){
        super.paint(g);
    }
}

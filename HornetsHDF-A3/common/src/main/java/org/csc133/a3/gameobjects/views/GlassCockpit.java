package org.csc133.a3.gameobjects.views;

import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.GridLayout;
import org.csc133.a3.GameWorld;

public class GlassCockpit extends Container {
    GameWorld gw;
    Label heading, speed, fuel, fires, fireSize, damage, loss;

    public GlassCockpit(GameWorld gw) {
        this.gw = gw;

        setLayout(new GridLayout(2,7));
        add("Heading"); add("Speed"); add("Fuel"); add("Fires");
        add("Fire Size"); add("Damage"); add("Loss");

        heading = new Label("0");
        speed = new Label("0");
        fuel = new Label("0");
        fires = new Label("0");
        fireSize = new Label("0");
        damage = new Label("0");
        loss = new Label("0");

        add(heading); add(speed); add(fuel); add(fires);
        add(fireSize); add(damage); add(loss);
    }

    public void update(){
        heading.setText(gw.getHeading());
        speed.setText(gw.getSpeed());
        fuel.setText(gw.getFuel());
        fires.setText(gw.getFires());
        fireSize.setText(gw.getFireSize());
        damage.setText(gw.getTotalDamage());
        loss.setText(gw.getLoss());
        repaint();
    }
}

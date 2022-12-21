package org.csc133.a3.gameobjects.views;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import org.csc133.a3.GameWorld;
import org.csc133.a3.commands.*;

public class ControlCluster extends Container {
    private GameWorld gw;

    public ControlCluster(GameWorld gw){
        this.gw = gw;

        setLayout(new BorderLayout());
        Form gridLeft = new Form(new GridLayout(1,3));
        Form gridCenter = new Form(new GridLayout(1,2));
        Form gridRight = new Form(new GridLayout(1,3));

        Button turnLeft = buttonMaker("Left", new TurnLeftCommand(gw));
        Button turnRight = buttonMaker("Right", new TurnRightCommand(gw));
        Button fight = buttonMaker("Fight", new FightCommand(gw));
        Button startOrStop = buttonMaker("Start/Stop",
            new EngineCommand(gw));
        Button exit = buttonMaker("Exit", new CloseCommand(gw));
        Button drink = buttonMaker("Drink", new DrinkComand(gw));
        Button brake = buttonMaker("Brake", new BrakeCommand(gw));
        Button accel = buttonMaker("Accel", new AccelerateCommand(gw));

        gridLeft.add(turnLeft); gridLeft.add(turnRight); gridLeft.add(fight);
        gridCenter.add(startOrStop); gridCenter.add(exit);
        gridRight.add(drink); gridRight.add(brake); gridRight.add(accel);

        add(BorderLayout.WEST, gridLeft);
        add(BorderLayout.CENTER, gridCenter);
        add(BorderLayout.EAST, gridRight);
    }

    private Button buttonMaker(String name, Command command){
        Button b = new Button(name);
        b.setCommand(command);
        return b;
    }
}

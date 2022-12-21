package org.csc133.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a3.GameWorld;

public class BrakeCommand extends Command {
    private GameWorld gw;

    public BrakeCommand(GameWorld gw){
        super("Brake");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent event){
        gw.brake();
    }
}

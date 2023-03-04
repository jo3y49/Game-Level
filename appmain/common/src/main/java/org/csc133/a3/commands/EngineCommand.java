package org.csc133.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a3.GameWorld;

public class EngineCommand extends Command {
    private GameWorld gw;
    private static String text = "Start";

    public EngineCommand(GameWorld gw){
        super(text);
        this.gw = gw;
    }
    @Override
    public void actionPerformed(ActionEvent event){
        gw.startOrStopEngine();
    }
}

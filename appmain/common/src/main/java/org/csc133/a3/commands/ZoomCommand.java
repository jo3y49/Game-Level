package org.csc133.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a3.gameobjects.views.MapView;

public class ZoomCommand extends Command {
    private MapView mv;

    public ZoomCommand(MapView mv) {
        super("Zoom");
        this.mv = mv;
    }
    @Override
    public void actionPerformed(ActionEvent event){
        mv.zoom();
    }
}

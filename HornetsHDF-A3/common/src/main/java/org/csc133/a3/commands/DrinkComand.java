package org.csc133.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a3.GameWorld;

public class DrinkComand extends Command {
    private GameWorld gw;

    public DrinkComand(GameWorld gw){
        super("Drink");
        this.gw = gw;
    }

    @Override
    public void actionPerformed(ActionEvent event){
        gw.drinkWater();
    }
}

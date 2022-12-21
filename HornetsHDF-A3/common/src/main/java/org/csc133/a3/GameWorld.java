package org.csc133.a3;

import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.gameobjects.*;
import org.csc133.a3.gameobjects.collections.GameObjectCollection;
import org.csc133.a3.gameobjects.curves.FlightControl;
import org.csc133.a3.gameobjects.curves.FlightPath;

import java.util.Random;

public class GameWorld {
    final int HELICOPTER_FUEL = 25000;
    final int FIRE_BUDGET = 1000;

    private static Dimension worldSize;
    private River river;
    private Helipad helipad;
    private static PlayerHelicopter playerHelicopter;
    private static NonPlayerHelicopter nonPlayerHelicopter;
    private FlightControl fc;
    private FireDispatch fd;
    private GameObjectCollection gameObjects;

    public GameWorld(){}

    public void init() {
        river = new River();
        helipad = new Helipad();
        fd = new FireDispatch();

        playerHelicopter = new PlayerHelicopter(helipad.getLocation(),
            HELICOPTER_FUEL);

        gameObjects = new GameObjectCollection();
        gameObjects.add(river);
        gameObjects.add(helipad);

        buildingInit();

        for (Building building : gameObjects.getBuildings()){
            for (Fire fire : building.getFires()){
                gameObjects.add(fire);
            }
        }

        gameObjects.add(playerHelicopter);
    }

    private void buildingInit(){
        int f1 = new Random().nextInt(100)+300;
        int f2 = (FIRE_BUDGET-f1)/2;
        int f3 = FIRE_BUDGET - (f1 + f2);

        Building topBuilding = new Building(new Point2D(
                worldSize.getWidth()/2f, river.getY() +
            river.getHeight()*2),
                new Dimension((worldSize.getWidth()*6)/10,
                        worldSize.getHeight()/10));
        gameObjects.add(topBuilding);

        Building leftBuilding = new Building(new Point2D(
                worldSize.getWidth()/6f, worldSize.getHeight()/2f),
                new Dimension(worldSize.getWidth()/8,
                        worldSize.getHeight()/3));
        gameObjects.add(leftBuilding);

        Building rightBuilding = new Building(new Point2D(
                5*worldSize.getWidth()/6f, worldSize.getHeight()/2f),
                new Dimension(worldSize.getWidth()/8,
                        worldSize.getHeight()/3));
        gameObjects.add(rightBuilding);

        setFireInBuilding(topBuilding, f1);
        setFireInBuilding(leftBuilding, f2);
        setFireInBuilding(rightBuilding, f3);
    }
    private void setFireInBuilding(Building building, int fireMaxArea){
        int totalFires = fireMaxArea/110;
        int t = fireMaxArea;
        int a;
        for(int i = 0; i < totalFires; i++){
            if (i < totalFires - 1){
                a = (new Random().nextInt(50)+fireMaxArea-25)/totalFires;
                t -= a;
            } else {
                a = t;
            }
            Fire fire = new Fire(a, building, fd);
            building.setFireInBuilding(fire);
            fd.getFires().add(fire);
            gameObjects.add(fire);
        }
    }

    public void tick() {
        spawnNPH();
        spawnFire();

        checkWin();
        checkLose();
    }

    private void spawnNPH(){
        if (nonPlayerHelicopter == null && (GameObject.distance(
            playerHelicopter.getLocation(), helipad.getLocation())) >=
            playerHelicopter.getHeight()*2){

            nonPlayerHelicopter = new NonPlayerHelicopter(HELICOPTER_FUEL,
                new FlightPath(helipad.getLocation(),river.getLocation()), fd);

            gameObjects.add(nonPlayerHelicopter);
            gameObjects.add(nonPlayerHelicopter
                .getFlightControl().getPrimary());
            gameObjects.add(nonPlayerHelicopter
                .getFlightControl().getCorrection());

        }
    }
    private void spawnFire(){
        for (Building b : gameObjects.getBuildings()) {
            if (new Random().nextInt(b.getMAX_HEALTH()*40 -
                (int)b.getDamage()) == 0) {
                setFireInBuilding(b,200);
            }
        }
    }

    private void checkWin(){
        if (playerHelicopter.checkLand(helipad.getLocation(),
            helipad.getRadius()) &&
                gameObjects.getBuildings().getTotalFireSize() <= 0){
            win();
        }
    }
    private void checkLose(){
        if (playerHelicopter.getFuel() <= 0){
            lose("You ran out of fuel!");
        }
        if (gameObjects.getBuildings().getTotalValue() <= 0){
            lose("The buildings burned down!");
        }
        if (nonPlayerHelicopter != null) {
            if (nonPlayerHelicopter.getFuel() <= 0) {
                lose("Your ally ran out of fuel!");
            }
            if (nonPlayerHelicopter.checkForHelicopter(
                playerHelicopter.getLocation())) {
                lose("You crashed into your ally!");
            }
        }
    }

    private void win(){
        if(Dialog.show("VICTORY","You Won! your final score is " + 
                       (int)gameObjects.getBuildings().getTotalValue(),
                "Quit","Play Again")){
            quit();
        } else {
            init();
        }
    }
    private void lose(String text){
        if (Dialog.show("FAILURE",text,
                      "Quit","Play Again")){
            quit();
        } else {
            init();
        }
    }

    public void startOrStopEngine(){
        playerHelicopter.startOrStopEngine();
    }
    public void accelerate(){
        playerHelicopter.accelerate();
    }
    public void brake(){
        playerHelicopter.brake();
    }
    public void steerLeft(){
        playerHelicopter.steerLeft();
    }
    public void steerRight(){
        playerHelicopter.steerRight();
    }
    public void fireWater() {
        playerHelicopter.fireWater(gameObjects.getBuildings().getFires());
    }
    public void drinkWater(){
        playerHelicopter.drinkWater(river.getDimension().getHeight(),
                              river.getLocation());
    }
    public void quit(){
        Display.getInstance().exitApplication();
    }
    public void zoom(){}

    public GameObjectCollection getGameObjectCollection() {
        return gameObjects;
    }
    public static Dimension getWorldSize(){
        return worldSize;
    }
    public static PlayerHelicopter getPlayerHelicopter(){
        return playerHelicopter;
    }
    public static NonPlayerHelicopter nonPlayerHelicopter(){
        return nonPlayerHelicopter;
    }

    public FireDispatch getFireDispatch(){
        return fd;
    }

    public String getFuel() {
        return String.valueOf(playerHelicopter.getFuel());
    }
    public String getFires(){
        return String.valueOf(gameObjects.getBuildings().getTotalFires());
    }
    public String getHeading() {
        return String.valueOf(playerHelicopter.getHeadingD());
    }
    public String getSpeed() {
        return String.valueOf(playerHelicopter.getSpeed());
    }
    public String getFireSize() {
        return String.valueOf((int)gameObjects.getBuildings()
            .getTotalFireSize());
    }
    public String getTotalDamage() {
        return (int)gameObjects.getBuildings().getTotalDamage()/3+"%";
    }
    public String getLoss(){
        return String.valueOf((int)gameObjects.getBuildings().getTotalLoss());
    }

    public void setDimension(Dimension worldSize) {
        GameWorld.worldSize = worldSize;
    }
}
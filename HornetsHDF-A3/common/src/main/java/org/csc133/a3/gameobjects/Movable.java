package org.csc133.a3.gameobjects;

public abstract class Movable extends GameObject {
    private int speed, headingD;
    private double headingR;

    public int getSpeed(){
        return speed;
    }
    public int getHeadingD(){
        return headingD;
    }
    public double getHeadingR(){
        return headingR;
    }

    void setSpeed(int speed){
        this.speed = speed;
    }
    public void setHeadingD(int headingD) {
        this.headingD = headingD;
    }
    void setHeadingR(double headingR){
        this.headingR = headingR;
    }

    void move(){
        translate(getHeight() * Math.sin(headingR) *
            speed/30f, getWidth() * Math.cos(headingR) *
            speed/30f);
    }
}

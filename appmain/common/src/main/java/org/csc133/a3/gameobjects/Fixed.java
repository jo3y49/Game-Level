package org.csc133.a3.gameobjects;

import com.codename1.ui.geom.Dimension;

public abstract class Fixed extends GameObject{
    private final Dimension dimension;

    public Fixed(Dimension dimension) {
        this.dimension = dimension;
    }
    public Fixed(int dimension){
        this.dimension = new Dimension(dimension, dimension);
    }

    @Override
    public Dimension getDimension(){
        return dimension;
    }
    @Override
    public int getWidth() {
        return dimension.getWidth();
    }
    @Override
    public int getHeight(){
        return dimension.getHeight();
    }
}

package org.csc133.a3.gameobjects.collections;

import org.csc133.a3.gameobjects.GameObject;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class GameObjectCollectionIterator<T>
        extends GameObject implements Iterable<T>{
    ArrayList<T> gameObjects;

    class GameObjectIterator implements Iterator<T>{
        int i = 0;

        @Override
        public boolean hasNext() {
            return i < gameObjects.size();
        }

        @Override
        public T next() {
            return gameObjects.get(i++);
        }
    }

    public GameObjectCollectionIterator(){
        gameObjects = new ArrayList<>();
    }

    public ArrayList<T> getGameObjects(){
        return gameObjects;
    }

    public void add(T gameObject){
        gameObjects.add(gameObject);
    }
    public void remove(T gameObject){
        gameObjects.remove(gameObject);
    }
    public int size(){
        return gameObjects.size();
    }

    @Override
    public Iterator<T> iterator(){
        return new GameObjectIterator();
    }
}

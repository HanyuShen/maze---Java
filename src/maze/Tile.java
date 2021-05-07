package maze;

import java.io.Serializable;

public class Tile implements Serializable{
    private Type type;
    private boolean visited = false;

    private Tile(Type typeIn){
        this.type = typeIn;
    }

   
    protected static Tile fromChar(char charIn){
        Tile tile;
        if(charIn == 'e'){
            tile = new Tile(Type.ENTRANCE);
            return tile;
        }
        else if(charIn == 'x'){
            tile = new Tile(Type.EXIT);
            return tile;
        }
        else if(charIn == '#'){
            tile = new Tile(Type.WALL);
            return tile;
        }
        else if(charIn == '.'){
            tile = new Tile(Type.CORRIDOR);
            return tile;
        }
        else{
            return null;
        }
    }

    
    public Type getType(){
        return type;
    }

    
    public boolean isNavigable(){
        if(type==Type.WALL){
            return false;
        }
        else
            return true;
    }

    public boolean getStatus(){
        return visited;
    }

    public boolean setStatus(){
        visited = true;
        return visited;
    }

    
    public String toString(){
        if(type==Type.ENTRANCE)
            return "e";
        else if(type==Type.EXIT)
            return "x";
        else if(type==Type.WALL)
            return "#";
        else
            return ".";
    }

    
    public enum Type {
        CORRIDOR,ENTRANCE,EXIT,WALL;
    }

}
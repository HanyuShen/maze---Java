package maze.routing;

import maze.Maze;
import maze.Tile;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.*;
import java.util.*;

public class RouteFinder implements Serializable {
    private Maze maze;
    private Stack<Tile> route =new Stack<Tile>();
    private boolean finished=false;
    private Stack<Tile> wrong_route =new Stack<Tile>();

    public RouteFinder(Maze mazeIn){
        maze=mazeIn;
    }

    
    public Maze getMaze() {
        return this.maze;
    }

    
    public List<Tile> getRoute(){
        return this.route;
    }

    
    public static RouteFinder load(String path){
        RouteFinder routefinder = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    new File(path)));
            routefinder = (RouteFinder) ois.readObject();
            return routefinder;
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not open "  + ".obj for writing.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: IOException when writing "+  ".obj");
        } 

        return routefinder;
    }

    
    public void save(String fileIn){
        RouteFinder cRouteFinder = new RouteFinder(maze);
        cRouteFinder.maze=maze;
        cRouteFinder.route=route;
        cRouteFinder.finished=finished;
        cRouteFinder.wrong_route=wrong_route;
        ObjectOutputStream objectStream = null;
        try {
            objectStream = new ObjectOutputStream(new FileOutputStream(fileIn));
            objectStream.writeObject(cRouteFinder);
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not open "  + ".obj for writing.");
        } catch (IOException e) {
            System.out.println("There was problem saving!");
        } finally {
            try {
                objectStream.close();
            } catch (IOException e) {
                System.out.println("Error: IOException when closing "+ ".obj");
            }
        }
    }

    
    public boolean step() throws NoRouteFoundException {
        System.out.println(isFinished());
        if(route.size()==0){
            route.add(maze.getEntrance());
            return false;
        }
        else if(route.size()==1){
            if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.SOUTH)!=null){
                if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.SOUTH).isNavigable()){
                    route.add(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.SOUTH));
                    return false;
                }
                else if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.NORTH)!=null){
                    if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.NORTH).isNavigable()){
                        route.add(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.NORTH));
                        return false;
                    }
                    else if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.WEST)!=null){
                        if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.WEST).isNavigable()){
                            route.add(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.WEST));
                            return false;
                        }
                        else if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST)!=null){
                            if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST).isNavigable()){
                                route.add(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST));
                                return false;
                            }
                            else {
                                finished=true;
                                throw new NoRouteFoundException();
                            }
                        }
                    }
                }
            }
            if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.NORTH)!=null){
                if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.NORTH).isNavigable()){
                    route.add(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.NORTH));
                    return false;
                }
                else if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.WEST)!=null){
                    if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.WEST).isNavigable()){
                        route.add(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.WEST));
                        return false;
                    }
                    else if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST)!=null){
                        if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST).isNavigable()){
                            route.add(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST));
                            return false;
                        }
                        else {
                            finished=true;
                            throw new NoRouteFoundException();
                        }
                    }
                }
            }
            if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.WEST)!=null){
                if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.WEST).isNavigable()){
                    route.add(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.WEST));
                    return false;
                }
                else if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST)!=null){
                    if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST).isNavigable()){
                        route.add(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST));
                        return false;
                    }
                    else {
                        finished=true;
                        throw new NoRouteFoundException();
                    }
                }
            }
            if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST)!=null){
                if(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST).isNavigable()){
                    route.add(maze.getAdjacentTile(maze.getEntrance(),Maze.Direction.EAST));
                    return false;
                }
                else {
                    finished=true;
                    throw new NoRouteFoundException();
                }
            }
            else{
                finished=true;
                throw new NoRouteFoundException();
            }
        }


        else{
            Tile cplace, previous_place;
            cplace=route.pop();
            previous_place=route.pop();
            if(cplace.getType()==Tile.Type.EXIT){
                finished=true;
                route.add(previous_place);
                route.add(cplace);
                return true;
            }
            System.out.println(maze.getTileLocation(cplace));
            System.out.println(maze.getTileLocation(previous_place)+"\n");
            if(maze.getAdjacentTile(cplace,Maze.Direction.SOUTH)!=null){
                if(maze.getAdjacentTile(cplace,Maze.Direction.SOUTH).isNavigable()) {
                    if (maze.getAdjacentTile(cplace, Maze.Direction.SOUTH) != previous_place &&
                            !route.contains(maze.getAdjacentTile(cplace,Maze.Direction.SOUTH))) {
                        route.add(previous_place);
                        route.add(cplace);
                        route.add(maze.getAdjacentTile(cplace, Maze.Direction.SOUTH));
                        return false;
                    }
                    else {
                        if(maze.getAdjacentTile(cplace,Maze.Direction.EAST).getType().equals(Tile.Type.WALL) &&
                                maze.getAdjacentTile(cplace,Maze.Direction.WEST).getType().equals(Tile.Type.WALL) &&
                                maze.getAdjacentTile(cplace,Maze.Direction.NORTH).getType().equals(Tile.Type.WALL)){
                            route.add(previous_place);
                            int y = maze.getTileLocation(cplace).getY();
                            int x = maze.getTileLocation(cplace).getX();
                            y = maze.getTiles().size() - y - 1;
                            Collections.replaceAll(maze.getTiles().get(y), cplace, maze.getwrongTile());
                            wrong_route.add(maze.getTiles().get(y).get(x));
                            return false;
                        }
                        else if(route.contains(maze.getAdjacentTile(cplace,Maze.Direction.SOUTH))){
                            route.add(previous_place);
                            int y = maze.getTileLocation(cplace).getY();
                            int x = maze.getTileLocation(cplace).getX();
                            y = maze.getTiles().size() - y - 1;
                            Collections.replaceAll(maze.getTiles().get(y), cplace, maze.getwrongTile());
                            wrong_route.add(maze.getTiles().get(y).get(x));
                            return false;
                        }
                    }
                }
            }
            if(maze.getAdjacentTile(cplace,Maze.Direction.EAST)!=null){
                if(maze.getAdjacentTile(cplace,Maze.Direction.EAST).isNavigable()) {
                    if (maze.getAdjacentTile(cplace, Maze.Direction.EAST) != previous_place &&
                            !route.contains(maze.getAdjacentTile(cplace,Maze.Direction.EAST))) {
                        route.add(previous_place);
                        route.add(cplace);
                        route.add(maze.getAdjacentTile(cplace, Maze.Direction.EAST));
                        return false;
                    }
                    else {
                        if(maze.getAdjacentTile(cplace,Maze.Direction.NORTH).getType().equals(Tile.Type.WALL) &&
                                maze.getAdjacentTile(cplace,Maze.Direction.WEST).getType().equals(Tile.Type.WALL) &&
                                maze.getAdjacentTile(cplace,Maze.Direction.SOUTH).getType().equals(Tile.Type.WALL)){
                            route.add(previous_place);
                            int y = maze.getTileLocation(cplace).getY();
                            int x = maze.getTileLocation(cplace).getX();
                            y = maze.getTiles().size() - y - 1;
                            Collections.replaceAll(maze.getTiles().get(y), cplace, maze.getwrongTile());
                            wrong_route.add(maze.getTiles().get(y).get(x));
                            return false;
                        }
                        else if(route.contains(maze.getAdjacentTile(cplace,Maze.Direction.EAST))){
                            route.add(previous_place);
                            int y = maze.getTileLocation(cplace).getY();
                            int x = maze.getTileLocation(cplace).getX();
                            y = maze.getTiles().size() - y - 1;
                            Collections.replaceAll(maze.getTiles().get(y), cplace, maze.getwrongTile());
                            wrong_route.add(maze.getTiles().get(y).get(x));
                            return false;
                        }
                    }
                }
            }


            if(maze.getAdjacentTile(cplace,Maze.Direction.NORTH)!=null){
                if(maze.getAdjacentTile(cplace,Maze.Direction.NORTH).isNavigable()) {
                    if (maze.getAdjacentTile(cplace, Maze.Direction.NORTH) != previous_place &&
                            !route.contains(maze.getAdjacentTile(cplace,Maze.Direction.NORTH))) {
                        route.add(previous_place);
                        route.add(cplace);
                        route.add(maze.getAdjacentTile(cplace, Maze.Direction.NORTH));
                        return false;
                    }
                    else {
                        if(maze.getAdjacentTile(cplace,Maze.Direction.EAST).getType().equals(Tile.Type.WALL) &&
                                maze.getAdjacentTile(cplace,Maze.Direction.WEST).getType().equals(Tile.Type.WALL) &&
                                maze.getAdjacentTile(cplace,Maze.Direction.SOUTH).getType().equals(Tile.Type.WALL)){
                            route.add(previous_place);
                            int y = maze.getTileLocation(cplace).getY();
                            int x = maze.getTileLocation(cplace).getX();
                            y = maze.getTiles().size() - y - 1;
                            Collections.replaceAll(maze.getTiles().get(y), cplace, maze.getwrongTile());
                            wrong_route.add(maze.getTiles().get(y).get(x));
                            return false;
                        }
                        else if(route.contains(maze.getAdjacentTile(cplace,Maze.Direction.NORTH))){
                            route.add(previous_place);
                            int y = maze.getTileLocation(cplace).getY();
                            int x = maze.getTileLocation(cplace).getX();
                            y = maze.getTiles().size() - y - 1;
                            Collections.replaceAll(maze.getTiles().get(y), cplace, maze.getwrongTile());
                            wrong_route.add(maze.getTiles().get(y).get(x));
                            return false;
                        }
                    }
                }
            }

            if(maze.getAdjacentTile(cplace,Maze.Direction.WEST)!=null){
                if(maze.getAdjacentTile(cplace,Maze.Direction.WEST).isNavigable()) {
                    if (maze.getAdjacentTile(cplace, Maze.Direction.WEST) != previous_place &&
                            !route.contains(maze.getAdjacentTile(cplace,Maze.Direction.WEST))) {
                        route.add(previous_place);
                        route.add(cplace);
                        route.add(maze.getAdjacentTile(cplace, Maze.Direction.WEST));
                        return false;
                    }
                    else {
                        if(maze.getAdjacentTile(cplace,Maze.Direction.EAST).getType().equals(Tile.Type.WALL) &&
                                maze.getAdjacentTile(cplace,Maze.Direction.NORTH).getType().equals(Tile.Type.WALL) &&
                                maze.getAdjacentTile(cplace,Maze.Direction.SOUTH).getType().equals(Tile.Type.WALL)){
                            route.add(previous_place);
                            int y = maze.getTileLocation(cplace).getY();
                            int x = maze.getTileLocation(cplace).getX();
                            y = maze.getTiles().size() - y - 1;
                            Collections.replaceAll(maze.getTiles().get(y), cplace, maze.getwrongTile());
                            wrong_route.add(maze.getTiles().get(y).get(x));
                            return false;
                        }
                        else if(route.contains(maze.getAdjacentTile(cplace,Maze.Direction.WEST))){
                            route.add(previous_place);
                            int y = maze.getTileLocation(cplace).getY();
                            y = maze.getTiles().size() - y - 1;
                            Collections.replaceAll(maze.getTiles().get(y), cplace, maze.getwrongTile());
                            int x = maze.getTileLocation(cplace).getX();
                            wrong_route.add(maze.getTiles().get(y).get(x));
                            return false;
                        }
                    }
                }
                else{
                    throw new NoRouteFoundException();
                }
            }
            else{
                throw new NoRouteFoundException();
            }
        }
        return true;
}

    
    public boolean isFinished() {
        if(this.route!=null) {
            if(this.route.contains(maze.getExit())) {
                this.finished = true;
            }
        }
        else {
            this.finished = false;
        }

        return this.finished;
    }


  
    public String toString(){
        String mazeString = "";
        for(int i = 0;i<maze.getTiles().size();i++){
            for(int j=0;j<maze.getTiles().get(0).size();j++){
                if(route.contains(maze.getTiles().get(i).get(j))){
                    mazeString += " * ";
                }
                else if((route.contains(maze.getTiles().get(i).get(j)) == false) && (maze.getTiles().get(i).get(j).getStatus() == true)){
                    mazeString += " . ";
                }
                else{
                    mazeString += " " + maze.getTiles().get(i).get(j).toString() + " ";
                }
            }
            mazeString += "\n";
        }
        System.out.println(mazeString);
        return mazeString;
    }
}












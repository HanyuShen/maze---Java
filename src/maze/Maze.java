package maze;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.Serializable;


public class Maze implements Serializable{
    private Tile entrance;
    private Tile exit;
    private List<List<Tile>> tiles;

    
    private Maze() {
        // constructor
        this.entrance = null;
        this.exit = null;
        this.tiles = new ArrayList<>();
    }
    
    
    
    private Maze(Tile entrance, Tile exit, List<List<Tile>> tiles) {
        // pass each Maze's value & attributes
        this.entrance = entrance;
        this.exit = exit;
        this.tiles = tiles;
    }

    private void setEntrance(Tile tile)throws MultipleEntranceException, InvalidSetException{
        if(this.entrance!=null) {
            // to catch the multipleEntranceException
            throw new MultipleEntranceException();
        
        }
        getTiles().forEach(tilelist->{
            if(tilelist.contains(tile)) {
                this.entrance = tile;
            }
        
        });
        try {
            if(this.entrance==null) {
                throw new InvalidSetException();
            }
        }
        catch( InvalidSetException e) {
            e.printStackTrace();
        }
        
    }

    private void setExit(Tile tile)throws MultipleExitException, InvalidSetException{
        if(this.exit!=null) {
            // to catch the multipleEntranceException
            throw new MultipleExitException();
        
        }

        getTiles().forEach(tilelist->{
            if(tilelist.contains(tile)) {
                this.exit = tile;
            }
        
        });
        try {
            if(this.exit==null) {
                throw new InvalidSetException();
            }
        }
        catch( InvalidSetException e) {
            e.printStackTrace();
        }
    }

    
    public static Maze fromTxt(String mazeIn) throws Exception{
        Maze maze = new Maze();
        maze.tiles= new ArrayList<>();
        int count_exit=0;
        int count_entrance=0;
        int count1;
        int count2=0;
        try (
                BufferedReader bufferedReader = new BufferedReader(
                        new FileReader(mazeIn)
                )
        ) {
            BufferedReader bufferedReader2 = new BufferedReader(
                    new FileReader(mazeIn)
            );
            String line = bufferedReader.readLine();
            String line2 = bufferedReader2.readLine();
            count1 = line.length();
            while(line2 != null){
                count2+=1;
                line2=bufferedReader2.readLine();
            }
            int count_row=0;
            while(line !=null){
                if (count1 != line.length()) {
                    throw new RaggedMazeException();
                }
                String[] parts = line.strip().split("");
                List<Tile> row = new ArrayList();
                maze.tiles.add(row);
                System.out.print(count2-1 + "  ");
                Tile ntile=null;
                for (int i = 0; i < count1; i++) {
                    if(line.charAt(i) == 'e' || line.charAt(i) == 'x'|| line.charAt(i) == '.'
                            || line.charAt(i) == '#') {

                        ntile = Tile.fromChar(line.charAt(i));
                        row.add(i,ntile);
                    }
                    else {
                        throw new InvalidCharException();
                    }
                    System.out.print(parts[i]);
                    maze.tiles.set(count_row, row);
                    if( ntile.getType().equals(Tile.Type.ENTRANCE)){
                        if(count_entrance == 0){
                            maze.setEntrance(maze.tiles.get(count_row).get(i));
                            count_entrance=count_entrance+1;
                        }
                        else {
                            throw new MultipleEntranceException();
                        }
                    }
                    if( ntile.getType().equals(Tile.Type.EXIT)){
                        if(count_exit == 0){
                            maze.setExit(maze.tiles.get(count_row).get(i));
                            count_exit=count_exit+1;
                        }
                        else {
                            throw new MultipleExitException();
                        }
                    }
                }
                System.out.println("");
                line = bufferedReader.readLine();
                count_row=count_row+1;
                count2=count2-1;
            }
            if(count_exit==0){
                throw new NoExitException();
            }
            if(count_entrance==0){
                throw new NoEntranceException();
            }
            else if(count_entrance>=2){
                throw new MultipleEntranceException();
            }
            for(int i=0;i<count1;i++){
                System.out.print(i);
            }
            System.out.println("");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not open " + mazeIn + "for writing.");
        } catch (IOException e) {
            System.out.println("Error: IOException when writing "+ mazeIn);
        }
        Maze maze_new = new Maze(maze.entrance,maze.exit, maze.tiles);
        return maze_new;
    }

    
    public Tile getwrongTile(){
        Tile update_tile = Tile.fromChar('-');
        return update_tile;
    }

    
    public Tile getAdjacentTile(Tile tileIn,Direction directionIn){
        Coordinate coord = getTileLocation(tileIn);
        int x = coord.getX();
        int y = coord.getY();

        if(directionIn == Direction.SOUTH){
            Coordinate south = new Coordinate(x,y-1);
            if((y-1) < 0){
                System.out.println("Not a valid tile.");
                return null;
            }
            else{
                Tile atSouth = getTileAtLocation(south);
                return atSouth;
            }
        }
        else if(directionIn == Direction.NORTH){
            Coordinate north = new Coordinate(x,y+1);
            if((y+1) > (getTiles().size()-1)){
                System.out.println("Not a valid tile.");
                return null;
            }
            else{
                Tile atNorth = getTileAtLocation(north);
                return atNorth;
            }
        }
        else if(directionIn == Direction.WEST){
            Coordinate west = new Coordinate(x-1,y);
            if(x-1<0){
                System.out.println("Not a valid tile.");
                return null;
            }
            else{
                Tile atWest = getTileAtLocation(west);
                return atWest;
            }
        }
        else if(directionIn == Direction.EAST){
            Coordinate east = new Coordinate(x+1,y);
            if((x+1) > (getTiles().get(0).size()-1)){
                System.out.println("Not a valid tile.");
                return null;
            }
            else{
                Tile atEast = getTileAtLocation(east);
                return atEast;
            }
        }
        else{
            return null;
        }
    }

    
    public Tile getEntrance(){
        return this.entrance;
    }

    
    public Tile getExit(){
        return this.exit;
    }

    
     public Tile getTileAtLocation(Coordinate coordIn){
        Tile tile;
        int line = tiles.size();
        int x=coordIn.getX();
        int y=coordIn.getY();
        tile = tiles.get(line-y-1).get(x);
        return tile;
     }

    
     public Coordinate getTileLocation(Tile tileIn){
        int x=0;
        int y=0;
        int line;
        line = tiles.size();
        for(int i=0;i<line;i++){
            if(tiles.get(i).contains(tileIn)){
                x=tiles.get(i).indexOf(tileIn);
                y= line-i-1;
            }
        }
        Coordinate coordinate = new Coordinate(x, y);
        return coordinate;
     }


    
     public List<List<Tile>> getTiles(){
        return this.tiles;
     }

    
     public String toString() {
         String mazeString = "";
         String mazeTxt = "";
         for(int j=0;j<=getTiles().size();j++){
            if(j == getTiles().size()){
                mazeTxt += "\n" + "\n" + "   ";
                for(int l=0;l<getTiles().get(0).size();l++){
                    if(l<10){
                        mazeTxt += l + "  ";
                    }
                    else{
                        mazeTxt += l + "  ";
                    }
                }
                return mazeTxt;
            }
            mazeTxt += "\n" + getTiles().get(getTiles().size()-1 - j).toString().replace(",", " ").
            replace("[[", "" + String.valueOf(getTiles().size() -j-1) + "  ").
            replace("[", "" + String.valueOf(getTiles().size() -j-1) + "  ").
            replace("]", "" + "\n").trim();
         }
         return mazeTxt;
     }


    
    public enum Direction {
        NORTH,SOUTH,EAST,WEST;
    }

    public static class Coordinate {
        private int x;
        private int y;

        public Coordinate(int xIn,int yIn){
            x=xIn;
            y=yIn;
        }

        
        public int getX(){
            return this.x;
        }

        
        public int getY(){
            return this.y;
        }

        
        public String toString(){
            String coordStr = "";
            int x = getX();
            int y = getY();
            coordStr = "(" + String.valueOf(x) + ", " + String.valueOf(y) + ")";
            return coordStr;
        }

    }

}
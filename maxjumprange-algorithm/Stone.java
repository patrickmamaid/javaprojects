package frog;
/*
 *  Stone.java
 *  Assignment 2 (freddy needs to find a path to fiona using least # of hops)
 *      algorithms used: BFS  &  GREEDY algos
 *  Author:
 *      Patrick Mamaid,  Kevin Hsieh
 */


//this object file is inactive
//it only acts as a stucture

public class Stone {//stores data and adjacent stones
    private double dToFiona = 0;
    private int x;
    private int y;
    private int StoneName;
    public boolean visited;
    
    private Stone(){
        StoneName = 0;
        dToFiona = 0;
        visited = false;
    }
    Stone(int ix, int iy, int stoneName){
        this.StoneName = stoneName;
        this.x = ix;
        this.y = iy;
        visited = false;
    }
    public double getDistanceToFiona(){
        return dToFiona;
    }
    public void setDistanceToFiona(double x){
        dToFiona = x;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getStoneName() {
        return StoneName;
    }
    
    
    
}

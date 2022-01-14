/*
 * Node.java
 *
 * By   Patrick Mamaid  A00609663
 *      Kevin Hsieh     A00527530
 *   Date :  12/6/2007
 *
 * This object is a node, it has a nodes properties ie: has an edge etc..
 * i use this node in the billy's currency algorithm
 *
 *
 */

package travel;

import java.util.ArrayList;

public class Node {

    public boolean visited = false;
    private boolean thisHasAnEdge = false;
    private String name;
    private ArrayList<Edge> edges = new ArrayList<Edge>();

    Node(String name_) {
        this.name = name_;
    }

    public boolean isThisHasAnEdge() {
        return thisHasAnEdge;
    }

    public void setThisHasAnEdge(boolean thisHasAnEdge) {
        this.thisHasAnEdge = thisHasAnEdge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public Edge getEdge(int i) {
        Edge result = null;

        try {
            result = edges.get(i);
        } catch (Exception e) {
            System.err.println("out of bounds, when trying to get edge " + i);
        }

        return result;
    }

    public void setAddEdge(Edge edge) {
        this.edges.add(edge);
        this.thisHasAnEdge = true;
    }
}
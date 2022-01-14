/*
 * Edge.java
 *
 * By   Patrick Mamaid  A00609663
 *      Kevin Hsieh     A00527530
 *   Date :  12/6/2007
 * this is essentialy an edge object. it simulates the properties
 * of an edge in algorithms.
 * i use this to connect nodes one way only
 */

package travel;

public class Edge {

    private int weight;
    private Node node;

    Edge(int weight_) {
        weight = weight_;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
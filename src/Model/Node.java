package Model;

import java.util.ArrayList;

public class Node {

    private char origin;
    private char dest;
    private boolean turn;       // TRUE - white; FALSE - black
    private char[][] matriz;
    private int minMax;
    private ArrayList<Node> children;

    public Node() {
        this.children = new ArrayList<>();
        this.minMax = Integer.MIN_VALUE;
    }

    public Node(char origin, char dest) {
        this.origin = origin;
        this.dest = dest;
        this.children = new ArrayList<>();
        this.minMax = Integer.MIN_VALUE;
    }

    public char getDest() {
        return dest;
    }

    public void setDest(char dest) {
        this.dest = dest;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

    public int getMinMax() {
        return minMax;
    }

    public void setMinMax(int minMax) {
        this.minMax = minMax;
    }

    public char[][] getMatriz() {
        return matriz;
    }

    public void setMatriz(Tabuleiro tabuleiro) {
       matriz = tabuleiro.getMatriz();
    }

    public char getOrigin() {
        return origin;
    }

    public void setOrigin(char origin) {
        this.origin = origin;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void addChild(Node child) {
        if (this.children == null)
            this.children = new ArrayList<>();
        children.add(child);
    }

    public void clear() {
        if (children != null) {
            children.clear();
        }
        matriz = null;
        minMax = Integer.MIN_VALUE;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node other = (Node) o;
        return origin == other.origin && dest == other.dest;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(origin);
        result = 31 * result + Integer.hashCode(dest);
        return result;
    }
}

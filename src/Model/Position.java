package Model;

public class Position {

    private int Row;
    private int Col;

    public Position(int row, int col) {
        this.Row = row;
        this.Col = col;
    }

    public int getRow() {return Row;}
    public int getCol() {return Col;}

    public void setRow(int row) {this.Row = row;}
    public void setCol(int col) {this.Col = col;}
    public void setPosition(int row, int col) {
        this.Row = row;
        this.Col = col;
    }

    public Position getPosition(Position pos, int r, int c) {
        return new Position(pos.getRow() + r, pos.getCol() + c);
    }

}

package src.server;

public class Piece extends Position {
    boolean king = false;
    Piece(int x, int y) {
        super(x, y);
    }

    public Piece(int x, int y, boolean b) {
        super(x, y);
        king = b;
    }

    public String toString() {
        if (!king)
            return "(" + x + ", " + y + ")";
        return "{" + x + ", " + y + "}";
    }

}

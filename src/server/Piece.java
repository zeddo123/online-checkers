package src.server;

public class Piece extends Position {
    boolean king = false;
    Piece(int x, int y) {
        super(x, y);
    }

    public String toString() {
        if (!king)
            return "(" + x + ", " + y + ")";
        return "{" + x + ", " + y + "}";
    }

}

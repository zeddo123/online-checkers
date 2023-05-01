package src.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Position implements Serializable {
    protected int x;
    protected int y;

    public Position(int x, int y) {
        this.x = x; this.y = y;
    }

    public List<Integer> possibleCols() {
        var l = new ArrayList<Integer>();
        if (y - 1 >= 0)
            l.add(y - 1);
        if (y + 1 <= 7)
            l.add(y + 1);
        return l;
    }

    public boolean isXMovePossible(int direction) {
        return x + direction <= 7 && x + direction >= 0;
    }
    public boolean at(Position x) {
        return this.at(x.x, x.y);
    }

    public boolean at(int x, int y) {
        return x == this.x && y == this.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Position))
            return false;

        return this.at((Position) obj);
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(x).hashCode() * 10 + Integer.valueOf(y).hashCode();
    }

    public boolean diagonal(Position p) {
        return Math.abs(this.x - p.x) == Math.abs(this.y - p.y);
    }

    public void changePosition(Position newPosition) {
        this.x = newPosition.x;
        this.y = newPosition.y;
    }
}

package src.client;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import src.server.Position;

import java.util.List;

public class Tile extends Rectangle {
    public int x;
    public int y;
    public Color primary;
    public Color semiHighlighted = Color.YELLOW;
    public Color highlighted = Color.YELLOWGREEN;
    public List<Position> moves;

    public Tile(int x, int y, int i, int i1, int i2, int i3) {
        super(i, i1, i2, i3);
        this.x = x;
        this.y = y;
    }

    public void resetColor() {
        setFill(primary);
    }

    public void semiHighlight() {
        setFill(semiHighlighted);
    }

    public void highlight() {
        setFill(highlighted);
    }

    public boolean isHighlighted() {
        return this.getFill() == highlighted;
    }

    public boolean notHighlighted() {
        return this.getFill() != highlighted && this.getFill() != semiHighlighted;
    }

}

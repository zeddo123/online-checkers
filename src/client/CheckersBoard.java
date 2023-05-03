package src.client;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import src.server.Piece;
import src.server.Position;

import java.util.ArrayList;
import java.util.List;

public class CheckersBoard extends GridPane {
    public java.awt.Color yourColor;
    public java.awt.Color opponentsColor;
    public Piece currSelected;
    public List<List<Position>> currSelectedMoves;
    public List<Tile> toReset = new ArrayList<>();
    public Client client;

    public CheckersBoard(Client client) {
        this.client = client;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var rect = new Tile(i, j, 0, 0, 80, 80);
                rect.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                    if (rect.isHighlighted()) {
                        var error = client.playMove(rect.moves);
                        if (error != null) {
                            switch (error) {
                                case NotTurn -> UI.showAlert("Not your turn!");
                                case PlayerPieceNotFound -> UI.showAlert("piece not found");
                                case UnCompleteMove -> UI.showAlert("Uncomplete");
                                case TooManyMoveOptions -> UI.showAlert("Too many options");
                                case IllegalMove -> UI.showAlert("Illegal move");
                                case GameOver -> UI.showAlert("Game is over");
                                default -> UI.showAlert("other problem");
                            }
                        }
                    }
                });
                if ((j + i) % 2 == 0) {
                    rect.primary = Color.rgb(181, 226, 184, 1);
                    rect.setFill(rect.primary);
                } else {
                    rect.primary = Color.rgb(112, 162, 162, 1);
                    rect.setFill(rect.primary);
                }
                add(rect, i, j);
            }
        }
    }
    public Color getPaint(java.awt.Color awtColor) {
        int r = awtColor.getRed();
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();
        int a = awtColor.getAlpha();
        double opacity = a / 255.0 ;
        return javafx.scene.paint.Color.rgb(r, g, b, opacity);
    }

    public void OnBoardChanged(List<Piece> yours, List<Piece> opponents){
        getChildren().removeIf(node -> {return node instanceof Circle;});
        resetTiles();

        yours.forEach(p -> {
            var c = new Circle(35, getPaint(yourColor));
            c.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                currSelected = p;
                currSelectedMoves = client.getPieceMoves(p);

                resetTiles();
                for (var moves : currSelectedMoves) {
                    Tile lastMove = null;
                    for (var move : moves) {
                        var node = getNodeByRowColumnIndex(move.getX(), move.getY());
                        ((Tile) node).semiHighlight();
                        toReset.add((Tile) node);
                        lastMove = (Tile) node;
                    }
                    if (lastMove != null){
                        lastMove.highlight();
                        lastMove.moves = moves;
                    }
                }

            });
            this.add(c, p.getY(), p.getX());
        });
        opponents.forEach(p -> this.add(new Circle(35, getPaint(opponentsColor)), p.getY(), p.getX()));
    }

    public Node getNodeByRowColumnIndex (final int row, final int column) {
        Node result = null;

        for (Node node : this.getChildren()) {
            if(getRowIndex(node) == row && getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    public void resetTiles() {
        if(!toReset.isEmpty()) {
            for (var rekt : toReset){
                rekt.resetColor();
            }
        }
        toReset.clear();
    }

}

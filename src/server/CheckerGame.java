package src.server;

import java.util.ArrayList;
import java.util.List;

public class CheckerGame {
    private List<Piece> p1Pieces = new ArrayList<>();
    private List<Piece> p2Pieces = new ArrayList<>();
    public int turn = -1;

    // 0 - Game isn't over yet.
    // 1 - Player 1 won the game
    // -1 - Player 2 won the game
    public int gameover = 0;

    CheckerGame() {
        int i;
        int j;
        for (i = 0; i < 3; i++) {
            if (i % 2 == 0){
                j = 1;
            } else {
                j = 0;
            }
            for (; j <= 7; j += 2){
                p1Pieces.add(new Piece(i, j));
            }
        }
        for (i = 5; i <= 7; i++) {
            if (i % 2 == 0){
                j = 1;
            } else {
                j = 0;
            }
            for (; j <= 7; j += 2){
                p2Pieces.add(new Piece(i, j));
            }
        }
    }

    public void makeMove(int player, GraphMoves move) throws NotTurnException, TooManyMoveOptions, PlayersPieceNotFoundException, UncompleteMoveException, IllegalMoveException, GameOverException {
        if (isTheGameOver()){
            throw new GameOverException();
        }
        if (player != turn)
            throw new NotTurnException();

        List<Position> moves;
        try {
            moves = move.toList();
        } catch (NotPathologicalException e) {
            throw new TooManyMoveOptions();
        }
        if (moves.size() < 2)
            throw new UncompleteMoveException();

        System.out.println(moves);

        var piece = getPiece(player, moves.get(0));
        if (piece == null)
            throw new PlayersPieceNotFoundException();

        var legalMoves = possibleMoves(piece, player, true, null, false);
        if (!legalMoves.isPath(move)) {
            throw new IllegalMoveException();
        }

        Position lastmove = null;
        for (Position p : moves) {
            p1Pieces.remove(new Piece(p.x, p.y));
            p2Pieces.remove(new Piece(p.x, p.y));
            lastmove = p;
        }
        piece.changePosition(lastmove);
        currPlayerPieces(player).add(piece);

        // Complete the turn
        turn *= -1;
    }

    public boolean isTheGameOver() {
        if(gameover != 0)
            return true;

        if(p1Pieces.isEmpty() || allPossibleMovesp1().isEmpty()){
            this.gameover = -1;
            return true;
        }
        if (p2Pieces.isEmpty() || allPossibleMovesp2().isEmpty()){
            this.gameover = 1;
            return true;
        }
        return false;
    }

    public int Winner() {
        isTheGameOver();
        return this.gameover;
    }

    private Piece getPiece(int player, Position position) {
        var playerPieces = currPlayerPieces(player);
        for (var piece : playerPieces)
            if (piece.equals(position))
                return piece;
        return null;
    }

    public boolean occupied(Position x) {
        for (Piece p : this.p1Pieces)
            if (p.at(x))
                return true;
        for (Piece p : this.p2Pieces)
            if (p.at(x))
                return true;

        return false;
    }

    public GraphMoves possibleMoves(Piece p, int direction, boolean hop, Position diagonalTo, boolean onlyOccup) {
        var graph = new GraphMoves(p);

        var rows = new ArrayList<Integer>();
        var cols = p.possibleCols();
        if (p.isXMovePossible(direction))
            rows.add(p.x + direction);
        if (p.king && p.isXMovePossible(-direction))
            rows.add(p.x - direction);

        for (int row : rows) {
            for (int col : cols) {
                var position = new Piece(row, col);
                if (!onlyOccup && !occupied(position)){
                    if (diagonalTo == null) {
                        //moves.put(position, new Moves<>());
                        graph.add(new GraphMoves(position));
                    } else if (diagonalTo.diagonal(position)) {
                       //moves.put(position, possibleMoves(position, direction, true, p, true));
                       graph.add(possibleMoves(position, direction, true, p, true));
                    }
                } else if (hop && opponentsPiece(position, -1 * direction)) {
                    //moves.put(
                    //        position,
                    //        possibleMoves(position, direction, false, p, false)
                    //);
                    graph.add(possibleMoves(position, direction, false, p, false));
                }
            }
        }
        return graph;
    }

    public List<Piece> currPlayerPieces(int player) {
        // 1 - is the first player (Black)
        // -1 - is the second player (White)
        return player == 1 ? this.p1Pieces : this.p2Pieces;
    }

    private boolean opponentsPiece(Piece position, int direction) {
        return currPlayerPieces(direction).contains(position);
    }

    public List<GraphMoves> allPossibleMoves(List<Piece> pieces, int direction) {
        List<GraphMoves> result = new ArrayList<>();

        for (Piece p : pieces){
            var moves = possibleMoves(p, direction, true, null, false);
            if (moves.size() > 0)
                result.add(moves);
        }
        return result;
    }

    public List<GraphMoves> allPossibleMovesp1() {
        return allPossibleMoves(this.p1Pieces, 1);
    }

    public List<GraphMoves> allPossibleMovesp2() {
        return allPossibleMoves(this.p2Pieces, -1);
    }

    public List<GraphMoves> allPossibleMovesOf(int player) {
        return allPossibleMoves(currPlayerPieces(player), player);
    }

    public String toString() {
        return "Black (" + this.p1Pieces.size() + ") White (" + this.p2Pieces.size() + ")";
    }

    public void CliBoard() {
        System.out.println("  0  1  2  3  4  5  6  7");
        for (int i = 0; i < 8; i++) {
            System.out.print(i);
            for (int j = 0; j < 8; j++) {
                if (p1Pieces.contains(new Piece(i, j))){
                    System.out.print("|X|");
                } else if (p2Pieces.contains(new Piece(i, j))){
                    System.out.print("|Y|");
                }else {
                    System.out.print("| |");
                }

            }
            System.out.println();
        }
    }

}

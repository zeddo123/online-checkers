package src.server;

public class Testing {
    public static void main(String[] args) {
        var g = new CheckerGame();
        g.p1Pieces.clear();
        g.p2Pieces.clear();

        /*
        g.p1Pieces.add(new Piece(6, 3, true));
        g.p1Pieces.add(new Piece(3, 6));
        g.p1Pieces.add(new Piece(4, 3));

        g.p2Pieces.add(new Piece(5, 4));
        g.p2Pieces.add(new Piece(7, 4));
        System.out.println(g.possibleMoves(1, new Piece(6, 3), 1, true, null, false));
         */
        g.p1Pieces.add(new Piece(7, 2, true));

        g.p2Pieces.add(new Piece(6, 1));
        g.p2Pieces.add(new Piece(6, 3));
        g.CliBoard();
        System.out.println(g.possibleMoves(1, new Piece(7, 2), 1, true, null, false));
    }
}

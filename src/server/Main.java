package src.server;

public class Main {
    public static void main(String[] args) {
        var g = new CheckerGame();
        g.CliBoard();
        var move = new GraphMoves(new Position(5, 0));
        move.add(new GraphMoves(new Position(4, 1)));

        try {
            g.makeMove(-1, move);
        } catch (NotTurnException | TooManyMoveOptions | PlayersPieceNotFoundException | UncompleteMoveException |
                 IllegalMoveException | GameOverException e) {
            throw new RuntimeException(e);
        }
        g.CliBoard();
        System.out.println(g.isTheGameOver());
    }
}

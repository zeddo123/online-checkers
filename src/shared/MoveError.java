package src.shared;

import java.io.Serializable;

public enum MoveError implements Serializable {
    ServerSideError,
    NotTurn,
    PlayerPieceNotFound,
    GameOver,
    IllegalMove,
    TooManyMoveOptions,
    UnCompleteMove
}

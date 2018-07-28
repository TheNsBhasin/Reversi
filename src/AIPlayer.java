import java.util.HashSet;
import java.util.Set;

public class AIPlayer {
    private static final int MAX_DEPTH = 7;
    private static final int ALPHA = -100;
    private static final int BETA = +100;

    private Move bestMove;          //Stores the row and col value for best move
    private int positons;
    private int numberOfWhite;      //Store the Number or White Discs on the Board
    private int numberOfBlack;      //Store the Number or Black Discs on the Board
    private int noOfWins;           //Stores the Number of Won Matches
    private int noOfLosses;         //Store the Number of Lost Matches
    private int noOfDraws;          //Store the Number of Draw Matches

    public AIPlayer() {
        this.positons = 0;
        this.noOfWins = 0;
        this.noOfLosses = 0;
        this.noOfDraws = 0;
    }

    private int MiniMax(Board board, Player player, int currentDepth, int alpha, int beta) {
        bestMove = new Move();
        int bestScore = 0;
        int score = 0;

        Set<Move> moveList = new HashSet<>();

        if (currentDepth == MAX_DEPTH) {
            return evaluate(board, player);
        }

        Board tempBoard = new Board(board);

        if (player.equals(Player.BLACK)) {
            bestScore = alpha;
            moveList = tempBoard.possibleMoves(Player.WHITE);
        } else {
            bestScore = beta;
            moveList = tempBoard.possibleMoves(Player.BLACK);
        }

        for (Move move : moveList) {
            ++positons;
            tempBoard.makeMove(move.row, move.col, player);

            if (player == Player.WHITE) {
                score = MiniMax(tempBoard, Player.BLACK, currentDepth + 1, alpha, beta);
            } else {
                score = MiniMax(tempBoard, Player.WHITE, currentDepth + 1, alpha, beta);
            }

            if (player.equals(Player.BLACK)) {
                if (score > bestScore) {
                    alpha = bestScore = score;
                    bestMove = move;
                }
            } else {
                if (score < bestScore) {
                    beta = bestScore = score;
                    bestMove = move;
                }
            }

            if (alpha >= beta) {
                return bestScore;
            }
        }

        if (moveList.size() == 0) {
            bestScore = evaluate(board, player);
        }

        if (currentDepth != 0) {
            return bestScore;
        } else {
            bestScore = score;
            this.bestMove = bestMove;
            board.makeMove(bestMove.row, bestMove.col, player);
            return 1;
        }
    }

    private void GenerateMoveList(Board board, Player player, Set<Move> moveList, Set<Move> moveDirection) {
        for (int row = 0; row < Board.BOARD_SIZE; ++row) {
            for (int col = 0; col < Board.BOARD_SIZE; ++col) {
                if (board.getSlotStatus(row, col) == Player.getValue(player)) {
                    LookAround(board, player, moveList, moveDirection, row, col);
                }
            }
        }
    }

    private void LookAround(Board board, Player player, Set<Move> moveList, Set<Move> moveDirection, int row, int col) {
        for (int dir = 0; dir < 8; ++dir) {
            int x = row + Board.dx[dir];
            int y = col + Board.dy[dir];

            if (!Board.isSafe(x, y)) {
                continue;
            }

            if (board.getSlotStatus(x, y) == Player.getValue(Player.EMPTY)) {
                if (board.isMovePossible(x, y, player)) {
                    moveList.add(new Move(x, y));
                    moveDirection.add(new Move(row, col));
                }
            }
        }
    }

    private int evaluate(Board board, Player player) {
        int corners = 0;

        numberOfBlack = board.countPlayerSlots(Player.BLACK);
        numberOfWhite = board.countPlayerSlots(Player.WHITE);

        if (numberOfBlack > numberOfWhite) {
            if (board.getSlotStatus(0, 0) == Player.getValue(Player.BLACK)) {
                ++corners;
            }
            if (board.getSlotStatus(0, Board.BOARD_SIZE - 1) == Player.getValue(Player.BLACK)) {
                ++corners;
            }
            if (board.getSlotStatus(Board.BOARD_SIZE - 1, 0) == Player.getValue(Player.BLACK)) {
                ++corners;
            }
            if (board.getSlotStatus(Board.BOARD_SIZE - 1, Board.BOARD_SIZE - 1) == Player.getValue(Player.BLACK)) {
                ++corners;
            }

            ++noOfWins;
            return 1 + corners;

        } else if (numberOfBlack < numberOfWhite) {
            if (board.getSlotStatus(0, 0) == Player.getValue(Player.WHITE)) {
                --corners;
            }
            if (board.getSlotStatus(0, Board.BOARD_SIZE - 1) == Player.getValue(Player.WHITE)) {
                --corners;
            }
            if (board.getSlotStatus(Board.BOARD_SIZE - 1, 0) == Player.getValue(Player.WHITE)) {
                --corners;
            }
            if (board.getSlotStatus(Board.BOARD_SIZE - 1, Board.BOARD_SIZE - 1) == Player.getValue(Player.WHITE)) {
                --corners;
            }

            ++noOfLosses;
            return -1 + corners;

        } else {
            ++noOfDraws;
            return 0;
        }
    }

    public int getComputerMove(Board board, Player player) {
        positons = 0;
        noOfDraws = 0;
        noOfWins = 0;
        noOfLosses = 0;
        return MiniMax(board, player, 0, ALPHA, BETA);
    }
}

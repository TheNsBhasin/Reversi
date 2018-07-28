import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Board {
    public static final int BOARD_SIZE = 8;    // Store size of the board
    private static final int THRESH = 10;

    public static final int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    public static final int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

    private static BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));


    private Player[][] gameboard;               // Matrix to store gameboard
    private int[] scores = new int[3];          // Matrix to store score
    private boolean status;                     // Store the status of the game

    // Constructor to initialize the board and place initial pieces on the board
    public Board() {
        gameboard = new Player[BOARD_SIZE][BOARD_SIZE];
        status = true;

        // Initialise the board matrix and put 0 at every index
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                setMove(i, j, Player.EMPTY);
            }
        }

        // Set initial black and white pieces
        setMove((BOARD_SIZE - 1) >> 1, (BOARD_SIZE - 1) >> 1, Player.BLACK);
        setMove(BOARD_SIZE >> 1, BOARD_SIZE >> 1, Player.BLACK);
        setMove((BOARD_SIZE - 1) >> 1, BOARD_SIZE >> 1, Player.WHITE);
        setMove(BOARD_SIZE >> 1, (BOARD_SIZE - 1) >> 1, Player.WHITE);

        // Initialize score array to 0
        for (int i = 0; i < scores.length; ++i) {
            scores[i] = 0;
        }
    }

    public Board(Board board) {
        this.gameboard = board.gameboard;
        this.scores = board.scores;
        this.status = board.status;
    }

    // Function to check if solt (x, y) is valid.
    public static boolean isSafe(int x, int y) {
        return x >= 0 && y >= 0 && x < BOARD_SIZE && y < BOARD_SIZE;
    }

    // Print all the possible moves for a player
    public Set<Move> possibleMoves(Player player) {
        Set<Move> moveList = new HashSet<>();
        for (int x = 0; x < BOARD_SIZE; ++x) {
            for (int y = 0; y < BOARD_SIZE; ++y) {
                if (getSlotStatus(x, y) == Player.getValue(Player.EMPTY) && countTheFlips(x, y, player) > 0) {
                    moveList.add(new Move(x, y));
                }
            }
        }
        return moveList;
    }

    public void printPossibleMoves(Player player) {
        Set<Move> moveList = possibleMoves(player);
        Iterator<Move> iter = moveList.iterator();
        System.out.print("    [<<] Possible Moves :\t");
        while (iter.hasNext()) {
            Move move = iter.next();
            System.out.print("(" + (move.row + 1) + ", " + (move.col + 1) + ")");
            if (iter.hasNext()) {
                System.out.print(", ");
            }
        }
        System.out.println("\n");
    }

    // Returns how many flips a move will make
    private int countTheFlips(int x, int y, Player player) {
        if (!isSafe(x, y)) {
            return 0;
        }

        int count = 0;

        for (int dir = 0; dir < 8; ++dir) {
            int new_x = x + dx[dir];
            int new_y = y + dy[dir];
            int flips = 0;
            while (isSafe(new_x, new_y)) {
                if (gameboard[new_x][new_y] == Player.EMPTY) {
                    break;
                }
                if (gameboard[new_x][new_y] == player) {
                    count += flips;
                    break;
                }
                ++flips;

                new_x += dx[dir];
                new_y += dy[dir];
            }
        }

        return count;
    }

    // Set the slot based on received x and y
    public void setMove(int x, int y, Player player) {
        if (!isSafe(x, y)) {
            return;
        }

        gameboard[x][y] = player;
        changePlayerScore(+THRESH, player);
    }

    public boolean getMove(Player player) {
        int x = -1, y = -1;
        while (true) {
            System.out.println("[>>] Player " + Player.getValue(player) + "'s turn: ");
            System.out.print("  [>>] Input x: ");

            try {
                // Get x coordinate
                x = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (x > BOARD_SIZE || x < 1) {
                System.out.println("    [!<<] Invalid input: x value must be between 1 and " + BOARD_SIZE);
                continue;
            }

            System.out.print("  [>>] Input y: ");

            try {
                // Get y coordinate
                y = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (y > BOARD_SIZE || y < 1) {
                System.out.println("    [!<<] Invalid input: y value must be between 1 and " + BOARD_SIZE);
                continue;
            }

            boolean move = makeMove(x - 1, y - 1, player);
            if (move) {
                return true;
            } else {
                System.out.println("    [!<<] Invalid move, try again.");
            }
        }
    }

    public boolean makeMove(int x, int y, Player player) {
        if (!isSafe(x, y)) {
            return false;
        }

        if (isMovePossible(x, y, player)) {
            setMove(x, y, player);
            flipDiscMove(x, y, player);
            return true;
        } else {
            return false;
        }
    }

    private void flipDiscMove(int x, int y, Player player) {
        if (!isSafe(x, y)) {
            return;
        }

        for (int dir = 0; dir < 8; ++dir) {
            int new_x = x + dx[dir];
            int new_y = y + dy[dir];
            int flips = 0;
            while (isSafe(new_x, new_y)) {
                if (gameboard[new_x][new_y] == Player.EMPTY) {
                    break;
                }
                if (gameboard[new_x][new_y] == player) {
                    int temp_x = new_x;
                    int temp_y = new_y;
                    while (flips > 0) {
                        temp_x -= dx[dir];
                        temp_y -= dy[dir];
                        setMove(temp_x, temp_y, player);
                        if (player == Player.BLACK) {
                            changePlayerScore(-THRESH, Player.WHITE);
                        } else {
                            changePlayerScore(-THRESH, Player.BLACK);
                        }
                        --flips;
                    }
                    break;
                }

                ++flips;

                new_x += dx[dir];
                new_y += dy[dir];
            }
        }
    }

    // Returns the number of slots owned by the player given
    public int countPlayerSlots(Player player) {
        int count = 0;
        for (int x = 0; x < BOARD_SIZE; ++x) {
            for (int y = 0; y < BOARD_SIZE; ++y) {
                if (getSlotStatus(x, y) == Player.getValue(player)) {
                    ++count;
                }
            }
        }
        return count;
    }

    // Count the no. of 0's on the board
    private int countFreeSlots() {
        int count = 0;

        for (int x = 0; x < BOARD_SIZE; ++x) {
            for (int y = 0; y < BOARD_SIZE; ++y) {
                if (getSlotStatus(x, y) == Player.getValue(Player.EMPTY)) {
                    ++count;
                }
            }
        }
        return count;
    }

    private void changePlayerScore(int change, Player player) {
        scores[Player.getValue(player)] += change;
    }

    private int getPlayerScore(Player player) {
        return scores[Player.getValue(player)];
    }

    // Tell whether move is possible or not
    public boolean isMovePossible(int x, int y, Player player) {

        // Check if slot is already taken
        if (getSlotStatus(x, y) != Player.getValue(Player.EMPTY)) {
            return false;
        }

        return countTheFlips(x, y, player) > 0;
    }

    public Player determineWinner() {
        int numberOfBlack = 0, numberOfWhite = 0;

        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (gameboard[x][y] == Player.BLACK) {
                    ++numberOfBlack;
                } else if (gameboard[x][y] == Player.WHITE) {
                    ++numberOfWhite;
                }
            }
        }

        if (numberOfBlack > numberOfWhite) {
            return Player.BLACK;
        } else if (numberOfBlack < numberOfWhite) {
            return Player.WHITE;
        } else {
            return Player.EMPTY;
        }
    }

    // Returns the current status of the game.
    public boolean getStatus() {
        return status;
    }

    // Function to tell whether slot (x, y) is 0, 1 or 2.
    public int getSlotStatus(int x, int y) {
        if (!isSafe(x, y)) {
            return 0;
        }
        return Player.getValue(gameboard[x][y]);
    }

    // Return the player who is winner
    public boolean checkWin(Player player) {
        if (countFreeSlots() == 0) {
            changeGameStatus();
            return true;
        } else if (player == Player.BLACK && existMoves(Player.WHITE)) {
            changeGameStatus();
            return true;
        } else if (player == Player.WHITE && existMoves(Player.BLACK)) {
            changeGameStatus();
            return true;
        }
        return false;
    }

    // Returns whether there are any possible moves for a player left
    private boolean existMoves(Player player) {
        int x, y;
        for (x = 0; x < BOARD_SIZE; x++) {
            for (y = 0; y < BOARD_SIZE; y++) {
                if (getSlotStatus(x, y) == Player.getValue(Player.EMPTY) && countTheFlips(x, y, player) > 0) {
                    return false;
                }
            }
        }

        return true;
    }

    // Toggle the Status of the game
    private void changeGameStatus() {
        status = !status;
    }

    // Print the current board state.
    public void printGameboard() {
        System.out.println("\n    [<<] Current Board");
        System.out.println("");
        System.out.print("      ");
        for (int i = 1; i <= BOARD_SIZE; ++i) {
            System.out.print(i + " ");
        }
        System.out.println("\n");
        for (int x = 0; x < BOARD_SIZE; x++) {
            System.out.print("   " + (x + 1) + "  ");
            for (int y = 0; y < BOARD_SIZE; y++) {
                System.out.print(getSlotStatus(x, y) + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    // Print score of both the players
    public void printScoreboard() {
        System.out.println("[<<] Current stats:");
        System.out.println("    [<<] Player " + Player.getValue(Player.BLACK) + "'s score: " + getPlayerScore(Player.BLACK));
        System.out.println("    [<<] Player " + Player.getValue(Player.WHITE) + "'s score: " + getPlayerScore(Player.WHITE));
    }
}

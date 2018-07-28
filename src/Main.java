import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    private static BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    private static Player playerColor;

    public static void main(String[] args) {
        System.out.println("[<<] Let's Begin with the Game");
        // Prompt for play mode
        // 1 => against computer
        // 2 => against another player
        int mode = selectPlayingMode();
        playerColor = chooseDisc();

        if (mode == 1) {
            singlePlayer();
        } else {
            multiPlayer();
        }
    }

    // Function to play against Computer
    private static void singlePlayer() {
        System.out.println("[<<] Creating game object...");
        // Create game object
        Board game = new Board();
        System.out.println("    [<<] Game object successfully created.");

        System.out.println("[<<] Creating AI object...");
        AIPlayer aiPlayer = new AIPlayer();
        System.out.println("    [<<] AI object successfully created.");

        System.out.println("    [<<] Player " + Player.getValue(Player.BLACK) + " is BLACK");
        System.out.println("    [<<] Player " + Player.getValue(Player.WHITE) + " is WHITE");

        System.out.println("[<<] Player 1's turn:");

        // Print the game board on the console
        game.printGameboard();

        while (game.getStatus()) {
            game.printPossibleMoves(Player.BLACK);
            game.getMove(Player.BLACK);
            game.printGameboard();
            game.printScoreboard();
            if (game.checkWin(Player.BLACK)) {
                break;
            }
            aiPlayer.getComputerMove(game, Player.WHITE);
            game.printGameboard();
            game.printScoreboard();
            if (game.checkWin(Player.WHITE)) {
                break;
            }
        }

        if (!game.getStatus()) {
            System.out.println("[<<] Game over !");
            game.printScoreboard();
            if (game.determineWinner() == Player.EMPTY) {
                System.out.println("    [!!!] Player " + Player.getValue(Player.BLACK) + " and Player " + Player.getValue(Player.WHITE) + " have TIED.");
            } else {
                System.out.println("    [!!!] Player " + Player.getValue(game.determineWinner()) + " WINS!");
            }
        }
    }

    // Function to play against another player
    private static void multiPlayer() {
        System.out.println("[<<] Creating game object...");
        // Create game object
        Board game = new Board();
        System.out.println("    [<<] Game object successfully created.");

        System.out.println("    [<<] Player " + Player.getValue(Player.BLACK) + " is BLACK");
        System.out.println("    [<<] Player " + Player.getValue(Player.WHITE) + " is WHITE");

        System.out.println("[<<] Player 1's turn:");

        // Print the game board on the console
        game.printGameboard();
        while (game.getStatus()) {
            game.printPossibleMoves(Player.BLACK);
            game.getMove(Player.BLACK);
            game.printGameboard();
            game.printScoreboard();
            if (game.checkWin(Player.BLACK)) {
                break;
            }
            game.printPossibleMoves(Player.WHITE);
            game.getMove(Player.WHITE);
            game.printGameboard();
            game.printScoreboard();
            if (game.checkWin(Player.WHITE)) {
                break;
            }
        }

        if (!game.getStatus()) {
            System.out.println("[<<] Game over !");
            game.printScoreboard();
            if (game.determineWinner() == Player.EMPTY) {
                System.out.println("    [!!!] Player " + Player.getValue(Player.BLACK) + " and Player " + Player.getValue(Player.WHITE) + " have TIED.");
            } else {
                System.out.println("    [!!!] Player " + Player.getValue(game.determineWinner()) + " WINS!");
            }
        }
    }

    // Function to get the playing disc color from user
    private static Player chooseDisc() {
        System.out.println("[<<] Select Your Color of Disc");
        System.out.println("1. Black Disc");
        System.out.println("2. White Disc");

        while (true) {
            System.out.print("  [<<] Enter the Choice: ");
            int value = 0;
            try {
                value = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // If user enters value other than 1 or 2
            if (value != 1 && value != 2) {
                System.out.println("    [!<<] Invalid input: Disc Color must be Black or White.");
            }

            // If value is 1 or 2
            // Return the value to main
            else {
                return (value == 1) ? Player.BLACK : Player.WHITE;
            }
        }
    }

    // Function to get the playing mode from user
    private static int selectPlayingMode() {
        System.out.println("[<<] Select play mode");
        System.out.println("1. Single Player Mode");
        System.out.println("2. Multi Player Mode");

        while (true) {
            System.out.print("  [<<] Enter the Choice: ");
            int value = 0;
            try {
                value = Integer.parseInt(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // If user enters value other than 1 or 2
            if (value != 1 && value != 2) {
                System.out.println("    [!<<] Invalid input: play mode must be 1 or 2.");
            }

            // If value is 1 or 2
            // return the value to main
            else {
                return value;
            }
        }

    }
}

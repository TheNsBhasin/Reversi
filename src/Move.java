public class Move {
    public int row;
    public int col;

    Move() {
        row = col = -1;
    }

    Move(int x, int y) {
        this.row = x;
        this.col = y;
    }
}
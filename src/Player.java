public enum Player {
    EMPTY, BLACK, WHITE;

    public static int getValue(Player player) {
        switch (player) {
            case EMPTY:
                return 0;
            case BLACK:
                return 1;
            case WHITE:
                return 2;
            default:
                return -1;
        }
    }
}

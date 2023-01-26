package PlusWorld;

public class Position {
    int x;
    int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Position shift(int x, int y) {
        return new Position(x + this.x, y + this.y);
    }

    /* Return a position that is up to the right of the current Position
     * Used for drawing within column */
    public static Position upRight(Position p, int size) {
        Position newPos = p.shift(size, size * 2);
        return newPos;
    }

    /* Return a position that is down to the left of the current Position
     * Used for drawing within column */
    public static Position downLeft(Position p, int size) {
        Position newPos = p.shift(-size, -size * 2);
        return newPos;
    }

    /* Return a position that is down to the right of the current Position
     * Used for drawing next column to the right */
    public static Position downRight(Position p, int size) {
        Position newPos = p.shift(size * 2, -size);
        return newPos;
    }

    /* Return a position that is up to the left of the current Position
     * Used for drawing next column to the left */
    public static Position upLeft(Position p, int size) {
        Position newPos = p.shift(- 2 * size, size );
        return newPos;
    }
}

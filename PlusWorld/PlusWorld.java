package PlusWorld;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of plus shaped regions.
 */
public class PlusWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final long SEED = 26869;
    private static final Random RANDOM = new Random(SEED);

    /** Fill the tiles with Nothing tiles */
    public static void fillWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** Add a row in the position p of input size with given type of tile
     * Skips indexes that fails vaildity check */
    public static void addRow(TETile[][] tiles, Position p, TETile tile, int size) {
        for (int x = p.x; x < p.x + size; x++) {
            if (checkValidity(x, p.y, WIDTH, HEIGHT)) {
                tiles[x][p.y] = tile;
            }
        }
    }

    /** Recursively add rows to the tiles. As depth increases shift position of y decreases by 2 * d - 1
     * d: depth of the recursion */
    public static void addPlusHelper(TETile[][] tiles, Position p, TETile tile, int size, int d) {
        Position startOfRow = p.shift(size, 0);
        addRow(tiles, startOfRow, tile, size);

        if (d < size) {
            Position nextP = p.shift(0, -1);
            addPlusHelper(tiles, nextP, tile, size, d + 1);
        } else {
            for (int i = 0; i < size; i++) {
                p = p.shift(0, -1);
                addRow(tiles, p, tile, size * 3);
            }
        }

        Position startOfReflectedRow = p.shift(size, -((size * 3) - (2 * d)));
        addRow(tiles, startOfReflectedRow, tile, size);
    }

    /* Add plus sign using helper function */
    public static void addPlus(TETile[][] tiles, Position p, TETile tile, int size) {
        if (size < 2) {
            return;
        }
        addPlusHelper(tiles, p, tile, size, 1);
    }

    /* Check validity of the index */
    public static boolean checkValidity(int x, int y, int width, int height) {
        if (x >= 0 && x < width && y < height && y >= 0) {
            return true;
        }
        return false;
    }

    /* Check if all index are out of bound */
    public static boolean outOfBound(Position p, int size, int width, int height) {
        if (p.x >= width + height + size|| p.x <= - 3 * size - height || p.y >= height + size * 3 || p.y < 0) {
            return true;
        }
        return false;
    }

    /* Expand plus columns to both left and right */
    public static void addPlusExpand(TETile[][] tiles, Position p, TETile tile, int size) {
        addPlusColumn(tiles, p, tile, size);
        addPlusLeftColumn(tiles, p, randomeTile(), size);
        addPlusRightColumn(tiles, p, randomeTile(), size);
    }

    /* Add columns recursively to the right of the given position */
    public static void addPlusLeftColumn(TETile[][] tiles, Position p, TETile tile, int size) {
        Position upLeftPos = Position.upLeft(p, size);

        if (outOfBound(upLeftPos, size, WIDTH, HEIGHT)) {
            return;
        }

        addPlusColumn(tiles, upLeftPos, tile, size);
        addPlusLeftColumn(tiles, upLeftPos, randomeTile(), size);
    }

    /* Add columns recursively to the left of the given position */
    public static void addPlusRightColumn(TETile[][] tiles, Position p, TETile tile, int size) {
        Position downRightPos = Position.downRight(p, size);

        if (outOfBound(downRightPos, size, WIDTH, HEIGHT)) {
            return;
        }

        addPlusColumn(tiles, downRightPos, tile, size);
        addPlusRightColumn(tiles, downRightPos, randomeTile(), size);
    }


    /* Add one column of plus */
    public static void addPlusColumn(TETile[][] tiles, Position p, TETile tile, int size) {
        // add plus in the current tile
        addPlus(tiles, p, tile, size);
        addPlusUpRight(tiles, p, randomeTile(), size);
        addPlusDownLeft(tiles, p, randomeTile(), size);
    }

    /* Add plus to the up right direction recursively until it fails */
    public static void addPlusUpRight(TETile[][] tiles, Position p, TETile tile, int size) {

        /* change position */
        Position upRightPos = Position.upRight(p, size);

        /* base case */
        if (outOfBound(upRightPos, size, WIDTH, HEIGHT)) {
            return;
        }

        /* Add upRight plus */
        addPlus(tiles, upRightPos, tile, size);
        addPlusUpRight(tiles, upRightPos, randomeTile(), size);
    }

    /* Add plus down left recursively until it fails */
    public static void addPlusDownLeft(TETile[][] tiles, Position p, TETile tile, int size)
    {
        /* Change position */
        Position downLeftPos = Position.downLeft(p, size);

        /* Base Case */
        if (outOfBound(p, size, WIDTH, HEIGHT)) {
            return;
        }

        /* Add downLeft plus */
        addPlus(tiles, downLeftPos, tile, size);
        addPlusDownLeft(tiles, downLeftPos, randomeTile(), size);
    }

    /* Returns a random tile */
    public static TETile randomeTile() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.FLOWER;
            case 1: return Tileset.TREE;
            case 2: return Tileset.SAND;
            case 3: return Tileset.LOCKED_DOOR;
            case 4: return Tileset.GRASS;
            case 5: return Tileset.FLOOR;
            default: return Tileset.NOTHING;
        }
    }

    public static void drawWorld(TETile[][] tiles) {
        fillWithNothing(tiles);
        Position p = new Position(20, 30);
        addPlusExpand(tiles, p, randomeTile(), 2);
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] plustile = new TETile[WIDTH][HEIGHT];
        drawWorld(plustile);

        ter.renderFrame(plustile);
    }
}

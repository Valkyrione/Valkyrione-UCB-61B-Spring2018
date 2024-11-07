package byog.lab5;
import org.junit.Test;

import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        int[][] map = mapHexagon(s);
        drawByMap(world, p, map, t);
    }

    // Returns an 2D int array for a hexagon shape with value 1 where it should be painted and 0 where it shouldn't
    private static int[][] mapHexagon(int s) {
        int width = widthHexagon(s);
        int height = heightHexagon(s);
        int[][] map = new int[width][height];

        for (int y = 0; y < height/2; y++) {
            for (int x = 0; x < width; x++) {
                if (x >= s - 1 - y && x <= width - s + y) {
                    map[x][y] = 1;
                    map[x][height - 1 - y] = 1;
                } else {
                    map[x][y] = 0;
                    map[x][height - 1- y] = 0;
                }
            }
        }

        return map;
    }

    // Returns the width of a hexagon area
    private static int widthHexagon(int s) {
        return s + (s - 1) * 2;
    }

    // Returns the height of a hexagon area
    private static int heightHexagon(int s) {
        return s * 2;
    }

    // Paints the TETile according to a map on the specified position of a world
    private static void drawByMap(TETile[][] world, Position p, int[][] map, TETile t) {
        int width = map.length;
        int height = map[0].length;
        if (p.x + width > world.length || p.y + height > world[0].length) {
            return;
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] == 1) {
                    world[p.x + x][p.y + y] = t;
                }
            }
        }
    }

    // Returns the position for any shape at the bottom-center position of the world
    private static Position startingPoint(TETile[][] world, int width, int height) {
        if (width > world.length || height > world[0].length) {
            return null;
        }
        Position BLCorner = new Position();
        BLCorner.x = (int) (world.length - width) / 2;
        int spacing = world[0].length - height * numberShapeVertical(world, height);
        BLCorner.y = (int) spacing / 2;
        return BLCorner;
    }

    // Returns the number of shapes in the center column
    private static int numberShapeVertical(TETile[][] world, int height) {
        int worldHeight = world[0].length;
        return (int) worldHeight / height;
    }

    // Returns the starting position for drawing hexagons of a specified size
    private static Position startingPointHexagon(TETile[][] world, int s) {
        int width = widthHexagon(s);
        int height = heightHexagon(s);
        return startingPoint(world, width, height);
    }

    // Returns the position of the neighboring hexagon on top
    private static Position topNeighborHexagon(Position p, int s) {
        Position top = new Position();
        top.x = p.x;
        top.y = p.y + s * 2;
        return top;
    }

    // Returns the position of the top-left neighboring hexagon
    private static Position topLeftNeighborHexagon(Position p, int s) {
        Position topLeft = new Position();
        topLeft.x = p.x + 1 - s * 2;
        topLeft.y = p.y + s;
        return topLeft;
    }

    // Returns the position of the top-right neighboring hexagon
    private static Position topRightNeighborHexagon(Position p, int s) {
        Position topRight = new Position();
        topRight.x = p.x - 1 + s * 2;
        topRight.y = p.y + s;
        return topRight;
    }

    // Returns a random TETile from all the types in Tileset
    private static TETile randomTile() {
        Random random = new Random();
        int tileNum = random.nextInt(6);
        return switch (tileNum) {
            case 0 -> Tileset.GRASS;
            case 1 -> Tileset.FLOWER;
            case 2 -> Tileset.MOUNTAIN;
            case 3 -> Tileset.TREE;
            case 4 -> Tileset.SAND;
            case 5 -> Tileset.WATER;
            default -> Tileset.NOTHING;
        };
    }

    // Draws a column of hexagons with random tile
    private static void drawHexagonColumn(TETile[][] world, Position p, int s, int n) {
        Position current = new Position();
        current.x = p.x;
        current.y = p.y;
        for (int i = 0; i < n; i++) {
            addHexagon(world, current, s, randomTile());
            current.y += s * 2;
        }
    }

    // Draw hexagons to cover the canvas in a hexagon pattern
    public static void hexagonOfHexagons(TETile[][] world, int s) {
        Position head = startingPointHexagon(world, s);
        int height = heightHexagon(s);
        int number = numberShapeVertical(world, height);
        drawHexagonColumn(world, head, s, number);
        Position leftHead = topLeftNeighborHexagon(head, s);
        Position rightHead = topRightNeighborHexagon(head, s);
        while (leftHead.x >= 0) {
            number--;
            drawHexagonColumn(world, leftHead, s, number);
            drawHexagonColumn(world, rightHead, s, number);
            leftHead = topLeftNeighborHexagon(leftHead, s);
            rightHead = topRightNeighborHexagon(rightHead, s);
        }
    }

}

class Position {
    public int x;
    public int y;
}
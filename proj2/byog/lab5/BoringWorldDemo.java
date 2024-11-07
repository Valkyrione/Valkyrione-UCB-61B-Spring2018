package byog.lab5;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/**
 *  Draws a world that is mostly empty except for a small region.
 */
public class BoringWorldDemo {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // fills in a block 14 tiles wide by 4 tiles tall
        for (int x = 20; x < 35; x += 1) {
            for (int y = 5; y < 16; y += 1) {
                if (y == 5) {
                    world[x][y] = Tileset.PLAYER;
                } else if (y == 6) {
                    world[x][y] = Tileset.WALL;
                } else if (y == 7) {
                    world[x][y] = Tileset.FLOOR;
                } else if (y == 8) {
                    world[x][y] = Tileset.GRASS;
                } else if (y == 9) {
                    world[x][y] = Tileset.WATER;
                } else if (y == 10) {
                    world[x][y] = Tileset.FLOWER;
                } else if (y == 11) {
                    world[x][y] = Tileset.LOCKED_DOOR;
                } else if (y == 12) {
                    world[x][y] = Tileset.UNLOCKED_DOOR;
                } else if (y == 13) {
                    world[x][y] = Tileset.SAND;
                } else if (y == 14) {
                    world[x][y] = Tileset.MOUNTAIN;
                } else if (y == 15) {
                    world[x][y] = Tileset.TREE;
                }
            }
        }

        // draws the world to the screen
        ter.renderFrame(world);
    }


}

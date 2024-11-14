package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.sql.Array;
import java.util.*;
import java.util.regex.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        /* Original code.
        * Change back after testing RandomWorld.
        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
         */
        String regex = "^[Nn](\\d+)[Ss]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        long seed;
        if (matcher.matches()) {
            String numberPart = matcher.group(1);
            seed = Long.parseLong(numberPart);
        } else {
            seed = (long) 0;
        }

        RandomWorld newWorld = new RandomWorld(seed);
        return newWorld.world;
    }

    private static class RandomWorld {
        private Random random;
        private TETile[][] world = new TETile[WIDTH][HEIGHT];
        private Deque<Node> nodes = new ArrayDeque<>();

        private int indoorTilesNum = 0;

        /** The percentage of tiles that are indoor. */
        private static final double PERCENTINDOOR = 0.4;

        /** The upper length limit of hallways. */
        private static final int HALLUPPER = 5;

        /**
         * The lower length limit of hallways.
         * At least 2!!!
         */
        private static final int HALLLOWER = 2;

        /** The chance to generate a rectangular room at each node. */
        private static final double CHANCERECROOM = 0.8;

        /** The upper size limit of rectangular room on x. */
        private static final int XROOMUPPER = 7;

        /**
         * The lower size limit of rectangular room on x.
         * At least 2!!!
         */
        private static final int XROOMLOWER = 3;

        /** The upper size limit of rectangular room on y. */
        private static final int YROOMUPPER = 7;

        /**
         * The lower size limit of rectangular room on y.
         * At least 2!!!
         */
        private static final int YROOMLOWER = 3;

        /** The chance to generate a random-shaped room at each node. */
        /*private static final double PERCENTRANDOMROOM = 0.3;*/

        RandomWorld() {
            this(0);
        }
        RandomWorld(long seed) {
            // Fill world with NOTHING tiles
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    world[x][y] = Tileset.NOTHING;
                }
            }
            random = new Random(seed);
            Pixel startingPoint = randomPixelForStart();
            nodes.add(createRectRoom(startingPoint));
            int totalTilesNum = WIDTH * HEIGHT;
            int targetIndoorTilesNum = (int) Math.round(totalTilesNum * PERCENTINDOOR);
            /*
            for (int i = 0; i < 6; i++) {
                proceed();
            }
             */

            while (indoorTilesNum < targetIndoorTilesNum) {
                proceed();
            }

            paintWall();
        }

        private class Pixel {
            public int x;
            public int y;

            // 0-left; 1-up; 2-right; 3-down
            public char direction;
            public Pixel(int x, int y, char direction) {
                this.x = x;
                this.y = y;
                this.direction = direction;
            }
            public Pixel() {
                this(0, 0, 'N');
            }

            /**
             * Return true if the Pixel is good for floor.
             * The pixel should not be on the edge of the canvas.
             * The pixel should not neighbor with any non-NOTHING pixel on either of its four directions.
             * The given exception will be overlooked (like testing the first pixel of a room should overlook the
             * entrance which it always neighbors with).
             * Pass in null if no exception needed.
             */
            public boolean goodForFloor(Pixel exception) {
                return !(onEdge() || hasNeighbor(exception));
            }

            /**
             * Return true if the pixel is on the edge.
             */
            public boolean onEdge() {
                return x == 0 || x == WIDTH - 1 || y == 0 || y == HEIGHT - 1;
            }

            /**
             * Return true if any of four neighboring pixels is FLOOR.
             */
            public boolean hasNeighbor(Pixel exception) {
                Pixel left = findNeighbor('L');
                Pixel up = findNeighbor('U');
                Pixel right = findNeighbor('R');
                Pixel down = findNeighbor('D');
                Pixel[] neighbors = {left, up, right, down};
                boolean haveNeighbor = false;
                for (Pixel neighbor : neighbors) {
                    if (exception != null && neighbor.x == exception.x && neighbor.y == exception.y) {
                        continue;
                    }
                    if (neighbor != null && neighbor.checkTile() != Tileset.NOTHING) {
                        haveNeighbor = true;
                    }
                }
                return haveNeighbor;
            }

            public boolean hasNeighborDiag() {
                Pixel topLeft = findPixel(this, 'U', 1, 1);
                Pixel topRight = findPixel(this, 'R', 1, 1);
                Pixel bottomRight = findPixel(this, 'D', 1, 1);
                Pixel bottomLeft = findPixel(this, 'L', 1, 1);
                Pixel[] neighborsDiag = {topLeft, topRight, bottomRight, bottomLeft};
                boolean haveNeighborDiag = false;
                for (Pixel neighborDiag : neighborsDiag) {
                    if (neighborDiag != null && neighborDiag.checkTile() != Tileset.NOTHING) {
                        haveNeighborDiag = true;
                    }
                }
                return haveNeighborDiag;
            }

            /**
             * Return the neighboring pixel on the given direction.
             */
            public Pixel findNeighbor(char direction) {
                return findPixel(this, direction, 1, 0);
            }

            /**
             * Return the TETile of this pixel.
             */
            public TETile checkTile() {
                return world[x][y];
            }

            /**
             * Return true if a pixel should be wall.
             * It can only be NOTHING before painting walls.
             * It should have a neighboring pixel that is not NOTHING.
             * Return false for edge pixels that don't neighbor with non-NOTHING pixels.
             */
            public boolean shouldBeWall() {
                return (hasNeighbor(null) || hasNeighborDiag()) && checkTile() == Tileset.NOTHING;
            }

            /**
             * Paint this pixel with the specified tile.
             */
            public void paint(TETile tile) {
                world[x][y] = tile;
            }

            /**
             * Return true if the pixel is the edge of a room using isEdge() and can draw a hallway from it.
             */
            public List<HallHead> listHallHeads() {
                List<HallHead> list = new ArrayList<>();
                if (world[x][y] != Tileset.FLOOR) {
                    return list;
                }
                if (world[x-1][y] == Tileset.NOTHING) {
                    HallHead left = new HallHead(x-1, y, 'L');
                    if (left.isGoodHead) {
                        list.add(left);
                    }
                }
                if (world[x][y+1] == Tileset.NOTHING) {
                    HallHead up = new HallHead(x, y+1, 'U');
                    if (up.isGoodHead) {
                        list.add(up);
                    }
                }
                if (world[x+1][y] == Tileset.NOTHING) {
                    HallHead right = new HallHead(x+1, y, 'R');
                    if (right.isGoodHead) {
                        list.add(right);
                    }
                }
                if (world[x][y-1] == Tileset.NOTHING) {
                    HallHead down = new HallHead(x, y-1, 'D');
                    if (down.isGoodHead) {
                        list.add(down);
                    }
                }
                return list;
            }
        }

        /**
         * A special pixel that can serve as the head of a hallway.
         * Has an extra field for the highest extension, including the starting pixel, up to HALLUPPER.
         */
        private class HallHead extends Pixel {
            public int extension = 0;
            public boolean isGoodHead = false;

            public HallHead(int x, int y, char direction) {
                super(x, y, direction);
                findExtension();
            }

            public HallHead() {
                super();
                findExtension();
            }

            private void findExtension() {
                for (int i = 1; i < HALLUPPER; i++) {
                    Pixel current = findPixel(this, i, 0);
                    if (current != null && current.goodForFloor(null)) {
                        extension = i;
                    } else {
                        break;
                    }
                }
                if (extension + 1 >= HALLLOWER) {
                    isGoodHead = true;
                }
            }
        }

        /**
         * Each Node is the starting point of a painting step.
         * It can be a hallway connection or a room.
         */
        private interface Node {
        }

        private class Room implements Node{
            public static final int MAXHALLNUMPERROOM = 4;
            public int hallNum = 0;
            public Pixel entrance = null;
            List<Pixel> pixels;

            public Room(Pixel entrance) {
                this.entrance = entrance;
            }

            public Room() {
                this.entrance = null;
            }

            public List<HallHead> hallHeads() {
                List<HallHead> hallHeads = new ArrayList<>();
                for (Pixel pixel : pixels) {
                    List<HallHead> current = pixel.listHallHeads();
                    hallHeads.addAll(current);
                }
                return hallHeads;
            }
        }

        private class Hallway implements Node{
            public Pixel end = null;
            public HallHead head = null;
            public int length = 0;
            public Hallway(HallHead head, int length) {
                this.head = head;
                if (length <= head.extension + 1) {
                    end = findPixel(head, length - 1, 0);
                    this.length = length;
                }
            }

            public Hallway() {
            }
        }

        /**
         * Create a random node for starting.
         * Direction is U(up).
         * Leaving space for minimum rectangular room.
         * Coordinate of the starting point is in the wall at the downside.
         */
        private Pixel randomPixelForStart() {
            /* Horizontally, the coordinate can range with 1 pixel (for wall) from either left or right side. */
            int x = RandomUtils.uniform(random, WIDTH - 2) + 1;

            /* Vertically, the coordinate can go down to bottom (be the wall) or up to 1(wall)+YROOMLOWER away from the top. */
            int y = RandomUtils.uniform(random, HEIGHT - 1 - YROOMLOWER);

            return new Pixel(x, y, 'U');
        }

        /**
         * Proceed with the next creation.
         * Read the end of nodes deque as the starting point.
         * If from a room, paint a hallway from the room. If no hallway can be painted from that room, remove the
         * last node from nodes and return.
         * If from a hallway, randomly decide whether to create another hallway or a room. If one can't be done, do
         * the other. If neither can be done, remove the last node from nodes and return.
         * proceed (manage nodes)
         *      - createHallFromRoom (choose head; manage null return)
         *          - paintHall (choose length; paint tiles; update indoorTilesNum; find end; return Hallway)
     *          - createRectRoomFromHall/createRectRoom(choose depth, width, and position; manage null return)
         *          - paintRectRoom(paint tiles; update indoorTilesNum; list possible hallheads; return Room)
     *          - createHallFromHall (choose head; manage null return)
         *          - paintHall (choose length; paint tiles; update indoorTilesNum; find end; return Hallway)
         */
        private void proceed() {
            Node last = nodes.peekLast();
            if (last instanceof Room) {
                Hallway newHallway = createHallFromRoom((Room) last);
                if (newHallway == null) {
                    nodes.removeLast();
                    return;
                } else {
                    nodes.addLast(newHallway);
                    return;
                }
            } else if (last instanceof Hallway) {
                double selector = RandomUtils.uniform(random);
                if (selector < CHANCERECROOM) {
                    Room newRoom = createRectRoomFromHall((Hallway) last);
                    if (newRoom != null) {
                        nodes.removeLast();
                        nodes.addLast(newRoom);
                        return;
                    } else {
                        Hallway newHallway = createHallFromHall((Hallway) last);
                        if (newHallway != null) {
                            nodes.addLast(newHallway);
                            return;
                        } else {
                            nodes.removeLast();
                            return;
                        }
                    }
                } else {
                    Hallway newHallway = createHallFromHall((Hallway) last);
                    if (newHallway != null) {
                        nodes.addLast(newHallway);
                        return;
                    } else {
                        Room newRoom = createRectRoomFromHall((Hallway) last);
                        if (newRoom != null) {
                            nodes.removeLast();
                            nodes.add(newRoom);
                            return;
                        } else {
                            nodes.removeLast();
                            return;
                        }
                    }
                }
            }
        }

        /**
         * Generate and paint a rectangular room, and return it.
         * The room is connected to the entrance node onto its direction.
         * Size and location (relative to the entrance) are calculated based on distance to the closest floor.
         * Return null if there's no space for smallest room from the given entrance.
         */
        private Room createRectRoom(Pixel entrance) {
            List<int[]> possibleRectRooms = listPossibleRectRooms(entrance);
            if (possibleRectRooms.isEmpty()) {
                return null;
            }
            int randomIndex = RandomUtils.uniform(random, possibleRectRooms.size());
            int[] randomRectRoom = possibleRectRooms.get(randomIndex);
            return paintRectRoom(entrance, randomRectRoom);
        }

        /**
         * Given the entrance, return a list of coordinates for all possible rectangular rooms.
         * The returned int[] has three values
         * 0: depth of room (distance between the entrance and the opposite wall.
         * 1: moving left from the direction of entrance.
         * 2: width of room.
         */
        private List<int[]> listPossibleRectRooms(Pixel entrance) {
            int widthUpper;
            int widthLower;
            int depthUpper;
            int depthLower;
            if (entrance.direction == 'U' || entrance.direction == 'D') {
                widthUpper = XROOMUPPER;
                widthLower = XROOMLOWER;
                depthUpper = YROOMUPPER;
                depthLower = YROOMLOWER;
            } else {
                widthUpper = YROOMUPPER;
                widthLower = YROOMLOWER;
                depthUpper = XROOMUPPER;
                depthLower = XROOMLOWER;
            }
            int leftLimit = widthUpper;
            int rightLimit = widthUpper;

            /* To find the space available for different depth of rectangular rooms.
            * For each array, {depth, left reach, right reach}
            */
            List<int[]> widthByDepth = new ArrayList<>();
            for (int depth = 1; depth <= depthUpper; depth++) {
                int leftReach = -1;
                int rightReach = -1;
                for (int left = 0; left < widthUpper; left++) {
                    if (left == leftLimit) {
                        break;
                    }
                    Pixel current = findPixel(entrance, depth, left);
                    if (current != null && current.goodForFloor(entrance)) {
                        leftReach = left;
                    } else {
                        leftLimit = left;
                        break;
                    }
                }
                for (int right = 0; right < widthUpper; right++) {
                    if (right == rightLimit) {
                        break;
                    }
                    Pixel current = findPixel(entrance, depth, right * -1);
                    if (current != null && current.goodForFloor(entrance)) {
                        rightReach = right;
                    } else {
                        rightLimit = right;
                        break;
                    }
                }
                if (leftReach == -1 || rightReach == -1) {
                    break;
                }
                if (depth >= depthLower) {
                    int[] array = {depth, leftReach, rightReach};
                    widthByDepth.add(array);
                }
            }

            /* To list all possible rooms.
            * For each array {depth, left, width}
             */
            List<int[]> possibleRooms = new ArrayList<>();
            for (int[] array : widthByDepth) {
                int depth = array[0];
                int leftReach = array[1];
                int rightReach = array[2];
                int span = leftReach + rightReach + 1;
                if (span < widthLower) {
                    continue;
                }
                for (int roomWidth = widthLower; roomWidth <= widthUpper; roomWidth++) {
                    if (roomWidth > span) {
                        break;
                    }
                    for (int left = roomWidth - 1; left >= 0; left--) {
                        if (left <= leftReach && roomWidth - 1 - left <= rightReach) {
                            int[] currentRoom = {depth, left, roomWidth};
                            possibleRooms.add(currentRoom);
                        }
                    }
                }
            }
            return possibleRooms;
        }

        /**
         * Return a Pixel given the starting pixel, its direction, distance along its direction, and distance towards
         * the left of its direction.
         */
        private Pixel findPixel(Pixel startingPixel, char direction, int forward, int left) {
            int indexOfForward;
            switch (direction) {
                case 'L': indexOfForward = 0; break;
                case 'U': indexOfForward = 1; break;
                case 'R': indexOfForward = 2; break;
                case 'D': indexOfForward = 3; break;
                default: indexOfForward = 0; break;
            }
            int indexOfLeft = indexOfForward - 1;
            if (indexOfLeft == -1) {
                indexOfLeft = 3;
            }
            int[] dx = {-1, 0, 1, 0};
            int[] dy = {0, 1, 0, -1};
            int x = startingPixel.x + dx[indexOfForward] * forward + dx[indexOfLeft] * left;
            int y = startingPixel.y + dy[indexOfForward] * forward + dy[indexOfLeft] * left;
            if (x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) {
                return null;
            }
            return new Pixel(x, y, direction);
        }

        /**
         * Overloaded version if using the direction of the startingPixel.
         */
        private Pixel findPixel(Pixel startingPixel, int forward, int left) {
            return findPixel(startingPixel, startingPixel.direction, forward, left);
        }

        /**
         * Paints the rectangular room on the canvas.
         * Creates a Room instance, including a list for all pixels, and return it.
         * Update numIndoorTiles.
         */
        private Room paintRectRoom(Pixel entrance, int[] array) {
            Room thisRoom = new Room(entrance);
            List<Pixel> roomPixels = new ArrayList<>();
            int depth = array[0];
            int leftEnd = array[1];
            int width = array[2];
            for (int forward = 1; forward <= depth; forward++) {
                for (int i = 0; i < width; i++) {
                    int left = leftEnd - i;
                    Pixel current = findPixel(entrance, forward, left);
                    if (current != null) {
                        roomPixels.add(current);
                    }
                }
            }

            for (Pixel pixel : roomPixels) {
                pixel.paint(Tileset.FLOOR);
            }

            int numRoomPixels = depth * width;
            indoorTilesNum += numRoomPixels;

            thisRoom.pixels = roomPixels;
            return thisRoom;
            }

        /**
         * Paint wall across the whole canvas.
         * A tile is a wall when it is NOTHING and right next to a FLOOR.
         */
        private void paintWall() {
            List<Pixel> toBeWall = new ArrayList<>();
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    Pixel current = new Pixel(x, y, 'N');
                    if (current.shouldBeWall()) {
                        toBeWall.add(current);
                    }
                }
            }
            for (Pixel pixel : toBeWall) {
                pixel.paint(Tileset.WALL);
            }
        }

        /**
         * Create a hallway from the specified room.
         * Return null if the hallNum reaches max (MAXHALLNUMPERROOM).
         * Return null if the edgePixelsForHallway field of Room is empty.
         * Choose a head randomly.
         * Paint the hallway.
         */
        private Hallway createHallFromRoom(Room room) {
            List<HallHead> hallHeads = room.hallHeads();
            if (hallHeads.isEmpty() || room.hallNum >= Room.MAXHALLNUMPERROOM) {
                return null;
            }
            int randomIndex = RandomUtils.uniform(random, hallHeads.size());
            HallHead head = hallHeads.remove(randomIndex);
            Hallway newHall = paintHall(head);
            if (newHall != null) {
                room.hallNum++;
            }
            return newHall;
        }

        /**
         * Paint a Hallway and return it.
         * Choose a length randomly.
         * Update indoorTilesNum.
         */
        private Hallway paintHall(HallHead head) {
            int length = RandomUtils.uniform(random, HALLLOWER, head.extension + 2);
            Hallway hall = new Hallway(head, length);
            for (int i = 0; i < length; i++) {
                Pixel current = findPixel(head, i, 0);
                if (current != null) {
                    current.paint(Tileset.FLOOR);
                }
            }
            indoorTilesNum += length;
            return hall;
        }

        /**
         * Paint a rectangular room from the end of a hallway, and return the room.
         * Use the end of hallway as entrance.
         * Use createRectRoom(Pixel).
         * If no space for the smallest possible room, createRectRoom() returns null, this method also returns null.
         */
        private Room createRectRoomFromHall(Hallway hallway) {
            return createRectRoom(hallway.end);
        }

        /**
         * Create a hallway from the end of a hallway.
         * List all possible hallway heads from the end of the given hallway.
         * If empty, return null.
         * If not empty, randomly choose a head, and paint it.
         * Return the new Hallway.
         */
        private Hallway createHallFromHall(Hallway hallway) {
            List<HallHead> hallHeads = hallway.end.listHallHeads();
            if (hallHeads.isEmpty()) {
                return null;
            }
            int randomIndex = RandomUtils.uniform(random, hallHeads.size());
            HallHead head = hallHeads.get(randomIndex);
            return paintHall(head);
        }
    }
}

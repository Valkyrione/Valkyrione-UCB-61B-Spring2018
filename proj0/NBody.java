public class NBody {
    /**
     * Reads the radius from a file.
     */
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        int first = in.readInt();
        double second = in.readDouble();
        return second;
    }

    /**
     * Reads the Planets from a file and return an array.
     */
    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int arrayLength = in.readInt();
        double radius = in.readDouble(); // not used in this method
        Planet[] planets = new Planet[arrayLength];
        double xxPos;
        double yyPos;
        double xxVel;
        double yyVel;
        double mass;
        String imgFileName;
        for (int i = 0; i < arrayLength; i++) {
            xxPos = in.readDouble();
            yyPos = in.readDouble();
            xxVel = in.readDouble();
            yyVel = in.readDouble();
            mass = in.readDouble();
            imgFileName = in.readString();
            planets[i] = new Planet(xxPos, yyPos, xxVel, yyVel, mass, imgFileName);
        }
        return planets;
    }

    /**
     * Runs the simulator.
     */
    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]); // total time
        double dt = Double.parseDouble(args[1]); // time interval
        String filename = args[2]; // information of universe and planets
        double universeRadius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        StdDraw.setScale(0 - universeRadius, universeRadius);
        String background = "images/starfield.jpg";
        StdDraw.picture(0, 0, background, universeRadius * 2, universeRadius * 2); // draw background
        for (Planet planet : planets) {
            planet.draw();
        } // draw all planets
        StdDraw.enableDoubleBuffering(); // make it smooth. try disable it and see difference
        double timer = 0; // record the time passed
        while (timer < T) {
            timer += dt;
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for (int i = 0; i < planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            StdDraw.picture(0, 0, background, universeRadius * 2, universeRadius * 2);
            for (int i = 0; i < planets.length; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
                planets[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", universeRadius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
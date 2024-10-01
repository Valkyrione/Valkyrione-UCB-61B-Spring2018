public class Planet {
    /**
     *  Gravitational constant (G) in Newton square meters per kilogram squared.
     */
    private static final double gCons = 6.67e-11;

    /**
     * Current x position in meters.
     */
    public double xxPos;

    /**
     * Current y position in meters.
     */
    public double yyPos;

    /**
     * Current velocity in the x direction in meters per second squared.
     */
    public double xxVel;

    /**
     * Current velocity in the y direction in meters per second squared.
     */
    public double yyVel;

    /**
     * Mass in kilograms.
     */
    public double mass;

    /**
     * Filename of planet image.
     * e.g. `jupiter.gif`.
     */
    public String imgFileName;

    /**
     * Constructor with x-y position, x-y velocity, mass, and image filename.
     */
    public Planet(double xxPos, double yyPos, double xxVel, double yyVel, double mass, String imgFileName) {
        this.xxPos = xxPos;
        this.yyPos = yyPos;
        this.xxVel = xxVel;
        this.yyVel = yyVel;
        this.mass = mass;
        this.imgFileName = imgFileName;
    }

    /**
     * Constructor that makes a copy of the parameter planet.
     */
    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    /**
     * Calculates the distance in meters between this Planet and the passed Planet.
     */
    public double calcDistance(Planet other) {
        double xxDist = this.xxPos - other.xxPos;
        double yyDist = this.yyPos - other.yyPos;
        return Math.sqrt(xxDist * xxDist + yyDist * yyDist);
    }

    /**
     * Calculates the strength of the force in Newtons exerted by the passed Planet on this Planet.
     */
    public double calcForceExertedBy(Planet other) {
        double distance = this.calcDistance(other);
        return Planet.gCons * this.mass * other.mass / (distance * distance);
    }

    /**
     * Calculates the force on X-axis in Newtons exerted by the passed Planet on this Planet.
     */
    public double calcForceExertedByX(Planet other) {
        return calcForceExertedBy(other) * (other.xxPos - this.xxPos) / calcDistance(other);
    }

    /**
     * Calculates the force on Y-axis in Newtons exerted by the passed Planet on this Planet.
     */
    public double calcForceExertedByY(Planet other) {
        return calcForceExertedBy(other) * (other.yyPos - this.yyPos) / calcDistance(other);
    }

    /**
     * Calculates the net force on X-axis in Newtons exerted by an array of Planets on this Planet.
     * This Planet will be ignored if in the array.
     */
    public double calcNetForceExertedByX(Planet[] planets){
        double netForceX = 0;
        for (Planet planet : planets) {
            if (this.equals(planet)) {
                continue;
            }
            netForceX += calcForceExertedByX(planet);
        }
        return netForceX;
    }

    /**
     * Calculates the net force on Y-axis in Newtons exerted by an array of Planets on this Planet.
     * This Planet will be ignored if in the array.
     */
    public double calcNetForceExertedByY(Planet[] planets){
        double netForceY = 0;
        for (Planet planet : planets) {
            if (this.equals(planet)) {
                continue;
            }
            netForceY += calcForceExertedByY(planet);
        }
        return netForceY;
    }

    /**
     * Updates the position and velocity.
     */
    public void update(double dt, double fX, double fY) {
        double aX = fX / this.mass;
        double aY = fY / this.mass;
        this.xxVel = this.xxVel + aX * dt;
        this.yyVel = this.yyVel + aY * dt;
        this.xxPos = this.xxPos + this.xxVel * dt;
        this.yyPos = this.yyPos + this.yyVel * dt;
    }

    /**
     * Draws the Planet using StdDraw in default size.
     */
    public void draw() {
        StdDraw.picture(this.xxPos, this.yyPos, "images/" + imgFileName);
    }
}
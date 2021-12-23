package movement;

import temporal.ArrivalState;
import core.Coord;
import core.Settings;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Random Waypoint Movement with a prohibited region where nodes may not move
 * into. The polygon is defined by a *closed* (same point as first and
 * last) path, represented as a list of {@code Coord}s.
 *
 * @author teemuk
 */
public class FMIPolygon extends MapBasedMovement {

    //==========================================================================//
    // Settings
    //==========================================================================//
    /**
     * {@code true} to confine nodes inside the polygon
     */
    public static final String INVERT_SETTING = "rwpInvert";
    public static final boolean INVERT_DEFAULT = true;
    //==========================================================================//


    //==========================================================================//
    // Instance vars
    //==========================================================================//
    final List<Coord> polygon = initializePolygon();

    /**
     * Inverted, i.e., only allow nodes to move inside the polygon.
     */
    private final boolean invert;
    private int localMaxX;
    private int localMaxY;
    private boolean goToLecture = false;
    //==========================================================================//


    //==========================================================================//
    // Implementation
    //==========================================================================//

    @Override
    public Path getPath(Coord source, Coord destination, double speed) {
        Path p = new Path();
        int areaSource = getArea(source);
        int areaDestination = getArea(destination);
        if (areaSource == areaDestination) {       // move in the same finger
            p.addWaypoint(destination, speed);
            return p;
        }
        if (areaSource == -1) {      // Movement to building
            if (1 == areaDestination && areaDestination <= 13) {   // Movement to destination through main hall
                p.addWaypoint(ArrivalState.ENTRANCE_COORDS);       // Movement to building through entrance
                if (1 < areaDestination)
                    p.addWaypoint(getAreaEntrance(areaDestination));    // Movement to destination through area entrance
            }
        }
        if (areaDestination == -1) {
            //TODO move into/out of the Building via Entrance x
            p.addWaypoint(destination, speed);
            return p;
        }
        if (2 <= areaSource && areaSource <= 13) {      // Movement from some area
            p.addWaypoint(getAreaEntrance(areaSource), speed);
        }
        if (1 <= areaDestination && areaDestination <= 13) {  // Movement to another area
            p.addWaypoint(getAreaEntrance(areaDestination), speed);
        }
        p.addWaypoint(destination, speed);
        return p;
    }

    private void printPath(Path p, Coord source, Coord destination) {

        System.out.println("Path");
        System.out.println("\tSource: \t\t" + source);
        System.out.println("\tDestination: \t" + destination);
        for (Coord c : p.getCoords()) {
            System.out.println("\t\t\t\t\t" + c.toString());
        }
    }

    private Coord getAreaEntrance(int areaSource) {
        switch (areaSource) {
            case 1:
                // Main hall
            case 2:
                // Hoersaal1
                return new Coord(116, 41);
            case 3:
                // Library
                return new Coord(22, 44 - 2);
            case 4:
                return new Coord(118, 57);
            case 5:
                return new Coord(92, 36 + 1);
            case 6:
                return new Coord(104, 57 - 2);
            case 7:
                return new Coord(70, 31 + 1);
            case 8:
                return new Coord(77, 55 - 2);
            case 9:
                return new Coord(48, 31 + 2);
            case 10:
                return new Coord(60, 54 - 2);
            case 11:
                return new Coord(26, 31 + 2);
            case 12:
                return new Coord(41, 47 - 1);
            case 13:
                return new Coord(9, 34);

        }
        return null;

    }

    private int getArea(Coord position) {


        Polygon pHoersaal1Area2 = new Polygon();
        pHoersaal1Area2.addPoint(127, 46);
        pHoersaal1Area2.addPoint(130, 40);
        pHoersaal1Area2.addPoint(123, 32);
        pHoersaal1Area2.addPoint(116, 33);
        pHoersaal1Area2.addPoint(113, 36);
        pHoersaal1Area2.addPoint(127, 46);

        Polygon pFinger3 = new Polygon();
        pFinger3.addPoint(16, 39);
        pFinger3.addPoint(3, 43);
        pFinger3.addPoint(3, 64);
        pFinger3.addPoint(27, 64);
        pFinger3.addPoint(27, 46);
        pFinger3.addPoint(16, 39);

        Polygon pFinger4 = new Polygon();
        pFinger4.addPoint(120, 53);
        pFinger4.addPoint(127, 53);
        pFinger4.addPoint(123, 86);
        pFinger4.addPoint(115, 85);
        pFinger4.addPoint(116, 59);
        pFinger4.addPoint(116, 53);
        pFinger4.addPoint(120, 53);

        Polygon pFinger5 = new Polygon();
        pFinger5.addPoint(88, 25);
        pFinger5.addPoint(88, 0);
        pFinger5.addPoint(96, 0);
        pFinger5.addPoint(96, 36);
        pFinger5.addPoint(88, 36);
        pFinger5.addPoint(88, 25);


        Polygon pFinger6 = new Polygon();
        pFinger6.addPoint(97, 57);
        pFinger6.addPoint(95, 83);
        pFinger6.addPoint(103, 84);
        pFinger6.addPoint(106, 58);
        pFinger6.addPoint(97, 57);

        Polygon pFinger7 = new Polygon();
        pFinger7.addPoint(66, 31);
        pFinger7.addPoint(66, 0);
        pFinger7.addPoint(74, 0);
        pFinger7.addPoint(74, 25);
        pFinger7.addPoint(74, 31);
        pFinger7.addPoint(66, 31);

        Polygon pFinger8 = new Polygon();
        pFinger8.addPoint(77, 55);
        pFinger8.addPoint(86, 55);
        pFinger8.addPoint(83, 82);
        pFinger8.addPoint(75, 81);
        pFinger8.addPoint(77, 55);

        Polygon pFinger9 = new Polygon();
        pFinger9.addPoint(44, 31);
        pFinger9.addPoint(44, 0);
        pFinger9.addPoint(53, 0);
        pFinger9.addPoint(53, 31);
        pFinger9.addPoint(44, 31);

        Polygon pFinger10 = new Polygon();
        pFinger10.addPoint(65, 54);
        pFinger10.addPoint(63, 81);
        pFinger10.addPoint(54, 80);
        pFinger10.addPoint(56, 69);
        pFinger10.addPoint(56, 52);
        pFinger10.addPoint(65, 54);

        Polygon pFinger11 = new Polygon();
        pFinger11.addPoint(22, 31);
        pFinger11.addPoint(22, 0);
        pFinger11.addPoint(31, 0);
        pFinger11.addPoint(31, 31);
        pFinger11.addPoint(22, 31);


        Polygon pFinger12 = new Polygon();
        pFinger12.addPoint(37, 47);
        pFinger12.addPoint(34, 78);
        pFinger12.addPoint(42, 79);
        pFinger12.addPoint(45, 57);
        pFinger12.addPoint(45, 49);
        pFinger12.addPoint(37, 47);

        Polygon pFinger13 = new Polygon();
        pFinger13.addPoint(0, 0);
        pFinger13.addPoint(9, 0);
        pFinger13.addPoint(9, 31);
        pFinger13.addPoint(16, 31);
        pFinger13.addPoint(16, 39);
        pFinger13.addPoint(0, 39);
        pFinger13.addPoint(0, 0);

        Polygon pBuilding = new Polygon();
        for (Coord c : polygon) {
            pBuilding.addPoint((int) c.getX(), (int) c.getY());
        }

        if (!pBuilding.contains(position.getX(), position.getY())) {      // not in building
            return -1;
        }
        if (pHoersaal1Area2.contains(position.getX(), position.getY())) {
            return 2;
        }
        if (pFinger3.contains(position.getX(), position.getY())) {
            return 3;
        }
        if (pFinger4.contains(position.getX(), position.getY())) {
            return 4;
        }
        if (pFinger5.contains(position.getX(), position.getY())) {
            return 5;
        }
        if (pFinger6.contains(position.getX(), position.getY())) {
            return 6;
        }
        if (pFinger7.contains(position.getX(), position.getY())) {
            return 7;
        }
        if (pFinger8.contains(position.getX(), position.getY())) {
            return 8;
        }
        if (pFinger9.contains(position.getX(), position.getY())) {
            return 9;
        }
        if (pFinger10.contains(position.getX(), position.getY())) {
            return 10;
        }
        if (pFinger11.contains(position.getX(), position.getY())) {
            return 11;
        }
        if (pFinger12.contains(position.getX(), position.getY())) {
            return 12;
        }
        if (pFinger13.contains(position.getX(), position.getY())) {
            return 14;
        }
        return 0; // Somewhere else in the building
    }

    public MovementVector getPath(Coord destination, double speed) {
        Coord c = destination;
        this.lastWaypoint = c;
        return new MovementVector(c, speed);
    }

    @Override
    public MapBasedMovement replicate() {
        return new FMIPolygon(this);
    }

    public Coord randomCoord() {

        Coord c;
        do {
            c = new Coord(
                    rng.nextDouble() * super.getMaxX(),
                    rng.nextDouble() * super.getMaxY());
        } while (pathIntersects(this.polygon, this.lastWaypoint, c));
        return c;
    }

    @Override
    public double nextPathAvailable() {
        final double curTime = core.SimClock.getTime();
        return curTime;
    }
    //==========================================================================//


    //==========================================================================//
    // API
    //==========================================================================//
    public FMIPolygon(final Settings settings) {
        super(settings);
        // Read the invert setting
        this.invert = settings.getBoolean(INVERT_SETTING, INVERT_DEFAULT);
    }

    public FMIPolygon(final FMIPolygon other) {
        // Copy constructor will be used when settings up nodes. Only one
        // prototype node instance in a group is created using the Settings
        // passing constructor, the rest are replicated from the prototype.
        super(other);
        // Remember to copy any state defined in this class.
        this.invert = other.invert;
    }
    //==========================================================================//


    //==========================================================================//
    // Polygon Tools
    //==========================================================================//
    private static List<Coord> initializePolygon() {

        List<Coord> polygon = Arrays.asList(
                new Coord(107, -36),
                new Coord(96, -36),
                new Coord(96, -0),
                new Coord(88, -0),
                new Coord(88, -25),
                new Coord(74, -25),
                new Coord(74, -0),
                new Coord(66, -0),
                new Coord(66, -31),
                new Coord(53, -31),
                new Coord(53, -0),
                new Coord(44, -0),
                new Coord(44, -31),
                new Coord(31, -31),
                new Coord(31, -0),
                new Coord(22, -0),
                new Coord(22, -31),
                new Coord(9, -31),
                new Coord(9, -0),
                new Coord(0, -0),
                new Coord(0, -39),
                new Coord(16, -39),
                new Coord(16, -43),

                new Coord(3, -43),
                new Coord(3, -64),
                new Coord(27, -64),
                new Coord(27, -46),
                new Coord(37, -47),
                new Coord(34, -78),
                new Coord(42, -79),
                new Coord(45, -57),
                new Coord(56, -59),
                new Coord(54, -80),
                new Coord(63, -81),
                new Coord(65, -54),
                new Coord(77, -55),
                new Coord(75, -81),
                new Coord(83, -82),
                new Coord(86, -55),
                new Coord(89, -60),
                new Coord(93, -60),
                new Coord(97, -57),
                new Coord(95, -83),
                new Coord(103, -84),
                new Coord(106, -58),

                new Coord(110, -61),
                new Coord(113, -61),
                new Coord(116, -59),
                new Coord(115, -85),
                new Coord(123, -86),
                new Coord(127, -53),
                new Coord(120, -53),
                new Coord(120, -50),
                new Coord(127, -46),
                new Coord(130, -40),
                new Coord(130, -35),
                new Coord(123, -32),
                new Coord(116, -33),
                new Coord(113, -36),
                new Coord(107, -36)
        );

        polygon = mirrorPolygon(polygon);
        Coord minBound = setBoundsOfPolygon(polygon, true);
        polygon = translatePolygon(polygon, -minBound.getX(), -minBound.getY());

        return polygon;
    }


    private static List<Coord> mirrorPolygon(List<Coord> pol) {
        for (Coord c : pol) {
            c.setLocation(c.getX(), -c.getY());
        }

        return pol;
    }

    private static Coord setBoundsOfPolygon(List<Coord> pol, boolean returnMinimum) {
        double minX, minY, maxX, maxY;
        minX = minY = Double.MAX_VALUE;
        maxX = maxY = -Double.MAX_VALUE;

        for (Coord c : pol) {

            if (c.getX() < minX) {
                minX = c.getX();
            }
            if (c.getX() > maxX) {
                maxX = c.getX();
            }
            if (c.getY() < minY) {
                minY = c.getY();
            }
            if (c.getY() > maxY) {
                maxY = c.getY();
            }
        }

        if (returnMinimum)
            return new Coord(minX, minY);
        else
            return new Coord(maxX, maxY);
    }

    private static List<Coord> translatePolygon(List<Coord> pol, double dx, double dy) {
        for (Coord c : pol) {
            c.translate(dx, dy);
        }

        return pol;
    }


    private void setLocalMaxima() {
        Coord maxBound = setBoundsOfPolygon(polygon, false);
        this.localMaxX = (int) Math.round(maxBound.getX());
        this.localMaxY = (int) Math.round(maxBound.getY());
    }
    //==========================================================================//


    //==========================================================================//
    // Private - geometry
    //==========================================================================//


    public boolean pathIntersects(Coord start, Coord end) {
        return pathIntersects(this.polygon, start, end);
    }

    private static boolean pathIntersects(
            final List<Coord> polygon,
            final Coord start,
            final Coord end) {
        final int count = countIntersectedEdges(polygon, start, end);
        return (count > 0);
    }

    private static boolean isInside(
            final List<Coord> polygon,
            final Coord point) {
        final int count = countIntersectedEdges(polygon, point,
                new Coord(-10, 0));
        return ((count % 2) != 0);
    }

    private static boolean isOutside(
            final List<Coord> polygon,
            final Coord point) {
        return !isInside(polygon, point);
    }

    private static int countIntersectedEdges(
            final List<Coord> polygon,
            final Coord start,
            final Coord end) {
        int count = 0;
        for (int i = 0; i < polygon.size() - 1; i++) {
            final Coord polyP1 = polygon.get(i);
            final Coord polyP2 = polygon.get(i + 1);

            final Coord intersection = intersection(start, end, polyP1, polyP2);
            if (intersection == null) continue;

            if (isOnSegment(polyP1, polyP2, intersection)
                    && isOnSegment(start, end, intersection)) {
                count++;
            }
        }
        return count;
    }

    private static boolean isOnSegment(
            final Coord L0,
            final Coord L1,
            final Coord point) {
        final double crossProduct
                = (point.getY() - L0.getY()) * (L1.getX() - L0.getX())
                - (point.getX() - L0.getX()) * (L1.getY() - L0.getY());
        if (Math.abs(crossProduct) > 0.0000001) return false;

        final double dotProduct
                = (point.getX() - L0.getX()) * (L1.getX() - L0.getX())
                + (point.getY() - L0.getY()) * (L1.getY() - L0.getY());
        if (dotProduct < 0) return false;

        final double squaredLength
                = (L1.getX() - L0.getX()) * (L1.getX() - L0.getX())
                + (L1.getY() - L0.getY()) * (L1.getY() - L0.getY());
        if (dotProduct > squaredLength) return false;

        return true;
    }

    private static Coord intersection(
            final Coord L0_p0,
            final Coord L0_p1,
            final Coord L1_p0,
            final Coord L1_p1) {
        final double[] p0 = getParams(L0_p0, L0_p1);
        final double[] p1 = getParams(L1_p0, L1_p1);
        final double D = p0[1] * p1[0] - p0[0] * p1[1];
        if (D == 0.0) return null;

        final double x = (p0[2] * p1[1] - p0[1] * p1[2]) / D;
        final double y = (p0[2] * p1[0] - p0[0] * p1[2]) / D;

        return new Coord(x, y);
    }

    private static double[] getParams(
            final Coord c0,
            final Coord c1) {
        final double A = c0.getY() - c1.getY();
        final double B = c0.getX() - c1.getX();
        final double C = c0.getX() * c1.getY() - c0.getY() * c1.getX();
        return new double[]{A, B, C};
    }
    //==========================================================================//
}

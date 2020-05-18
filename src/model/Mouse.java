package model;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.PI;

public class Mouse {
    // Constants
    private static final double TAU = 2 * PI;
    private static final double MOUSE_STEP = 0.001;
    private static final double SPEED_FACTOR = 4.0;
    private static final double CAT_STEP = SPEED_FACTOR * MOUSE_STEP;
    private static final ThreadLocalRandom RNG = ThreadLocalRandom.current();

    // State variables
    private double x;
    private double y;
    private double catAngle;
    private double catDirection; // 1.0 for increasing angle, -1.0 for decreasing angle

    public Mouse() {
        initialize();
    }

    public void optimalMove() {
        double cashedX = x;
        double cashedY = y;
        double cashedCatAlpha = catAngle;
        double cashedCatDir = catDirection;
        double minCost = 1.0;
        double optimalDirection = 0.0;

        int numAngles = 720;
        for (double direction = 0; direction < TAU; direction += TAU / numAngles) {
            double cost = move(direction);
            if (cost < minCost) {
                optimalDirection = direction;
                minCost = cost;
            }
            x = cashedX;
            y = cashedY;
            catAngle = cashedCatAlpha;
            catDirection = cashedCatDir;
        }
        move(optimalDirection);
    }

    private double move(double dir) {
        x += Math.cos(dir) * MOUSE_STEP;
        y += Math.sin(dir) * MOUSE_STEP;
        double mouseAngle = Math.atan2(y, x);
        double alpha = angleDiff(catAngle, mouseAngle); // 0 <= alpha < TWO_PI
        if (PI - 0.001 < alpha && alpha < PI + 0.001) {
            catAngle += catDirection * CAT_STEP;
        } else if (CAT_STEP < alpha && alpha <= PI) {
            catDirection = 1.0;
            catAngle += CAT_STEP;
        } else if (PI < alpha && alpha < TAU - CAT_STEP) {
            catDirection = -1.0;
            catAngle -= CAT_STEP;
        } else {
            catAngle = mouseAngle;
        }
        alpha = angleDiff(catAngle, mouseAngle);
        return getCost(alpha);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getCatX() {
        return Math.cos(catAngle);
    }

    public double getCatY() {
        return Math.sin(catAngle);
    }

    public double getDistanceToEdge() {
        return 1.0 - Math.sqrt(x * x + y * y);
    }

    private double getCost(double alpha) {
        alpha = alpha > PI ? TAU - alpha : alpha; // 0 <= alpha <= PI
        return getDistanceToEdge() - alpha / SPEED_FACTOR;
    }

    /**
     * @return angle from alpha to beta as a value between 0 (inclusive) and 2 * PI (exclusive)
     */
    private double angleDiff(double alpha, double beta) {
        double gamma = (beta - alpha) % TAU;
        return gamma < 0.0 ? gamma + TAU : gamma;
    }

    public void initialize() {
        x = 0.0;
        y = 0.0;
        catAngle = PI / 2; // South
        catDirection = RNG.nextBoolean() ? 1.0 : -1.0;
    }
}

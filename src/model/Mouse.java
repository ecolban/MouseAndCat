package model;

import static java.lang.Math.PI;

public class Mouse {
    // Constants
    private static double TWO_PI = 2 * PI;
    private static double MOUSE_SPEED = 0.001;
    private static double SPEED_FACTOR = 4.0;
    private static double CAT_SPEED = SPEED_FACTOR * MOUSE_SPEED;

    // State variables
    private double x = 0.0;
    private double y = 0.0;
    private double catAngle = PI / 2.0; // = South
    private double catDirection = 1.0; // 1.0 for increasing angle, -1.0 for decreasing angle.

    public void optimalMove() {
        double cashedX = x;
        double cashedY = y;
        double cashedCatAlpha = catAngle;
        double cashedCatDir = catDirection;
        double minCost = 1.0;
        double optimalDirection = 0.0;

        int numAngles = 360;
        for (double direction = 0; direction < TWO_PI; direction += TWO_PI / numAngles) {
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
        x += Math.cos(dir) * MOUSE_SPEED;
        y += Math.sin(dir) * MOUSE_SPEED;
        double mouseAngle = Math.atan2(y, x);
        double alpha = angleDiff(catAngle, mouseAngle); // 0 <= alpha < TWO_PI
        if (PI - CAT_SPEED < alpha && alpha < PI + CAT_SPEED) {
            catAngle += catDirection * CAT_SPEED;
        } else if (CAT_SPEED < alpha && alpha <= PI) {
            catDirection = 1.0;
            catAngle += CAT_SPEED;
        } else if (PI < alpha && alpha < TWO_PI - CAT_SPEED) {
            catDirection = -1.0;
            catAngle -= CAT_SPEED;
        } else {
            catAngle = mouseAngle;
        }
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
        alpha = alpha > PI ? TWO_PI - alpha : alpha; // 0 <= alpha <= PI
        return getDistanceToEdge() - alpha / SPEED_FACTOR;
    }

    /**
     * @return angle from alpha to beta as a value between 0 (inclusive) and 2 * PI (exclusive)
     */
    private double angleDiff(double alpha, double beta) {
        double gamma = (beta - alpha) % TWO_PI;
        return gamma < 0.0 ? gamma + TWO_PI : gamma;
    }
}

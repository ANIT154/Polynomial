import java.util.Arrays;

import components.polynomial.Polynomial;
import components.polynomial.Polynomial1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Finds the real roots and turning points of polynomials numerically, using
 * Newton's method.
 *
 * <p>
 * Newton's method improves a guess {@code x} for a root of {@code f} by
 * repeatedly replacing it with {@code x - f(x) / f'(x)}. The
 * {@code Polynomial} component supplies everything this needs:
 * {@code evaluate} for {@code f(x)} and {@code differentiate} for
 * {@code f'}. Applying the same method to {@code f'} finds the turning points
 * of {@code f}, and the sign of {@code f''} classifies them.
 * </p>
 *
 * <p>
 * Use case: the {@code Polynomial} component used directly by a client
 * program as a mathematical function (contrast with
 * {@code DiceDistribution}, which hides a polynomial inside another class).
 * </p>
 */
public final class RootFinder {

    /**
     * Two numbers closer than this are treated as equal.
     */
    private static final double TOLERANCE = 1e-9;

    /**
     * Upper bound on Newton iterations from a single starting guess.
     */
    private static final int MAX_ITERATIONS = 100;

    /**
     * Distance between starting guesses when scanning an interval.
     */
    private static final double GUESS_SPACING = 0.5;

    /**
     * Largest number of roots a scan can report (more than the degree of any
     * polynomial in this demo).
     */
    private static final int MAX_ROOTS = 20;

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private RootFinder() {
    }

    /**
     * Returns a new polynomial equal to {@code p}.
     *
     * @param p
     *            the polynomial to copy
     * @return a copy of {@code p}
     * @ensures copyOf = p
     */
    private static Polynomial copyOf(Polynomial p) {
        Polynomial copy = p.newInstance();
        copy.add(p);
        return copy;
    }

    /**
     * Returns the derivative of {@code p}, leaving {@code p} unchanged.
     *
     * @param p
     *            the polynomial to differentiate
     * @return d/dx(p)
     * @ensures derivativeOf = d/dx(p)
     */
    private static Polynomial derivativeOf(Polynomial p) {
        Polynomial derivative = copyOf(p);
        derivative.differentiate();
        return derivative;
    }

    /**
     * Builds the monic polynomial {@code (x - r1)(x - r2)...(x - rn)} with
     * the given roots.
     *
     * @param roots
     *            the roots of the polynomial to build
     * @return the product of {@code (x - r)} for every {@code r} in
     *         {@code roots}
     * @requires roots is not null
     * @ensures [fromRoots has exactly the given roots, with multiplicity]
     */
    private static Polynomial fromRoots(int[] roots) {
        Polynomial result = new Polynomial1L(new int[] {1});
        for (int root : roots) {
            result.multiply(new Polynomial1L(new int[] {-root, 1}));
        }
        return result;
    }

    /**
     * Runs Newton's method on {@code p} from {@code initialGuess}.
     *
     * @param p
     *            the polynomial whose root is sought
     * @param derivative
     *            the derivative of {@code p}
     * @param initialGuess
     *            where the search starts
     * @return a root of {@code p}, or {@code Double.NaN} if the search did not
     *         converge
     * @requires derivative = d/dx(p)
     * @ensures <pre>
     * newtonsMethod = Double.NaN or |p(newtonsMethod)| <= TOLERANCE
     * </pre>
     */
    private static double newtonsMethod(Polynomial p, Polynomial derivative,
            double initialGuess) {
        double x = initialGuess;
        int iterations = 0;
        while (Math.abs(p.evaluate(x)) > TOLERANCE
                && iterations < MAX_ITERATIONS) {
            double slope = derivative.evaluate(x);
            if (slope == 0.0) {
                /*
                 * Flat spot: nudge off it instead of dividing by zero
                 */
                x += GUESS_SPACING;
            } else {
                x -= p.evaluate(x) / slope;
            }
            iterations++;
        }
        double result = Double.NaN;
        if (Math.abs(p.evaluate(x)) <= TOLERANCE) {
            result = x;
        }
        return result;
    }

    /**
     * Finds the distinct real roots of {@code p} in [{@code low},
     * {@code high}] by starting Newton's method at evenly spaced guesses.
     *
     * @param p
     *            the polynomial whose roots are sought
     * @param low
     *            the left end of the interval
     * @param high
     *            the right end of the interval
     * @return the distinct roots found, in increasing order
     * @requires low <= high and p is not the zero polynomial
     * @ensures [every value returned is a root of p in [low, high]]
     */
    private static double[] findRoots(Polynomial p, double low, double high) {
        assert !p.isZero() : "Violation of: p is not the zero polynomial";

        Polynomial derivative = derivativeOf(p);
        double[] found = new double[MAX_ROOTS];
        int count = 0;
        for (double guess = low; guess <= high; guess += GUESS_SPACING) {
            double root = newtonsMethod(p, derivative, guess);
            if (!Double.isNaN(root) && root >= low - TOLERANCE
                    && root <= high + TOLERANCE && count < MAX_ROOTS) {
                boolean isNew = true;
                for (int i = 0; i < count; i++) {
                    if (Math.abs(found[i] - root) < Math.sqrt(TOLERANCE)) {
                        isNew = false;
                    }
                }
                if (isNew) {
                    found[count] = root;
                    count++;
                }
            }
        }
        double[] roots = Arrays.copyOf(found, count);
        Arrays.sort(roots);
        return roots;
    }

    /**
     * Prints the roots of {@code p} and its turning points, classified as
     * local minima or maxima.
     *
     * @param out
     *            the output stream
     * @param p
     *            the polynomial to analyze
     * @param low
     *            the left end of the interval to search
     * @param high
     *            the right end of the interval to search
     * @updates out.content
     * @requires out.is_open and low <= high and p is not zero
     * @ensures out.content = #out.content * [the analysis of p]
     */
    private static void analyze(SimpleWriter out, Polynomial p, double low,
            double high) {
        out.println("f(x)   = " + p);
        Polynomial first = derivativeOf(p);
        Polynomial second = derivativeOf(first);
        out.println("f'(x)  = " + first);
        out.println("f''(x) = " + second);

        double[] roots = findRoots(p, low, high);
        out.print("  roots:");
        if (roots.length == 0) {
            out.print(" none in [" + low + ", " + high + "]");
        }
        for (double root : roots) {
            out.print(String.format("  %.6f", root));
        }
        out.println();

        if (!first.isZero()) {
            double[] critical = findRoots(first, low, high);
            for (double x : critical) {
                double curvature = second.evaluate(x);
                String kind = "inflection or flat point";
                if (curvature > TOLERANCE) {
                    kind = "local minimum";
                } else if (curvature < -TOLERANCE) {
                    kind = "local maximum";
                }
                out.println(String.format("  %s at x = %.6f, f(x) = %.6f",
                        kind, x, p.evaluate(x)));
            }
        }
        out.println();
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments (not used)
     */
    public static void main(String[] args) {
        final double searchLow = -10.0;
        final double searchHigh = 10.0;
        final int ten = 10;
        SimpleWriter out = new SimpleWriter1L();

        /*
         * 1. Round trip: build (x - 1)(x + 2)(x - 3) by multiplying factors,
         * then recover its roots numerically from the expanded form
         */
        out.println("== Round trip: expand (x - 1)(x + 2)(x - 3) ==");
        analyze(out, fromRoots(new int[] {1, -2, 3}), searchLow, searchHigh);

        /*
         * 2. Irrational root: x^2 - 2 has root sqrt(2), so Newton's method
         * computes square roots
         */
        out.println("== Square root of 2 as a root of x^2 - 2 ==");
        Polynomial sqrtTwo = new Polynomial1L(new int[] {-2, 0, 1});
        analyze(out, sqrtTwo, 0.0, searchHigh);
        out.println(String.format("  Math.sqrt(2) = %.6f", Math.sqrt(2.0)));
        out.println();

        /*
         * 3. Optimization: a box is made from a 10 x 10 sheet by cutting
         * squares of side x from each corner. Its volume is
         * V(x) = x(10 - 2x)^2 = 4x^3 - 40x^2 + 100x. Build V from its
         * factors, then find the cut that maximizes the volume.
         */
        out.println("== Box with largest volume from a 10 x 10 sheet ==");
        Polynomial side = new Polynomial1L(new int[] {ten, -2});
        Polynomial volume = new Polynomial1L(new int[] {0, 1});
        volume.multiply(side);
        volume.multiply(side);
        analyze(out, volume, 0.0, ten / 2.0);

        /*
         * 4. No real roots: x^2 + 1 never crosses zero
         */
        out.println("== x^2 + 1 has no real roots ==");
        analyze(out, new Polynomial1L(new int[] {1, 0, 1}), searchLow,
                searchHigh);

        out.close();
    }

}

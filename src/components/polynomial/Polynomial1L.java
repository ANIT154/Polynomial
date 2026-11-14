package components.polynomial;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * {@code Polynomial} represented as a {@link java.util.TreeMap} from exponent
 * to coefficient, with implementations of primary methods.
 *
 * <p>
 * A sorted map was chosen because it stores only the non-zero terms (so a
 * sparse polynomial like {@code x^1000 + 1} costs two entries, not 1001), it
 * gives O(log n) lookup and update of a single term, and its largest key is
 * exactly the degree, which {@code lastKey()} reports in O(log n).
 * </p>
 *
 * @convention <pre>
 * [every key in $this.terms is >= 0] and
 * [every value in $this.terms is /= 0]
 * </pre>
 * @correspondence <pre>
 * this = {(e, c) : $this.terms maps e to c}
 * (equivalently, this is the polynomial
 *  sum over all (e, c) in $this.terms of c * x^e)
 * </pre>
 */
public class Polynomial1L extends PolynomialSecondary {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Map from each exponent with a non-zero coefficient to that coefficient.
     */
    private NavigableMap<Integer, Integer> terms;

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        this.terms = new TreeMap<>();
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor: creates the zero polynomial.
     */
    public Polynomial1L() {
        this.createNewRep();
    }

    /**
     * Constructor from an array of coefficients, where
     * {@code coefficients[e]} is the coefficient of {@code x^e}. For example,
     * {@code new Polynomial1L(new int[] {5, -1, 3})} creates
     * {@code 3x^2 - x + 5}. The array is copied, not aliased.
     *
     * @param coefficients
     *            the coefficients, from the constant term upward
     * @requires coefficients is not null
     * @ensures <pre>
     * this = {(e, coefficients[e]) :
     *         0 <= e < |coefficients| and coefficients[e] /= 0}
     * </pre>
     */
    public Polynomial1L(int[] coefficients) {
        assert coefficients != null : "Violation of: coefficients is not null";

        this.createNewRep();
        for (int exponent = 0; exponent < coefficients.length; exponent++) {
            if (coefficients[exponent] != 0) {
                this.terms.put(exponent, coefficients[exponent]);
            }
        }
    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @Override
    public final Polynomial newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(Polynomial source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Polynomial1L : ""
                + "Violation of: source is of dynamic type Polynomial1L";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case
         */
        Polynomial1L localSource = (Polynomial1L) source;
        this.terms = localSource.terms;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void setCoefficient(int exponent, int coefficient) {
        assert exponent >= 0 : "Violation of: exponent >= 0";

        /*
         * Removing zero coefficients (rather than storing them) is what keeps
         * the convention "every value is /= 0" true
         */
        if (coefficient == 0) {
            this.terms.remove(exponent);
        } else {
            this.terms.put(exponent, coefficient);
        }
    }

    @Override
    public final int coefficient(int exponent) {
        assert exponent >= 0 : "Violation of: exponent >= 0";

        return this.terms.getOrDefault(exponent, 0);
    }

    @Override
    public final int degree() {
        int degree = 0;
        if (!this.terms.isEmpty()) {
            degree = this.terms.lastKey();
        }
        return degree;
    }

}

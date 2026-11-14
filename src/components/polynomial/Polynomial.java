package components.polynomial;

/**
 * {@code PolynomialKernel} enhanced with secondary methods.
 *
 * <p>
 * In the contracts below, {@code p(x)} denotes the value of polynomial
 * {@code p} at {@code x}, and {@code +}, {@code -}, and {@code *} on
 * polynomials denote the usual polynomial arithmetic.
 * </p>
 */
public interface Polynomial extends PolynomialKernel {

    /**
     * Reports whether {@code this} is the zero polynomial.
     *
     * @return true iff {@code this} has no terms
     * @ensures isZero = (this = {})
     */
    boolean isZero();

    /**
     * Reports the coefficient of the highest-degree term of {@code this}.
     *
     * @return the leading coefficient of {@code this}, or 0 if {@code this}
     *         is the zero polynomial
     * @ensures <pre>
     * if this = {}
     *  then leadingCoefficient = 0
     *  else [leadingCoefficient is the coefficient of the term of this with
     *        the largest exponent]
     * </pre>
     */
    int leadingCoefficient();

    /**
     * Adds {@code p} to {@code this}.
     *
     * @param p
     *            the polynomial to add
     * @updates this
     * @ensures this = #this + p
     */
    void add(Polynomial p);

    /**
     * Subtracts {@code p} from {@code this}.
     *
     * @param p
     *            the polynomial to subtract
     * @updates this
     * @ensures this = #this - p
     */
    void subtract(Polynomial p);

    /**
     * Multiplies every coefficient of {@code this} by {@code factor}.
     *
     * @param factor
     *            the constant to multiply by
     * @updates this
     * @ensures this = factor * #this
     */
    void scale(int factor);

    /**
     * Multiplies {@code this} by {@code p}.
     *
     * @param p
     *            the polynomial to multiply by
     * @updates this
     * @ensures this = #this * p
     */
    void multiply(Polynomial p);

    /**
     * Evaluates {@code this} at {@code x}.
     *
     * @param x
     *            the point at which to evaluate {@code this}
     * @return the value of {@code this} at {@code x}
     * @ensures evaluate = this(x)
     */
    double evaluate(double x);

    /**
     * Replaces {@code this} with its derivative with respect to {@code x}.
     * Each term {@code c * x^e} with {@code e > 0} becomes
     * {@code (c * e) * x^(e - 1)}, and the constant term is dropped.
     *
     * @updates this
     * @ensures this = d/dx(#this)
     */
    void differentiate();

}

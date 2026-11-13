package components.polynomial;

/**
 * Layered implementations of secondary methods for {@code Polynomial}.
 *
 * <p>
 * Every method in this class is written using only the kernel methods
 * ({@code setCoefficient}, {@code coefficient}, {@code degree}) and the
 * {@code Standard} methods ({@code clear}, {@code newInstance},
 * {@code transferFrom}), so it works unchanged on top of any kernel
 * implementation.
 * </p>
 */
public abstract class PolynomialSecondary implements Polynomial {

    /*
     * Private constants ------------------------------------------------------
     */

    /**
     * Multiplier used to combine coefficients in {@code hashCode}.
     */
    private static final int HASH_MULTIPLIER = 31;

    /*
     * Common methods (from Object) -------------------------------------------
     */

    @Override
    public final boolean equals(Object obj) {
        boolean result = false;
        if (obj == this) {
            result = true;
        } else if (obj instanceof Polynomial) {
            Polynomial other = (Polynomial) obj;
            int degree = this.degree();
            if (degree == other.degree()) {
                result = true;
                int exponent = 0;
                while (result && exponent <= degree) {
                    result = this.coefficient(exponent) == other
                            .coefficient(exponent);
                    exponent++;
                }
            }
        }
        return result;
    }

    @Override
    public final int hashCode() {
        int result = 0;
        int degree = this.degree();
        for (int exponent = 0; exponent <= degree; exponent++) {
            result = HASH_MULTIPLIER * result + this.coefficient(exponent);
        }
        return result;
    }

    @Override
    public final String toString() {
        StringBuilder result = new StringBuilder();
        for (int exponent = this.degree(); exponent >= 0; exponent--) {
            int coefficient = this.coefficient(exponent);
            if (coefficient != 0) {
                /*
                 * Sign: a leading "-" for the first term, otherwise a spaced
                 * " + " or " - " between terms
                 */
                if (result.length() == 0) {
                    if (coefficient < 0) {
                        result.append('-');
                    }
                } else if (coefficient < 0) {
                    result.append(" - ");
                } else {
                    result.append(" + ");
                }
                /*
                 * Magnitude: omitted when it is 1, unless this is the constant
                 * term (widened to long so that Integer.MIN_VALUE is safe)
                 */
                long magnitude = Math.abs((long) coefficient);
                if (magnitude != 1 || exponent == 0) {
                    result.append(magnitude);
                }
                /*
                 * Variable and exponent
                 */
                if (exponent >= 1) {
                    result.append('x');
                }
                if (exponent >= 2) {
                    result.append('^').append(exponent);
                }
            }
        }
        if (result.length() == 0) {
            result.append('0');
        }
        return result.toString();
    }

    /*
     * Other non-kernel methods -----------------------------------------------
     */

    @Override
    public boolean isZero() {
        return this.degree() == 0 && this.coefficient(0) == 0;
    }

    @Override
    public int leadingCoefficient() {
        return this.coefficient(this.degree());
    }

    @Override
    public void add(Polynomial p) {
        assert p != null : "Violation of: p is not null";

        /*
         * The degree of p is recorded up front because, when p is this, it
         * may change as terms are updated. Each coefficient of p is read
         * before the matching term of this is written, so aliasing is safe.
         */
        int pDegree = p.degree();
        for (int exponent = 0; exponent <= pDegree; exponent++) {
            int pCoefficient = p.coefficient(exponent);
            if (pCoefficient != 0) {
                this.setCoefficient(exponent,
                        this.coefficient(exponent) + pCoefficient);
            }
        }
    }

    @Override
    public void subtract(Polynomial p) {
        assert p != null : "Violation of: p is not null";

        int pDegree = p.degree();
        for (int exponent = 0; exponent <= pDegree; exponent++) {
            int pCoefficient = p.coefficient(exponent);
            if (pCoefficient != 0) {
                this.setCoefficient(exponent,
                        this.coefficient(exponent) - pCoefficient);
            }
        }
    }

    @Override
    public void scale(int factor) {
        /*
         * When factor is 0, every term is set to 0 and therefore removed, so
         * no special case is needed
         */
        int degree = this.degree();
        for (int exponent = 0; exponent <= degree; exponent++) {
            int coefficient = this.coefficient(exponent);
            if (coefficient != 0) {
                this.setCoefficient(exponent, coefficient * factor);
            }
        }
    }

    @Override
    public void multiply(Polynomial p) {
        assert p != null : "Violation of: p is not null";

        /*
         * The product is accumulated in a separate object so that neither
         * factor changes while it is being read (this also makes
         * this.multiply(this) safe)
         */
        Polynomial product = this.newInstance();
        int thisDegree = this.degree();
        int pDegree = p.degree();
        for (int i = 0; i <= thisDegree; i++) {
            int thisCoefficient = this.coefficient(i);
            if (thisCoefficient != 0) {
                for (int j = 0; j <= pDegree; j++) {
                    int pCoefficient = p.coefficient(j);
                    if (pCoefficient != 0) {
                        int exponent = i + j;
                        product.setCoefficient(exponent,
                                product.coefficient(exponent)
                                        + thisCoefficient * pCoefficient);
                    }
                }
            }
        }
        this.transferFrom(product);
    }

    @Override
    public double evaluate(double x) {
        /*
         * Horner's method: a_n x^n + ... + a_1 x + a_0 is computed as
         * (...((a_n) x + a_(n-1)) x + ...) x + a_0
         */
        double result = 0.0;
        for (int exponent = this.degree(); exponent >= 0; exponent--) {
            result = result * x + this.coefficient(exponent);
        }
        return result;
    }

    @Override
    public void differentiate() {
        Polynomial derivative = this.newInstance();
        int degree = this.degree();
        for (int exponent = 1; exponent <= degree; exponent++) {
            int coefficient = this.coefficient(exponent);
            if (coefficient != 0) {
                derivative.setCoefficient(exponent - 1, coefficient * exponent);
            }
        }
        this.transferFrom(derivative);
    }

}

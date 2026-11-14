package components.polynomial;

import components.standard.Standard;

/**
 * Polynomial kernel component with primary methods.
 *
 * <p>
 * A polynomial is a finite sum of terms of the form {@code c * x^e}, where the
 * exponent {@code e} is a non-negative integer and the coefficient {@code c}
 * is a non-zero integer. For example, {@code 3x^2 - x + 5} has three terms:
 * (2, 3), (1, -1), and (0, 5). The zero polynomial has no terms at all.
 * </p>
 *
 * <p>
 * Coefficients are stored as {@code int}s. Clients are responsible for
 * keeping coefficient arithmetic within the range of {@code int}; behavior on
 * overflow is not specified.
 * </p>
 *
 * @mathsubtypes <pre>
 * TERM is (exponent: integer, coefficient: integer)
 *
 * POLYNOMIAL_MODEL is finite set of TERM
 *  exemplar p
 *  constraint
 *   [for every term t in p, t.exponent >= 0 and t.coefficient /= 0] and
 *   [no two terms in p have the same exponent]
 * </pre>
 * @mathmodel type PolynomialKernel is modeled by POLYNOMIAL_MODEL
 * @initially <pre>
 * ():
 *  ensures
 *   this = {}
 * </pre>
 */
public interface PolynomialKernel extends Standard<Polynomial> {

    /**
     * Sets the coefficient of the term {@code x^exponent} in {@code this}.
     * Any existing term with the same exponent is replaced. Setting a
     * coefficient of 0 removes the term with that exponent, if there is one.
     *
     * @param exponent
     *            the exponent of the term to set
     * @param coefficient
     *            the new coefficient of {@code x^exponent}
     * @updates this
     * @requires exponent >= 0
     * @ensures <pre>
     * if coefficient = 0
     *  then this = #this \ {(exponent, c) : c is an integer}
     *  else this = (#this \ {(exponent, c) : c is an integer})
     *              union {(exponent, coefficient)}
     * </pre>
     */
    void setCoefficient(int exponent, int coefficient);

    /**
     * Reports the coefficient of the term {@code x^exponent} in {@code this}.
     *
     * @param exponent
     *            the exponent of the term to look up
     * @return the coefficient of {@code x^exponent}, or 0 if {@code this} has
     *         no term with that exponent
     * @requires exponent >= 0
     * @ensures <pre>
     * if there exists c such that (exponent, c) is in this
     *  then coefficient = c
     *  else coefficient = 0
     * </pre>
     */
    int coefficient(int exponent);

    /**
     * Reports the degree of {@code this}: the largest exponent of any term.
     * By convention, the degree of the zero polynomial is reported as 0.
     *
     * @return the degree of {@code this}
     * @ensures <pre>
     * if this = {}
     *  then degree = 0
     *  else [degree is the largest exponent of any term in this]
     * </pre>
     */
    int degree();

}

package components.polynomial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JUnit test fixture for the secondary methods of {@code Polynomial} and the
 * common methods ({@code equals}, {@code hashCode}, {@code toString})
 * implemented in {@code PolynomialSecondary}.
 *
 * <p>
 * Each test builds its expected values as separate objects and compares the
 * whole polynomial with {@code assertEquals}, so a test fails if a method
 * produces the wrong result <em>or</em> disturbs an object it should have
 * restored. The {@code equals} method these comparisons depend on is itself
 * tested at the bottom of this file.
 * </p>
 */
public final class PolynomialTest {

    /**
     * Tolerance for comparing results of {@code evaluate}.
     */
    private static final double DELTA = 1e-9;

    /*
     * Helpers ----------------------------------------------------------------
     */

    /**
     * Creates a polynomial from its coefficients, constant term first. For
     * example, {@code poly(5, -1, 3)} is {@code 3x^2 - x + 5}, and
     * {@code poly()} is the zero polynomial.
     *
     * @param coefficients
     *            the coefficients, from the constant term upward
     * @return the new polynomial
     */
    private static Polynomial poly(int... coefficients) {
        return new Polynomial1L(coefficients);
    }

    /*
     * isZero -----------------------------------------------------------------
     */

    @Test
    public void testIsZeroOnZero() {
        Polynomial p = poly();
        assertTrue(p.isZero());
        assertEquals(poly(), p);
    }

    @Test
    public void testIsZeroOnNonZeroConstant() {
        Polynomial p = poly(3);
        assertFalse(p.isZero());
        assertEquals(poly(3), p);
    }

    @Test
    public void testIsZeroOnZeroConstantTermButHigherTerms() {
        Polynomial p = poly(0, 0, 1);
        assertFalse(p.isZero());
        assertEquals(poly(0, 0, 1), p);
    }

    @Test
    public void testIsZeroAfterLastTermRemoved() {
        Polynomial p = poly(0, 2);
        p.setCoefficient(1, 0);
        assertTrue(p.isZero());
    }

    /*
     * leadingCoefficient -----------------------------------------------------
     */

    @Test
    public void testLeadingCoefficientOfZero() {
        Polynomial p = poly();
        assertEquals(0, p.leadingCoefficient());
        assertEquals(poly(), p);
    }

    @Test
    public void testLeadingCoefficientOfConstant() {
        Polynomial p = poly(7);
        assertEquals(7, p.leadingCoefficient());
        assertEquals(poly(7), p);
    }

    @Test
    public void testLeadingCoefficientTypical() {
        Polynomial p = poly(0, -1, 0, 2);
        assertEquals(2, p.leadingCoefficient());
        assertEquals(poly(0, -1, 0, 2), p);
    }

    @Test
    public void testLeadingCoefficientNegative() {
        Polynomial p = poly(4, 0, -5);
        assertEquals(-5, p.leadingCoefficient());
        assertEquals(poly(4, 0, -5), p);
    }

    /*
     * add --------------------------------------------------------------------
     */

    @Test
    public void testAddZeroToZero() {
        Polynomial p = poly();
        Polynomial q = poly();
        p.add(q);
        assertEquals(poly(), p);
        assertEquals(poly(), q);
    }

    @Test
    public void testAddZeroToNonZero() {
        Polynomial p = poly(1, 2, 3);
        Polynomial q = poly();
        p.add(q);
        assertEquals(poly(1, 2, 3), p);
        assertEquals(poly(), q);
    }

    @Test
    public void testAddNonZeroToZero() {
        Polynomial p = poly();
        Polynomial q = poly(1, 2, 3);
        p.add(q);
        assertEquals(poly(1, 2, 3), p);
        assertEquals(poly(1, 2, 3), q);
    }

    @Test
    public void testAddSameDegree() {
        Polynomial p = poly(1, 2, 3);
        Polynomial q = poly(4, -5, 6);
        p.add(q);
        assertEquals(poly(5, -3, 9), p);
        assertEquals(poly(4, -5, 6), q);
    }

    @Test
    public void testAddHigherDegree() {
        Polynomial p = poly(1, 1);
        Polynomial q = poly(0, 0, 0, 2);
        p.add(q);
        assertEquals(poly(1, 1, 0, 2), p);
        assertEquals(poly(0, 0, 0, 2), q);
    }

    @Test
    public void testAddLowerDegree() {
        Polynomial p = poly(0, 0, 0, 2);
        Polynomial q = poly(1, 1);
        p.add(q);
        assertEquals(poly(1, 1, 0, 2), p);
        assertEquals(poly(1, 1), q);
    }

    @Test
    public void testAddCancelsLeadingTerm() {
        Polynomial p = poly(1, 2, 3);
        Polynomial q = poly(0, 0, -3);
        p.add(q);
        assertEquals(poly(1, 2), p);
        assertEquals(1, p.degree());
        assertEquals(poly(0, 0, -3), q);
    }

    @Test
    public void testAddCancelsToZero() {
        Polynomial p = poly(1, -2, 3);
        Polynomial q = poly(-1, 2, -3);
        p.add(q);
        assertTrue(p.isZero());
        assertEquals(poly(-1, 2, -3), q);
    }

    @Test
    public void testAddToItself() {
        Polynomial p = poly(1, 0, 3);
        p.add(p);
        assertEquals(poly(2, 0, 6), p);
    }

    /*
     * subtract ---------------------------------------------------------------
     */

    @Test
    public void testSubtractZero() {
        Polynomial p = poly(1, 2, 3);
        Polynomial q = poly();
        p.subtract(q);
        assertEquals(poly(1, 2, 3), p);
        assertEquals(poly(), q);
    }

    @Test
    public void testSubtractFromZero() {
        Polynomial p = poly();
        Polynomial q = poly(1, -2, 3);
        p.subtract(q);
        assertEquals(poly(-1, 2, -3), p);
        assertEquals(poly(1, -2, 3), q);
    }

    @Test
    public void testSubtractTypical() {
        Polynomial p = poly(5, 5, 5);
        Polynomial q = poly(1, 2, 0, 4);
        p.subtract(q);
        assertEquals(poly(4, 3, 5, -4), p);
        assertEquals(poly(1, 2, 0, 4), q);
    }

    @Test
    public void testSubtractCancelsLeadingTerm() {
        Polynomial p = poly(1, 2, 3);
        Polynomial q = poly(0, 0, 3);
        p.subtract(q);
        assertEquals(poly(1, 2), p);
        assertEquals(poly(0, 0, 3), q);
    }

    @Test
    public void testSubtractEqualIsZero() {
        Polynomial p = poly(1, 2, 3);
        Polynomial q = poly(1, 2, 3);
        p.subtract(q);
        assertTrue(p.isZero());
        assertEquals(poly(1, 2, 3), q);
    }

    @Test
    public void testSubtractItself() {
        Polynomial p = poly(1, 2, 3);
        p.subtract(p);
        assertTrue(p.isZero());
    }

    /*
     * scale ------------------------------------------------------------------
     */

    @Test
    public void testScaleZeroPolynomial() {
        Polynomial p = poly();
        p.scale(5);
        assertEquals(poly(), p);
    }

    @Test
    public void testScaleByZero() {
        Polynomial p = poly(1, 2, 3);
        p.scale(0);
        assertTrue(p.isZero());
        assertEquals(0, p.degree());
    }

    @Test
    public void testScaleByOne() {
        Polynomial p = poly(1, -2, 3);
        p.scale(1);
        assertEquals(poly(1, -2, 3), p);
    }

    @Test
    public void testScaleByNegativeOne() {
        Polynomial p = poly(1, -2, 3);
        p.scale(-1);
        assertEquals(poly(-1, 2, -3), p);
    }

    @Test
    public void testScaleTypical() {
        Polynomial p = poly(1, 0, -2, 4);
        p.scale(3);
        assertEquals(poly(3, 0, -6, 12), p);
    }

    /*
     * multiply ---------------------------------------------------------------
     */

    @Test
    public void testMultiplyByZero() {
        Polynomial p = poly(1, 2, 3);
        Polynomial q = poly();
        p.multiply(q);
        assertTrue(p.isZero());
        assertEquals(poly(), q);
    }

    @Test
    public void testMultiplyZeroByNonZero() {
        Polynomial p = poly();
        Polynomial q = poly(1, 2, 3);
        p.multiply(q);
        assertTrue(p.isZero());
        assertEquals(poly(1, 2, 3), q);
    }

    @Test
    public void testMultiplyByOne() {
        Polynomial p = poly(1, 2, 3);
        Polynomial q = poly(1);
        p.multiply(q);
        assertEquals(poly(1, 2, 3), p);
        assertEquals(poly(1), q);
    }

    @Test
    public void testMultiplyByConstant() {
        Polynomial p = poly(1, 2, 3);
        Polynomial q = poly(-2);
        p.multiply(q);
        assertEquals(poly(-2, -4, -6), p);
        assertEquals(poly(-2), q);
    }

    @Test
    public void testMultiplyByX() {
        Polynomial p = poly(1, 2, 3);
        Polynomial q = poly(0, 1);
        p.multiply(q);
        assertEquals(poly(0, 1, 2, 3), p);
        assertEquals(poly(0, 1), q);
    }

    @Test
    public void testMultiplyDifferenceOfSquares() {
        /*
         * (x + 1)(x - 1) = x^2 - 1
         */
        Polynomial p = poly(1, 1);
        Polynomial q = poly(-1, 1);
        p.multiply(q);
        assertEquals(poly(-1, 0, 1), p);
        assertEquals(poly(-1, 1), q);
    }

    @Test
    public void testMultiplyTypical() {
        /*
         * (2x^2 + 3)(x - 4) = 2x^3 - 8x^2 + 3x - 12
         */
        Polynomial p = poly(3, 0, 2);
        Polynomial q = poly(-4, 1);
        p.multiply(q);
        assertEquals(poly(-12, 3, -8, 2), p);
        assertEquals(poly(-4, 1), q);
    }

    @Test
    public void testMultiplySparse() {
        /*
         * (x^10 + 1)(x^5 - 1) = x^15 - x^10 + x^5 - 1
         */
        final int ten = 10;
        final int five = 5;
        final int fifteen = 15;
        Polynomial p = poly(1);
        p.setCoefficient(ten, 1);
        Polynomial q = poly(-1);
        q.setCoefficient(five, 1);
        p.multiply(q);
        Polynomial expected = poly(-1);
        expected.setCoefficient(five, 1);
        expected.setCoefficient(ten, -1);
        expected.setCoefficient(fifteen, 1);
        assertEquals(expected, p);
    }

    @Test
    public void testMultiplyByItself() {
        /*
         * (x + 1)^2 = x^2 + 2x + 1
         */
        Polynomial p = poly(1, 1);
        p.multiply(p);
        assertEquals(poly(1, 2, 1), p);
    }

    /*
     * evaluate ---------------------------------------------------------------
     */

    @Test
    public void testEvaluateZero() {
        Polynomial p = poly();
        assertEquals(0.0, p.evaluate(3.5), DELTA);
        assertEquals(poly(), p);
    }

    @Test
    public void testEvaluateConstant() {
        Polynomial p = poly(-7);
        assertEquals(-7.0, p.evaluate(100.0), DELTA);
        assertEquals(poly(-7), p);
    }

    @Test
    public void testEvaluateAtZeroGivesConstantTerm() {
        Polynomial p = poly(5, -1, 3);
        assertEquals(5.0, p.evaluate(0.0), DELTA);
        assertEquals(poly(5, -1, 3), p);
    }

    @Test
    public void testEvaluateTypical() {
        /*
         * 3(2)^2 - 2 + 5 = 15
         */
        Polynomial p = poly(5, -1, 3);
        assertEquals(15.0, p.evaluate(2.0), DELTA);
        assertEquals(poly(5, -1, 3), p);
    }

    @Test
    public void testEvaluateNegativeX() {
        /*
         * (-2)^3 - 1 = -9
         */
        Polynomial p = poly(-1, 0, 0, 1);
        assertEquals(-9.0, p.evaluate(-2.0), DELTA);
        assertEquals(poly(-1, 0, 0, 1), p);
    }

    @Test
    public void testEvaluateFractionalX() {
        /*
         * 4(0.5)^2 + 2(0.5) = 2
         */
        Polynomial p = poly(0, 2, 4);
        assertEquals(2.0, p.evaluate(0.5), DELTA);
        assertEquals(poly(0, 2, 4), p);
    }

    @Test
    public void testEvaluateAtRoot() {
        /*
         * x^2 - 2 has a root at sqrt(2)
         */
        Polynomial p = poly(-2, 0, 1);
        assertEquals(0.0, p.evaluate(Math.sqrt(2.0)), DELTA);
        assertEquals(poly(-2, 0, 1), p);
    }

    /*
     * differentiate ----------------------------------------------------------
     */

    @Test
    public void testDifferentiateZero() {
        Polynomial p = poly();
        p.differentiate();
        assertEquals(poly(), p);
    }

    @Test
    public void testDifferentiateConstant() {
        Polynomial p = poly(9);
        p.differentiate();
        assertTrue(p.isZero());
    }

    @Test
    public void testDifferentiateX() {
        Polynomial p = poly(0, 1);
        p.differentiate();
        assertEquals(poly(1), p);
    }

    @Test
    public void testDifferentiateLinear() {
        Polynomial p = poly(4, -3);
        p.differentiate();
        assertEquals(poly(-3), p);
    }

    @Test
    public void testDifferentiateTypical() {
        /*
         * d/dx (2x^3 - 3x^2 + 5x - 7) = 6x^2 - 6x + 5
         */
        Polynomial p = poly(-7, 5, -3, 2);
        p.differentiate();
        assertEquals(poly(5, -6, 6), p);
    }

    @Test
    public void testDifferentiateSparse() {
        /*
         * d/dx (x^4 + x) = 4x^3 + 1
         */
        Polynomial p = poly(0, 1, 0, 0, 1);
        p.differentiate();
        assertEquals(poly(1, 0, 0, 4), p);
    }

    @Test
    public void testDifferentiateTwice() {
        /*
         * d^2/dx^2 (x^3) = 6x
         */
        Polynomial p = poly(0, 0, 0, 1);
        p.differentiate();
        p.differentiate();
        assertEquals(poly(0, 6), p);
    }

    /*
     * toString ---------------------------------------------------------------
     */

    @Test
    public void testToStringZero() {
        Polynomial p = poly();
        assertEquals("0", p.toString());
        assertEquals(poly(), p);
    }

    @Test
    public void testToStringPositiveConstant() {
        Polynomial p = poly(5);
        assertEquals("5", p.toString());
    }

    @Test
    public void testToStringNegativeConstant() {
        Polynomial p = poly(-5);
        assertEquals("-5", p.toString());
    }

    @Test
    public void testToStringConstantOne() {
        Polynomial p = poly(1);
        assertEquals("1", p.toString());
    }

    @Test
    public void testToStringX() {
        Polynomial p = poly(0, 1);
        assertEquals("x", p.toString());
    }

    @Test
    public void testToStringNegativeX() {
        Polynomial p = poly(0, -1);
        assertEquals("-x", p.toString());
    }

    @Test
    public void testToStringXSquared() {
        Polynomial p = poly(0, 0, 1);
        assertEquals("x^2", p.toString());
    }

    @Test
    public void testToStringTypical() {
        Polynomial p = poly(5, -1, 3);
        assertEquals("3x^2 - x + 5", p.toString());
        assertEquals(poly(5, -1, 3), p);
    }

    @Test
    public void testToStringNegativeLeadingAndSkippedTerms() {
        Polynomial p = poly(0, 1, 0, -2);
        assertEquals("-2x^3 + x", p.toString());
    }

    @Test
    public void testToStringNegativeConstantTerm() {
        Polynomial p = poly(-1, 0, 1);
        assertEquals("x^2 - 1", p.toString());
    }

    @Test
    public void testToStringMinimumInt() {
        Polynomial p = poly(Integer.MIN_VALUE);
        assertEquals("-2147483648", p.toString());
    }

    /*
     * equals -----------------------------------------------------------------
     */

    @Test
    public void testEqualsZeroAndZero() {
        assertEquals(poly(), poly());
    }

    @Test
    public void testEqualsSameCoefficients() {
        Polynomial p = poly(5, -1, 3);
        Polynomial q = poly(5, -1, 3);
        assertTrue(p.equals(q));
        assertTrue(q.equals(p));
    }

    @Test
    public void testEqualsReflexive() {
        Polynomial p = poly(5, -1, 3);
        assertTrue(p.equals(p));
    }

    @Test
    public void testEqualsBuiltDifferently() {
        Polynomial p = poly(1, 0, 2);
        Polynomial q = poly();
        q.setCoefficient(2, 2);
        q.setCoefficient(0, 1);
        assertEquals(p, q);
    }

    @Test
    public void testEqualsDifferentCoefficient() {
        Polynomial p = poly(5, -1, 3);
        Polynomial q = poly(5, 1, 3);
        assertFalse(p.equals(q));
        assertFalse(q.equals(p));
    }

    @Test
    public void testEqualsDifferentDegree() {
        Polynomial p = poly(1, 2);
        Polynomial q = poly(1, 2, 3);
        assertFalse(p.equals(q));
        assertFalse(q.equals(p));
    }

    @Test
    public void testEqualsZeroAndNonZeroConstant() {
        assertFalse(poly().equals(poly(1)));
        assertFalse(poly(1).equals(poly()));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(poly(1, 2).equals(null));
    }

    @Test
    public void testEqualsOtherType() {
        assertFalse(poly(1, 2).equals("2x + 1"));
    }

    @Test
    public void testEqualsRestoresBoth() {
        Polynomial p = poly(5, -1, 3);
        Polynomial q = poly(5, -1, 3);
        p.equals(q);
        assertEquals("3x^2 - x + 5", p.toString());
        assertEquals("3x^2 - x + 5", q.toString());
    }

    /*
     * hashCode ---------------------------------------------------------------
     */

    @Test
    public void testHashCodeEqualObjectsHaveEqualHashes() {
        Polynomial p = poly(5, -1, 3);
        Polynomial q = poly();
        q.setCoefficient(1, -1);
        q.setCoefficient(2, 3);
        q.setCoefficient(0, 5);
        assertEquals(p.hashCode(), q.hashCode());
    }

    @Test
    public void testHashCodeZero() {
        assertEquals(poly().hashCode(), poly().hashCode());
    }

    @Test
    public void testHashCodeUsuallyDiffers() {
        assertNotEquals(poly(1, 2).hashCode(), poly(2, 1).hashCode());
    }

    @Test
    public void testHashCodeRestoresThis() {
        Polynomial p = poly(5, -1, 3);
        p.hashCode();
        assertEquals(poly(5, -1, 3), p);
    }

}

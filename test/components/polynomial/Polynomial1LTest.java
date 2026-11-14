package components.polynomial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JUnit test fixture for the constructors, kernel methods, and
 * {@code Standard} methods of {@code Polynomial1L}.
 *
 * <p>
 * There is no reference implementation to compare against, so every test
 * checks the full abstract state of each object it touches with
 * {@link #assertTerms}, which inspects the polynomial through the kernel
 * methods only. This verifies both the reported results and that any
 * parameter or receiver that should be restored really is unchanged.
 * </p>
 *
 * <p>
 * Precondition tests rely on assertions being enabled ({@code -ea}), which
 * VSCode's Java test runner does by default.
 * </p>
 */
public final class Polynomial1LTest {

    /*
     * Helpers ----------------------------------------------------------------
     */

    /**
     * Asserts that {@code actual} has exactly the coefficients in
     * {@code expected}, where {@code expected[e]} is the coefficient of
     * {@code x^e}. {@code expected} must have no trailing zeros; an empty
     * array means the zero polynomial.
     *
     * @param expected
     *            the expected coefficients, from the constant term upward
     * @param actual
     *            the polynomial to check
     */
    private static void assertTerms(int[] expected, Polynomial actual) {
        int expectedDegree = Math.max(0, expected.length - 1);
        assertEquals("degree", expectedDegree, actual.degree());
        for (int exponent = 0; exponent < expected.length; exponent++) {
            assertEquals("coefficient of x^" + exponent, expected[exponent],
                    actual.coefficient(exponent));
        }
        /*
         * Nothing above the degree, including just above it
         */
        assertEquals("coefficient above degree", 0,
                actual.coefficient(expectedDegree + 1));
        assertEquals("coefficient well above degree", 0,
                actual.coefficient(expectedDegree + 100));
    }

    /*
     * Constructor tests ------------------------------------------------------
     */

    @Test
    public void testNoArgumentConstructorIsZero() {
        Polynomial p = new Polynomial1L();
        assertTerms(new int[] {}, p);
    }

    @Test
    public void testArrayConstructorEmptyArray() {
        Polynomial p = new Polynomial1L(new int[] {});
        assertTerms(new int[] {}, p);
    }

    @Test
    public void testArrayConstructorAllZeros() {
        Polynomial p = new Polynomial1L(new int[] {0, 0, 0});
        assertTerms(new int[] {}, p);
    }

    @Test
    public void testArrayConstructorConstant() {
        Polynomial p = new Polynomial1L(new int[] {7});
        assertTerms(new int[] {7}, p);
    }

    @Test
    public void testArrayConstructorTypical() {
        Polynomial p = new Polynomial1L(new int[] {5, 0, -3, 2});
        assertTerms(new int[] {5, 0, -3, 2}, p);
    }

    @Test
    public void testArrayConstructorIgnoresTrailingZeros() {
        Polynomial p = new Polynomial1L(new int[] {1, 2, 0, 0});
        assertTerms(new int[] {1, 2}, p);
    }

    @Test
    public void testArrayConstructorDoesNotAliasArray() {
        int[] coefficients = {1, 2, 3};
        Polynomial p = new Polynomial1L(coefficients);
        coefficients[0] = 100;
        coefficients[2] = 0;
        assertTerms(new int[] {1, 2, 3}, p);
    }

    @Test(expected = AssertionError.class)
    public void testArrayConstructorRejectsNull() {
        new Polynomial1L(null);
    }

    /*
     * Kernel method tests: setCoefficient ------------------------------------
     */

    @Test
    public void testSetCoefficientConstantOnZero() {
        Polynomial p = new Polynomial1L();
        p.setCoefficient(0, 4);
        assertTerms(new int[] {4}, p);
    }

    @Test
    public void testSetCoefficientHighExponentOnZero() {
        Polynomial p = new Polynomial1L();
        p.setCoefficient(3, 2);
        assertTerms(new int[] {0, 0, 0, 2}, p);
    }

    @Test
    public void testSetCoefficientAddsNewTermBelowDegree() {
        Polynomial p = new Polynomial1L(new int[] {1, 0, 0, 1});
        p.setCoefficient(1, -6);
        assertTerms(new int[] {1, -6, 0, 1}, p);
    }

    @Test
    public void testSetCoefficientAddsNewTermAboveDegree() {
        Polynomial p = new Polynomial1L(new int[] {1, 1});
        p.setCoefficient(4, 3);
        assertTerms(new int[] {1, 1, 0, 0, 3}, p);
    }

    @Test
    public void testSetCoefficientReplacesExistingTerm() {
        Polynomial p = new Polynomial1L(new int[] {1, 2, 3});
        p.setCoefficient(1, 9);
        assertTerms(new int[] {1, 9, 3}, p);
    }

    @Test
    public void testSetCoefficientNegative() {
        Polynomial p = new Polynomial1L(new int[] {1, 2, 3});
        p.setCoefficient(2, -3);
        assertTerms(new int[] {1, 2, -3}, p);
    }

    @Test
    public void testSetCoefficientZeroRemovesMiddleTerm() {
        Polynomial p = new Polynomial1L(new int[] {1, 2, 3});
        p.setCoefficient(1, 0);
        assertTerms(new int[] {1, 0, 3}, p);
    }

    @Test
    public void testSetCoefficientZeroRemovesLeadingTermAndLowersDegree() {
        Polynomial p = new Polynomial1L(new int[] {1, 2, 0, 0, 5});
        p.setCoefficient(4, 0);
        assertTerms(new int[] {1, 2}, p);
    }

    @Test
    public void testSetCoefficientZeroRemovesOnlyTerm() {
        Polynomial p = new Polynomial1L(new int[] {0, 0, 8});
        p.setCoefficient(2, 0);
        assertTerms(new int[] {}, p);
    }

    @Test
    public void testSetCoefficientZeroOnMissingTermChangesNothing() {
        Polynomial p = new Polynomial1L(new int[] {1, 0, 3});
        p.setCoefficient(1, 0);
        p.setCoefficient(10, 0);
        assertTerms(new int[] {1, 0, 3}, p);
    }

    @Test
    public void testSetCoefficientExtremeValues() {
        Polynomial p = new Polynomial1L();
        p.setCoefficient(0, Integer.MIN_VALUE);
        p.setCoefficient(1, Integer.MAX_VALUE);
        assertTerms(new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE}, p);
    }

    @Test(expected = AssertionError.class)
    public void testSetCoefficientRejectsNegativeExponent() {
        Polynomial p = new Polynomial1L();
        p.setCoefficient(-1, 5);
    }

    /*
     * Kernel method tests: coefficient ---------------------------------------
     */

    @Test
    public void testCoefficientOfZeroPolynomial() {
        Polynomial p = new Polynomial1L();
        assertEquals(0, p.coefficient(0));
        assertEquals(0, p.coefficient(7));
        assertTerms(new int[] {}, p);
    }

    @Test
    public void testCoefficientPresentTerm() {
        Polynomial p = new Polynomial1L(new int[] {5, 0, -3, 2});
        assertEquals(-3, p.coefficient(2));
        assertTerms(new int[] {5, 0, -3, 2}, p);
    }

    @Test
    public void testCoefficientConstantTerm() {
        Polynomial p = new Polynomial1L(new int[] {5, 0, -3, 2});
        assertEquals(5, p.coefficient(0));
        assertTerms(new int[] {5, 0, -3, 2}, p);
    }

    @Test
    public void testCoefficientMissingTermBelowDegree() {
        Polynomial p = new Polynomial1L(new int[] {5, 0, -3, 2});
        assertEquals(0, p.coefficient(1));
        assertTerms(new int[] {5, 0, -3, 2}, p);
    }

    @Test
    public void testCoefficientAboveDegree() {
        Polynomial p = new Polynomial1L(new int[] {5, 0, -3, 2});
        assertEquals(0, p.coefficient(4));
        assertTerms(new int[] {5, 0, -3, 2}, p);
    }

    @Test(expected = AssertionError.class)
    public void testCoefficientRejectsNegativeExponent() {
        Polynomial p = new Polynomial1L(new int[] {1});
        p.coefficient(-1);
    }

    /*
     * Kernel method tests: degree --------------------------------------------
     */

    @Test
    public void testDegreeOfZeroPolynomial() {
        Polynomial p = new Polynomial1L();
        assertEquals(0, p.degree());
        assertTerms(new int[] {}, p);
    }

    @Test
    public void testDegreeOfNonZeroConstant() {
        Polynomial p = new Polynomial1L(new int[] {-4});
        assertEquals(0, p.degree());
        assertTerms(new int[] {-4}, p);
    }

    @Test
    public void testDegreeOfLinear() {
        Polynomial p = new Polynomial1L(new int[] {0, 1});
        assertEquals(1, p.degree());
        assertTerms(new int[] {0, 1}, p);
    }

    @Test
    public void testDegreeOfTypical() {
        Polynomial p = new Polynomial1L(new int[] {5, 0, -3, 2});
        assertEquals(3, p.degree());
        assertTerms(new int[] {5, 0, -3, 2}, p);
    }

    @Test
    public void testDegreeOfSparseHighDegree() {
        final int highExponent = 1000;
        Polynomial p = new Polynomial1L();
        p.setCoefficient(highExponent, 1);
        p.setCoefficient(0, 1);
        assertEquals(highExponent, p.degree());
        assertEquals(1, p.coefficient(highExponent));
        assertEquals(0, p.coefficient(highExponent - 1));
        assertEquals(1, p.coefficient(0));
    }

    /*
     * Standard method tests: clear -------------------------------------------
     */

    @Test
    public void testClearOnZero() {
        Polynomial p = new Polynomial1L();
        p.clear();
        assertTerms(new int[] {}, p);
    }

    @Test
    public void testClearOnNonZero() {
        Polynomial p = new Polynomial1L(new int[] {5, 0, -3, 2});
        p.clear();
        assertTerms(new int[] {}, p);
    }

    @Test
    public void testClearThenReuse() {
        Polynomial p = new Polynomial1L(new int[] {5, 0, -3, 2});
        p.clear();
        p.setCoefficient(1, 1);
        assertTerms(new int[] {0, 1}, p);
    }

    /*
     * Standard method tests: newInstance -------------------------------------
     */

    @Test
    public void testNewInstanceFromZero() {
        Polynomial p = new Polynomial1L();
        Polynomial q = p.newInstance();
        assertTerms(new int[] {}, q);
        assertTerms(new int[] {}, p);
        assertNotSame(p, q);
    }

    @Test
    public void testNewInstanceFromNonZero() {
        Polynomial p = new Polynomial1L(new int[] {5, 0, -3, 2});
        Polynomial q = p.newInstance();
        assertTerms(new int[] {}, q);
        assertTerms(new int[] {5, 0, -3, 2}, p);
        assertNotSame(p, q);
    }

    @Test
    public void testNewInstanceHasSameDynamicType() {
        Polynomial p = new Polynomial1L(new int[] {1});
        Polynomial q = p.newInstance();
        assertEquals(p.getClass(), q.getClass());
    }

    @Test
    public void testNewInstanceIsIndependent() {
        Polynomial p = new Polynomial1L(new int[] {1, 2});
        Polynomial q = p.newInstance();
        q.setCoefficient(3, 3);
        assertTerms(new int[] {1, 2}, p);
        assertTerms(new int[] {0, 0, 0, 3}, q);
    }

    /*
     * Standard method tests: transferFrom ------------------------------------
     */

    @Test
    public void testTransferFromNonZeroIntoZero() {
        Polynomial p = new Polynomial1L();
        Polynomial source = new Polynomial1L(new int[] {5, 0, -3, 2});
        p.transferFrom(source);
        assertTerms(new int[] {5, 0, -3, 2}, p);
        assertTerms(new int[] {}, source);
    }

    @Test
    public void testTransferFromZeroIntoNonZero() {
        Polynomial p = new Polynomial1L(new int[] {5, 0, -3, 2});
        Polynomial source = new Polynomial1L();
        p.transferFrom(source);
        assertTerms(new int[] {}, p);
        assertTerms(new int[] {}, source);
    }

    @Test
    public void testTransferFromNonZeroIntoNonZero() {
        Polynomial p = new Polynomial1L(new int[] {1, 1});
        Polynomial source = new Polynomial1L(new int[] {0, 0, 4});
        p.transferFrom(source);
        assertTerms(new int[] {0, 0, 4}, p);
        assertTerms(new int[] {}, source);
    }

    @Test
    public void testTransferFromLeavesNoAlias() {
        Polynomial p = new Polynomial1L();
        Polynomial source = new Polynomial1L(new int[] {1, 2});
        p.transferFrom(source);
        source.setCoefficient(0, 9);
        assertTerms(new int[] {1, 2}, p);
        assertTerms(new int[] {9}, source);
    }

    @Test(expected = AssertionError.class)
    public void testTransferFromRejectsSelf() {
        Polynomial p = new Polynomial1L(new int[] {1});
        p.transferFrom(p);
    }

    @Test(expected = AssertionError.class)
    public void testTransferFromRejectsNull() {
        Polynomial p = new Polynomial1L(new int[] {1});
        p.transferFrom(null);
    }

    /*
     * Sanity check that assertions are enabled, so the precondition tests
     * above are meaningful
     */

    @Test
    public void testAssertionsAreEnabled() {
        boolean enabled = false;
        try {
            assert false;
        } catch (AssertionError e) {
            enabled = true;
        }
        assertTrue("run the tests with -ea", enabled);
    }

}

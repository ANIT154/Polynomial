import components.polynomial.Polynomial;
import components.polynomial.Polynomial1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Models the distribution of totals when rolling a collection of dice, using
 * a {@code Polynomial} as its representation.
 *
 * <p>
 * The trick is a <em>generating function</em>: a die with faces 1 through 6
 * is the polynomial {@code x + x^2 + x^3 + x^4 + x^5 + x^6}, where the
 * coefficient of {@code x^t} is the number of faces showing {@code t}.
 * Multiplying the polynomials of several dice gives a polynomial whose
 * coefficient of {@code x^t} is the number of ways the dice can add up to
 * {@code t}. Counting outcomes becomes polynomial multiplication.
 * </p>
 *
 * <p>
 * Use case: the {@code Polynomial} component as the hidden representation of
 * another class (contrast with {@code RootFinder}, which uses it directly as
 * a mathematical function).
 * </p>
 *
 * @convention <pre>
 * [this.ways has only non-negative coefficients] and
 * [the sum of the coefficients of this.ways = this.outcomes] and
 * this.diceCount >= 0
 * </pre>
 * @correspondence <pre>
 * [for each total t, the number of ways to roll t with the dice added so
 *  far = this.ways.coefficient(t)]
 * </pre>
 */
public final class DiceDistribution {

    /**
     * Generating function: the coefficient of x^t is the number of ways to
     * roll a total of t.
     */
    private final Polynomial ways;

    /**
     * Total number of equally likely outcomes (product of face counts).
     */
    private long outcomes;

    /**
     * Number of dice added so far.
     */
    private int diceCount;

    /**
     * Creates a distribution with no dice. With nothing rolled, the only
     * possible total is 0, reached in exactly one way, so the generating
     * function starts as the constant polynomial 1.
     */
    public DiceDistribution() {
        this.ways = new Polynomial1L(new int[] {1});
        this.outcomes = 1;
        this.diceCount = 0;
    }

    /**
     * Adds a standard die with faces 1 through {@code sides}.
     *
     * @param sides
     *            the number of sides of the new die
     * @updates this
     * @requires sides >= 1
     * @ensures [this models #this with one more die numbered 1..sides]
     */
    public void addDie(int sides) {
        assert sides >= 1 : "Violation of: sides >= 1";

        int[] faces = new int[sides];
        for (int i = 0; i < sides; i++) {
            faces[i] = i + 1;
        }
        this.addDie(faces);
    }

    /**
     * Adds a die whose faces show the given values (values may repeat).
     *
     * @param faces
     *            the value shown on each face of the new die
     * @updates this
     * @requires faces is not null and |faces| >= 1 and [every face >= 0]
     * @ensures [this models #this with one more die with the given faces]
     */
    public void addDie(int[] faces) {
        assert faces != null : "Violation of: faces is not null";
        assert faces.length >= 1 : "Violation of: |faces| >= 1";

        Polynomial die = new Polynomial1L();
        for (int face : faces) {
            assert face >= 0 : "Violation of: every face >= 0";
            die.setCoefficient(face, die.coefficient(face) + 1);
        }
        this.ways.multiply(die);
        this.outcomes *= faces.length;
        this.diceCount++;
    }

    /**
     * Reports the number of ways the dice can add up to {@code total}.
     *
     * @param total
     *            the total to count
     * @return the number of outcomes whose faces sum to {@code total}
     * @requires total >= 0
     * @ensures waysToRoll = [number of outcomes summing to total]
     */
    public int waysToRoll(int total) {
        assert total >= 0 : "Violation of: total >= 0";

        return this.ways.coefficient(total);
    }

    /**
     * Reports the probability of rolling {@code total}.
     *
     * @param total
     *            the total whose probability is reported
     * @return the probability of rolling {@code total}
     * @requires total >= 0
     * @ensures probabilityOf = waysToRoll(total) / [number of outcomes]
     */
    public double probabilityOf(int total) {
        return (double) this.waysToRoll(total) / this.outcomes;
    }

    /**
     * Reports the probability of rolling {@code total} or more.
     *
     * @param total
     *            the smallest total counted
     * @return the probability of rolling at least {@code total}
     * @requires total >= 0
     * @ensures <pre>
     * probabilityOfAtLeast = [sum of probabilityOf(t) for all t >= total]
     * </pre>
     */
    public double probabilityOfAtLeast(int total) {
        assert total >= 0 : "Violation of: total >= 0";

        double result = 0.0;
        for (int t = total; t <= this.ways.degree(); t++) {
            result += this.probabilityOf(t);
        }
        return result;
    }

    /**
     * Reports the largest possible total.
     *
     * @return the largest total the dice can show
     * @ensures maximumTotal = [largest possible total]
     */
    public int maximumTotal() {
        return this.ways.degree();
    }

    /**
     * Reports the smallest possible total.
     *
     * @return the smallest total the dice can show
     * @ensures minimumTotal = [smallest possible total]
     */
    public int minimumTotal() {
        int total = 0;
        while (this.ways.coefficient(total) == 0) {
            total++;
        }
        return total;
    }

    /**
     * Reports a most likely total (the smallest one, if there is a tie).
     *
     * @return a total with the largest number of ways to roll it
     * @ensures [waysToRoll(mostLikelyTotal) is maximal]
     */
    public int mostLikelyTotal() {
        int best = 0;
        for (int total = 1; total <= this.ways.degree(); total++) {
            if (this.ways.coefficient(total) > this.ways.coefficient(best)) {
                best = total;
            }
        }
        return best;
    }

    /**
     * Reports whether {@code this} and {@code other} produce every total with
     * the same probability, even if the dice themselves are different.
     *
     * @param other
     *            the distribution to compare with
     * @return true iff the two distributions are identical
     * @ensures <pre>
     * hasSameDistributionAs = [this and other give every total the same
     *                          probability]
     * </pre>
     */
    public boolean hasSameDistributionAs(DiceDistribution other) {
        assert other != null : "Violation of: other is not null";

        return this.outcomes == other.outcomes && this.ways.equals(other.ways);
    }

    /**
     * Prints a text histogram of the distribution.
     *
     * @param out
     *            the output stream
     * @param title
     *            a heading for the histogram
     * @updates out.content
     * @requires out.is_open
     * @ensures out.content = #out.content * [the histogram]
     */
    public void printHistogram(SimpleWriter out, String title) {
        final int barWidth = 40;
        out.println(title + " (" + this.diceCount + " dice, " + this.outcomes
                + " outcomes)");
        int peak = this.ways.coefficient(this.mostLikelyTotal());
        for (int total = this.minimumTotal(); total <= this
                .maximumTotal(); total++) {
            int count = this.ways.coefficient(total);
            int length = (int) Math.round((double) count * barWidth / peak);
            out.println(String.format("%3d | %-" + barWidth + "s %5d  %6.2f%%",
                    total, "#".repeat(length), count,
                    100.0 * this.probabilityOf(total)));
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
        final int six = 6;
        final int twenty = 20;
        final int seven = 7;
        final int sixteen = 16;
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Two ordinary six-sided dice: the familiar triangle peaking at 7
         */
        DiceDistribution twoD6 = new DiceDistribution();
        twoD6.addDie(six);
        twoD6.addDie(six);
        twoD6.printHistogram(out, "2d6");
        out.println("Generating function: " + twoD6.ways);
        out.println("Most likely total: " + twoD6.mostLikelyTotal());
        out.println();

        /*
         * Three dice: the shape starts to look like a bell curve
         */
        DiceDistribution threeD6 = new DiceDistribution();
        threeD6.addDie(six);
        threeD6.addDie(six);
        threeD6.addDie(six);
        threeD6.printHistogram(out, "3d6");

        /*
         * Tabletop question: a 1d20 roll and a 3d6 roll both average 10.5,
         * but which is more likely to roll an extreme result?
         */
        DiceDistribution d20 = new DiceDistribution();
        d20.addDie(twenty);
        out.println("Both average 10.5, but the spread is very different:");
        out.println(String.format("  P(1d20 >= 16) = %.4f",
                d20.probabilityOfAtLeast(sixteen)));
        out.println(String.format("  P(3d6  >= 16) = %.4f",
                threeD6.probabilityOfAtLeast(sixteen)));
        out.println();

        /*
         * Sicherman dice: two strangely numbered dice that behave exactly
         * like a pair of ordinary dice. The polynomial equality check proves
         * it for every total at once.
         */
        DiceDistribution sicherman = new DiceDistribution();
        sicherman.addDie(new int[] {1, 2, 2, 3, 3, 4});
        sicherman.addDie(new int[] {1, 3, 4, 5, 6, 8});
        sicherman.printHistogram(out, "Sicherman dice {1,2,2,3,3,4} + "
                + "{1,3,4,5,6,8}");
        out.println("Same distribution as 2d6? "
                + sicherman.hasSameDistributionAs(twoD6));
        out.println("Ways to roll 7 with each: " + sicherman.waysToRoll(seven)
                + " vs " + twoD6.waysToRoll(seven));

        out.close();
    }

}

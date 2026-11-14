# Polynomial: An OSU Software Sequence Component

<!-- Optional: add a status badge by replacing USERNAME and REPO below
[![Tests](https://github.com/USERNAME/REPO/actions/workflows/test.yml/badge.svg)](https://github.com/USERNAME/REPO/actions/workflows/test.yml)
-->

`Polynomial` is a software component for single-variable polynomials with
integer coefficients, such as `3x^2 - x + 5`. It supports polynomial
arithmetic, evaluation, and differentiation, and it is built in the layered
style of the OSU software sequence: kernel interface, enhanced interface,
abstract secondary class, and kernel implementation.

## Quick Example

```java
import components.polynomial.Polynomial;
import components.polynomial.Polynomial1L;

// coefficients are listed from the constant term upward
Polynomial p = new Polynomial1L(new int[] {1, 1});   // x + 1
Polynomial q = new Polynomial1L(new int[] {-1, 1});  // x - 1

p.multiply(q);                       // p = x^2 - 1
System.out.println(p);               // "x^2 - 1"
System.out.println(p.evaluate(3.0)); // 8.0

p.differentiate();                   // p = 2x
System.out.println(p);               // "2x"
```

## API Overview

| Layer | Type | Methods |
| --- | --- | --- |
| Standard | `Standard<Polynomial>` (OSU) | `clear`, `newInstance`, `transferFrom` |
| Kernel | `PolynomialKernel` | `setCoefficient`, `coefficient`, `degree` |
| Enhanced | `Polynomial` | `isZero`, `leadingCoefficient`, `add`, `subtract`, `scale`, `multiply`, `evaluate`, `differentiate` |
| Secondary | `PolynomialSecondary` | all enhanced methods, plus `equals`, `hashCode`, `toString`, written using only kernel and `Standard` methods |
| Implementation | `Polynomial1L` | kernel and `Standard` methods on a `TreeMap` from exponent to coefficient |

The full contracts (`@requires`, `@ensures`, `@updates`, and the mathematical
model) are in the Javadoc of each file in
[`src/components/polynomial`](src/components/polynomial). The hierarchy
diagram is in the [Part 3 doc](doc/03-component-interfaces/03-component-interfaces.md).

## Use Cases

The two sample programs in [`src`](src) use the component in different ways:

- **[`DiceDistribution`](src/DiceDistribution.java)** uses a `Polynomial` as
  the hidden representation of another class. Each die becomes a generating
  function (`x + x^2 + ... + x^6`), and multiplying them counts every way the
  dice can add up to each total. The demo prints histograms for 2d6 and 3d6,
  compares 1d20 against 3d6, and uses `equals` to show that the oddly
  numbered *Sicherman dice* behave exactly like two ordinary dice.
- **[`RootFinder`](src/RootFinder.java)** uses `Polynomial` directly as a
  mathematical function. It runs Newton's method with `evaluate` and
  `differentiate` to find roots and turning points. The demo recovers
  the roots of an expanded product, computes the square root of 2, and solves
  a classic optimization problem (the largest open box that can be folded from
  a square sheet).

## Testing

The test suites in [`test/components/polynomial`](test/components/polynomial)
contain 121 JUnit 4 tests:

- `Polynomial1LTest` covers the constructors, kernel methods, and `Standard`
  methods, checking the whole abstract state of every object after each call.
- `PolynomialTest` covers every secondary method and `equals`, `hashCode`,
  and `toString`, including edge cases such as the zero polynomial, cancelling
  the leading term, sparse polynomials, and aliasing (`p.add(p)`,
  `p.multiply(p)`).

Precondition tests need assertions to be enabled (`-ea`). The VSCode Java test
runner enables them by default.

Every push and pull request is tested automatically by
[GitHub Actions](.github/workflows/test.yml).

## Building and Running

1. Put `components.jar`, `junit-4.13.2.jar`, and `hamcrest-core-1.3.jar` in
   `lib` (see [`lib/README.md`](lib/README.md)).
2. In VSCode, open a demo in `src` and click **Run**, or open the Testing panel
   (beaker icon) to run the tests.

From the command line:

```bash
mkdir -p bin
javac -d bin -cp "lib/*" $(find src test -name "*.java")
java -cp "bin:lib/*" DiceDistribution
java -cp "bin:lib/*" RootFinder
java -ea -cp "bin:lib/*" org.junit.runner.JUnitCore \
    components.polynomial.Polynomial1LTest components.polynomial.PolynomialTest
```

(On Windows, use `;` instead of `:` in the classpath.)

## Project Layout

```text
src/components/polynomial/   the component (interfaces, abstract class, kernel)
src/                         the two use cases
test/components/polynomial/  JUnit test suites
doc/                         design documents for each part of the project
```

## Limitations

- Coefficients are `int`s, and arithmetic that overflows `int` is not
  detected. A future version could use `long` or `NaturalNumber`-style
  arbitrary precision coefficients.
- Division and integration are not supported, since they can produce
  non-integer coefficients.

See the [CHANGELOG](CHANGELOG.md) for version history.

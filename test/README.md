# Test Folder

This folder holds the JUnit 4 test suites for the `Polynomial` component,
mirroring the package layout of `src`:

```text
test
└───components
    └───polynomial
            Polynomial1LTest.java   constructors, kernel, and Standard methods
            PolynomialTest.java     secondary methods, equals, hashCode, toString
```

## Testing Approach

There is no reference implementation to compare against, so the tests check
the abstract state of each polynomial directly:

- `Polynomial1LTest` uses a helper, `assertTerms`, that checks the degree and
  every coefficient through the kernel methods. It is called on every object a
  test touches, so a test fails if a method gives a wrong answer *or* changes
  something it should have left alone.
- `PolynomialTest` builds its expected results as separate objects and
  compares whole polynomials with `assertEquals`. The `equals` method it
  relies on is tested on its own at the end of the file.

Each method is tested on the zero polynomial, constants, typical cases, and
edge cases such as cancelling the leading term, sparse polynomials with large
exponents, extreme `int` values, and calls where an argument is the receiver
itself (`p.add(p)`). Precondition checks are tested too, which requires
running with assertions enabled (`-ea`, which is VSCode's default).

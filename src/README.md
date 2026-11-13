# SRC Folder

This folder holds the `Polynomial` component and two programs that use it.

## The Component

The component lives in the `components.polynomial` package:

```text
src
└───components
    └───polynomial
            PolynomialKernel.java      kernel interface (extends Standard)
            Polynomial.java            enhanced interface
            PolynomialSecondary.java   secondary methods + equals/hashCode/toString
            Polynomial1L.java          kernel implementation on a TreeMap
```

## The Use Cases

- `DiceDistribution.java`: counts dice outcomes by multiplying generating
  functions. A `Polynomial` is the representation of this class.
- `RootFinder.java`: finds roots and turning points with Newton's method,
  using a `Polynomial` directly as a function.

Run either file's `main` method to see it in action.

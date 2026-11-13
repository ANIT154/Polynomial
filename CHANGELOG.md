# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Calendar Versioning](https://calver.org/) of
the following form: YYYY.0M.0D.

## 2026.11.13

### Added

- Designed a `Polynomial` component with a term lookup kernel
  (`setCoefficient`, `coefficient`, `degree`)
- Designed a `Polynomial` component with an assemble/disassemble kernel
  (`addTerm`, `removeLeadingTerm`, `degree`, `isZero`)
- Designed a `Polynomial` component with a dense `NaturalNumber`-style kernel
  (`multiplyByX`, `divideByX`, `isZero`)
- Designed kernel and enhanced interfaces for the `Polynomial` component,
  with a hierarchy diagram
- Designed abstract class `PolynomialSecondary`, implementing every enhanced
  method plus `equals`, `hashCode`, and `toString` using only kernel and
  `Standard` methods
- Designed kernel implementation `Polynomial1L` on a `TreeMap` from exponent
  to coefficient, with its convention and correspondence
- Designed test suite for the `Polynomial` component (121 JUnit tests across
  `Polynomial1LTest` and `PolynomialTest`)
- Designed two different use cases for the `Polynomial` component:
  `DiceDistribution` (polynomial as a representation) and `RootFinder`
  (polynomial as a function)
- Added a GitHub Actions workflow that compiles the project and runs the tests
  on every push and pull request

### Updated

- Changed design to make `coefficient` a kernel method rather than a
  secondary method, so that reading a term no longer requires taking the
  polynomial apart (Design #2) or recursion over every degree (Design #3)
- Changed design to store only non-zero terms, so the degree is always the
  largest stored exponent and sparse polynomials stay small
- Changed design to add `scale` and `leadingCoefficient`, which both use
  cases needed

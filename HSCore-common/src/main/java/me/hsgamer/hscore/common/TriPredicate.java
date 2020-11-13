package me.hsgamer.hscore.common;

/**
 * Predicate with THREE parameters
 */
public interface TriPredicate<A, B, C> {
  /**
   * Test the predicate
   *
   * @param a first parameter
   * @param b second parameter
   * @param c third parameter
   *
   * @return the result
   */
  boolean test(A a, B b, C c);
}

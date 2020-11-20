package me.hsgamer.hscore.common.interfaces;

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

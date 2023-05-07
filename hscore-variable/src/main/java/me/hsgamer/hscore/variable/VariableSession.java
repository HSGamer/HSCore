package me.hsgamer.hscore.variable;

/**
 * A session for replacing variables
 */
public interface VariableSession {
  /**
   * Check if the string has a variable
   *
   * @return true if it does
   */
  boolean hasVariable();

  /**
   * Get the variable
   *
   * @return the variable
   */
  String getVariable();

  /**
   * Replace the variable with the replacement
   *
   * @param replacement the replacement
   */
  void replaceVariable(String replacement);

  /**
   * Get the final string
   *
   * @return the final string
   */
  String getFinalString();
}

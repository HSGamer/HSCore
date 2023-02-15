package me.hsgamer.hscore.expansion.common;

public interface ExpansionStateListener {
  void onStateChange(ExpansionClassLoader classLoader, ExpansionState newState);
}

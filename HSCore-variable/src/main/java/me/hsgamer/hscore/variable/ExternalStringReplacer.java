package me.hsgamer.hscore.variable;

import me.hsgamer.hscore.common.interfaces.StringReplacer;

public interface ExternalStringReplacer extends StringReplacer {
  default boolean canBeReplaced(String string) {
    return false;
  }
}

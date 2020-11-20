package me.hsgamer.hscore.common.interfaces;

import java.util.List;
import java.util.UUID;

/**
 * A simple interface for String replacement
 */
public interface StringReplacer {
  /**
   * The dummy replacer that does nothing
   */
  StringReplacer DUMMY = (original, uuid) -> original;

  /**
   * Replace a string based on the unique id
   *
   * @param original        the original string
   * @param uuid            the unique id
   * @param stringReplacers the list of string replacer
   *
   * @return the replaced string
   */
  static String replace(String original, UUID uuid, List<StringReplacer> stringReplacers) {
    String replaced = original;
    for (StringReplacer replacer : stringReplacers) {
      replaced = replacer.replace(original, uuid);
    }
    return replaced;
  }

  /**
   * Replace a string based on the unique id
   *
   * @param original the original string
   * @param uuid     the unique id
   *
   * @return the replaced string
   */
  String replace(String original, UUID uuid);

  /**
   * Replace a string
   *
   * @param original the original string
   *
   * @return the replaced string
   */
  default String replace(String original) {
    return replace(original, UUID.randomUUID());
  }
}

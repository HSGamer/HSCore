package me.hsgamer.hscore.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CommonUtils {

  private CommonUtils() {
    // EMPTY
  }

  /**
   * Create a list of string
   *
   * @param value the object
   * @param trim  should we trim the strings
   * @return the string list
   */
  public static List<String> createStringListFromObject(Object value, boolean trim) {
    List<String> list = new ArrayList<>();
    if (value instanceof Collection) {
      ((Collection<?>) value).forEach(o -> list.add(String.valueOf(o)));
    } else {
      list.add(String.valueOf(value));
    }
    if (trim) {
      list.replaceAll(String::trim);
    }
    return list;
  }

  /**
   * Reverse a collection
   *
   * @param original the original collection
   * @param <T> the type of the elements
   * @return the reversed list
   */
  public static <T> List<T> reverse(Collection<T> original) {
    List<T> list = new ArrayList<>(original);
    Collections.reverse(list);
    return list;
  }
}

package me.hsgamer.hscore.common;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Methods on collections
 */
public class CollectionUtils {

  private CollectionUtils() {
    // EMPTY
  }

  /**
   * Create a list of string
   *
   * @param value the object
   * @param trim  should we trim the strings
   *
   * @return the string list
   */
  @NotNull
  public static List<String> createStringListFromObject(@NotNull Object value, boolean trim) {
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
   * @param <T>      the type of the elements
   *
   * @return the reversed list
   */
  @NotNull
  public static <T> List<T> reverse(@NotNull Collection<T> original) {
    List<T> list = new ArrayList<>(original);
    Collections.reverse(list);
    return list;
  }

  /**
   * Rotate a collection
   *
   * @param original the original collection
   * @param distance the distance to rotate
   * @param <T>      the type of the elements
   *
   * @return the rotated list
   */
  public static <T> List<T> rotate(Collection<T> original, int distance) {
    List<T> list = new ArrayList<>(original);
    Collections.rotate(list, distance);
    return list;
  }

  /**
   * Repeat the element of the collection
   *
   * @param original the original collection
   * @param repeat   the times the element repeats
   * @param <T>      the type of the elements
   *
   * @return the repeated list
   */
  public static <T> List<T> repeatElement(Collection<T> original, int repeat) {
    List<T> list = new ArrayList<>();
    for (T element : original) {
      list.addAll(IntStream.range(0, repeat).mapToObj(i -> element).collect(Collectors.toList()));
    }
    return list;
  }

  /**
   * Repeat the collection
   *
   * @param original the original collection
   * @param repeat   the times the list repeats
   * @param <T>      the type of the elements
   *
   * @return the repeated list
   */
  public static <T> List<T> repeatCollection(Collection<T> original, int repeat) {
    List<T> list = new ArrayList<>();
    IntStream.range(0, repeat).mapToObj(i -> original).forEach(list::addAll);
    return list;
  }

  /**
   * Split the strings from the string list
   *
   * @param regex   the delimiting regular expression
   * @param strings the string list
   *
   * @return the split string list
   */
  public static List<String> splitAll(String regex, Collection<String> strings) {
    return strings.stream().flatMap(s -> Stream.of(s.split(regex))).collect(Collectors.toList());
  }
}

package me.hsgamer.hscore.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Methods on collections
 */
public final class CollectionUtils {

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
  public static List<String> createStringListFromObject(@NotNull final Object value, final boolean trim) {
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
  public static <T> List<T> reverse(@NotNull final Collection<T> original) {
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
  @NotNull
  public static <T> List<T> rotate(@NotNull final Collection<T> original, final int distance) {
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
  @NotNull
  public static <T> List<T> repeatElement(@NotNull final Collection<T> original, final int repeat) {
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
  @NotNull
  public static <T> List<T> repeatCollection(@NotNull final Collection<T> original, final int repeat) {
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
  @NotNull
  public static List<String> splitAll(@NotNull final String regex, @NotNull final Collection<String> strings) {
    return strings.stream().flatMap(s -> Stream.of(s.split(regex))).collect(Collectors.toList());
  }

  /**
   * Pick a random element from the collection
   *
   * @param collection     the collection
   * @param matchCondition the condition of the picked element
   * @param <T>            the type of the elements
   *
   * @return the picked element, or null if the collection is empty or there is no element that matches the condition
   */
  @Nullable
  public static <T> T pickRandom(@NotNull final Collection<T> collection, @NotNull final Predicate<T> matchCondition) {
    List<T> list = new ArrayList<>(collection);
    T picked = null;
    while (!list.isEmpty()) {
      int index = ThreadLocalRandom.current().nextInt(list.size());
      T element = list.get(index);
      if (matchCondition.test(element)) {
        picked = element;
        break;
      } else {
        list.remove(index);
      }
    }
    return picked;
  }

  /**
   * Pick a random element from the collection
   *
   * @param collection the collection
   * @param <T>        the type of the elements
   *
   * @return the picked element, or null if the collection is empty
   */
  @Nullable
  public static <T> T pickRandom(@NotNull final Collection<T> collection) {
    return pickRandom(collection, t -> true);
  }

  /**
   * Pick a random element from the array
   *
   * @param array          the array
   * @param matchCondition the condition of the picked element
   * @param <T>            the type of the elements
   *
   * @return the picked element, or null if the array is empty or there is no element that matches the condition
   */
  @Nullable
  public static <T> T pickRandom(@NotNull final T[] array, @NotNull final Predicate<T> matchCondition) {
    return pickRandom(Arrays.asList(array), matchCondition);
  }

  /**
   * Pick a random element from the array
   *
   * @param array the array
   * @param <T>   the type of the elements
   *
   * @return the picked element, or null if the collection is empty
   */
  @Nullable
  public static <T> T pickRandom(@NotNull final T[] array) {
    return pickRandom(Arrays.asList(array));
  }
}

package me.hsgamer.hscore.minecraft.gui.common;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * An element of the GUI
 */
public interface GUIElement {
  /**
   * Handle the object if it is an instance of {@link GUIElement}
   *
   * @param o               the object
   * @param elementConsumer the consumer
   */
  static void handleIfElement(Object o, Consumer<GUIElement> elementConsumer) {
    if (o instanceof GUIElement) {
      elementConsumer.accept((GUIElement) o);
    }
  }

  /**
   * Loop through the collection and handle the element if it is an instance of {@link GUIElement}
   *
   * @param collection      the collection
   * @param elementConsumer the consumer
   */
  static <T> void handleIfElement(Collection<T> collection, Consumer<GUIElement> elementConsumer) {
    collection.forEach(o -> handleIfElement(o, elementConsumer));
  }

  /**
   * Initialize the element. Should be called before adding to the GUI.
   */
  default void init() {
  }

  /**
   * Stop the element. Should be called after removing from the GUI.
   */
  default void stop() {
  }
}

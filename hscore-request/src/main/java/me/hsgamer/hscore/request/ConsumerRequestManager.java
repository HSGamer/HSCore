package me.hsgamer.hscore.request;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A simple request manager
 *
 * @param <T> the type of the argument
 */
public class ConsumerRequestManager<T> extends RequestManager<UUID, T, Void> {
  /**
   * Add a request
   *
   * @param uuid     the unique id
   * @param consumer the consumer when the request is called
   */
  public CompletableFuture<Void> addRequest(@NotNull UUID uuid, @NotNull BiConsumer<UUID, T> consumer) {
    return super.addRequest(uuid, t -> {
      consumer.accept(uuid, t);
      return null;
    });
  }

  /**
   * Add a request
   *
   * @param uuid     the unique id
   * @param consumer the consumer when the request is called
   */
  public CompletableFuture<Void> addRequest(@NotNull UUID uuid, @NotNull Consumer<T> consumer) {
    return addRequest(uuid, (uuid1, t) -> consumer.accept(t));
  }
}

package me.hsgamer.hscore.request;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * The generic request manager
 *
 * @param <D> the type of the identifier
 * @param <I> the type of the input
 * @param <O> the type of the output
 */
public class RequestManager<D, I, O> {
  private final Map<D, Queue<CompletableFuture<I>>> requests = new HashMap<>();

  /**
   * Add a request
   *
   * @param identifier the identifier
   * @param function   the input converter
   *
   * @return the output future
   */
  public CompletableFuture<O> addRequest(D identifier, Function<I, O> function) {
    CompletableFuture<I> inputFuture = new CompletableFuture<>();
    requests.computeIfAbsent(identifier, k -> new ArrayDeque<>()).add(inputFuture);
    return inputFuture.thenApply(function);
  }

  /**
   * Remove all requests
   *
   * @param identifier the identifier
   */
  public void removeRequests(D identifier) {
    Optional.ofNullable(requests.remove(identifier)).ifPresent(queue -> {
      queue.forEach(future -> future.cancel(true));
      queue.clear();
    });
  }

  /**
   * Remove a request
   *
   * @param identifier the identifier
   */
  public void removeRequest(D identifier) {
    Optional.ofNullable(requests.get(identifier)).ifPresent(queue -> {
      CompletableFuture<I> future = queue.poll();
      if (future != null) {
        future.cancel(true);
      }
    });
  }

  /**
   * Complete the request of the identifier
   *
   * @param identifier the identifier
   * @param input      the input
   * @param handleAll  if true, all requests will be completed
   */
  public void completeRequest(D identifier, I input, boolean handleAll) {
    Optional.ofNullable(requests.get(identifier)).ifPresent(queue -> {
      if (handleAll) {
        queue.forEach(future -> future.complete(input));
        queue.clear();
      } else {
        CompletableFuture<I> future = queue.poll();
        if (future != null) {
          future.complete(input);
        }
      }
    });
  }

  /**
   * Complete the request of the identifier. This method will handle only one request.
   *
   * @param identifier the identifier
   * @param input      the input
   */
  public void completeRequest(D identifier, I input) {
    completeRequest(identifier, input, false);
  }
}

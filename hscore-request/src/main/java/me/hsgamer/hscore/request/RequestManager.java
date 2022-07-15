package me.hsgamer.hscore.request;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * The generic request manager
 *
 * @param <I> the type of the identifier
 * @param <D> the type of the data
 */
public class RequestManager<I, D> {
  private final Map<I, Queue<CompletableFuture<D>>> requests = new HashMap<>();

  /**
   * Add a request
   *
   * @param identifier the identifier
   *
   * @return the data future
   */
  public CompletableFuture<D> addRequest(I identifier) {
    CompletableFuture<D> dataFuture = new CompletableFuture<>();
    requests.computeIfAbsent(identifier, k -> new ArrayDeque<>()).add(dataFuture);
    return dataFuture;
  }

  /**
   * Add a request
   *
   * @param identifier the identifier
   * @param function   the data converter
   * @param <O>        the type of the output
   *
   * @return the output future
   */
  public <O> CompletableFuture<O> addRequest(I identifier, Function<D, O> function) {
    return addRequest(identifier).thenApply(function);
  }

  /**
   * Remove all requests
   *
   * @param identifier the identifier
   */
  public void removeRequests(I identifier) {
    Queue<CompletableFuture<D>> queue = requests.remove(identifier);
    if (queue == null) return;
    queue.forEach(future -> future.cancel(true));
    queue.clear();
  }

  /**
   * Remove a request
   *
   * @param identifier the identifier
   */
  public void removeRequest(I identifier) {
    Queue<CompletableFuture<D>> queue = requests.get(identifier);
    if (queue == null) return;
    CompletableFuture<D> future = queue.poll();
    if (future != null) {
      future.cancel(true);
    }
  }

  /**
   * Complete the request of the identifier
   *
   * @param identifier the identifier
   * @param data       the data
   * @param handleAll  if true, all requests will be completed
   */
  public void completeRequest(I identifier, D data, boolean handleAll) {
    Queue<CompletableFuture<D>> queue = requests.get(identifier);
    if (queue == null) return;

    if (handleAll) {
      queue.forEach(future -> future.complete(data));
      queue.clear();
    } else {
      CompletableFuture<D> future = queue.poll();
      if (future != null) {
        future.complete(data);
      }
    }
  }

  /**
   * Complete the request of the identifier. This method will handle only one request.
   *
   * @param identifier the identifier
   * @param data       the data
   */
  public void completeRequest(I identifier, D data) {
    completeRequest(identifier, data, false);
  }
}

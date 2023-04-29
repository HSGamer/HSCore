package me.hsgamer.hscore.task;

import me.hsgamer.hscore.task.element.TaskPool;
import me.hsgamer.hscore.task.element.TaskProcess;
import me.hsgamer.hscore.task.exception.BatchRunnableException;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * A runnable that runs a batch of tasks in sequence
 */
public class BatchRunnable implements Runnable {
  private final Queue<TaskPool> tasks = new PriorityQueue<>(Comparator.comparingInt(TaskPool::getStage));
  private final Map<String, Object> data;
  private long timeout = 0;
  private TimeUnit timeoutUnit = TimeUnit.MILLISECONDS;

  /**
   * Create a new batch runnable
   *
   * @param data the initial data
   */
  public BatchRunnable(Map<String, Object> data) {
    this.data = data;
  }

  /**
   * Create a new batch runnable with no initial data
   */
  public BatchRunnable() {
    this(new ConcurrentHashMap<>());
  }

  @Override
  public void run() {
    AtomicBoolean isRunning = new AtomicBoolean(true);
    AtomicReference<TaskPool> currentTaskPool = new AtomicReference<>();
    AtomicReference<CompletableFuture<Void>> nextLock = new AtomicReference<>();
    TaskProcess process = new TaskProcess() {
      @Override
      public Map<String, Object> getData() {
        return data;
      }

      @Override
      public void next() {
        Optional.ofNullable(nextLock.get()).ifPresent(future -> future.complete(null));
      }

      @Override
      public void complete() {
        isRunning.set(false);
        next();
      }

      @Override
      public TaskPool getCurrentTaskPool() {
        return currentTaskPool.get();
      }

      @Override
      public TaskPool getTaskPool(int stage) {
        return BatchRunnable.this.getTaskPool(stage);
      }
    };

    while (isRunning.get()) {
      TaskPool taskPool = tasks.poll();
      if (taskPool == null) {
        isRunning.set(false);
        break;
      }
      currentTaskPool.set(taskPool);

      do {
        Consumer<TaskProcess> task = taskPool.pollTask();
        if (task == null) {
          break;
        }
        CompletableFuture<Void> next = new CompletableFuture<>();
        nextLock.set(next);
        task.accept(process);
        try {
          if (timeout > 0) {
            next.get(timeout, timeoutUnit);
          } else {
            next.get();
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          isRunning.set(false);
          break;
        } catch (ExecutionException | TimeoutException e) {
          throw new BatchRunnableException(e);
        }
      } while (isRunning.get());
    }
  }

  /**
   * Get the task pool
   *
   * @param stage the stage of the task pool
   *
   * @return the task pool
   */
  public TaskPool getTaskPool(int stage) {
    TaskPool taskPool = null;
    for (TaskPool t : tasks) {
      if (t.getStage() == stage) {
        taskPool = t;
        break;
      }
    }
    if (taskPool == null) {
      taskPool = new TaskPool(stage);
      tasks.add(taskPool);
    }
    return taskPool;
  }

  /**
   * Execute the consumer for the task pool
   *
   * @param stage            the stage of the task pool
   * @param taskPoolConsumer the consumer
   */
  public void addTaskPool(int stage, Consumer<TaskPool> taskPoolConsumer) {
    taskPoolConsumer.accept(getTaskPool(stage));
  }

  /**
   * Set the timeout for each task
   *
   * @param timeout the timeout
   * @param unit    the unit of the timeout
   */
  public void setTimeout(long timeout, TimeUnit unit) {
    this.timeout = timeout;
    this.timeoutUnit = unit;
  }

  /**
   * Get the data
   *
   * @return the data
   */
  public Map<String, Object> getData() {
    return data;
  }
}

package me.hsgamer.hscore.task;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * A runnable that runs a batch of tasks in sequence
 */
public class BatchRunnable implements Runnable {
  private final Queue<TaskPool> tasks = new PriorityQueue<>(Comparator.comparingInt(TaskPool::getStage));
  private final Map<String, Object> data;

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
    this(new HashMap<>());
  }

  @Override
  public void run() {
    AtomicBoolean isRunning = new AtomicBoolean(true);
    while (isRunning.get()) {
      TaskPool taskPool = tasks.poll();
      if (taskPool == null) {
        isRunning.set(false);
        break;
      }

      AtomicReference<CompletableFuture<Void>> nextLock = new AtomicReference<>();
      Process process = new Process() {
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
          next();
          isRunning.set(false);
        }

        @Override
        public TaskPool getCurrentTaskPool() {
          return taskPool;
        }

        @Override
        public TaskPool getTaskPool(int stage) {
          return BatchRunnable.this.getTaskPool(stage);
        }
      };
      do {
        Consumer<Process> task = taskPool.pollTask();
        if (task == null) {
          break;
        }
        CompletableFuture<Void> next = new CompletableFuture<>();
        nextLock.set(next);
        task.accept(process);
        try {
          next.get();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          isRunning.set(false);
          break;
        } catch (ExecutionException e) {
          e.printStackTrace();
          isRunning.set(false);
          break;
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
   * Get the data
   *
   * @return the data
   */
  public Map<String, Object> getData() {
    return data;
  }

  /**
   * The task process.
   * Used by the task to work with the running {@link BatchRunnable}
   */
  public interface Process {
    /**
     * Get the data of the running {@link BatchRunnable}
     *
     * @return the data
     */
    Map<String, Object> getData();

    /**
     * Notify the next task
     */
    void next();

    /**
     * Notify the {@link BatchRunnable} to stop
     */
    void complete();

    /**
     * Get the current task pool
     *
     * @return the current task pool
     */
    TaskPool getCurrentTaskPool();

    /**
     * Get the task pool
     *
     * @param stage the stage of the task pool
     *
     * @return the task pool
     */
    TaskPool getTaskPool(int stage);

    /**
     * Execute the consumer for the task pool
     *
     * @param stage            the stage of the task pool
     * @param taskPoolConsumer the consumer
     */
    default void addTaskPool(int stage, Consumer<TaskPool> taskPoolConsumer) {
      taskPoolConsumer.accept(getTaskPool(stage));
    }

    /**
     * Execute the consumer for the current task pool
     *
     * @param taskPoolConsumer the consumer
     */
    default void addCurrentTaskPool(Consumer<TaskPool> taskPoolConsumer) {
      taskPoolConsumer.accept(getCurrentTaskPool());
    }
  }

  /**
   * The task pool of the {@link BatchRunnable}
   */
  public static final class TaskPool {
    private final int stage;
    private final Deque<Consumer<Process>> tasks = new ArrayDeque<>();

    /**
     * Create a new task pool
     *
     * @param stage the stage of the task pool
     */
    private TaskPool(int stage) {
      this.stage = stage;
    }

    /**
     * Add the task to the head of the task pool
     *
     * @param task the task
     *
     * @return the current task pool, for chaining
     */
    public TaskPool addFirst(Consumer<Process> task) {
      tasks.addFirst(task);
      return this;
    }

    /**
     * Add the task to the tail of the task pool
     *
     * @param task the task
     *
     * @return the current task pool, for chaining
     */
    public TaskPool addLast(Consumer<Process> task) {
      tasks.addLast(task);
      return this;
    }

    /**
     * Add the task to the head of the task pool
     *
     * @param task the task
     *
     * @return the current task pool, for chaining
     */
    public TaskPool addFirst(Runnable... task) {
      for (Runnable t : task) {
        tasks.addFirst(process -> {
          t.run();
          process.next();
        });
      }
      return this;
    }

    /**
     * Add the task to the tail of the task pool
     *
     * @param task the task
     *
     * @return the current task pool, for chaining
     */
    public TaskPool addLast(Runnable... task) {
      for (Runnable t : task) {
        tasks.addLast(process -> {
          t.run();
          process.next();
        });
      }
      return this;
    }

    /**
     * Poll the task
     *
     * @return the task or null if there is no task
     */
    Consumer<Process> pollTask() {
      return tasks.poll();
    }

    /**
     * Get the stage of the task pool
     *
     * @return the stage
     */
    int getStage() {
      return stage;
    }
  }
}

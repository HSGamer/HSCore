package me.hsgamer.hscore.task;

import me.hsgamer.hscore.task.element.TaskPool;
import me.hsgamer.hscore.task.element.TaskProcess;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * A runnable that runs a batch of tasks in sequence
 */
public class BatchRunnable implements Runnable {
  private final Queue<TaskPool> tasks = new PriorityQueue<>(Comparator.comparingInt(TaskPool::getStage));
  private final Map<String, Object> data;
  private final AtomicBoolean isTimeout = new AtomicBoolean(false);
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
    AtomicBoolean isLocked = new AtomicBoolean(false);
    Object lock = new Object();

    TaskProcess process = new TaskProcess() {
      @Override
      public Map<String, Object> getData() {
        return data;
      }

      @Override
      public void next() {
        synchronized (lock) {
          isLocked.set(false);
          lock.notify();
        }
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
      TaskPool taskPool = currentTaskPool.get();
      Consumer<TaskProcess> task = taskPool == null ? null : taskPool.pollTask();
      if (task == null) {
        taskPool = tasks.poll();

        if (taskPool == null) {
          isRunning.set(false);
          break;
        }

        currentTaskPool.set(taskPool);
        continue;
      }

      isLocked.set(true);
      task.accept(process);

      synchronized (lock) {
        if (isLocked.get()) {
          try {
            if (timeout <= 0) {
              lock.wait();
            } else {
              lock.wait(timeoutUnit.toMillis(timeout));

              // Stop the task if it's still locked after the timeout
              if (isLocked.get()) {
                isTimeout.set(true);
                throw new InterruptedException();
              }
            }
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            isRunning.set(false);
            isLocked.set(false);
            break;
          }
        }
      }
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
   * Check if the task is interrupted by the timeout
   *
   * @return true if it is
   */
  public boolean isTimeout() {
    return isTimeout.get();
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

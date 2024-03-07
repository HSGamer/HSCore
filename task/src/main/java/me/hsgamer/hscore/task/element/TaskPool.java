package me.hsgamer.hscore.task.element;

import me.hsgamer.hscore.task.BatchRunnable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * The task pool of the {@link BatchRunnable}
 */
public final class TaskPool {
  private final int stage;
  private final Deque<Consumer<TaskProcess>> tasks = new ArrayDeque<>();

  /**
   * Create a new task pool
   *
   * @param stage the stage of the task pool
   */
  public TaskPool(int stage) {
    this.stage = stage;
  }

  /**
   * Add the task to the head of the task pool
   *
   * @param task the task
   *
   * @return the current task pool, for chaining
   */
  public TaskPool addFirst(Consumer<TaskProcess> task) {
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
  public TaskPool addLast(Consumer<TaskProcess> task) {
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
  public Consumer<TaskProcess> pollTask() {
    return tasks.poll();
  }

  /**
   * Get the stage of the task pool
   *
   * @return the stage
   */
  public int getStage() {
    return stage;
  }
}

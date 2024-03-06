package me.hsgamer.hscore.task.element;

import me.hsgamer.hscore.task.BatchRunnable;

import java.util.function.Consumer;

/**
 * The task process.
 * Used by the task to work with the running {@link BatchRunnable}
 */
public interface TaskProcess {
  /**
   * Get the data of the running {@link BatchRunnable}
   *
   * @return the data
   */
  TaskData getData();

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

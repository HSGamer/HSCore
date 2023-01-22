package me.hsgamer.hscore.task.exception;

import me.hsgamer.hscore.task.BatchRunnable;

/**
 * The exception thrown when the {@link BatchRunnable} is unexpectedly stopped
 */
public class BatchRunnableException extends RuntimeException {
  public BatchRunnableException(Throwable cause) {
    super(cause);
  }
}

package me.hsgamer.hscore.bukkit.scheduler;

import java.util.function.BooleanSupplier;

/**
 * An extension of {@link BooleanSupplier} that mimics {@link org.bukkit.scheduler.BukkitRunnable} so that it can be used in {@link Scheduler}.
 * The runnable can be cancelled by {@link #cancel()}.
 */
public abstract class CancellableRunnable implements BooleanSupplier {
  private boolean cancelled = false;

  /**
   * Cancel the runnable
   */
  public void cancel() {
    this.cancelled = true;
  }

  /**
   * Run the task
   */
  public abstract void run();

  @Override
  public final boolean getAsBoolean() {
    // Check if the task is cancelled before running
    if (cancelled) {
      return false;
    }

    this.run();
    return !cancelled;
  }
}

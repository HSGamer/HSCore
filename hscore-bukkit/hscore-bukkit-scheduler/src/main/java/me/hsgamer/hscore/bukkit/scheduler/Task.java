package me.hsgamer.hscore.bukkit.scheduler;

public interface Task {
  static Task completed(boolean async, boolean repeating) {
    return new Task() {
      @Override
      public boolean isCancelled() {
        return true;
      }

      @Override
      public void cancel() {
        // Do nothing
      }

      @Override
      public boolean isAsync() {
        return async;
      }

      @Override
      public boolean isRepeating() {
        return repeating;
      }
    };
  }

  boolean isCancelled();

  void cancel();

  boolean isAsync();

  boolean isRepeating();
}

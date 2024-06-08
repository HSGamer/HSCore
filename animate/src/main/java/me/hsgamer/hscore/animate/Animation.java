package me.hsgamer.hscore.animate;

import java.util.List;

public class Animation<T> {
  private final List<T> frames;
  private final long periodMillis;
  private Long startMillis;

  public Animation(List<T> frames, long periodMillis) {
    if (frames.isEmpty()) {
      throw new IllegalArgumentException("Frames cannot be empty");
    }
    if (periodMillis <= 0) {
      throw new IllegalArgumentException("Period must be positive");
    }

    this.frames = frames;
    this.periodMillis = periodMillis;
  }

  public List<T> getFrames() {
    return frames;
  }

  public T getFrame(int index) {
    return frames.get(index);
  }

  public T getCurrentFrame() {
    long currentMillis = System.currentTimeMillis();
    if (startMillis == null) {
      startMillis = currentMillis;
    }
    long diff = currentMillis - startMillis;
    int index = (int) (diff / periodMillis) % frames.size();
    return frames.get(index);
  }

  public void reset() {
    startMillis = null;
  }
}

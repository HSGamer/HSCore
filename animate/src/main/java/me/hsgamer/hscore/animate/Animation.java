package me.hsgamer.hscore.animate;

import java.util.Collections;
import java.util.List;

/**
 * The animation that gets the frame based on the period
 *
 * @param <T> the frame type
 */
public class Animation<T> {
  private final List<T> frames;
  private final long periodMillis;
  private Long startMillis;

  /**
   * Create a new animation
   *
   * @param frames       the frames
   * @param periodMillis the period in milliseconds
   */
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

  /**
   * Get the frames
   *
   * @return the frames
   */
  public List<T> getFrames() {
    return Collections.unmodifiableList(frames);
  }

  /**
   * Get the frame based on the current time
   *
   * @param currentMillis the current time in milliseconds
   *
   * @return the frame
   */
  public T getCurrentFrame(long currentMillis) {
    if (startMillis == null) {
      startMillis = currentMillis;
    }
    long diff = currentMillis - startMillis;
    int index = (int) (diff / periodMillis) % frames.size();
    return frames.get(index);
  }

  /**
   * Get the frame based on the current time
   *
   * @return the frame
   */
  public T getCurrentFrame() {
    return getCurrentFrame(System.currentTimeMillis());
  }

  /**
   * Reset the animation
   */
  public void reset() {
    startMillis = null;
  }

  /**
   * Check if it's the first run. It will return true if the animation is running for the first time.
   *
   * @param currentMillis the current time in milliseconds
   *
   * @return true if it's the first run
   */
  public boolean isFirstRun(long currentMillis) {
    return startMillis == null || currentMillis - startMillis < periodMillis * frames.size();
  }

  /**
   * Check if it's the first run. It will return true if the animation is running for the first time.
   *
   * @return true if it's the first run
   */
  public boolean isFirstRun() {
    return isFirstRun(System.currentTimeMillis());
  }
}

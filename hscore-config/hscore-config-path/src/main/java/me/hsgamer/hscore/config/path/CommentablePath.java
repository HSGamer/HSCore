package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.CommentType;
import me.hsgamer.hscore.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Optional;

/**
 * A commentable config path
 *
 * @param <T> the type of the value
 */
public class CommentablePath<T> implements ConfigPath<T> {
  private final ConfigPath<T> originalPath;
  private final EnumMap<CommentType, String> defaultCommentMap = new EnumMap<>(CommentType.class);

  /**
   * Create a config path
   *
   * @param originalPath    the original config path
   * @param defaultComments the default comments
   */
  public CommentablePath(@NotNull final ConfigPath<T> originalPath, @NotNull final String... defaultComments) {
    this.originalPath = originalPath;

    if (defaultComments.length > 0) {
      defaultCommentMap.put(CommentType.BLOCK, defaultComments[0]);
    }

    if (defaultComments.length > 1) {
      String[] sideComments = Arrays.copyOfRange(defaultComments, 1, defaultComments.length);
      defaultCommentMap.put(CommentType.SIDE, String.join("\n", sideComments));
    }
  }

  @Override
  public T getValue() {
    return originalPath.getValue();
  }

  @Override
  public void setValue(@Nullable final T value) {
    originalPath.setValue(value);
  }

  @Override
  public T getValue(@NotNull Config config) {
    return originalPath.getValue(config);
  }

  @Override
  public void setValue(@Nullable T value, @NotNull Config config) {
    originalPath.setValue(value, config);
  }

  @Override
  public @NotNull String getPath() {
    return originalPath.getPath();
  }

  @Override
  public @Nullable Config getConfig() {
    return originalPath.getConfig();
  }

  @Override
  public void setConfig(@NotNull final Config config) {
    originalPath.setConfig(config);
    defaultCommentMap.forEach((type, s) -> {
      if (config.getComment(getPath(), type) == null) {
        config.setComment(getPath(), s, type);
      }
    });
  }

  @Override
  public void migrateConfig(@NotNull final Config config) {
    originalPath.migrateConfig(config);
  }

  /**
   * Get the block comment
   *
   * @return the comment
   */
  @Nullable
  public String getComment() {
    return getComment(CommentType.BLOCK);
  }

  /**
   * Set the block comment
   *
   * @param comment the comment
   */
  public void setComment(@Nullable final String comment) {
    setComment(CommentType.BLOCK, comment);
  }

  /**
   * Get the comment
   *
   * @param commentType the comment type
   *
   * @return the comment
   */
  @Nullable
  public String getComment(@NotNull final CommentType commentType) {
    return Optional.ofNullable(getConfig())
      .map(config -> config.getComment(getPath(), commentType))
      .orElseGet(() -> defaultCommentMap.get(commentType));
  }

  /**
   * Set the comment
   *
   * @param commentType the comment type
   * @param comment     the comment
   */
  public void setComment(@NotNull final CommentType commentType, @Nullable final String comment) {
    Optional.ofNullable(getConfig()).ifPresent(config -> config.setComment(getPath(), comment, commentType));
  }
}

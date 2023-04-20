package me.hsgamer.hscore.config.path;

import me.hsgamer.hscore.config.CommentType;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A commentable config path
 *
 * @param <T> the type of the value
 */
public class CommentablePath<T> implements ConfigPath<T> {
  private final ConfigPath<T> originalPath;
  private final EnumMap<CommentType, List<String>> defaultCommentMap = new EnumMap<>(CommentType.class);

  /**
   * Create a config path
   *
   * @param originalPath  the original config path
   * @param blockComments the {@link CommentType#BLOCK} comments
   * @param sideComments  the {@link CommentType#SIDE} comments
   */
  public CommentablePath(@NotNull final ConfigPath<T> originalPath, @NotNull final List<String> blockComments, @NotNull final List<String> sideComments) {
    this.originalPath = originalPath;
    defaultCommentMap.put(CommentType.BLOCK, blockComments);
    defaultCommentMap.put(CommentType.SIDE, sideComments);
  }

  /**
   * Create a config path
   *
   * @param originalPath  the original config path
   * @param blockComments the {@link CommentType#BLOCK} comments
   */
  public CommentablePath(@NotNull final ConfigPath<T> originalPath, @NotNull final String... blockComments) {
    this(originalPath, Arrays.asList(blockComments), Collections.emptyList());
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
  public @NotNull PathString getPath() {
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
  public List<String> getComment() {
    return getComment(CommentType.BLOCK);
  }

  /**
   * Set the block comment
   *
   * @param comment the comment
   */
  public void setComment(@Nullable final List<String> comment) {
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
  public List<String> getComment(@NotNull final CommentType commentType) {
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
  public void setComment(@NotNull final CommentType commentType, @Nullable final List<String> comment) {
    Optional.ofNullable(getConfig()).ifPresent(config -> config.setComment(getPath(), comment, commentType));
  }
}

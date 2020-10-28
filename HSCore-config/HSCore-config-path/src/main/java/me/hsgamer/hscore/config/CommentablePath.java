package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.comments.CommentType;
import org.simpleyaml.configuration.comments.Commentable;
import org.simpleyaml.configuration.file.FileConfiguration;

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

    if (defaultComments.length > 1) {
      defaultCommentMap.put(CommentType.BLOCK, defaultComments[0]);
    }

    if (defaultComments.length > 2) {
      defaultCommentMap.put(CommentType.SIDE, defaultComments[1]);
    }
  }

  @Override
  public @Nullable T getValue() {
    return originalPath.getValue();
  }

  @Override
  public void setValue(@Nullable final T value) {
    originalPath.setValue(value);
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
    FileConfiguration configuration = config.getConfig();
    if (!(configuration instanceof Commentable)) {
      return;
    }
    defaultCommentMap.forEach((type, s) -> {
      if (((Commentable) configuration).getComment(getPath(), type) == null) {
        ((Commentable) configuration).setComment(getPath(), s, type);
      }
    });
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
   * @return the comment
   */
  @Nullable
  public String getComment(@NotNull final CommentType commentType) {
    return Optional.ofNullable(getConfig())
      .map(Config::getConfig)
      .filter(config -> config instanceof Commentable)
      .map(config -> ((Commentable) config).getComment(getPath(), commentType))
      .orElse(defaultCommentMap.get(commentType));
  }

  /**
   * Set the comment
   *
   * @param commentType the comment type
   * @param comment     the comment
   */
  public void setComment(@NotNull final CommentType commentType, @Nullable final String comment) {
    Optional.ofNullable(getConfig())
      .map(Config::getConfig)
      .filter(config -> config instanceof Commentable)
      .ifPresent(config -> ((Commentable) config).setComment(getPath(), comment, commentType));
  }
}

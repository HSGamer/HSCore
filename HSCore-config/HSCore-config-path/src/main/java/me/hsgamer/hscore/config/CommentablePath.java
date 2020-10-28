package me.hsgamer.hscore.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.comments.CommentType;
import org.simpleyaml.configuration.comments.Commentable;

import java.util.EnumMap;

/**
 * A commentable config path
 *
 * @param <T> the type of the value
 */
public class CommentablePath<T> implements ConfigPath<T> {
  private final ConfigPath<T> originalPath;
  private final EnumMap<CommentType, String> commentMap = new EnumMap<>(CommentType.class);

  /**
   * Create a config path
   *
   * @param originalPath    the original config path
   * @param defaultComments the default comments
   */
  public CommentablePath(@NotNull final ConfigPath<T> originalPath, @NotNull final String... defaultComments) {
    this.originalPath = originalPath;

    if (defaultComments.length > 1) {
      commentMap.put(CommentType.BLOCK, defaultComments[0]);
    }

    if (defaultComments.length > 2) {
      commentMap.put(CommentType.SIDE, defaultComments[1]);
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
    if (!(config instanceof Commentable)) {
      return;
    }

    for (CommentType commentType : CommentType.values()) {
      commentMap.compute(commentType, (type, s) -> {
        String comment = ((Commentable) config).getComment(getPath(), type);
        comment = comment == null ? s : comment;
        ((Commentable) config).setComment(getPath(), comment, type);
        return comment;
      });
    }
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
  public String getComment(@NotNull final CommentType commentType) {
    return commentMap.get(commentType);
  }

  /**
   * Set the comment
   *
   * @param commentType the comment type
   * @param comment     the comment
   */
  public void setComment(@NotNull final CommentType commentType, @Nullable final String comment) {
    commentMap.put(commentType, comment);

    Config config = getConfig();
    if (!(config instanceof Commentable)) {
      return;
    }

    ((Commentable) config).setComment(getPath(), comment, commentType);
  }
}

package me.hsgamer.hscore.config;

import me.hsgamer.hscore.config.comment.CommentType;
import me.hsgamer.hscore.config.comment.Commentable;

/**
 * A {@link Config} implementation to load {@link ConfigPath} automatically
 */
public class PathableConfig extends DecorativeConfig implements Commentable {

  /**
   * Create a pathable config
   *
   * @param config the original config
   */
  public PathableConfig(Config config) {
    super(config);
  }

  @Override
  public void setup() {
    super.setup();
    PathLoader.loadPath(this);
    this.save();
  }

  @Override
  public void reload() {
    super.reload();
    PathLoader.reloadPath(this);
  }

  @Override
  public String getComment(String path, CommentType type) {
    if (config instanceof Commentable) {
      return ((Commentable) config).getComment(path, type);
    }
    return null;
  }

  @Override
  public void setComment(String path, String value, CommentType type) {
    if (config instanceof Commentable) {
      ((Commentable) config).setComment(path, value, type);
    }
  }
}

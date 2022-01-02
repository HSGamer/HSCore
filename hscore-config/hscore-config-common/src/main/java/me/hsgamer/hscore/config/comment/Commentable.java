package me.hsgamer.hscore.config.comment;

/**
 * The commentable interface
 */
public interface Commentable {
  /**
   * Get the comment
   *
   * @param path the path
   * @param type the comment type
   *
   * @return the comment
   */
  String getComment(String path, CommentType type);

  /**
   * Set the comment
   *
   * @param path  the path
   * @param value the comment
   * @param type  the comment type
   */
  void setComment(String path, String value, CommentType type);

  /**
   * Get the block comment
   *
   * @param path the path
   *
   * @return the comment
   */
  default String getComment(String path) {
    return getComment(path, CommentType.BLOCK);
  }

  /**
   * Set the block comment
   *
   * @param path  the path
   * @param value the comment
   */
  default void setComment(String path, String value) {
    setComment(path, value, CommentType.BLOCK);
  }
}

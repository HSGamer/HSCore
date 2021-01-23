package me.hsgamer.hscore.config.comment;

public interface Commentable {
  String getComment(String path, CommentType type);

  void setComment(String path, String value, CommentType type);

  default String getComment(String path) {
    return getComment(path, CommentType.BLOCK);
  }

  default void setCommand(String path, String value) {
    setComment(path, value, CommentType.BLOCK);
  }
}

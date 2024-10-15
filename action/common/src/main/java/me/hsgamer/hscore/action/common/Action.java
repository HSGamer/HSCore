package me.hsgamer.hscore.action.common;

import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.task.element.TaskProcess;

import java.util.UUID;

/**
 * The action
 */
public interface Action {
  /**
   * Apply the action
   *
   * @param uuid           the unique id
   * @param process        the task process
   * @param stringReplacer the string replacer
   */
  void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer);
}

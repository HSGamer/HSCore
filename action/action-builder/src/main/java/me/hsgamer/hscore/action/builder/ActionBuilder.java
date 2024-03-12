package me.hsgamer.hscore.action.builder;

import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.builder.FunctionalMassBuilder;

/**
 * The builder for the {@link Action}
 */
public class ActionBuilder extends FunctionalMassBuilder<ActionInput, Action> {
  @Override
  protected String getType(ActionInput input) {
    return input.getType();
  }
}

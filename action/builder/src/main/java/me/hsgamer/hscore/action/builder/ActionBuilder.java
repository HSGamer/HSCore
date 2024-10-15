package me.hsgamer.hscore.action.builder;

import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.builder.FunctionalMassBuilder;

/**
 * The builder for the {@link Action}
 *
 * @param <I> the type of the input
 */
public class ActionBuilder<I extends ActionInput> extends FunctionalMassBuilder<I, Action> {
  @Override
  protected String getType(I input) {
    return input.getType();
  }
}

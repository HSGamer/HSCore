package me.hsgamer.hscore.bukkit.action.builder;

import me.hsgamer.hscore.action.builder.ActionBuilder;
import me.hsgamer.hscore.action.builder.ActionInput;
import me.hsgamer.hscore.bukkit.action.*;
import org.bukkit.plugin.Plugin;

/**
 * The utility class to register {@link me.hsgamer.hscore.action.common.Action} to the {@link ActionBuilder}
 */
public final class BukkitActionBuilder {
  private BukkitActionBuilder() {
    // EMPTY
  }

  /**
   * Register the actions
   *
   * @param actionBuilder the action builder
   * @param plugin        the plugin
   * @param <I>           the type of the input
   */
  public static <I extends ActionInput> void register(ActionBuilder<I> actionBuilder, Plugin plugin) {
    actionBuilder.register(input -> new BroadcastAction(input.getValue()), "broadcast");
    actionBuilder.register(input -> new ConsoleAction(plugin, input.getValue()), "console");
    actionBuilder.register(input -> new DelayAction(plugin, input.getValue()), "delay");
    actionBuilder.register(input -> new OpAction(plugin, input.getValue()), "op");
    actionBuilder.register(input -> new PermissionAction(plugin, input.getValue(), input.getOptionAsList()), "permission");
    actionBuilder.register(input -> new PlayerAction(plugin, input.getValue()), "player");
    actionBuilder.register(input -> new TellAction(input.getValue()), "tell");
  }
}
